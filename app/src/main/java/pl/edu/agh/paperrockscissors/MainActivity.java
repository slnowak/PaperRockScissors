package pl.edu.agh.paperrockscissors;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;

import butterknife.ButterKnife;
import lombok.SneakyThrows;
import pl.edu.agh.paperrockscissors.classification.ClassificationMetadata;
import pl.edu.agh.paperrockscissors.classification.ClassificationType;
import pl.edu.agh.paperrockscissors.classification.Classifier;
import pl.edu.agh.paperrockscissors.classification.CompositeImageClassifier;
import pl.edu.agh.paperrockscissors.classification.ImageClassifier;
import rx.Observable;

public class MainActivity extends AppCompatActivity {

    private Classifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        classifier = new CompositeImageClassifier(
                new ImageClassifier(fileNameFor("paper2.xml"), ClassificationType.PAPER),
                new ImageClassifier(fileNameFor("rock2.xml"), ClassificationType.ROCK),
                new ImageClassifier(fileNameFor("scissors.xml"), ClassificationType.SCISSORS)
        );

        final Observable<ClassificationMetadata> bitmaps = RxJavaCamera.open()
                .flatMap(RxJavaCamera::streamBitmaps)
                .map(src -> classifier.classify(src));

        startPreviewFragment(bitmaps);
    }

    private void startPreviewFragment(Observable<ClassificationMetadata> images) {
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
