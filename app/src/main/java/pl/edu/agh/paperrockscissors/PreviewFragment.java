package pl.edu.agh.paperrockscissors;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by novy on 14.05.16.
 */
public class PreviewFragment extends Fragment {
    @BindView(R.id.lenaView)
    ImageView lenaView;

    private Subscription imagesSubscription;

    public static PreviewFragment newInstance(Observable<ImageParsed> imagesToDisplay) {
        final PreviewFragment newFragment = new PreviewFragment();
        newFragment.subscribeTo(imagesToDisplay);
        return newFragment;
    }

    private void subscribeTo(Observable<ImageParsed> images) {
        imagesSubscription = images
                .subscribe(imageParsed -> lenaView.setImageBitmap(imageParsed.getBitmap()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.preview_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        imagesSubscription.unsubscribe();
        super.onDestroy();
    }
}
