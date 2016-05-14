package pl.edu.agh.paperrockscissors;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;

/**
 * Created by novy on 14.05.16.
 */
public class PreviewFragment extends Fragment {
    @BindView(R.id.previewImageView)
    ImageView previewImageView;

    private Subscription imagesSubscription;

    public static PreviewFragment newInstance(Observable<ParsedImage> imagesToDisplay) {
        final PreviewFragment newFragment = new PreviewFragment();
        newFragment.subscribeTo(imagesToDisplay);
        return newFragment;
    }

    private void subscribeTo(Observable<ParsedImage> images) {
        imagesSubscription = images.subscribe(this::updatePreview);
    }

    private void updatePreview(ParsedImage parsedImages) {
        previewImageView.setImageBitmap(parsedImages.getBitmap());
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
