package widget.picturebrows;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.thindo.base.Utils.UIHelper;
import com.ts.fmxt.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.StringUtils;
import widget.image.FMNetImageView;



public class DynamicPicturesView extends RelativeLayout {
	
	private FMNetImageView netIv;
	private LinearLayout llPage;
	private DynamicMode mode = DynamicMode.DYNAMIC_SHARE_IMAGE;
	private List<Object> arrayList = new ArrayList<Object>();
	private OnPictureItemClickListener listener;
	private TextView tvName;

	public DynamicPicturesView(Context context) {
		super(context);
		init();
	}

	public DynamicPicturesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DynamicPicturesView(Context context, AttributeSet attrs,
                               int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private void init(){
		LayoutInflater.from(getContext()).inflate(R.layout.widget_dynamic_picture_view, this);
		
		netIv = (FMNetImageView) findViewById(R.id.net_image);
		llPage = (LinearLayout) findViewById(R.id.ll_page_layout);
		tvName=(TextView) findViewById(R.id.stock_name);
		formatPictureMode();
	}
	
	//切换模式
	private void formatPictureMode(){
		switch(mode){
		case DYNAMIC_SHARE_IMAGE:
			netIv.setVisibility(View.VISIBLE);
			llPage.setVisibility(View.GONE);
			break;
		case DYNAMIC_CUSTOM_GRID:
			netIv.setVisibility(View.GONE);
			llPage.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	/** 数据初始化 */
	public void setAdapter(Object value){
		if(value != null){
			arrayList.clear();
			arrayList.add(value);
			picturesData();
		}
	}
	
	/** 数据初始化 */
	public void setAdapter(String url, String host){
		setAdapter(new String[]{url}, host);
	}
	
	/** 数据初始化 */
	public void setAdapter(String[] arraysUrl){
		setAdapter(arraysUrl, null);
	}
	
	/** 数据初始化 */
	public void setAdapter(String[] arraysUrl, String host){
		
		if(arraysUrl == null || arraysUrl.length == 0)
			return;
		
		arrayList.clear();
		if(StringUtils.isEmpty(host))
			arrayList.addAll(Arrays.asList(arraysUrl));
		else{
			StringBuffer sb = new StringBuffer();
			for(String url : arraysUrl){
				sb.setLength(0);
				sb.append(host);
				sb.append(url);
				arrayList.add(sb.toString());
			}
		}
		
		picturesData();
	}
	
	private void picturesData(){
		
		switch(mode){
		case DYNAMIC_SHARE_IMAGE:
			if(arrayList.size() > 0){
				netIv.loadImage(String.valueOf(arrayList.get(0)));
				netIv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(listener != null)
							listener.onPictureItemClick(0, arrayList.get(0),arrayList.toString());
					}
				});
			}
			break;
		case DYNAMIC_CUSTOM_GRID:
				PicturesGridAdapter pictureAdapter = new PicturesGridAdapter(getContext(), arrayList);
				llPage.removeAllViews();
				for(int i = 0 ; i < arrayList.size() ; i++){
					View v = pictureAdapter.getView(i, null, null);
					v.setOnClickListener(new OnPictureItemClick(i, arrayList.get(i),arrayList));
					llPage.addView(v);
				}
			break;
		}
	}
	
	private class OnPictureItemClick implements OnClickListener {
		
		private int position;
		private Object obj;
		private List<Object> arrayList;
		
		
		public OnPictureItemClick(int position, Object obj, List<Object> arrayList){
			this.position = position;
			this.obj = obj;
			this.arrayList=arrayList;
		}

		@Override
		public void onClick(View v) {
			if(listener != null)
				listener.onPictureItemClick(position, obj,arrayList.toString());
		}
		
	}
	
	/** 添加图片Url */
	public void addPictureUrl(String url){
		addPictureUrl(url, null);
	}
	
	/** 添加图片Url */
	public void addPictureUrl(String url, String host){
		
		if(!StringUtils.isEmpty(host))
			url = String.format("%s%s", host,url);
			
		switch(mode){
		case DYNAMIC_SHARE_IMAGE:
			arrayList.clear();
			arrayList.add(url);
			break;
		case DYNAMIC_CUSTOM_GRID:
			if(arrayList.size() > 0){
				Object obj = arrayList.get(arrayList.size() - 1);
				if(obj instanceof Integer)
					arrayList.add(arrayList.size() - 1, url);
				else
					arrayList.add(url);
			}else{
				arrayList.add(url);
			}
			break;
		}
		
		picturesData();
	}
	
	/** 设置图片显示模式 */
	public void setMode(DynamicMode mode){
		this.mode = mode;
		formatPictureMode();
		picturesData();
	}
	
	public enum DynamicMode{
		
		DYNAMIC_SHARE_IMAGE(0),
		DYNAMIC_CUSTOM_GRID(1);
		
		private int value;
		
		DynamicMode(int value){
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}
	}
	
	/** 获取图片Url数组 */
	public String[] getArrays(){

		if(arrayList.size() == 0)
			return null;
		
		switch(mode){
		case DYNAMIC_SHARE_IMAGE:
			return new String[]{String.valueOf(arrayList.get(0))};
		case DYNAMIC_CUSTOM_GRID:
			Object obj = arrayList.get(arrayList.size() - 1);
			int num = obj instanceof Integer ? arrayList.size() - 1 : arrayList.size();
			String[] arrays = new String[num];
			for(int i = 0 ; i < num; i++){
				arrays[i] = String.valueOf(arrayList.get(i));
			}
			return arrays;
		}
		
		return null;
	}
	
	private class PicturesGridAdapter extends FMBaseAdapter<Object> {
		
		private int widthItem;

		public PicturesGridAdapter(Context context, List<Object> arrayList) {
			super(context, arrayList);
			
			widthItem = UIHelper.dipToPx(getContext(), 80);
		}
		
		@Override
		public int getCount() {
			if(super.getCount() > 3)
				return 3;
			return super.getCount();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			FMNetImageView iv;
			if(convertView == null){
				iv = new FMNetImageView(getContext());
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthItem, widthItem);
				lp.setMargins(position == 0 ? 0 : UIHelper.dipToPx(getContext(), 8),0,0,0);
				iv.setLayoutParams(lp);
				iv.setScaleType(ScaleType.FIT_XY);
				iv.setFocusable(false);
				
				convertView = iv;
			}else{
				iv = (FMNetImageView) convertView;
			}
			
			Object obj = getItem(position);
			if(obj instanceof String)
				iv.loadImage(String.valueOf(obj));
			else if(obj instanceof Integer){
				iv.setImageResource(Integer.parseInt(String.valueOf(obj)));
			}
			
			return convertView;
		}
		
	}
	
	public void setOnPictureItemClickListener(OnPictureItemClickListener listener){
		this.listener = listener;
	}
	
	public interface OnPictureItemClickListener{
		
		public void onPictureItemClick(int position, Object value, String arrayList);
	}
	
	public void setStockName(String stockName, String stockCode){
		if(tvName==null||stockCode==null)
			return;
		stockCode=stockCode.replaceAll("index_", "");
		tvName.setText(String.format("%1s(%2s)", stockName,stockCode));
	}
	
	public void clearStockName(){
		 
		tvName.setText("");
	}

}
