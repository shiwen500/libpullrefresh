package www.seven.com.libpullrefresh.interfaces;

import android.view.View;

/**
 * Created by sunyun004_macmini on 16/6/13.
 * 实现的类 RefreshableListView
 */
public interface RefreshView {

    boolean readyForDownToRefresh();

    boolean readyForUpToLoad();

    View asView();
}
