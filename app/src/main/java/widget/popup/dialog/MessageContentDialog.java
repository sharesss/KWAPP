package widget.popup.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.ts.fmxt.R;

import widget.popup.BaseDoubleEventPopup;
import widget.popup.PopupObject;


public class MessageContentDialog extends BaseDoubleEventPopup implements OnClickListener {


    public MessageContentDialog(Activity context) {
        super(context);

    }

    @Override
    public View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.popup_new_msg_content_view, null);
    }

    @Override
    public void onClick(View arg0) {
        PopupObject object = new PopupObject();
        switch (arg0.getId()) {
            case R.id.btn_done:
                if (this.eventListener != null) {
                    object.setWhat(1);
                    this.eventListener.onEventClick(object);
                }
                break;
            case R.id.btn_cancel:
                if (this.eventListener != null) {
                    object.setWhat(0);
                    this.eventListener.onEventClick(object);
                }
                break;
        }

        dismiss();
    }


    @Override
    public void findTitleByID() {
    }

    @Override
    public void findMessageByID() {
        tvMessage = (TextView) getView().findViewById(R.id.tv_message);
    }

    @Override
    public void findDoubleEventByID() {
        btnDoubleEvent = (Button) getView().findViewById(R.id.btn_cancel);
        btnDoubleEvent.setOnClickListener(this);
    }

    @Override
    public void findEventByID() {
        btnEvent = (Button) getView().findViewById(R.id.btn_done);
        btnEvent.setOnClickListener(this);
    }

    @Override
    public Animation loadAnim() {
        return getScaleAnimation();
    }

    @Override
    public View getStartAnimViewGroup() {
        return getView().findViewById(R.id.popup_parent_layout);
    }

    public void setMeesageTextColor(int color) {
        tvMessage.setTextColor(getResourcesColor(color));
    }

    public void setRithButtonText(int text){
        btnEvent.setText(getResourcesStr(text));
    }
    public void setLeftButtonText(int text){
        btnDoubleEvent.setText(getResourcesStr(text));
    }

    public void hiddeLeft(){
        btnDoubleEvent.setVisibility(View.GONE);
        getView().findViewById(R.id.v_line).setVisibility(View.GONE);
    }
}
