package http.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kp on 2017/9/22.
 * 聊天室初始化信息
 */

public class ChatRoomInfoEntity {
    private  int stockEquityState;//拍卖状态：0未开始，1拍卖进行中，2已经结束
    private Long  stockStartTime;//距离开始多少秒或者距离结束多少秒
    private String userNickname;
    private String userHeadpic;
    private String nickname;
    private String headpic;
    private int offerPrice;

    public ChatRoomInfoEntity(JSONObject jsonObject){
        this.stockEquityState = jsonObject.optInt("stockEquityState");
        this.stockStartTime = jsonObject.optLong("stockStartTime");
        try {
        if (!jsonObject.isNull("user")) {
            JSONObject js =  jsonObject.getJSONObject("user");
            this.userNickname = js.optString("nickname");
            this.userHeadpic = js.optString("headpic");

        }
        if(!jsonObject.isNull("stockAuctionRecord")){
            JSONObject js =  jsonObject.getJSONObject("stockAuctionRecord");
            this.nickname = js.optString("nickName");
            this.headpic = js.optString("headPic");
            this.offerPrice = js.optInt("offerPrice");
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getStockEquityState() {
        return stockEquityState;
    }

    public void setStockEquityState(int stockEquityState) {
        this.stockEquityState = stockEquityState;
    }

    public Long getStockStartTime() {
        return stockStartTime;
    }

    public void setStockStartTime(Long stockStartTime) {
        this.stockStartTime = stockStartTime;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserHeadpic() {
        return userHeadpic;
    }

    public void setUserHeadpic(String userHeadpic) {
        this.userHeadpic = userHeadpic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
    }
}
