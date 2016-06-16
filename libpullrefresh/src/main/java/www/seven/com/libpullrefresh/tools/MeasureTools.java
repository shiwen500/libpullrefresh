package www.seven.com.libpullrefresh.tools;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by sunyun004_macmini on 16/6/13.
 */
public class MeasureTools {

    public static int dp2px(Context context, float dp) {

        Resources resources = context.getResources();

        float density = resources.getDisplayMetrics().density;

        return (int)(density*dp + 0.5f);
    }
}
