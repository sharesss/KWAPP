package widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import utils.SpannableUtils;


/**
 * Created by Administrator on 2016/7/21.
 */
public class FMEdittext extends RelativeLayout {
    private EditText ed_text;
    private int maxNum = 200;
    private int minNum = 200;
    private TextView tv_num;
    private TextWatcherCallBack mTextWatcherCallBack;
    private ImageView ivClear;

    public FMEdittext(Context context) {
        super(context);
        initView();
    }

    public FMEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FMEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_fm_edittext, this);
        ed_text = (EditText) findViewById(R.id.ed_text);
        ed_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNum)});
        tv_num = (TextView) findViewById(R.id.tv_num);


        ivClear = (ImageView) findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_text.setText("");
            }
        });
        ed_text.addTextChangedListener(new TextWatcherListene());
        setNumText();
    }

    public void showClear() {
        ivClear.setVisibility(VISIBLE);
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        this.minNum = maxNum;
        ed_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNum)});
        setNumText();
    }

    public void setHint(String hint) {
        ed_text.setHint(hint);
    }

    public String getTextVal() {
        return ed_text.getText().toString();
    }

    private void setNumText() {
        tv_num.setText(SpannableUtils.getSpannableLatterStr(String.valueOf(minNum), "/" + maxNum, getResources().getColor(R.color.font_main), 0f));
    }

    class TextWatcherListene implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable text) {

            minNum = maxNum - text.length();
            setNumText();
            ed_text.setSelection(text.length());
            if (mTextWatcherCallBack != null)
                mTextWatcherCallBack.afterTextChangedText(text.toString());
        }
    }

    public interface TextWatcherCallBack {
        public void afterTextChangedText(String string);
    }

    public void setTextWatcherCallBack(TextWatcherCallBack mTextWatcherCallBack) {
        this.mTextWatcherCallBack = mTextWatcherCallBack;
    }
}
