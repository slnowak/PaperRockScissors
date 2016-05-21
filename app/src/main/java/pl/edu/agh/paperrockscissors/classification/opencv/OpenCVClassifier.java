package pl.edu.agh.paperrockscissors.classification.opencv;

import android.graphics.Bitmap;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;

import pl.edu.agh.paperrockscissors.classification.ClassificationMetadata;
import pl.edu.agh.paperrockscissors.classification.ClassificationType;
import pl.edu.agh.paperrockscissors.classification.PaperRockScissorsClassifier;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_core.cvarrToMat;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;

/**
 * Created by novy on 08.05.16.
 */
public class OpenCVClassifier implements PaperRockScissorsClassifier {

    private final CvHaarClassifierCascade classifier;
    private final ClassificationType classificationType;
    private final BitmapToIplImageConverter toIplImageConverter;
    private final MatToBitmapConverter toBitmapConverter;

    public OpenCVClassifier(CvHaarClassifierCascade classifier,
                            ClassificationType classificationType) {

        this.classifier = classifier;
        this.classificationType = classificationType;
        this.toIplImageConverter = new BitmapToIplImageConverter();
        this.toBitmapConverter = new MatToBitmapConverter();
    }

    public ClassificationMetadata classify(Bitmap source) {
        final IplImage asIplImage = toIplImageConverter.toIplImage(source);
        return doClassification(asIplImage);
    }

    private ClassificationMetadata doClassification(IplImage image) {
        final CvSeq recognitionResult = doRecognizeIn(
                grayScaled(image)
        );

        return withRecognitionRectanglesAndScore(recognitionResult, image);
    }

    private IplImage grayScaled(IplImage originalImage) {
        final IplImage grayImagePlaceholder = IplImage.create(originalImage.width(), originalImage.height(), IPL_DEPTH_8U, 1);
        cvCvtColor(originalImage, grayImagePlaceholder, CV_RGB2GRAY);
        return grayImagePlaceholder;
    }

    private CvSeq doRecognizeIn(IplImage image) {
        final CvMemStorage cvMemStorage = CvMemStorage.create();
        final CvSeq recognitionResult = cvHaarDetectObjects(
                image, classifier, cvMemStorage, 1.1, 2, 0
        );
        cvClearMemStorage(cvMemStorage);
        return recognitionResult;
    }

    private long score(opencv_core.CvRect rect) {
        final double scale = 1.1;
        return Math.round(rect.width() * rect.height() * scale);
    }

    private ClassificationMetadata withRecognitionRectanglesAndScore(CvSeq recognitionResult, IplImage imgToModify) {
        long bestScore = Integer.MIN_VALUE;
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
            bestScore = Math.max(bestScore, score(r));
        }

        return new ClassificationMetadata(
                toBitmapConverter.toBitmap(cvarrToMat(imgToModify)),
                classificationType,
                bestScore
        );
    }
}
