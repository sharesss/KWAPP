package utils.dragtop;

import android.content.Context;
import android.util.AttributeSet;

import com.thindo.base.Widget.refresh.base.RefreshBase;


/**
 * @author meteorshower
 * Description : Used for Android-PullToRefresh
 * DragTopLayout
 */
public class RefreshDragTopLayout extends RefreshBase<DragTopLayout> {

    public RefreshDragTopLayout(Context context) {
        super(context);
    }

    public RefreshDragTopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getRefreshableView().onFinishInflate();
    }

    @Override
    protected DragTopLayout createRefreshableView(Context context, AttributeSet attrs) {
        return new DragTopLayout(context, attrs);
    }

    @Override
    protected boolean isReadyForPullStart() {
        DragTopLayout refreshableView = getRefreshableView();
        return refreshableView.getState() == DragTopLayout.PanelState.EXPANDED;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return false;
    }
    
}


