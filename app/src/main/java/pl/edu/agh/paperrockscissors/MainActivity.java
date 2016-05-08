package pl.edu.agh.paperrockscissors;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.SneakyThrows;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recognizeLena) Button lenaRecognitionButton;
    @BindView(R.id.lenaView) ImageView lenaView;

    private DummyClassifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        classifier = new DummyClassifier(fileNameFor("face.xml"));
    }

    @OnClick(R.id.recognizeLena)
    public void recognizeFace(View view) {
        final Mat withRecognizedLena = classifier.recognizeIn(
                loadImage("lena.png")
        );

        lenaView.setImageBitmap(
                new AndroidFrameConverter().convert(
                        new OpenCVFrameConverter.ToMat().convert(withRecognizedLena)
                )
        );
    }

    private IplImage loadImage(String imageAssetName) {
        return cvLoadImage(
                fileNameFor(imageAssetName)
        );
    }

    private String fileNameFor(String assetName) {
        final NowThisIsWhatICallUgly uglyFileReader = new NowThisIsWhatICallUgly(this);
        return uglyFileReader.fileNameFrom(
                assetFor(assetName)
        );
    }

    @SneakyThrows
    private InputStream assetFor(String name) {
        return getAssets().open(name);
    }
}
