package com.leo.neat.benchmark_tf_lite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.media.Image;
import android.media.ImageReader;
import android.view.SurfaceView;

import java.nio.ByteBuffer;
import java.util.List;

public class Detector implements ImageReader.OnImageAvailableListener {

    /**
     * Tag for debugging
     */
    private static final String TAG = "Detector";

    /**
     * The input image size for the model
     */
    private static final int INPUT_SIZE = 300;

    /**
     * The minimum confidence you want displayed
     */
    private static final float MIN_CONFIDENCE = 0.9f;

    /**
     * The object that controls all of the bounding boxes display
     */
    private BoundingBoxManager mBoundingBoxManager;


    /**
     * Classifier object that is used for object detection
     */
    private Classifier mImageClassifier;

    /**
     * Determines to run another detection or not;
     */
    private boolean isBusy;

    /**
     * The number of frames processed since the start of the detection
     */
    private int processedFrames;

    /**
     * The start time of the detector creation in ms
     */
    private long startTime;

    /**
     *
     * @param drawableSurface - the overlaying surface used for bounding box display
     * @param cxt - the context of the calling application
     * @param imageClassifier - the interface that is going to be used for image detection
     */
    public Detector(SurfaceView drawableSurface, Context cxt, Classifier imageClassifier){
        mBoundingBoxManager = new BoundingBoxManager(drawableSurface);
        isBusy = false;
        mImageClassifier = imageClassifier;
        processedFrames = 0;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onImageAvailable(ImageReader reader) {

        // If the classifier is currently working on an image
        if(isBusy) {
            reader.acquireLatestImage().close();
        }
        else {
            isBusy = true;
            final Image latestImage = reader.acquireLatestImage();

            Thread runningDetection = new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap currentImage = image2Bitmap(latestImage);
                    latestImage.close();
                    Bitmap croppedBitmap = Bitmap.createScaledBitmap(currentImage, INPUT_SIZE, INPUT_SIZE, false);
                    final List<Classifier.Recognition> detectionResults = mImageClassifier.recognizeImage(croppedBitmap);
                    for(final Classifier.Recognition result: detectionResults)
                    {
                        final RectF location = result.getLocation();
                        if(location != null && result.getConfidence() >= MIN_CONFIDENCE)
                        {
                            mBoundingBoxManager.addBoundingBox(convertRect(location),Color.GREEN, 250,result.getTitle());
                        }
                    }
                    isBusy = false;
                    processedFrames += 1;
                    float secElapsed = (System.currentTimeMillis() -startTime )/1000;
                    mBoundingBoxManager.setIPS(processedFrames/secElapsed);
                }
            });
            runningDetection.start();

        }

        // Update bounding box display
        mBoundingBoxManager.drawBoxes();

    }

    private static RectF convertRect(RectF inputRect)
    {
        return new RectF(inputRect.left/INPUT_SIZE, inputRect.top/INPUT_SIZE,
                inputRect.right/INPUT_SIZE, inputRect.bottom/INPUT_SIZE);
    }

    /**
     * Converts an image to bitmap
     * @param in the image you want the bitmap of
     * @return The bitmap of the image you want
     */
    private static Bitmap image2Bitmap(Image in){
        ByteBuffer buffer = in.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        Bitmap bp = BitmapFactory.decodeByteArray(bytes,0,bytes.length,null);
        return bp;
    }
}
