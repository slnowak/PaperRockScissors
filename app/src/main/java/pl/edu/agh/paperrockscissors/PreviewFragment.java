package pl.edu.agh.paperrockscissors;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.edu.agh.paperrockscissors.classification.ClassificationMetadata;
import rx.Observable;
import rx.Subscription;

/**
 * Created by novy on 14.05.16.
 */
public class PreviewFragment extends Fragment {
    @BindView(R.id.previewImageView)
    ImageView previewImageView;
    @BindView(R.id.textView)
    TextView textView;

    private Subscription imagesSubscription;

    public static PreviewFragment newInstance(Observable<ClassificationMetadata> imagesToDisplay) {
        final PreviewFragment newFragment = new PreviewFragment();
        newFragment.subscribeTo(imagesToDisplay);
        return newFragment;
    }

    private void subscribeTo(Observable<ClassificationMetadata> images) {
        imagesSubscription = images.subscribe(this::updatePreview);
    }

    private void updatePreview(ClassificationMetadata classificationMetadata) {
        previewImageView.setImageBitmap(classificationMetadata.getImage());
        final String resultInformation = classificationMetadata.getType().toString();
        textView.setText(resultInformation);
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
