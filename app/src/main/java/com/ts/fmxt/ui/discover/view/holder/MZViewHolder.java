package com.ts.fmxt.ui.discover.view.holder;

import android.content.Context;
import android.view.View;

/**
 * Created by kp on 17/11/22.
 */

public interface MZViewHolder<T> {
    /**
     *  创建View
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data);
}
