package www.seven.com.libpullrefresh.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import www.seven.com.libpullrefresh.R;
import www.seven.com.libpullrefresh.tools.MeasureTools;

/**
 * Created by sunyun004_macmini on 16/6/16.
 */
public class CustomLoadingView extends FrameLayout {

    SweepGradient sweepGradient;

    Paint paint;

    Paint paint1;

    RectF oval;

    private int progress;
//
//    private MyHandlerThread updateThread;
//
//    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            if (msg.obj instanceof Runnable) {
//                Runnable runnable = (Runnable) msg.obj;
//                runnable.run();
//            }
//        }
//    };
//
//    private void runOnMainThread(Runnable runnable) {
//
//        Message msg = Message.obtain();
//        msg.obj = runnable;
//
//        mainHandler.sendMessage(msg);
//    }
//
//    class MyHandlerThread extends Thread {
//
//        private boolean stop;
//
//        @Override
//        public void run() {
//
//            while (!stop) {
//
//                progress += 2;
//                runOnMainThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        postInvalidate();
//                    }
//                });
//
//                try {
//                    sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public void stopit() {
//            stop = true;
//        }
//    }

    public CustomLoadingView(Context context) {
        super(context);

        init();
    }

    public CustomLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CustomLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        int radius = Math.min(getMeasuredHeight()/2, getMeasuredWidth()/2);
        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;

//        canvas.save();
//        canvas.rotate(0, cx, cy);

        if (sweepGradient == null) {
            sweepGradient = new SweepGradient(cx, cy, Color.WHITE, Color.parseColor("#2233ee"));

        }

        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(MeasureTools.dp2px(getContext(), 3));
//            paint.setColor(Color.parseColor("#2233ee"));
            paint.setShader(sweepGradient);
//            paint.setStrokeCap(Paint.Cap.ROUND);
        }

        if (paint1 == null) {
            paint1 = new Paint();
            paint1.setAntiAlias(true);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setStrokeWidth(MeasureTools.dp2px(getContext(), 6));
            paint1.setColor(Color.parseColor("#ffffff"));
        }

        if (oval == null) {
            int off = MeasureTools.dp2px(getContext(), 6) / 2;
            oval = new RectF(cx-radius + off, cy-radius + off, cx+radius - off, cy+radius - off);
        }

        canvas.drawCircle(cx, cy, radius - MeasureTools.dp2px(getContext(), 3.0f), paint1);
        canvas.drawArc(oval, 0,progress*360.0f/100, false, paint);

//        canvas.restore();
    }

    public void setProgress(int progress) {
        this.progress = progress > 100? 100: progress;
        invalidate();
    }

    public void loading() {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        animation.setInterpolator(new LinearInterpolator());

        clearAnimation();
        setAnimation(animation);

        animation.start();

//        updateThread = new MyHandlerThread();
//        updateThread.start();
    }

//    public void stopLoading() {
//
//        if (updateThread != null) {
//            updateThread.stopit();
//        }
//
//        progress = 0;
//    }
}
