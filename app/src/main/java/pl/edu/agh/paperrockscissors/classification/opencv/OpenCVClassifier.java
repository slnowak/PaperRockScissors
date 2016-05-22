package pl.edu.agh.paperrockscissors.classification.opencv;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import pl.edu.agh.paperrockscissors.classification.ClassificationMetadata;
import pl.edu.agh.paperrockscissors.classification.ClassificationType;
import pl.edu.agh.paperrockscissors.classification.PaperRockScissorsClassifier;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.rectangle;


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
        final MatOfRect recognitionResult = doRecognizeIn(
                grayScaled(image)
        );

        return withRecognitionRectanglesAndScore(recognitionResult, image);
    }

    private Mat grayScaled(Mat originalImage) {
        final Mat grayImagePlaceholder = originalImage.clone();
        cvtColor(originalImage, grayImagePlaceholder, COLOR_RGB2GRAY);
        return grayImagePlaceholder;
    }

    private MatOfRect doRecognizeIn(Mat image) {
        final MatOfRect rectVector = new MatOfRect();
        classifier.detectMultiScale(image, rectVector);
        return rectVector;
    }

    private long score(Rect rect) {
        final double scale = 1.1;
        return Math.round(rect.area() * scale);
    }

    private ClassificationMetadata withRecognitionRectanglesAndScore(MatOfRect recognitionResult, Mat imgToModify) {
        long bestScore = Integer.MIN_VALUE;
        for (Rect r : recognitionResult.toList()) {
            rectangle(
                    imgToModify,
                    new Point(r.x, r.y),
                    new Point(r.width + r.x, r.height + r.y),
                    red(),
                    2,
                    Imgproc.LINE_AA,
                    0);
            bestScore = Math.max(bestScore, score(r));
        }

        return new ClassificationMetadata(
                toBitmapConverter.toBitmap(imgToModify),
                classificationType,
                bestScore
        );
    }

    private Scalar red() {
        return new Scalar(0, 0, 255);
    }
}
