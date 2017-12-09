package android.support.v7.widget;

import android.content.Context;
import android.view.View;

import com.ts.fmxt.R;

/**
 * Created by liangzhenxiong on 2017/12/9.
 */

public class OlLinearSmoothScroller extends LinearSmoothScroller {
    public OlLinearSmoothScroller(Context context) {
        super(context);
    }

    @Override
    protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
        final int dx = calculateDxToMakeVisible(targetView, getHorizontalSnapPreference());
        int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());
        final int distance = (int) Math.sqrt(dx * dx + dy * dy);
        final int time = calculateTimeForDeceleration(distance);
        if (time > 0) {
            dy = (int) (-targetView.getY() + targetView.getResources().getDimension(R.dimen.tap_h));
            action.update(-dx, -dy, time, mDecelerateInterpolator);
        }
    }

}
