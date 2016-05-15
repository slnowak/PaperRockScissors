package pl.edu.agh.paperrockscissors.faceregonition;

import android.graphics.Bitmap;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;

import lombok.SneakyThrows;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvLoad;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_core.cvarrToMat;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;

/**
 * Created by novy on 08.05.16.
 */
public class FaceClassifier {

    private final CvHaarClassifierCascade classifier;
    private final BitmapToIplImageConverter toIplImageConverter;
    private final MatToBitmapConverter toBitmapConverter;

    public FaceClassifier(String xmlFile) {
        classifier = loadClassifier(xmlFile);
        toIplImageConverter = new BitmapToIplImageConverter();
        toBitmapConverter = new MatToBitmapConverter();
    }

    @SneakyThrows
    private static CvHaarClassifierCascade loadClassifier(String xmlFileName) {
        return new CvHaarClassifierCascade(cvLoad(xmlFileName));
    }

    public Bitmap recognizeIn(Bitmap source) {
        final IplImage asIplImage = toIplImageConverter.toIplImage(source);
        final Mat asMat = recognizeIn(asIplImage);
        return toBitmapConverter.toBitmap(asMat);
    }

    private Mat recognizeIn(IplImage image) {
        final CvSeq recognitionResult = doRecognizeIn(
                grayScaled(image)
        );

        return withRecognitionRectangles(recognitionResult, image);
    }

    private IplImage grayScaled(IplImage originalImage) {
        final IplImage grayImagePlaceholder = IplImage.create(originalImage.width(), originalImage.height(), IPL_DEPTH_8U, 1);
        cvCvtColor(originalImage, grayImagePlaceholder, CV_RGB2GRAY);
        return grayImagePlaceholder;
    }


    private CvSeq doRecognizeIn(IplImage image) {
        final CvMemStorage cvMemStorage = CvMemStorage.create();
        final CvSeq recognitionResult = cvHaarDetectObjects(
                image, classifier, cvMemStorage, 1.1, 1, 0
        );
        cvClearMemStorage(cvMemStorage);
        return recognitionResult;
    }

    private Mat withRecognitionRectangles(CvSeq recognitionResult, IplImage imgToModify) {
        for (int i = 0; i < recognitionResult.total(); i++) {
            opencv_core.CvRect r = new opencv_core.CvRect(cvGetSeqElem(recognitionResult, i));
            cvRectangle(
                    imgToModify,
                    cvPoint(r.x(), r.y()),
                    cvPoint(r.width() + r.x(), r.height() + r.y()),
                    opencv_core.CvScalar.RED,
                    2,
                    CV_AA,
                    0);
        }

        return cvarrToMat(imgToModify);
    }
}
