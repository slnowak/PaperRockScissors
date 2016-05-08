package pl.edu.agh.paperrockscissors;

import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;

import java.io.IOException;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvLoad;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;

/**
 * Created by novy on 08.05.16.
 */
public class StupidOpenCVExample {

    public static CvSeq weAllLoveStaticMethodsThatDoesUglyStuff(String imageFileName, String xmlFileName) throws IOException {
        final IplImage img = loadImage(imageFileName);
        final CvHaarClassifierCascade classifier = loadClassifier(xmlFileName);
        return doRecognize(img, classifier);
    }

    private static CvSeq doRecognize(IplImage image, CvHaarClassifierCascade classifier) {
        final CvMemStorage cvMemStorage = CvMemStorage.create();
        final CvSeq classificationResult = cvHaarDetectObjects(
                image, classifier, cvMemStorage, 1.1, 1, 0
        );
        cvClearMemStorage(cvMemStorage);
        return classificationResult;
    }

    private static IplImage loadImage(String imageFileName) throws IOException {
        final IplImage originalImage = cvLoadImage(imageFileName);
        final IplImage grayImagePlaceholder = IplImage.create(originalImage.width(), originalImage.height(), IPL_DEPTH_8U, 1);
        cvCvtColor(originalImage, grayImagePlaceholder, CV_RGB2GRAY);
        return grayImagePlaceholder;

    }

    private static CvHaarClassifierCascade loadClassifier(String xmlFileName) throws IOException {
        return new CvHaarClassifierCascade(cvLoad(xmlFileName));
    }

}
