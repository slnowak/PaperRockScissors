package pl.edu.agh.paperrockscissors.classification;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;

public class CompositeImageClassifier implements Classifier {
    private static final int CLASSIFICATION_THRESHOLD = 10000;
    private final ImageClassifier paperClassifier;
    private final ImageClassifier rockClassifier;
    private final ImageClassifier scissorsClassifier;

    public CompositeImageClassifier(ImageClassifier paperClassifier, ImageClassifier rockClassifier, ImageClassifier scissorsClassifier) {
        this.paperClassifier = paperClassifier;
        this.rockClassifier = rockClassifier;
        this.scissorsClassifier = scissorsClassifier;
    }

    @Override
    public ClassificationMetadata classify(Bitmap source) {
        return paperOrRock(source)
                .orElseGet(
                        scissorsOrNothing(source)
                );
    }

    private Optional<ClassificationMetadata> paperOrRock(Bitmap source) {
        final ClassificationMetadata rockClassification = rockClassifier.classify(source);
        final ClassificationMetadata paperClassification = paperClassifier.classify(source);

        return Stream.of(rockClassification, paperClassification)
                .filter(exceedsClassificationThreshold())
                .sortBy(matchingScoreDescending())
                .findFirst();
    }

    private Function<ClassificationMetadata, Long> matchingScoreDescending() {
        return metadata -> -metadata.getMatchScore();
    }

    private Supplier<ClassificationMetadata> scissorsOrNothing(Bitmap source) {
        return () -> Optional.of(scissorsClassifier.classify(source))
                .filter(exceedsClassificationThreshold())
                .orElseGet(notRecognized(source));
    }

    private Predicate<ClassificationMetadata> exceedsClassificationThreshold() {
        return classificationResult -> classificationResult.getMatchScore() > CLASSIFICATION_THRESHOLD;
    }


    private Supplier<ClassificationMetadata> notRecognized(Bitmap source) {
        return () -> new ClassificationMetadata(source, ClassificationType.UNKNOWN, -1L);
    }
}
