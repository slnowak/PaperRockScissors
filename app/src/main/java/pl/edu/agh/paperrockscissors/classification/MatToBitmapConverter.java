package pl.edu.agh.paperrockscissors.classification;

import android.graphics.Bitmap;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 * Created by novy on 15.05.16.
 */
class MatToBitmapConverter {
    private final OpenCVFrameConverter.ToMat toFrameConverter;
    private final AndroidFrameConverter toBitmapConverter;

    MatToBitmapConverter() {
        toFrameConverter = new OpenCVFrameConverter.ToMat();
        toBitmapConverter = new AndroidFrameConverter();
    }

    public Bitmap toBitmap(Mat from) {
        return toBitmapConverter.convert(
                toFrameConverter.convert(from)
        );
    }
}
