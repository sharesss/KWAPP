package widget.popup;

import android.app.Activity;
import android.widget.Button;

/**
 * @author meteorshower
 * Double Click Event
 */
public abstract class BaseDoubleEventPopup extends BaseEventPopup {
	
	protected Button btnDoubleEvent;
	protected OnDoubleEventClickListener doubleEventListener;

	public BaseDoubleEventPopup(Activity context) {
		super(context);
		findDoubleEventByID();
	}
	
	public abstract void findDoubleEventByID();

	public void setDoubleEventText(int resources){
		btnDoubleEvent.setText(resources);
	}
	
	public void setDoubleEventText(String value){
		btnDoubleEvent.setText(value);
	}
	
	public void setDoubleEventColor(int color){
		btnDoubleEvent.setTextColor(getContext().getResources().getColor(color));
	}
	
	public void setOnDoubleEventClickListener(OnDoubleEventClickListener listener){
		this.doubleEventListener = listener;
	}
	
	public interface OnDoubleEventClickListener{
		
		public void onDoubleEventClick(PopupObject obj);
	}

}
