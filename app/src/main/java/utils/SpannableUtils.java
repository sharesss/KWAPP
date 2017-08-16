package utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.ts.fmxt.FmxtApplication;


public class SpannableUtils {
	
	/** 换行后半部分变动颜色字体大小 */
	public static SpannableString getSpannableStr(int textId,Object obj,int color,float size){
		return getSpannableStr(FmxtApplication.getContext().getResources().getString(textId), obj, color, size);
	}
	
	/** 不换行后半部分变动颜色字体大小 */
	public static SpannableString getSpannableLatterStr(String text,String obj,int color,float size){
		StringBuffer sb = new StringBuffer();
		sb.append(text);
		sb.append(obj);
		return formatSpannable(sb.toString(), text.length(), sb.toString().length(), color,size);
	}
	
	/** 换行后半部分变动颜色字体大小 */
	public static SpannableString getSpannableStr(String text,Object obj,int color,float size){
		StringBuffer sb = new StringBuffer();
		sb.append(text);
		sb.append("\n");
		sb.append(obj);
		return formatSpannable(sb.toString(), text.length(), sb.toString().length(), color, size);
	}

	/**
	 * 
	 * 
	 * @param value
	 * @param startNum
	 * @param endNum
	 * @param color
	 * @param size
	 * @return
	 */
	public static SpannableString formatSpannable(String value, int startNum,
			int endNum, int color, float size) {
		SpannableString msp = new SpannableString(value);
		
		if (size != 0)
			msp.setSpan(new RelativeSizeSpan(size), startNum, endNum,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (color != 0)
			msp.setSpan(new ForegroundColorSpan(color), startNum, endNum,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}
	
	/**
	 * 改变文本指定长度文本的字体大小及颜色 本方法只适用于改变文本指定同一长度样式 color 及 size 0 则不参与执行样式改变,是否加粗
	 * @param value
	 * @param startNum
	 * @param endNum
	 * @param color
	 * @param
	 * @param
	 * @return
	 */
	public static SpannableString formatSpannableOverstriking(String value, int startNum,int endNum, int color) {
		SpannableString msp = new SpannableString(value); 
		msp.setSpan(new StyleSpan(Typeface.BOLD), startNum, endNum, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new ForegroundColorSpan(color), startNum, endNum,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}
	
	/**
	 * 改变文本指定多组长度文本的字体大小及颜色
	 * 
	 * @param value
	 * @param startNums
	 * @param endNums
	 * @param colors
	 * @param sizes
	 * @return
	 */
	public static SpannableString formatSpannable(String value,int[] startNums,int[] endNums,int[] colors,float[] sizes){
		SpannableString msp = new SpannableString(value);
		
		if(startNums.length == endNums.length){
			for(int i = 0 ; i < startNums.length ; i++){
				if(colors != null && colors.length > 0){
					if(colors.length == 1){
						msp.setSpan(new ForegroundColorSpan(colors[0]), startNums[i], endNums[i],
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}else if(colors.length == startNums.length){
						msp.setSpan(new ForegroundColorSpan(colors[i]), startNums[i], endNums[i],
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				} 
				if(sizes != null && sizes.length > 0){
					if(colors.length == 1){
						msp.setSpan(new RelativeSizeSpan(sizes[0]), startNums[i], endNums[i],
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}else if(sizes.length == startNums.length){
						msp.setSpan(new RelativeSizeSpan(sizes[i]), startNums[i], endNums[i],
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		
		return msp;
	}


}
