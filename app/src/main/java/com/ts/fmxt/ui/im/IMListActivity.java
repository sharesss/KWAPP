//package com.ts.fmxt.ui.im;
//
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Pair;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListView;
//
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMConversation;
//import com.thindo.base.NetworkAPI.BaseResponse;
//import com.thindo.base.NetworkAPI.OnResponseListener;
//import com.thindo.base.UI.Activity.BaseActivity;
//import com.ts.fmxt.R;
//import com.ts.fmxt.ui.ItemAdapter.IMListAdapter;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//
//import utils.ReceiverUtils;
//import utils.sharePreferences.FMWession;
//import widget.titlebar.NavigationView;
//
///**
// * IM环信消息列表
// */
//public class IMListActivity extends BaseActivity implements ReceiverUtils.MessageReceiver, AdapterView.OnItemClickListener, IMListAdapter.UserInfoInterface ,OnResponseListener {
//
//    private NavigationView mNavigationView;
//    private ListView mListView;
//    List<EMConversation> data = new ArrayList<>();
//    private IMListAdapter mAdapter;
//    private EditText query;
//    private ImageButton clearSearch;
//    protected InputMethodManager inputMethodManager;
//    private String keyworke = "";
//    public String userId;
//
//    @Override
//    public void onMessage(int receiverType, Bundle bundle) {
////        if (receiverType == ReceiverUtils.INDEX_IM) {
////            if (!FMWession.getInstance().isLogin()) {
////                List<EMConversation> tempData = loadConversationList();
////                data.clear();
////                if (!FMWession.getInstance().isLogin())
////                    data.addAll(tempData);
////                mAdapter.notifyDataSetChanged();
////            }
////        }
//    }
//
//    protected List<EMConversation> loadConversationList() {
//        // get all conversations
//        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
//        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
//        /**
//         * lastMsgTime will change if there is new message during sorting
//         * so use synchronized to make sure timestamp of last message won't change.
//         */
//        synchronized (conversations) {
//            for (EMConversation conversation : conversations.values()) {
//                if (conversation.getAllMessages().size() != 0) {
//                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
//                }
//            }
//        }
//        try {
//            // Internal is TimSort algorithm, has bug
//            sortConversationByLastChatTime(sortList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        List<EMConversation> list = new ArrayList<EMConversation>();
//        for (Pair<Long, EMConversation> sortItem : sortList) {
//            list.add(sortItem.second);
//        }
//        return list;
//    }
//
//    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
//        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
//            @Override
//            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {
//
//                if (con1.first.equals(con2.first)) {
//                    return 0;
//                } else if (con2.first.longValue() > con1.first.longValue()) {
//                    return 1;
//                } else {
//                    return -1;
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//       setContentView(R.layout.activity_im_view);
//        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
//        query = (EditText) findViewById(R.id.query);
//        // button to clear content in search bar
//        clearSearch = (ImageButton) findViewById(R.id.search_clear);
//        mNavigationView.setTitle("私信",new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        mListView = (ListView) findViewById(R.id.list_view);
//        mListView.setOnItemClickListener(this);
//       // mAdapter = new IMListAdapter(this, data,this);
//        mListView.setAdapter(mAdapter);
//        ReceiverUtils.addReceiver(this);
//        ReceiverUtils.sendReceiver(ReceiverUtils.INDEX_IM, null);
//        queryIM();
//    }
//
//
//    private void queryIM() {
//        query.addTextChangedListener(new TextWatcher() {
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                keyworke = s.toString();
//                queryIMList();
//                if (s.length() > 0) {
//                    clearSearch.setVisibility(View.VISIBLE);
//                } else {
//                    clearSearch.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        clearSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                query.getText().clear();
//                hideSoftKeyboard();
////                ReceiverUtils.sendReceiver(ReceiverUtils.INDEX_IM, null);
//            }
//        });
//    }
//
//    private void queryIMList() {
//        List<EMConversation> queryData = new ArrayList<>();
//        for (int i = 0; i < data.size(); i++) {
//            EMConversation info = data.get(i);
//            String name = FMWession.getInstance().getIM(String.format("im_name_%s", info.getUserName()));
//            if (name.indexOf(keyworke) != -1) {
//                queryData.add(info);
//            }
//        }
//        data.clear();
//        data.addAll(queryData);
//        mAdapter.notifyDataSetChanged();
//    }
//
//
//    protected void hideSoftKeyboard() {
//        if ( getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
//            if (getCurrentFocus() != null)
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
//
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        EMConversation info = data.get(position);
//        String name = FMWession.getInstance().getIM(String.format("im_name_%s", info.getUserName()));
//
//
//    }
//
//    public void cleak(final String name){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(name);
//                conversation.markAllMessagesAsRead();
//                // ReceiverUtils.sendReceiver(ReceiverUtils.INDEX_IM, null);
//            }
//        }).start();
//    }
//
//    @Override
//    public void onStart(BaseResponse response) {
//
//    }
//
//    @Override
//    public void onFailure(BaseResponse response) {
//
//    }
//
//    @Override
//    public void onSuccess(BaseResponse response) {
//        if (response.getStatus() == BaseResponse.SUCCEED) {
//            if (response.getRequestType() == 9) {
////                UserInfoEntity info = (UserInfoEntity) response.getData();
////                FMWession.getInstance().setIM(String.format("im_avatar_%s", userId), StringUtils.isEmpty(info.getHead_pic()) ? "-1" : info.getHead_pic());
////                FMWession.getInstance().setIM(String.format("im_name_%s", userId), info.getNick_name());
//                mAdapter.notifyDataSetChanged();
//            }
//        }
//    }
//
//    @Override
//    public void loadeInfo(String userId) {
//
//        new AsyncMainTask(userId).execute();
//    }
//
//
//    private class AsyncMainTask extends AsyncTask<String, Object, Integer> {
//        private String userId;
//        public AsyncMainTask(String userId){
//            this.userId=userId;
//        }
//
//        @Override
//        protected Integer doInBackground(String... params) {
//            SystemClock.sleep(500);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Integer result) {
//            super.onPostExecute(result);
//            IMListActivity.this.userId=userId;
////            UserInfoRequest request = new UserInfoRequest();
////            request.setOthers_user_id(userId);
////            request.setQuery_type("1");//他人主页
////            request.setOnResponseListener(IMFragment.this);
////            request.setRequestType(9);
////            request.setVisit_user_ip(NetWorkUtils.getLocalIpAddress(getContext()));
////            request.executePost(false);
//        }
//    }
//
//}
