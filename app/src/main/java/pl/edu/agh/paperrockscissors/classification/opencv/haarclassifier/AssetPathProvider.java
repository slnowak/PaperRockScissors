package pl.edu.agh.paperrockscissors.classification.opencv.haarclassifier;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import lombok.SneakyThrows;

/**
 * Created by novy on 08.05.16.
 */
class AssetPathProvider {

    private final Context androidContext;

    public AssetPathProvider(Context androidContext) {
        this.androidContext = androidContext;
    }

    @SneakyThrows
    public String accessiblePathFor(String resourceName) {
        final InputStream inputStream = streamForContextResource(resourceName);
        final File temporaryDir = androidContext.getDir("temporary", Context.MODE_PRIVATE);
        final File temporaryFile = File.createTempFile("tmp-", ".tmp", temporaryDir);
        temporaryFile.deleteOnExit();
        final FileOutputStream outputStream = new FileOutputStream(temporaryFile);
        IOUtils.copy(inputStream, outputStream);

        inputStream.close();
        outputStream.close();

        return temporaryFile.getAbsolutePath();
    }

    @SneakyThrows
    private InputStream streamForContextResource(String resourceName) {
        return androidContext.getAssets().open(resourceName);
    }
}
