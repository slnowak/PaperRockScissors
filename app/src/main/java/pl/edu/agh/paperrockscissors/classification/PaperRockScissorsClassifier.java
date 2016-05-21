package pl.edu.agh.paperrockscissors.classification;

import android.graphics.Bitmap;

public interface PaperRockScissorsClassifier {

    ClassificationMetadata classify(Bitmap source);

}
