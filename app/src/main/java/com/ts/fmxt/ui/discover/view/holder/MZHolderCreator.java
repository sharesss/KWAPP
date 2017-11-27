package com.ts.fmxt.ui.discover.view.holder;

/**
 * Created by kp on 17/11/22.
 */

public interface MZHolderCreator<VH extends MZViewHolder> {
    /**
     * 创建ViewHolder
     * @return
     */
    public VH createViewHolder();
}
