package http.data;

import org.json.JSONObject;

/**
 * Created by kp on 2017/11/1.
 * 项目回报
 */

public class ProjectReturnEntity {
    private int id;
    private int investId;
    private int reserveAmount;
    private String reservePeopleNum;
    private String shareTitle;
    private String shareContent;
    private int yetReservePropleNum;
    private int isYetBut;

    public ProjectReturnEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id",0);
        this.investId = jsonObject.optInt("investId",0);
        this.reserveAmount = jsonObject.optInt("reserveAmount",0);
        this.reservePeopleNum = jsonObject.optString("reservePeopleNum","");
        this.shareTitle = jsonObject.optString("shareTitle","");
        this.shareContent = jsonObject.optString("shareContent","");
        this.yetReservePropleNum = jsonObject.optInt("yetReservePropleNum",0);
        this.isYetBut = jsonObject.optInt("isYetBut",0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvestId() {
        return investId;
    }

    public void setInvestId(int investId) {
        this.investId = investId;
    }

    public int getReserveAmount() {
        return reserveAmount;
    }

    public void setReserveAmount(int reserveAmount) {
        this.reserveAmount = reserveAmount;
    }

    public String getReservePeopleNum() {
        return reservePeopleNum;
    }

    public void setReservePeopleNum(String reservePeopleNum) {
        this.reservePeopleNum = reservePeopleNum;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public int getYetReservePropleNum() {
        return yetReservePropleNum;
    }

    public void setYetReservePropleNum(int yetReservePropleNum) {
        this.yetReservePropleNum = yetReservePropleNum;
    }

    public int getIsYetBut() {
        return isYetBut;
    }

    public void setIsYetBut(int isYetBut) {
        this.isYetBut = isYetBut;
    }
}
