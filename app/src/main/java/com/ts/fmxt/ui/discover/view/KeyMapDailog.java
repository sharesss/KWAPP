package com.ts.fmxt.ui.discover.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.fmxt.R;

import utils.SpannableUtils;


public class KeyMapDailog extends DialogFragment {

    //点击发表，内容不为空时的回调
    public SendBackListener sendBackListener;

    public interface SendBackListener {
        void sendBack(String inputText);
    }


    private String texthint;

    private Dialog dialog;
    private EditText inputDlg;
    private int numconut = 300;
    private String tag = null;
    private TextView num;

    public KeyMapDailog() {
    }


    @SuppressLint("ValidFragment")
    public KeyMapDailog(String texthint, SendBackListener sendBackListener) {//提示文字
        this.texthint = texthint;
        this.sendBackListener = sendBackListener;

    }

    @SuppressLint("ValidFragment")
    public KeyMapDailog(SendBackListener sendBackListener) {//提示文字
        this.texthint = texthint;
    }

    public void setTexthint(String texthint) {
        this.texthint = texthint;
        inputDlg.setHint(texthint);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View contentview = View.inflate(getActivity(), R.layout.comment_dialog_layout, null);
        dialog.setContentView(contentview);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.5f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        num = (TextView) contentview.findViewById(R.id.num);
        num.setText(SpannableUtils.getSpannableLatterStr("500", "/500", getResources().getColor(R.color.font_main), 0f));
        inputDlg = (EditText) contentview.findViewById(R.id.dialog_comment_content);
        inputDlg.setHint(texthint);
        final TextView tv_send = (TextView) contentview.findViewById(R.id.dialog_comment_send);
        inputDlg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 500) {
                    return;
                }
                if (s.length() > 0) {
                    tv_send.setBackgroundResource(R.drawable.corners_review_cansend);
                } else {
                    tv_send.setBackgroundResource(R.drawable.corners_review_send);
                }
                int tempNum = 500 - s.length();
                num.setText(SpannableUtils.getSpannableLatterStr(String.valueOf(tempNum), "/500", getResources().getColor(R.color.font_main), 0f));
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputDlg.getText().toString())) {
//                    ToastUtils.getInstance().showToast("输入内容为空奥");
                    Toast.makeText(getActivity(), "输入内容为空", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    sendBackListener.sendBack(inputDlg.getText().toString());
                }
            }
        });
        inputDlg.setFocusable(true);
        inputDlg.setFocusableInTouchMode(true);
        inputDlg.requestFocus();
        final Handler hanler = new Handler();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public InputMethodManager mInputMethodManager;

            @Override
            public void onDismiss(DialogInterface dialog) {
                hanler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideSoftkeyboard();
                    }
                }, 200);

            }
        });
        return dialog;
    }


    public void hideSoftkeyboard() {
        try {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {

        }
    }


}
