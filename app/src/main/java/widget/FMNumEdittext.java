package widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import utils.SpannableUtils;
import utils.StringUtils;
import utils.helper.UIHelper;


/**
 * Created by Administrator on 2016/11/10.
 */
public class FMNumEdittext extends RelativeLayout {
    private EditText ed_text;
    private int maxNum = 200;
    private int minNum = 200;
    public TextView tv_num;
    private FMEdittext.TextWatcherCallBack mTextWatcherCallBack;
    private ImageView ivClear;
    private RelativeLayout rl_input;
    private boolean flgClear = false;
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("data");
                    ed_text.setText(data);
                    minNum = maxNum - data.length();
                    setNumText();
                    ed_text.setSelection(data.length());
                    if (flgClear && !StringUtils.isEmpty(data)) {
                        ivClear.setVisibility(View.VISIBLE);
                    } else {
                        ivClear.setVisibility(View.GONE);
                    }
                    break;
            }
            super.handleMessage(msg);

        }
    };

    public FMNumEdittext(Context context) {
        super(context);
        initView();
    }

    public FMNumEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FMNumEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_fm_numedittext, this);
        ed_text = (EditText) findViewById(R.id.ed_text);
        ed_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNum)});
        tv_num = (TextView) findViewById(R.id.tv_num);
        rl_input = (RelativeLayout) findViewById(R.id.rl_input);

        ivClear = (ImageView) findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_text.setText("");
            }
        });
        ed_text.addTextChangedListener(new FMNumEdittext.TextWatcherListene());
        setNumText();
    }

    public void showClear() {
        flgClear = true;
      //  ivClear.setVisibility(VISIBLE);
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
            if (flgClear && !StringUtils.isEmpty(text.toString())) {
                ivClear.setVisibility(View.VISIBLE);
            } else {
                ivClear.setVisibility(View.GONE);
            }
            minNum = maxNum - text.length();
            setNumText();
            // ed_text.setSelection(text.length());
            if (mTextWatcherCallBack != null)
                mTextWatcherCallBack.afterTextChangedText(text.toString());
        }
    }

    public void setText(String text) {
        Bundle bundle = new Bundle();
        bundle.putString("data", text);
        Message message = new Message();
        message.what = 0;
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public interface TextWatcherCallBack {
        public void afterTextChangedText(String string);
    }

    public void setTextWatcherCallBack(FMEdittext.TextWatcherCallBack mTextWatcherCallBack) {
        this.mTextWatcherCallBack = mTextWatcherCallBack;
    }

    public void setInPutBackground(Drawable drawable) {
        rl_input.setBackground(drawable);
    }

    public void setEdTextHigth(int higth) {
        ViewGroup.LayoutParams lp = ed_text.getLayoutParams();
        lp.height = UIHelper.dipToPx(getContext(), higth);
        ed_text.setLayoutParams(lp);
    }
}
