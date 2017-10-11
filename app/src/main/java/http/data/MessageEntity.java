package http.data;


import org.json.JSONObject;

import utils.DateFormatUtils;

/**
 * Created by A1 on 2016/12/6.
 */
public class MessageEntity {
    private String count;// 内容
    private int userId;//用户id
    private int readFlag;//是否已阅读：0否，1是
    private int type;//消息类型：1评论,2 股权拍卖
    private String userName;//用户昵称
    private String tiem;//发布内容时间
    private int  messageId;//本消息来源id
    private int  isInvestAuthen;//是否已是投资人
    private int  realId;//通过该id跳转对应信息
    private int  skipType;//消息类型  跳转类型:如果type=1,则1是项目跟投、2是股权拍卖，如果type=2,则1股权拍卖详情页、2保证金历史页、3股权拍卖审核页
    private String  picture;//图片


    public MessageEntity(JSONObject json) {
        this.count = json.optString("content");
        this.userId = json.optInt("userId", 0);
        this.readFlag = json.optInt("readFlag", 0);
        this.type = json.optInt("type", 0);
        this.userName = json.optString("nickName", "");
        this.messageId=json.optInt("messageId", 0);
        this.isInvestAuthen=json.optInt("isInvestAuthen", 0);
        this.tiem= DateFormatUtils.longToDate("yyyy-MM-dd", json.optLong("createTime", 0));
        this.realId = json.optInt("realId");
        this.skipType =json.optInt("skipType");
        this.picture = json.optString("picture");
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(int readFlag) {
        this.readFlag = readFlag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTiem() {
        return tiem;
    }

    public void setTiem(String tiem) {
        this.tiem = tiem;
    }


    public String getMessageId() {
        return String.valueOf(messageId);
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getIsInvestAuthen() {
        return isInvestAuthen;
    }

    public void setIsInvestAuthen(int isInvestAuthen) {
        this.isInvestAuthen = isInvestAuthen;
    }

    public int getRealId() {
        return realId;
    }

    public void setRealId(int realId) {
        this.realId = realId;
    }

    public int getSkipType() {
        return skipType;
    }

    public void setSkipType(int skipType) {
        this.skipType = skipType;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
