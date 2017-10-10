//package com.ts.fmxt.ui.ItemAdapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMConversation;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.util.DateUtils;
//import com.thindo.base.Adapter.FMBaseAdapter;
//import com.ts.fmxt.R;
//import com.ts.fmxt.ui.im.utils.EaseCommonUtils;
//import com.ts.fmxt.ui.im.utils.EaseSmileUtils;
//
//import java.util.Date;
//import java.util.List;
//
//import utils.ReceiverUtils;
//import utils.StringUtils;
//import utils.sharePreferences.FMWession;
//import widget.image.FMNetImageView;
//import widget.testlistview.view.CstSwipeDelMenuViewGroup;
//import widget.viewbadger.BadgeView;
//
//
//
//public class IMListAdapter extends FMBaseAdapter<EMConversation> {
////   private IMFragment mIMFragment;
//    public IMListAdapter(Context context, List<EMConversation> arrayList, IMFragment mIMFragment) {
//        super(context, arrayList);
////        this.mIMFragment=mIMFragment;
//    }
//
//    @Override
//    public EMConversation getItem(int position) {
//        return (EMConversation) super.getItem(position);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder vh = null;
//        if (convertView == null) {
//            vh = new ViewHolder();
//            convertView = getInflaterView(R.layout.adapter_im_item);
//            vh.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
//            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
//            vh.tv_del = (TextView) convertView.findViewById(R.id.tv_del);
//            vh.unread_msg_number = (ImageView) convertView.findViewById(R.id.unread_msg_number);
//            vh.msg_state = (ImageView) convertView.findViewById(R.id.msg_state);
//            vh.iv_picture = (FMNetImageView) convertView.findViewById(R.id.iv_picture);
//            vh.iv_cat = (FMNetImageView) convertView.findViewById(R.id.iv_cat);
//            vh.menuViewGroup= (CstSwipeDelMenuViewGroup) convertView.findViewById(R.id.rly_parent);
//            vh.iv_temp_num= (ImageView) convertView.findViewById(R.id.iv_temp_num);
//            vh.rl_item= (RelativeLayout) convertView.findViewById(R.id.rl_item);
//            vh.mBadgeView=new BadgeView(getContext(),vh.iv_temp_num);
//            vh. mBadgeView.setTextColor(getResourColor(R.color.white));
//           vh.mBadgeView.setTextSize(12);
//            vh.mBadgeView.setBadgePosition(BadgeView.POSITION_CENTER);
//            convertView.setTag(vh);
//        } else {
//            vh = (ViewHolder) convertView.getTag();
//        }
//        vh.menuViewGroup.smoothClose();
//        EMConversation info = getItem(position);
//        vh.tv_message.setText(info.getUserName());
//        String avatar = String.format("im_avatar_%s", info.getUserName());
//        String name = FMWession.getInstance().getIM(String.format("im_name_%s", info.getUserName()));
//        vh.tv_name.setText(StringUtils.isEmpty(name) ? info.getUserName() : name);
//        String uri = FMWession.getInstance().getIM(avatar);
//        if (!StringUtils.isEmpty(uri)) {
//            if ("-1".equals(uri)) {
////                vh.iv_picture.setImageResource(R.drawable.ease_default_avatar);
//            } else {
//                vh.iv_picture.loadImage(uri);
//            }
//        }
//        String cats = String.format("cat_%s", info.getUserName());
//        String cat = FMWession.getInstance().getIM(cats);
//        if (!StringUtils.isEmpty(cat)) {
//                vh.iv_cat.loadImage(cat);
//        }
//
////        else {
////            if (mUserInfoInterface != null)
////                mUserInfoInterface.loadeInfo(info.getUserName());
////            vh.iv_picture.setImageResource(R.drawable.ease_default_avatar);
////        }
//
//        EMMessage lastMessage = info.getLastMessage();
//        String msg = EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()));
//
//        // kSendGiftMessageSuccessNAMETip
//
//        String kSendGiftMessageSuccessNAMETip = lastMessage.getStringAttribute("kSendGiftMessageSuccessNAMETip", "");
//        String kSendGiftMessageSuccessSystemTip = lastMessage.getStringAttribute("kSendGiftMessageSuccessSystemTip", "");
//        if (!StringUtils.isEmpty(kSendGiftMessageSuccessNAMETip) || !StringUtils.isEmpty(kSendGiftMessageSuccessSystemTip)) {
//            if (lastMessage.direct() == EMMessage.Direct.SEND ) {
//                msg = String.format("送出%s", kSendGiftMessageSuccessNAMETip);
//            } else {
//                msg = String.format("送来%s", kSendGiftMessageSuccessNAMETip);
//            }
//            vh.tv_message.setText(msg);
//        } else {
//            vh.tv_message.setText(EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
//                    TextView.BufferType.SPANNABLE);
//        }
//        vh.tv_time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
//
//        if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
//            vh.msg_state.setVisibility(View.VISIBLE);
//        } else {
//            vh.msg_state.setVisibility(View.GONE);
//        }
//
//        if (info.getUnreadMsgCount() > 0) {
//            vh. mBadgeView.setText(String.valueOf(info.getUnreadMsgCount()));
//            vh. mBadgeView.show();
//          //  vh.unread_msg_number.setVisibility(View.VISIBLE);
//
//        } else {
//            vh. mBadgeView.hide();
//          //  vh.unread_msg_number.setVisibility(View.INVISIBLE);
//        }
//
//
////        vh.tv_del.setOnClickListener(new DelMsg(FMWession.getInstance().getUserInfo().getUuid().equals(lastMessage.getFrom())?lastMessage.getTo():lastMessage.getFrom(),position));
//       vh.rl_item.setOnClickListener(new OnItemClick(position));
//        return convertView;
//    }
//
//    class OnItemClick implements  View.OnClickListener{
//        private int position;
//        public OnItemClick(int position){
//            this.position=position;
//        }
//
//
//        @Override
//        public void onClick(View v) {
////            if (!FMWession.getInstance().isDelTag())
////                 mIMFragment.onItemClick(position);
//        }
//    }
//
//    class DelMsg implements View.OnClickListener{
//        private String userName;
//        private int position;
//        public DelMsg(String userName, int position){
//            this.userName=userName;
//            this.position=position;
//        }
//
//        @Override
//        public void onClick(View v) {
//            getList().remove(position);
//            EMClient.getInstance().chatManager().deleteConversation(userName, true);
////            ReceiverUtils.sendReceiver(ReceiverUtils.INDEX_IM, null);
//        }
//    }
//
//
//    private class ViewHolder {
//        private TextView tv_message, tv_name, tv_time,tv_del;
//        private ImageView msg_state, unread_msg_number;
//        private FMNetImageView iv_picture,iv_cat;
//        private CstSwipeDelMenuViewGroup menuViewGroup;
//        private RelativeLayout rl_item;
//        private BadgeView mBadgeView;
//        private ImageView iv_temp_num;
//
//    }
//
//    public interface UserInfoInterface {
//        public void loadeInfo(String userId);
//    }
//}
