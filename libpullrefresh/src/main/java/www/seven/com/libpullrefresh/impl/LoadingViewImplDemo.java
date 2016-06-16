package www.seven.com.libpullrefresh.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import www.seven.com.libpullrefresh.interfaces.LoadingView;
import www.seven.com.libpullrefresh.tools.MeasureTools;

/**
 * Created by sunyun004_macmini on 16/6/13.
 */
public class LoadingViewImplDemo extends TextView implements LoadingView {
    public LoadingViewImplDemo(Context context) {
        super(context);

        init();
    }

    public LoadingViewImplDemo(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public LoadingViewImplDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingViewImplDemo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        setGravity(Gravity.CENTER);

        setBackgroundColor(Color.GRAY);

        setTextColor(Color.WHITE);

        setGravity(Gravity.CENTER);

        // 20 sp
        setTextSize(20);
    }

    @Override
    public void loading() {
        setText("加载中");
    }

    @Override
    public void loadComplete() {
        setText("加载完成");
    }

    @Override
    public void loadFail() {
        setText("加载失败");
    }

    @Override
    public void loadInit() {
        setText("下拉加载");
    }

    @Override
    public void readyload() {

    }

    @Override
    public void loadedTime(String timeDesc) {

    }

    @Override
    public int getLoadViewHeight() {
        return MeasureTools.dp2px(getContext(), 50);
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void publishProgress(int progress) {

        setText(progress + "%");
    }
}
