package pl.edu.agh.paperrockscissors.classification;

import android.graphics.Bitmap;

public interface Classifier {

    ClassificationMetadata classify(Bitmap source);

}
