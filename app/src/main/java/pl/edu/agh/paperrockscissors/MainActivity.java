package pl.edu.agh.paperrockscissors;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.bytedeco.javacpp.opencv_core.CvSeq;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            final NowThisIsWhatICallUgly fileReaderHack = new NowThisIsWhatICallUgly(this);
            final CvSeq recognitionResult = StupidOpenCVExample.weAllLoveStaticMethodsThatDoesUglyStuff(
                    fileReaderHack.fileNameFrom(getAssets().open("lena.png")),
                    fileReaderHack.fileNameFrom(getAssets().open("face.xml"))
            );
            int foundPatterns = recognitionResult.total();
            System.out.println(foundPatterns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
