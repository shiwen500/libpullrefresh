package www.seven.com.pullrefresh.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

/**
 * Created by sunyun004_macmini on 16/6/15.
 */
public class ElasticListView extends GridView implements AbsListView.OnScrollListener, View.OnTouchListener{

    private static int SCROLLING_UP = 1;
    private static int SCROLLING_DOWN = 2;

    private int mScrollState;
    private int mScrollDirection;
    private int mTouchedIndex;

    private View touchedView;

    private int mScrollOffset;
    private int mStartScrollOffset;

    private boolean mAnimate;


    public ElasticListView(Context context) {
        super(context);
        init();
    }

    public ElasticListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ElasticListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        mScrollState = SCROLL_STATE_IDLE;
        mScrollDirection = 0;
        mStartScrollOffset = -1;
        mTouchedIndex = Integer.MAX_VALUE;
        mAnimate = true;
        this.setOnTouchListener(this);
        this.setOnScrollListener(this);

    }




    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mScrollState != scrollState){
            mScrollState = scrollState;

        }
        if(scrollState == SCROLL_STATE_IDLE){
            mStartScrollOffset = Integer.MAX_VALUE;
            mAnimate = true;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if(mScrollState == SCROLL_STATE_TOUCH_SCROLL && mAnimate){

            if(mStartScrollOffset == Integer.MAX_VALUE){
                touchedView = getChildAt(mTouchedIndex-firstVisibleItem);
                mStartScrollOffset = touchedView.getTop();
                return;
            }
            if(touchedView == null) return;
            Log.d("test", touchedView.getTop()+"px");
            mScrollOffset = touchedView.getTop() - mStartScrollOffset;
            if(mScrollOffset > 0){
                mScrollDirection = SCROLLING_UP;

            }else{
                mScrollDirection = SCROLLING_DOWN;
            }




            if(Math.abs(mScrollOffset) > 200){
                mAnimate = false;
            }
            Log.d("test", "direction:"+ (mScrollDirection == SCROLLING_UP ? "up" :"down")+", scrollOffset:"+mScrollOffset+", toucheId:"+mTouchedIndex + ", fvisible:"+ firstVisibleItem+", " +
                    "visibleItemCount:"+visibleItemCount+", " +
                    "totalCount:"+totalItemCount);
            int indexOfLastAnimatedItem = mScrollDirection == SCROLLING_DOWN ?
                    mTouchedIndex + visibleItemCount :
                    mTouchedIndex - visibleItemCount;

            //check for bounds
            if(indexOfLastAnimatedItem >= getChildCount()) {
                indexOfLastAnimatedItem = getChildCount()-1;
            }
            else if(indexOfLastAnimatedItem < 0){
                indexOfLastAnimatedItem = 0;
            }

            if(mScrollDirection == SCROLLING_DOWN){
                setAnimationForScrollingDown(mTouchedIndex, indexOfLastAnimatedItem, firstVisibleItem);
            }else{
                setAnimationForScrollingUp(mTouchedIndex, indexOfLastAnimatedItem, firstVisibleItem);
            }

        }
    }

    private void setAnimationForScrollingUp(int indexOfTouchedChild, int indexOflastAnimatedChild, int firstVisibleIndex) {
        for(int i = indexOfTouchedChild+1; i <= indexOflastAnimatedChild; i++){
            View v = getChildAt(i-firstVisibleIndex);
            v.setTranslationY((-1*mScrollOffset)-v.getTranslationY());
            v.animate().translationY(0).setDuration(300).setStartDelay(50*i).start();
        }
    }

    private void setAnimationForScrollingDown(int indexOfTouchedChild, int indexOflastAnimatedChild,  int firstVisibleIndex) {
        for(int i = indexOflastAnimatedChild; i > indexOfTouchedChild; i--){
            View v = getChildAt(i - firstVisibleIndex);
            v.setTranslationY((-1*mScrollOffset) - v.getTranslationY());
            v.animate().translationY(0).setDuration(300).setStartDelay(50*(i-indexOfTouchedChild)).start();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Rect rect = new Rect();
                int childCount = getChildCount();
                int[] listViewCoords = new int[2];
                getLocationOnScreen(listViewCoords);
                int x = (int) event.getRawX() - listViewCoords[0];
                int y = (int) event.getRawY() - listViewCoords[1];
                View child;
                for (int i = 0; i < childCount; i++) {
                    child = getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                        mTouchedIndex = getPositionForView(child); // This is your down view
                        break;
                    }
                }
                return false;

        }
        return false;

    }

}
