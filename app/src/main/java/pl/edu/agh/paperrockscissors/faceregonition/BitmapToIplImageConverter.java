package pl.edu.agh.paperrockscissors.faceregonition;

import android.graphics.Bitmap;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 * Created by novy on 15.05.16.
 */
class BitmapToIplImageConverter {

    private final AndroidFrameConverter toFrameConverter = new AndroidFrameConverter();
    private final OpenCVFrameConverter.ToIplImage toIplImageConverter = new OpenCVFrameConverter.ToIplImage();

    public IplImage toIplImage(Bitmap from) {
        return toIplImageConverter.convert(
                toFrameConverter.convert(from)
        );
    }
}