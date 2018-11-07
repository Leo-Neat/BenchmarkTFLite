package com.leo.neat.benchmark_tf_lite;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.media.ImageReader;
import android.view.SurfaceView;

public class Detector implements ImageReader.OnImageAvailableListener {
    private static final String TAG = "Detector";

    private SurfaceView mBoundingDisplaySurface;

    private BoundingBoxManager mBoundingBoxManager;

    private int counter = 1;

    public Detector(SurfaceView drawableSurface, Context cxt){
        mBoundingBoxManager = new BoundingBoxManager(drawableSurface);
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        reader.acquireLatestImage().close();
        mBoundingBoxManager.drawBoxes();
        counter += 1;
        if(counter %100 == 1)
        {
            mBoundingBoxManager.addBoundingBox(new RectF(.0f,.0f,.3f,.5f),
                    Color.GREEN, 2000, "No Label");
        }
    }
}
