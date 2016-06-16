package www.seven.com.libpullrefresh.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import www.seven.com.libpullrefresh.interfaces.RefreshView;

/**
 * Created by sunyun004_macmini on 16/6/13.
 */
public class RefreshableListView extends ListView implements RefreshView {
    public RefreshableListView(Context context) {
        super(context);
    }

    public RefreshableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean readyForDownToRefresh() {
        return isFirstItemVisible();
    }

    @Override
    public boolean readyForUpToLoad() {
        return isLastItemVisible();
    }

    private boolean isFirstItemVisible() {
        final Adapter adapter = getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (getChildCount() > 0) ? getChildAt(0).getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }

    private boolean isLastItemVisible() {
        final Adapter adapter = getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - getFirstVisiblePosition();
            final int childCount = getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= getBottom();
            }
        }

        return false;
    }

    @Override
    public View asView() {
        return this;
    }
}
