package www.seven.com.libpullrefresh.interfaces;

/**
 * Created by sunyun004_macmini on 16/6/13.
 * 唯一实现 PullToRefreshFrameLayout
 */
public interface PullToRefresh {

    void enableUpToLoad(boolean enable);

    void enableDownToRefresh(boolean enable);

    boolean isUpToLoadEnable();

    boolean isDownToRefreshEnable();

    void setOnRefreshListener(OnRefreshListener onRefreshListener);

    void finishRefresh();

    void finishLoad();
}
