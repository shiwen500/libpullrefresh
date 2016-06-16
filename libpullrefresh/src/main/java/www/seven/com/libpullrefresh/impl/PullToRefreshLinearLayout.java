package www.seven.com.libpullrefresh.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.text.SimpleDateFormat;
import java.util.Date;

import www.seven.com.libpullrefresh.interfaces.LoadingView;
import www.seven.com.libpullrefresh.interfaces.OnRefreshListener;
import www.seven.com.libpullrefresh.interfaces.PullToRefresh;
import www.seven.com.libpullrefresh.interfaces.RefreshView;

/**
 * Created by sunyun004_macmini on 16/6/13.
 */
public class PullToRefreshLinearLayout extends LinearLayout implements PullToRefresh {

    private static final String TAG = "PullToRefresh";

    private boolean init;

    private boolean upToLoad = true;

    private boolean downToRefresh = true;

    private OnRefreshListener mOnRefreshListener;

    private LoadingView mLoadingView;

    private LoadingView mLoadingViewFooter;

    private RefreshView mRefreshView;

    private float lastMotionEventY;

    private Scroller mScroller;

    private boolean refreshing;

    private long lastLoadTimeStamp;

    public PullToRefreshLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public PullToRefreshLinearLayout(Context context) {
        super(context);

    }

    public PullToRefreshLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullToRefreshLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (!init) {
            init();
            init = true;
        }

