package com.leo.neat.benchmark_tf_lite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class BoundingBoxManager {

    /**
     * Tag used for debugging purposes
     */
    private static final String TAG = "BoundingBoxManager";

    /**
     * The underlying surface object of this class
     */
    private SurfaceView mBaseSurface;

    /**
     * The surface holder for the base surface view
     */
    private SurfaceHolder mSurfaceHolder;

    /**
     * The underlying list of bounding boxes
     */
    private ArrayList<BoundingBox> mBoundingBoxList;

    /**
     * The concurency to make sure the box list is not being drawn and updated at the same time
     */
    private Semaphore mListLock;

    /**
     * The number of inferences done every second
     */
    private double mIPS;

    /**
     * The color to make the ips
     */
    private Paint mTextPaint;

    /**
     * The size of text to make the IPS
     */
    private static final int TEXT_SIZE = 50;

    /**
     * Constructor
     * @param sv - the surface view on which the bounding boxes are placed
     */
    public BoundingBoxManager(SurfaceView sv)
    {

        mBaseSurface = sv;
        mBaseSurface.setZOrderOnTop(true);
        mBaseSurface.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder = mBaseSurface.getHolder();
        mBoundingBoxList = new ArrayList<>();
        mListLock = new Semaphore(1);
        mIPS = 0;
        mTextPaint = new Paint();
        mTextPaint.setTextSize(TEXT_SIZE);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    public void addBoundingBox(RectF box, int c, int duration, String label){
        try {
            mListLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "Error in box creation: " + e.toString());
        }
        mBoundingBoxList.add(new BoundingBox(box, c, label, duration));
        Log.d(TAG, "Adding bounding box: ");
        mListLock.release();
    }

    private void updateBoxList(){
        ArrayList<BoundingBox> toRemove = new ArrayList<>();
        try {
            mListLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "Error exception: " + e.toString());
        }
        for(BoundingBox b: mBoundingBoxList)
        {
            if(!b.isActive()){
                toRemove.add(b);
                Log.d(TAG, "Removing bounding box");
            }
        }
        mBoundingBoxList.removeAll(toRemove);
        mListLock.release();
    }

    public void drawBoxes()
    {
        updateBoxList();
        Canvas c = mSurfaceHolder.lockCanvas();
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        c.drawText("IPS: " + mIPS,0,TEXT_SIZE,mTextPaint);
        try {
            mListLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "Error drawing boxes: " + e.toString());
        }
        for(BoundingBox b : mBoundingBoxList)
        {
            Log.i(TAG, "Drawing bounding box: " + b.toString());
            b.drawBox(c);
        }
        mListLock.release();
        mSurfaceHolder.unlockCanvasAndPost(c);
    }

    public void setIPS(double ips)
    {
        mIPS = ips;
    }
}
