package www.seven.com.libpullrefresh.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import www.seven.com.libpullrefresh.R;
import www.seven.com.libpullrefresh.interfaces.LoadingView;
import www.seven.com.libpullrefresh.tools.MeasureTools;
import www.seven.com.libpullrefresh.views.CustomLoadingView;

/**
 * Created by sunyun004_macmini on 16/6/16.
 */
public class LoadingViewImplDemp3 extends FrameLayout implements LoadingView {

    private CustomLoadingView progress;

    public LoadingViewImplDemp3(Context context) {
        super(context);

        init();
    }

    public LoadingViewImplDemp3(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public LoadingViewImplDemp3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingViewImplDemp3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        LayoutInflater.from(getContext()).inflate(R.layout.loading_view_demo3, this, true);

        progress = (CustomLoadingView) findViewById(R.id.progress);
    }

    @Override
    public void loading() {

        progress.loading();
    }

    @Override
    public void loadComplete() {

        try {
            progress.getAnimation().cancel();
        } catch (Exception e) {

        }

        progress.clearAnimation();
//        progress.stopLoading();
    }

    @Override
    public void loadFail() {

    }

    @Override
    public void loadInit() {

    }

    @Override
    public void readyload() {

    }

    @Override
    public void loadedTime(String timeDesc) {

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
