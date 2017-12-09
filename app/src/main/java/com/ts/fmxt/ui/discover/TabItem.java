package com.ts.fmxt.ui.discover;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ts.fmxt.R;

import utils.helper.ToastHelper;

/**
 * Created by kp on 2017/11/27.
 */

public class TabItem implements BaseViewItem ,View.OnClickListener{
    private String checktext;
    CallBack callBack;
    int o = 0;
    DiscoverDetailsActivity activity;
    private Resources resources;
    private ViewHolder viewHolder;
//    int state;
    public interface CallBack {
        void onitem(int postion,TabItem item);
    }

    public TabItem(CallBack callBack,DiscoverDetailsActivity activity) {
//        this.state = state;
        this.callBack = callBack;
        this.activity = activity;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        viewHolder = (ViewHolder) holder;
        resources = viewHolder.tv_my_story.getResources();
        viewHolder.tv_my_story.setOnClickListener(this);
        viewHolder.tv_project_return.setOnClickListener(this);
        viewHolder.tv_project_highlights.setOnClickListener(this);
        viewHolder.tv_project_schedule.setOnClickListener(this);
        //标签UI
//        viewHolder.llTemp.setVisibility(View.VISIBLE);
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    public void select(View view) {
        switch (view.getId()) {
            case R.id.tv_my_story:
                viewHolder.tv_my_story.setTextColor(resources.getColor(R.color.orange));
                viewHolder.tv_my_story_lin.setBackgroundColor(resources.getColor(R.color.orange));
                viewHolder.tv_project_return.setTextColor(resources.getColor(R.color.gray));
                viewHolder.tv_project_return_lin.setBackgroundColor(resources.getColor(R.color.gray_line));
                viewHolder.tv_project_highlights.setTextColor(resources.getColor(R.color.gray));
                viewHolder.tv_project_highlights_lin.setBackgroundColor(resources.getColor(R.color.gray_line));
                viewHolder.tv_project_schedule.setTextColor(resources.getColor(R.color.gray));
                viewHolder.tv_project_schedule_lin.setBackgroundColor(resources.getColor(R.color.gray_line));

                break;
            case R.id.tv_project_return://

                viewHolder.tv_my_story.setTextColor(resources.getColor(R.color.gray));
                viewHolder.tv_my_story_lin.setBackgroundColor(resources.getColor(R.color.gray_line));
                viewHolder.tv_project_return.setTextColor(resources.getColor(R.color.orange));
                viewHolder.tv_project_return_lin.setBackgroundColor(resources.getColor(R.color.orange));
                viewHolder.tv_project_highlights.setTextColor(resources.getColor(R.color.gray));
                viewHolder.tv_project_highlights_lin.setBackgroundColor(resources.getColor(R.color.gray_line));
                viewHolder.tv_project_schedule.setTextColor(resources.getColor(R.color.gray));
                viewHolder.tv_project_schedule_lin.setBackgroundColor(resources.getColor(R.color.gray_line));

                break;
            case R.id.tv_project_highlights://

                viewHolder.tv_my_story.setTextColor(resources.getColor(R.color.gray));
                viewHolder.tv_my_story_lin.setBackgroundColor(resources.getColor(R.color.gray_line));
                viewHolder.tv_project_return.setTextColor(resources.getColor(R.color.gray));
                viewHolder.tv_project_return_lin.setBackgroundColor(resources.getColor(R.color.gray_line));
                viewHolder.tv_project_highlights.setTextColor(resources.getColor(R.color.orange));
                viewHolder.tv_project_highlights_lin.setBackgroundColor(resources.getColor(R.color.orange));
                viewHolder.tv_project_schedule.setTextColor(resources.getColor(R.color.gray));
                viewHolder.tv_project_schedule_lin.setBackgroundColor(resources.getColor(R.color.gray_line));

                break;
            case R.id.tv_project_schedule://
//                if(state==1){
                    viewHolder.tv_my_story.setTextColor(resources.getColor(R.color.gray));
                    viewHolder.tv_my_story_lin.setBackgroundColor(resources.getColor(R.color.gray_line));
                    viewHolder.tv_project_return.setTextColor(resources.getColor(R.color.gray));
                    viewHolder.tv_project_return_lin.setBackgroundColor(resources.getColor(R.color.gray_line));
                    viewHolder.tv_project_highlights.setTextColor(resources.getColor(R.color.gray));
                    viewHolder.tv_project_highlights_lin.setBackgroundColor(resources.getColor(R.color.gray_line));
                    viewHolder.tv_project_schedule.setTextColor(resources.getColor(R.color.orange));
                    viewHolder.tv_project_schedule_lin.setBackgroundColor(resources.getColor(R.color.orange));
//                }



                break;
        }
    }
    @Override
    public void onClick(View view) {
        select(view);
        switch (view.getId()) {
            case R.id.tv_my_story:

                if (callBack != null) {
                    callBack.onitem(0,this);
                }
                break;
            case R.id.tv_project_return://

                if (callBack != null) {
                    callBack.onitem(1,this);
                }
                break;
            case R.id.tv_project_highlights://

                if (callBack != null) {
                    callBack.onitem(2,this);
                }
                break;
            case R.id.tv_project_schedule://
//                if (state == 1) {
                    if (callBack != null) {
                        callBack.onitem(3, this);
                    }
//                } else {
//                    ToastHelper.toastMessage(activity, "暂未发布项目更新");
//                }
                break;
        }
    }
    public class ViewHolder extends RecyclerViewHolder {
        TextView tv_my_story,tv_project_return,tv_project_highlights,tv_project_schedule,tv_my_story_lin,tv_project_return_lin,tv_project_highlights_lin,tv_project_schedule_lin;


        private ViewHolder(final View covert) {
            super(covert);
            tv_my_story = (TextView) covert.findViewById(R.id.tv_my_story);
            tv_project_return = (TextView) covert.findViewById(R.id.tv_project_return);
            tv_project_highlights = (TextView) covert.findViewById(R.id.tv_project_highlights);
            tv_project_schedule = (TextView) covert.findViewById(R.id.tv_project_schedule);
            tv_my_story_lin = (TextView) covert.findViewById(R.id.tv_my_story_lin);
            tv_project_return_lin = (TextView) covert.findViewById(R.id.tv_project_return_lin);
            tv_project_highlights_lin = (TextView) covert.findViewById(R.id.tv_project_highlights_lin);
            tv_project_schedule_lin = (TextView) covert.findViewById(R.id.tv_project_schedule_lin);

        }
    }
}
