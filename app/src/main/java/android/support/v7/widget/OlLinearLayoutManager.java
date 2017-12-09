package android.support.v7.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by liangzhenxiong on 2017/12/9.
 */

public class OlLinearLayoutManager extends LinearLayoutManager {
    public OlLinearLayoutManager(Context context) {
        super(context);
    }

    public OlLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public OlLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        OlLinearSmoothScroller linearSmoothScroller =
                new OlLinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

}
