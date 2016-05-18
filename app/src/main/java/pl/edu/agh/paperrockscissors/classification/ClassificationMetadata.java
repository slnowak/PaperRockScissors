package pl.edu.agh.paperrockscissors.classification;

import android.graphics.Bitmap;

import lombok.Value;

@Value
public class ClassificationMetadata {
    Bitmap image;
    ClassificationType type;
    Long matchScore;
}
