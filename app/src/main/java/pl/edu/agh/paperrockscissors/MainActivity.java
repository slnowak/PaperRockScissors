package pl.edu.agh.paperrockscissors;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;

import butterknife.ButterKnife;
import lombok.SneakyThrows;
import pl.edu.agh.paperrockscissors.faceregonition.FaceClassifier;
import rx.Observable;

public class MainActivity extends AppCompatActivity {

    private FaceClassifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        classifier = new FaceClassifier(fileNameFor("scissors.xml"));

        final Observable<Bitmap> bitmaps = RxJavaCamera.open()
                .flatMap(RxJavaCamera::streamBitmaps)
                .map(ipl -> classifier.recognizeIn(ipl));

        startPreviewFragment(bitmaps);
    }

    private void startPreviewFragment(Observable<Bitmap> images) {
        final FragmentManager fm = getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.preview_layout, PreviewFragment.newInstance(images));
        ft.commit();
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
