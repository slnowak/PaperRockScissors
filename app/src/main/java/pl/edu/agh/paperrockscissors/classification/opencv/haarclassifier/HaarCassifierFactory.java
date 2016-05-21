package pl.edu.agh.paperrockscissors.classification.opencv.haarclassifier;

import android.content.Context;

import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;

import lombok.SneakyThrows;

import static org.bytedeco.javacpp.opencv_core.cvLoad;

/**
 * Created by novy on 21.05.16.
 */
public class HaarCassifierFactory {

    private final AssetPathProvider pathProvider;

    public HaarCassifierFactory(Context androidContext) {
        pathProvider = new AssetPathProvider(androidContext);
    }

    public CvHaarClassifierCascade trainedClassifierFrom(String androidAssetName) {
        return loadClassifier(pathProvider.accessiblePathFor(androidAssetName));
    }

    @SneakyThrows
    private CvHaarClassifierCascade loadClassifier(String xmlFileName) {
        return new CvHaarClassifierCascade(cvLoad(xmlFileName));
    }
}
