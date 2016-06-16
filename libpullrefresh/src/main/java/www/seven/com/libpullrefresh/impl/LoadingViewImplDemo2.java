package www.seven.com.libpullrefresh.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import www.seven.com.libpullrefresh.R;
import www.seven.com.libpullrefresh.interfaces.LoadingView;
import www.seven.com.libpullrefresh.tools.MeasureTools;
import www.seven.com.libpullrefresh.views.CustomLoadingView;

/**
 * Created by sunyun004_macmini on 16/6/16.
 */
public class LoadingViewImplDemo2 extends FrameLayout implements LoadingView {

    private CustomLoadingView progress;

    private TextView state;

    private TextView time;

    public LoadingViewImplDemo2(Context context) {
        super(context);

        init();
    }

    public LoadingViewImplDemo2(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public LoadingViewImplDemo2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingViewImplDemo2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        LayoutInflater.from(getContext()).inflate(R.layout.loading_view_demo, this, true);

        progress = (CustomLoadingView) findViewById(R.id.progress);
        state = (TextView) findViewById(R.id.state);
        time = (TextView) findViewById(R.id.time);
    }

    @Override
    public void loading() {

        state.setText("加载中...");

        progress.loading();
    }

    @Override
    public void loadComplete() {
        state.setText("加载完成");

        try {
            progress.getAnimation().cancel();
        } catch (Exception e) {

        }
        progress.clearAnimation();

//        progress.stopLoading();
    }

    @Override
    public void loadFail() {
        state.setText("加载失败");
    }

    @Override
    public void loadInit() {
        state.setText("下拉刷新");
    }

    @Override
    public void readyload() {
        state.setText("松开刷新");
    }

    @Override
    public void loadedTime(String timeDesc) {
        time.setText(timeDesc);
    }

    @Override
    public int getLoadViewHeight() {
        return MeasureTools.dp2px(getContext(), 60);
    }

    @Override
    public void publishProgress(int progress) {

        this.progress.setProgress(progress);
    }

    @Override
    public View asView() {
        return this;
    }
}
