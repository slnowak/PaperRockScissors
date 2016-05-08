package pl.edu.agh.paperrockscissors;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by novy on 08.05.16.
 */
class NowThisIsWhatICallUgly {

    private final Context androidContext;

    public NowThisIsWhatICallUgly(Context androidContext) {
        this.androidContext = androidContext;
    }

    public String fileNameFrom(InputStream inputStream) throws IOException {
        final File temporaryDir = androidContext.getDir("temporary", Context.MODE_PRIVATE);
        final File temporaryFile = File.createTempFile("tmp-", ".tmp", temporaryDir);
        temporaryFile.deleteOnExit();
        final FileOutputStream outputStream = new FileOutputStream(temporaryFile);
        IOUtils.copy(inputStream, outputStream);

        inputStream.close();
        outputStream.close();

        return temporaryFile.getAbsolutePath();
    }
}