        /**
         * As we're currently in a Layout Pass, we need to schedule another one
         * to layout any changes we've made here
         */
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }



    private void init() {

        setOrientation(LinearLayout.VERTICAL);

        myassert(getChildCount() == 1, "刷新的View数量应该是1个，现在是" + getChildCount() + "个");

        View refreshContent = getChildAt(0);

        myassert(refreshContent instanceof RefreshView, "刷新的View不是RefreshView");

        mRefreshView = (RefreshView) refreshContent;

        mLoadingView = new LoadingViewImplDemo2(getContext());

        mLoadingViewFooter = new LoadingViewImplDemp3(getContext());

        mLoadingView.asView().setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, mLoadingView.getLoadViewHeight()));
        mLoadingViewFooter.asView().setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, mLoadingViewFooter.getLoadViewHeight()));

        // 头部刷新的view
        addView(mLoadingView.asView(), 0);
        addView(mLoadingViewFooter.asView());

        setPadding(0, -mLoadingView.getLoadViewHeight(), 0, 0);


        mScroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());

    }

    private void myassert(boolean bool, String msg) {
        if (!bool) {
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void enableUpToLoad(boolean enable) {
        upToLoad = enable;
    }

    @Override
    public void enableDownToRefresh(boolean enable) {
        downToRefresh = enable;
    }

    @Override
    public boolean isUpToLoadEnable() {
        return upToLoad;
    }

    @Override
    public boolean isDownToRefreshEnable() {
        return downToRefresh;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();


        switch (action) {

            case MotionEvent.ACTION_MOVE: {
                float deltaY = event.getY() - lastMotionEventY;

                Log.d(TAG, "ACTION_MOVE");

                if (mRefreshView.readyForDownToRefresh() && deltaY > 0 && getScrollY() <= 0) { // 下拉刷新

                    Log.d(TAG, "下拉刷新");

                    postScroll(deltaY / getDamping(getScrollY()));
                } else if (mRefreshView.readyForDownToRefresh() && deltaY < 0 && getScrollY() < 0) { // 下拉过程中 又上滑

                    // 如果是正在刷新，不允许在上滑
                    if (!refreshing) {
                        postScroll(deltaY / 2.5f);
                    }

                    Log.d(TAG, "下拉过程中 又上滑");
                } else if (mRefreshView.readyForUpToLoad() && deltaY < 0 && getScrollY() >= 0) { // 上拉加载
                    postScroll2(deltaY / getDamping(getScrollY()));
                } else if (mRefreshView.readyForUpToLoad() && deltaY > 0 && getScrollY() > 0) { // 上拉后 又下滑
                    // 如果是正在刷新，不允许再下滑
                    Log.d(TAG, "上拉后 又下滑");
                    if (!refreshing) {
                        postScroll2(deltaY / 2.5f);
                    } else {
                        Log.d(TAG, "不允许再下滑");
                    }
                } else {

                    Log.d(TAG, "listview 滑动了,什么事也不做");

                }

                lastMotionEventY = event.getY();

                break;
            }

            case MotionEvent.ACTION_UP: {
                Log.d(TAG, "ACTION_UP");
                if (shouldRefresh()) {

                    if (!refreshing) {
                        Log.d(TAG, "开始刷新");



                        refreshing = true;
                        mLoadingView.loading();

                        if (mOnRefreshListener != null) {

                            mOnRefreshListener.onDownToRefresh(mRefreshView.asView());

                        }
                    }

                    mScroller.startScroll(0, getScrollY(), 0, -mLoadingView.getLoadViewHeight() - getScrollY(), 200);
                    postInvalidate();
                } else if (shouldLoad()) {

                    if (!refreshing) {
                        Log.d(TAG, "开始加载");
                        mLoadingViewFooter.loading();
                        refreshing = true;

                        if (mOnRefreshListener != null) {

                            mOnRefreshListener.onUpToLoad(mRefreshView.asView());

                        }
                    }

                    mScroller.startScroll(0, getScrollY(), 0, mLoadingViewFooter.getLoadViewHeight() - getScrollY(), 200);
                    postInvalidate();

                } else {

                    Log.d(TAG, "滑回去0");

                    mScroller.abortAnimation();
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 200);
                    postInvalidate();
                }

                break;
            }
        }

        return super.onTouchEvent(event);
    }

    private boolean shouldRefresh() {

        return -getScrollY() >= mLoadingView.getLoadViewHeight();
    }

    private boolean shouldLoad() {

        return getScrollY() >= mLoadingViewFooter.getLoadViewHeight();
    }


    private float getDamping(int scrollY) {

        float res = 2.5f + Math.abs(scrollY) / 100.0f;
        Log.d(TAG, res + "");
        return res;
    }

    private void postScroll(float deltaY) {
        int scrollY = getScrollY();

        if (deltaY < 0) { // 视窗向下,手势向上
            int willScroll = (int) -deltaY;
            if (scrollY + willScroll > 0) {
                scrollTo(0, 0);
            } else {
                scrollBy(0, willScroll);
            }
        } else { // 视窗向上，手势向下

            int willScroll = (int) -deltaY;

//            if (scrollY + willScroll < -mLoadingView.getLoadViewHeight()) {
//                scrollTo(0, -mLoadingView.getLoadViewHeight());
//            } else {
//                scrollBy(0, willScroll);
//            }

            scrollBy(0, willScroll);
        }
    }

    private void postScroll2(float deltaY) {
        int scrollY = getScrollY();

        if (deltaY < 0) {
            int willScroll = (int) -deltaY;
            scrollBy(0, willScroll);
        } else {
            int willScroll = (int) -deltaY;

            if (scrollY + willScroll < 0) {
                scrollTo(0, 0);
            } else {
                scrollBy(0, willScroll);
            }
        }
    }

    // 当返回true时，后续的MotionEvent会一直给这个viewgroup处理, 会一直将MotionEvent传递给onTouchEvent,
    // 知道action_up.
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent");
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 返回false, 说明不拦截action_down事件, action_down事件被子view处理后， action_down不会触发这个viewGroup 的 onTouchEvent
            // 所以需要在这里记录下action_down事件的位置
            lastMotionEventY = ev.getY();
            return false;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE){

            // 只有前面几次MotionEvent才会触发onInterceptTouchEvent
            // 所以当listview上滑一点，再下滑大幅度，也不会出现下拉刷新的情况。
            Log.d(TAG, "action_move onInterceptTouchEvent");
            float deltaY = ev.getY() - lastMotionEventY;
            if (mRefreshView.readyForDownToRefresh() && deltaY > 0) { // 下拉刷新

                // 下拉刷新的第一次事件是这里，所以在这里显示上次刷新的时间
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                String last = sdf.format(new Date(lastLoadTimeStamp));

                mLoadingView.loadedTime("上次刷新:" + last);

                return true;
            } else if (mRefreshView.readyForDownToRefresh() && deltaY < 0 && getScrollY() < 0) { // 下拉后 又上滑

                return true;
            } else if (mRefreshView.readyForUpToLoad() && deltaY < 0) { // 上拉加载
                return true;
            } else if (mRefreshView.readyForUpToLoad() && deltaY > 0 && getScrollY() > 0) { // 上拉后，下滑
                return true;
            } else {
                return false;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void computeScroll() {

        if (!mScroller.isFinished()) {

            if (mScroller.computeScrollOffset()) {

                int cY = mScroller.getCurrY();

                scrollTo(0, cY);

                postInvalidate();
                // invalidate(); 某些情况下无效
            }
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);

        if (!refreshing) {
            int progress = Math.abs(y) * 100 / mLoadingView.getLoadViewHeight();
            mLoadingView.publishProgress(progress);

            if (progress < 100) {
                mLoadingView.loadInit();
            } else if (progress >= 100) {

                mLoadingView.readyload();
            }

            int progress2 = Math.abs(y) * 100 / mLoadingViewFooter.getLoadViewHeight();
            mLoadingViewFooter.publishProgress(progress2);
        }
    }

    @Override
    public void finishLoad() {
        refreshing = false;
        mLoadingViewFooter.loadComplete();
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 200);
        postInvalidate();
    }

    @Override
    public void finishRefresh() {
        // 记录当前时间
        lastLoadTimeStamp = System.currentTimeMillis();
        refreshing = false;
        mLoadingView.loadComplete();
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 200);
        postInvalidate();
    }
}
