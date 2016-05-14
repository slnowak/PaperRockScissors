package pl.edu.agh.paperrockscissors;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import rx.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final Observable<ParsedImage> bitmaps = RxJavaCamera.open()
                .flatMap(RxJavaCamera::streamBitmaps)
                .map(ParsedImage::new);

        startPreviewFragment(bitmaps);
    }

    private void startPreviewFragment(Observable<ParsedImage> images) {
        final FragmentManager fm = getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.preview_layout, PreviewFragment.newInstance(images));
        ft.commit();
    }
}
