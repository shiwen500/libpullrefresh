package www.seven.com.libpullrefresh.interfaces;

import android.view.View;

/**
 * Created by sunyun004_macmini on 16/6/13.
 */
public interface LoadingView {

    void loading();

    void loadComplete();

    void loadFail();

    void loadInit();

    void readyload();

    void loadedTime(String timeDesc);

    int getLoadViewHeight();

    void publishProgress(int progress);

    View asView();
}
