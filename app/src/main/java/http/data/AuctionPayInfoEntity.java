package http.data;

import org.json.JSONObject;

import utils.DateFormatUtils;

/**
 * Created by kp on 2017/9/25.
 * 冻结保证金及明细
 */

public class AuctionPayInfoEntity {
    private int  stockId;
    private String  stockName;
    private String   stockPic;
    private int earnestMoney;
    private String payTime;
    private int  payType;
    private int  fundFinallyFlow;
    private String  fundFinallyFlowDesc;
    public AuctionPayInfoEntity(JSONObject json){
        this.stockId = json.optInt("stockId");
        this.stockName = json.optString("stockName","");
        this.stockPic = json.optString("stockPic","");
        this.earnestMoney = json.optInt("earnestMoney");
        if (!json.isNull("payTime")) {
            this.payTime = DateFormatUtils.longToDate("yyyy-MM-dd", json.optLong("payTime", 0));
        }
        this.payType = json.optInt("payType");
        this.fundFinallyFlow = json.optInt("fundFinallyFlow");
        this.fundFinallyFlowDesc = json.optString("fundFinallyFlowDesc","");
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockPic() {
        return stockPic;
    }

    public void setStockPic(String stockPic) {
        this.stockPic = stockPic;
    }

    public int getEarnestMoney() {
        return earnestMoney;
    }

    public void setEarnestMoney(int earnestMoney) {
        this.earnestMoney = earnestMoney;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getFundFinallyFlow() {
        return fundFinallyFlow;
    }

    public void setFundFinallyFlow(int fundFinallyFlow) {
        this.fundFinallyFlow = fundFinallyFlow;
    }

    public String getFundFinallyFlowDesc() {
        return fundFinallyFlowDesc;
    }

    public void setFundFinallyFlowDesc(String fundFinallyFlowDesc) {
        this.fundFinallyFlowDesc = fundFinallyFlowDesc;
    }
}
