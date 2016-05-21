package pl.edu.agh.paperrockscissors.classification.opencv;

import android.content.Context;

import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;

import pl.edu.agh.paperrockscissors.classification.ClassificationType;
import pl.edu.agh.paperrockscissors.classification.PaperRockScissorsClassifier;
import pl.edu.agh.paperrockscissors.classification.PaperRockScissorsClassifierFactory;
import pl.edu.agh.paperrockscissors.classification.opencv.haarclassifier.HaarCassifierFactory;

/**
 * Created by novy on 21.05.16.
 */
public class CompositeClassifierFactory implements PaperRockScissorsClassifierFactory {

    private final HaarCassifierFactory haarCassifierFactory;

    public CompositeClassifierFactory(Context androidContext) {
        haarCassifierFactory = new HaarCassifierFactory(androidContext);
    }

    @Override
    public PaperRockScissorsClassifier paperRockScissorsClassifier() {
        return new CompositePaperRockScissorsClassifier(
                paperClassifier(),
                rockClassifier(),
                scissorsClassifier()
        );
    }

    private PaperRockScissorsClassifier paperClassifier() {
        return new OpenCVClassifier(haarClassifierFromFile("paper2.xml"), ClassificationType.PAPER);
    }

    private PaperRockScissorsClassifier rockClassifier() {
        return new OpenCVClassifier(haarClassifierFromFile("rock2.xml"), ClassificationType.ROCK);
    }

    private PaperRockScissorsClassifier scissorsClassifier() {
        return new OpenCVClassifier(haarClassifierFromFile("scissors.xml"), ClassificationType.SCISSORS);
    }

    private CvHaarClassifierCascade haarClassifierFromFile(String classifierFile) {
        return haarCassifierFactory.trainedClassifierFrom(classifierFile);
    }
}
