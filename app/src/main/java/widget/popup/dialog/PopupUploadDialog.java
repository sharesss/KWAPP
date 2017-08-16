package widget.popup.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.TextView;

import com.ts.fmxt.R;

import widget.popup.BaseDoubleEventPopup;


/**
 * 上传动画加载
 */
public class PopupUploadDialog extends BaseDoubleEventPopup implements OnClickListener {

    private TextView tv_content;

    public PopupUploadDialog(Activity context) {
        super(context);
        tv_content= (TextView) getView().findViewById(R.id.tv_content);
    }

    @Override
    public View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.popup_upload_dialog_view, null);
    }

    @Override
    public void onClick(View arg0) {

    }

    @Override
    public void findTitleByID() {

    }

    @Override
    public void findMessageByID() {

    }

    @Override
    public void findDoubleEventByID() {

    }

    @Override
    public void findEventByID() {

    }

    @Override
    public Animation loadAnim() {
        return getScaleAnimation();
    }

    @Override
    public View getStartAnimViewGroup() {
        return getView().findViewById(R.id.popup_parent_layout);
    }

    public void setContext(int resourcesId){
        tv_content.setText(getContext().getResources().getString(resourcesId));
    }



}
