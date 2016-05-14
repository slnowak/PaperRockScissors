package pl.edu.agh.paperrockscissors;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.SneakyThrows;
import rx.Observable;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recognizeLena)
    Button lenaRecognitionButton;

    private DummyClassifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        classifier = new DummyClassifier(fileNameFor("face.xml"));

        final Observable<ImageParsed> recognizedImages = RxView.clicks(lenaRecognitionButton)
                .map(this::tryToRecognizeLena)
                .map(this::convertToBitmap)
                .map(ImageParsed::new);

        final FragmentManager fm = getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.preview_layout, PreviewFragment.newInstance(recognizedImages));
        ft.commit();
    }

    private Mat tryToRecognizeLena(Void ignored) {
        return classifier.recognizeIn(
                loadImage("lena.png")
        );
    }

    private Bitmap convertToBitmap(Mat mat) {
        return new AndroidFrameConverter().convert(
                new OpenCVFrameConverter.ToMat().convert(mat)
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
