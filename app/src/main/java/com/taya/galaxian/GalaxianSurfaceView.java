package com.taya.galaxian;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static java.lang.Thread.sleep;

public class GalaxianSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    public boolean DEBUG = false;
    public boolean isStart;
    private SurfaceHolder holder;
    private Paint paint;
    private Canvas canvas;
    private Context mContext;
    private int screenWidth,screenHeight;
    private int pointX,pointY;
    private String TAG = "MyView";
    private Thread myThread;
    Bitmap myBee;

    public GalaxianSurfaceView(Context context) {
        super(context);
        mContext = context;
        holder = this.getHolder();
        holder.addCallback(this);
        paint = new Paint();
        myBee = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.bee);
        setFocusable(true);
        myThread = new Thread(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isStart = true;
        screenHeight = this.getHeight();
        screenWidth = this.getWidth();
        pointX = screenWidth/2;
        pointY = screenHeight/2;
        myThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        isStart = false;
        screenHeight = this.getHeight();
        screenWidth = this.getWidth();
        pointX = screenWidth/2;
        pointY = screenHeight/2;
        isStart = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isStart = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!isStart){
                    break;
                }
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(getResources().getColor(android.R.color.background_dark));
                    paint.setColor(getResources().getColor(android.R.color.holo_red_light));
                    canvas.drawLine(0,0,screenWidth,screenHeight,paint);
                    canvas.drawBitmap(myBee,pointX-25,pointY-50,paint);
                    sleep(20);
                }
            } catch (Exception e) {
                Log.e(TAG, "thread draw error");
            } finally {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointX = (int) event.getX();
        pointY = (int) event.getY();
        if (DEBUG) {
            Log.i(TAG, "onTouchEvent,x = " + pointX + ",y = " + pointY);
        }
        return true;
    }
}
