package http.data;

import org.json.JSONObject;

/**
 * Created by kp on 2017/11/1.
 * 项目回报
 */

public class ProjectReturnEntity {
    private int id;
    private int investId;
    private int reserveAmount;// 预约金额
    private String reservePeopleNum;// 预约名额
    private int financingAmount;//目标总数
    private Double initiateAmount;// 起投金额（万）
    private String shareTitle; // 投资比率说明标题
    private String shareContent;// 投资比率说明内容
    private int alreadyBookedMoney; // 已预约金额
    private int yetReservePropleNum; // 已预约名额
    private int isYetBut;;// 当前用户是否已经认购
    private int equityShareType;//1按照名额，2按照股权金额

    public ProjectReturnEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id",0);
        this.investId = jsonObject.optInt("investId",0);
        this.reserveAmount = jsonObject.optInt("reserveAmount",0);
        this.reservePeopleNum = jsonObject.optString("reservePeopleNum","");
        this.shareTitle = jsonObject.optString("shareTitle","");
        this.shareContent = jsonObject.optString("shareContent","");
        this.yetReservePropleNum = jsonObject.optInt("yetReservePropleNum",0);
        this.isYetBut = jsonObject.optInt("isYetBut",0);
        this.financingAmount = jsonObject.optInt("financingAmount",0);
        this.alreadyBookedMoney = jsonObject.optInt("alreadyBookedMoney",0);
        this.equityShareType = jsonObject.optInt("equityShareType",0);
        this.initiateAmount  = jsonObject.optDouble("initiateAmount");
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

    public int getFinancingAmount() {
        return financingAmount;
    }

    public void setFinancingAmount(int financingAmount) {
        this.financingAmount = financingAmount;
    }

    public int getAlreadyBookedMoney() {
        return alreadyBookedMoney;
    }

    public void setAlreadyBookedMoney(int alreadyBookedMoney) {
        this.alreadyBookedMoney = alreadyBookedMoney;
    }

    public int getEquityShareType() {
        return equityShareType;
    }

    public void setEquityShareType(int equityShareType) {
        this.equityShareType = equityShareType;
    }

    public Double getInitiateAmount() {
        return initiateAmount;
    }

    public void setInitiateAmount(Double initiateAmount) {
        this.initiateAmount = initiateAmount;
    }
}
