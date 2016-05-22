package pl.edu.agh.paperrockscissors.classification.opencv.haarclassifier;

import android.content.Context;

import org.opencv.objdetect.CascadeClassifier;


/**
 * Created by novy on 21.05.16.
 */
public class HaarCassifierFactory {

    private final AssetPathProvider pathProvider;

    public HaarCassifierFactory(Context androidContext) {
        pathProvider = new AssetPathProvider(androidContext);
    }

    public CascadeClassifier trainedClassifierFrom(String androidAssetName) {
        return loadClassifier(pathProvider.accessiblePathFor(androidAssetName));
    }

    private CascadeClassifier loadClassifier(String xmlFileName) {
        return new CascadeClassifier(xmlFileName);
    }
}
