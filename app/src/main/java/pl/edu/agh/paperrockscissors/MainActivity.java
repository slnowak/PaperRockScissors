package pl.edu.agh.paperrockscissors;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button lenaRecognitionButton = (Button) findViewById(R.id.recognizeLena);
        lenaRecognitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageView imageView = (ImageView) findViewById(R.id.lenaView);
                final Mat withRecognizedLena = uglyStuff();
                imageView.setImageBitmap(
                        new AndroidFrameConverter().convert(
                                new OpenCVFrameConverter.ToMat().convert(withRecognizedLena)
                        )
                );
            }
        });
    }

    private Mat uglyStuff() {
        try {
            final NowThisIsWhatICallUgly fileReaderHack = new NowThisIsWhatICallUgly(this);
            final Mat recognitionResult = StupidOpenCVExample.tryToRecognize(
                    fileReaderHack.fileNameFrom(getAssets().open("lena.png")),
                    fileReaderHack.fileNameFrom(getAssets().open("face.xml"))
            );
            return recognitionResult;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
