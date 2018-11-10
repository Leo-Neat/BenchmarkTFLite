package com.leo.neat.benchmark_tf_lite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.Log;
import android.view.TextureView;

class BoundingBox {

    /**
     * Tag used for logging errors and debugging
     */
    private static final String TAG = "Bounding Box";

    /**
     * The stroke width of the bounding boxes to be drawn
     */
    private static int STROKE_WIDTH = 5;

    /**
     * The text size of the labels to be drawn
     */
    private static int TEXT_SIZE = 50;

    /**
     * Rectangle that is the underlying size of the box in percentages
     * (top left x %, top left y%, bottom right x %, bottom right y %)
     */
    private RectF mBoxRect;

    /**
     * The color the box is to be drawn
     */
    private Paint mBoxPaint;

    /**
     * The String label of the box
     */
    private String mBoxLabel;

    /**
     * The amount of time in ms the box should stay alive
     */
    private long mLifespan;

    /**
     * The time of boxes creation
     */
    private long mStartTime;

    /**
     * The paint that will be used for drawing text on the screen
     */
    private Paint mTextPaint;

    /**
     * The amount of time in ms that the box is to be shown
     */

    /**
     *
     * @param box - the rectangle t
     * @param color - the color of the box
     * @param label - the string that identifys the box
     * @param duration - the amount of time in ms the box is shown
     */
    public BoundingBox(RectF box, int color, String label, long duration)
    {
        mTextPaint = new Paint();
        mTextPaint.setTextSize(TEXT_SIZE);
        mTextPaint.setColor(Color.CYAN);
        mTextPaint.setStyle(Paint.Style.FILL);
        mBoxRect = box;
        mBoxLabel = label;
        mLifespan = duration;
        mBoxPaint = new Paint();
        mBoxPaint.setColor(color);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(STROKE_WIDTH);
        mStartTime = System.currentTimeMillis();
    }

    public boolean isActive(){
        Log.d(TAG, "Life time: " + (mStartTime+mLifespan) + " current time: " + System.currentTimeMillis());
        Log.d(TAG, "Is alive: " + (mStartTime + mLifespan  > System.currentTimeMillis()));
        return mStartTime + mLifespan  > System.currentTimeMillis();
    }

    public void drawBox(Canvas c)
    {
        int canvasWidth = c.getWidth();
        int canvasHeight = c.getHeight();
        int topleftx = (int)(canvasWidth * mBoxRect.left);
        int toplefty = (int)(canvasHeight * mBoxRect.top);
        int bottomrightx = (int)(canvasWidth * mBoxRect.right);
        int bottomrighty = (int)(canvasHeight * mBoxRect.bottom);
        c.drawRect(new Rect(topleftx, toplefty,bottomrightx,bottomrighty ), mBoxPaint);
        c.drawText(mBoxLabel,topleftx,toplefty + TEXT_SIZE, mTextPaint);
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "mBoxRect=" + mBoxRect.toString() +
                ", mBoxPaint=" + mBoxPaint +
                ", mBoxLabel='" + mBoxLabel + '\'' +
                ", mLifespan=" + mLifespan +
                ", mStartTime=" + mStartTime +
                '}';
    }
}
