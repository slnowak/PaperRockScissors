package pl.edu.agh.paperrockscissors.faceregonition;

import android.graphics.Bitmap;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 * Created by novy on 15.05.16.
 */
class MatToBitmapConverter {
    private final OpenCVFrameConverter.ToMat toFrameConverter = new OpenCVFrameConverter.ToMat();
    private final AndroidFrameConverter toBitmapConverter = new AndroidFrameConverter();

    public Bitmap toBitmap(Mat from) {
        return toBitmapConverter.convert(
                toFrameConverter.convert(from)
        );
    }
}
