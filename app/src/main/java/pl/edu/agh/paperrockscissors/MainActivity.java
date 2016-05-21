package pl.edu.agh.paperrockscissors;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import pl.edu.agh.paperrockscissors.classification.ClassificationMetadata;
import pl.edu.agh.paperrockscissors.classification.PaperRockScissorsClassifier;
import pl.edu.agh.paperrockscissors.classification.PaperRockScissorsClassifierFactory;
import pl.edu.agh.paperrockscissors.classification.opencv.CompositeClassifierFactory;
import rx.Observable;

public class MainActivity extends AppCompatActivity {

    private PaperRockScissorsClassifierFactory classifierFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        classifierFactory = new CompositeClassifierFactory(this);

        final PaperRockScissorsClassifier paperRockScissorsClassifier =
                classifierFactory.paperRockScissorsClassifier();

        final Observable<ClassificationMetadata> bitmaps = RxJavaCamera.open()
                .flatMap(RxJavaCamera::streamBitmaps)
                .map(paperRockScissorsClassifier::classify);

        startPreviewFragment(bitmaps);
    }

    private void startPreviewFragment(Observable<ClassificationMetadata> images) {
        final FragmentManager fm = getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.preview_layout, PreviewFragment.newInstance(images));
        ft.commit();
    }
}
