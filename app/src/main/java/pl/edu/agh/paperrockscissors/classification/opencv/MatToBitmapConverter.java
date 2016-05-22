package pl.edu.agh.paperrockscissors.classification.opencv;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;


/**
 * Created by novy on 15.05.16.
 */
class MatToBitmapConverter {

    public Bitmap toBitmap(Mat from) {
        final Bitmap placeholder = Bitmap.createBitmap(from.cols(), from.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(from, placeholder);
        return placeholder;
    }
}
