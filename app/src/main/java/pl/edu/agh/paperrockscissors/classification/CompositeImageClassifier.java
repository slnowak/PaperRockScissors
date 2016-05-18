package pl.edu.agh.paperrockscissors.classification;

import android.graphics.Bitmap;
import android.util.Log;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

public class CompositeImageClassifier implements Classifier {
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
        final ClassificationMetadata paperClassification = paperClassifier.classify(source);
        final ClassificationMetadata rockClassification = rockClassifier.classify(source);
        final ClassificationMetadata scissorsClassification = scissorsClassifier.classify(source);

        return Stream
                .of(paperClassification, rockClassification, scissorsClassification)
                .filter(classifier -> classifier.getMatchScore() > 1000)
                .sortBy(ClassificationMetadata::getMatchScore)
                .findFirst()
                .orElse(new ClassificationMetadata(source, ClassificationType.UNKNOWN, -1L));
    }

}
