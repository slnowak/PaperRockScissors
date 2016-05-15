package pl.edu.agh.paperrockscissors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;

import java.io.ByteArrayOutputStream;

import lombok.SneakyThrows;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by novy on 14.05.16.
 */
public class RxJavaCamera {
    private static final int QUALITY = 50;
    private final PublishSubject<byte[]> bitmaps;
    private final Camera camera;
    private SurfaceTexture surfaceTexture;

    public RxJavaCamera() {
        bitmaps = PublishSubject.create();

        camera = openCamera();
        camera.setPreviewCallback((data, cameraRef) -> bitmaps.onNext(data));
        camera.startPreview();
    }

    public static Observable<RxJavaCamera> open() {
        return Observable.create(subscriber -> subscriber.onNext(new RxJavaCamera()));
    }

    private static Observable<byte[]> withThreadsSpecified(Observable<byte[]> input) {
        return input
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @SneakyThrows
    private Camera openCamera() {
        final Camera camera = Camera.open();
        surfaceTexture = new SurfaceTexture(10);
        camera.setPreviewTexture(surfaceTexture);
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(320, 240);
        camera.setParameters(parameters);
        return camera;
    }

    public Observable<Bitmap> streamBitmaps() {
        return withThreadsSpecified(bitmaps)
                .map(this::toBitmap)
                .map(rotateBy(90));
    }

    private byte[] fromNv21ToJpeg(byte[] rawBytes) {
        final Camera.Size previewSize = camera.getParameters().getPreviewSize();
        final YuvImage yuvimage = new YuvImage(rawBytes, ImageFormat.NV21, previewSize.width, previewSize.height, null);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(
                new Rect(0, 0, previewSize.width, previewSize.height),
                QUALITY,
                outputStream
        );

        return outputStream.toByteArray();
    }

    private Bitmap toBitmap(byte[] rawBytes) {
        final byte[] convertedToJpeg = fromNv21ToJpeg(rawBytes);
        return BitmapFactory.decodeByteArray(convertedToJpeg, 0, convertedToJpeg.length);
    }

    private Func1<Bitmap, Bitmap> rotateBy(int degree) {
        return notRotated -> {
            final Matrix rotationMatrix = new Matrix();
            rotationMatrix.postRotate(degree);
            return Bitmap.createBitmap(notRotated, 0, 0, notRotated.getWidth(), notRotated.getHeight(), rotationMatrix, true);
        };
    }
}
