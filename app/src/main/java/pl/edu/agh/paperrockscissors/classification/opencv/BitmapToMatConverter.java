package pl.edu.agh.paperrockscissors.classification.opencv;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;


/**
 * Created by novy on 15.05.16.
 */
class BitmapToMatConverter {

    public Mat toMat(Bitmap from) {
        final Mat placeholder = new Mat();
        Utils.bitmapToMat(from, placeholder);
        return placeholder;
    }
}