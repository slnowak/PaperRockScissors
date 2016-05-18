package pl.edu.agh.paperrockscissors.classification;

import android.graphics.Bitmap;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 * Created by novy on 15.05.16.
 */
class BitmapToIplImageConverter {

    private final AndroidFrameConverter toFrameConverter;
    private final OpenCVFrameConverter.ToIplImage toIplImageConverter;

    BitmapToIplImageConverter() {
        toFrameConverter = new AndroidFrameConverter();
        toIplImageConverter = new OpenCVFrameConverter.ToIplImage();
    }

    public IplImage toIplImage(Bitmap from) {
        return toIplImageConverter.convert(
                toFrameConverter.convert(from)
        );
    }
}