package pl.edu.agh.paperrockscissors;

import android.app.Application;

import org.opencv.android.OpenCVLoader;

/**
 * Created by novy on 22.05.16.
 */
public class OpenCvAwareApplication extends Application {

    @Override
    public void onCreate() {
        OpenCVLoader.initDebug();
        super.onCreate();
    }
}
