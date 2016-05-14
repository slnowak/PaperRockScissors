package pl.edu.agh.paperrockscissors;

import android.annotation.TargetApi;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;

/**
 * Created by novy on 08.05.16.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class Face implements ImageReader.OnImageAvailableListener {



    @Override
    public void onImageAvailable(ImageReader reader) {
        final Image image = reader.acquireNextImage();


    }
}
