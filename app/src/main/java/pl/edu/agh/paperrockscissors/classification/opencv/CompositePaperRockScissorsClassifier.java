package pl.edu.agh.paperrockscissors.classification.opencv;

import android.graphics.Bitmap;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;

import pl.edu.agh.paperrockscissors.classification.ClassificationMetadata;
import pl.edu.agh.paperrockscissors.classification.ClassificationType;
import pl.edu.agh.paperrockscissors.classification.PaperRockScissorsClassifier;

public class CompositePaperRockScissorsClassifier implements PaperRockScissorsClassifier {
    private static final int CLASSIFICATION_THRESHOLD = 20000;
    private final PaperRockScissorsClassifier paperPaperRockScissorsClassifier;
    private final PaperRockScissorsClassifier rockPaperRockScissorsClassifier;
    private final PaperRockScissorsClassifier scissorsPaperRockScissorsClassifier;

    public CompositePaperRockScissorsClassifier(PaperRockScissorsClassifier paperPaperRockScissorsClassifier, PaperRockScissorsClassifier rockPaperRockScissorsClassifier, PaperRockScissorsClassifier scissorsPaperRockScissorsClassifier) {
        this.paperPaperRockScissorsClassifier = paperPaperRockScissorsClassifier;
        this.rockPaperRockScissorsClassifier = rockPaperRockScissorsClassifier;
        this.scissorsPaperRockScissorsClassifier = scissorsPaperRockScissorsClassifier;
    }

    @Override
    public ClassificationMetadata classify(Bitmap source) {
        return paperOrRock(source)
                .orElseGet(
                        scissorsOrNothing(source)
                );
    }

    private Optional<ClassificationMetadata> paperOrRock(Bitmap source) {
        final ClassificationMetadata rockClassification = rockPaperRockScissorsClassifier.classify(source);
        final ClassificationMetadata paperClassification = paperPaperRockScissorsClassifier.classify(source);

        return Stream.of(rockClassification, paperClassification)
                .filter(exceedsClassificationThreshold())
                .sortBy(matchingScoreDescending())
                .findFirst();
    }

    private Function<ClassificationMetadata, Long> matchingScoreDescending() {
        return metadata -> -metadata.getMatchScore();
    }

    private Supplier<ClassificationMetadata> scissorsOrNothing(Bitmap source) {
        return () -> Optional.of(scissorsPaperRockScissorsClassifier.classify(source))
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
