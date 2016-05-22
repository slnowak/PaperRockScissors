package pl.edu.agh.paperrockscissors.classification.opencv;

import android.graphics.Bitmap;

import org.bytedeco.javacpp.helper.opencv_core.AbstractScalar;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;

import pl.edu.agh.paperrockscissors.classification.ClassificationMetadata;
import pl.edu.agh.paperrockscissors.classification.ClassificationType;
import pl.edu.agh.paperrockscissors.classification.PaperRockScissorsClassifier;

import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;

/**
 * Created by novy on 08.05.16.
 */
public class OpenCVClassifier implements PaperRockScissorsClassifier {

    private final CascadeClassifier classifier;
    private final ClassificationType classificationType;
    private final BitmapToMatConverter toIplImageConverter;
    private final MatToBitmapConverter toBitmapConverter;

    public OpenCVClassifier(CascadeClassifier classifier,
                            ClassificationType classificationType) {

        this.classifier = classifier;
        this.classificationType = classificationType;
        this.toIplImageConverter = new BitmapToMatConverter();
        this.toBitmapConverter = new MatToBitmapConverter();
    }

    public ClassificationMetadata classify(Bitmap source) {
        final Mat asMat = toIplImageConverter.toMat(source);
        return doClassification(asMat);
    }

    private ClassificationMetadata doClassification(Mat image) {
        final RectVector recognitionResult = doRecognizeIn(
                grayScaled(image)
        );

        return withRecognitionRectanglesAndScore(recognitionResult, image);
    }

    private Mat grayScaled(Mat originalImage) {
        final Mat grayImagePlaceholder = new Mat(originalImage.rows(), originalImage.cols(), originalImage.type());
        cvtColor(originalImage, grayImagePlaceholder, CV_RGB2GRAY);
        return grayImagePlaceholder;
    }

    private RectVector doRecognizeIn(Mat image) {
        final RectVector rectVector = new RectVector();
        classifier.detectMultiScale(image, rectVector);
        return rectVector;
    }

    private long score(opencv_core.Rect rect) {
        final double scale = 1.1;
        return Math.round(rect.width() * rect.height() * scale);
    }

    private ClassificationMetadata withRecognitionRectanglesAndScore(RectVector recognitionResult, Mat imgToModify) {
        long bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < recognitionResult.size(); i++) {
            final opencv_core.Rect r = recognitionResult.get(i);
            rectangle(
                    imgToModify,
                    new Point(r.x(), r.y()),
                    new Point(r.width() + r.x(), r.height() + r.y()),
                    AbstractScalar.RED,
                    2,
                    CV_AA,
                    0);
            bestScore = Math.max(bestScore, score(r));
        }

        return new ClassificationMetadata(
                toBitmapConverter.toBitmap(imgToModify),
                classificationType,
                bestScore
        );
    }
}
