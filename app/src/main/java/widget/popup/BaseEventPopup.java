package widget.popup;


import android.app.Activity;
import android.widget.Button;

/**
 * @author meteorshower
 * A Click Event
 */
public abstract class BaseEventPopup extends BaseContentPopup {
	
	protected Button btnEvent,btnShare;
	protected onEventClickListener eventListener;

	public BaseEventPopup(Activity context) {
		super(context);
		findEventByID();
	}
	
	public BaseEventPopup(Activity context, int width, int height){
		super(context, width, height);
		findEventByID();
	}
	
	public abstract void findEventByID();

	public void setEventText(int resources){
		btnEvent.setText(resources);
	}
	
	public void setEventText(String value){
		btnEvent.setText(value);
	}
	
	public void setEventColor(int color){
		btnEvent.setTextColor(getContext().getResources().getColor(color));
	}
	
	public void setOnEventClickListener(onEventClickListener listener){
		this.eventListener = listener;
	}
	
	public interface onEventClickListener{
		public void onEventClick(PopupObject obj);
	}

}
