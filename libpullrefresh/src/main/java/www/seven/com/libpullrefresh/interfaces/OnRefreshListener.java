package www.seven.com.libpullrefresh.interfaces;

import android.view.View;

/**
 * Created by sunyun004_macmini on 16/6/13.
 */
public interface OnRefreshListener {

    void onUpToLoad(View view);

    void onDownToRefresh(View view);
}
