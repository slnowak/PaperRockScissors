package pl.edu.agh.paperrockscissors.classification.opencv;

import android.graphics.Bitmap;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 * Created by novy on 15.05.16.
 */
class BitmapToMatConverter {

    private final AndroidFrameConverter toFrameConverter;
    private final OpenCVFrameConverter.ToMat toMatConverter;

    BitmapToMatConverter() {
        toFrameConverter = new AndroidFrameConverter();
        toMatConverter = new OpenCVFrameConverter.ToMat();
    }

    public Mat toMat(Bitmap from) {
        return toMatConverter.convert(
                toFrameConverter.convert(from)
        );
    }
}