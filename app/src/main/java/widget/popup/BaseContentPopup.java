package widget.popup;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author meteorshower
 * Add Title and Message
 */
public abstract class BaseContentPopup extends BasePopup {
	
	protected TextView tvTitle,tvMessage;
	protected EditText etMessage;

	public BaseContentPopup(Activity context) {
		super(context);
		findTitleByID();
		findMessageByID();
	}
	
	public BaseContentPopup(Activity context, int width, int height){
		super(context, width, height);
		findTitleByID();
		findMessageByID();
	}

	public abstract void findTitleByID();
	public abstract void findMessageByID();
	
	public void setTitle(int resources){
		tvTitle.setText(resources);
	}
	
	public void setTitle(String value){
		tvTitle.setText(value);
	}
	
	protected void setTitleColor(int color){
		tvTitle.setTextColor(getContext().getResources().getColor(color));
	}
	
	public void setMessage(int resources){
		tvMessage.setText(resources);
	}
	
	public void setMessage(String value){
		tvMessage.setText(value);
	}
	
	/** 文本显示样式 居中，左右对齐 */
	public void setMessageGravity(int gravity){
		tvMessage.setGravity(gravity);
	}
	
	public void setMessageColor(int color){
		tvMessage.setTextColor(getContext().getResources().getColor(color));
	}
}
