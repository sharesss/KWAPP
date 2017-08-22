package http.data;

import org.json.JSONObject;

/**
 * Created by A1 on 2017/8/18.
 */

public class ProjectReserveEntity {
    private int id;//项目id
    private int reserveAmount;//预约金额
    private int reservePeopleNum;//预约名额
    private int yetReservePropleNum;//多少人已入股
    private String investCompany;//股份所属公司名
    private String equityReserveExplain;//	股权预约说明
    private String shareholderEquity;//	股东权益

    public ProjectReserveEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id",0);
        this.reserveAmount = jsonObject.optInt("reserveAmount",0);
        this.reservePeopleNum = jsonObject.optInt("reservePeopleNum",0);
        this.yetReservePropleNum = jsonObject.optInt("yetReservePropleNum",0);
        this.investCompany = jsonObject.optString("investCompany");
        this.equityReserveExplain = jsonObject.optString("equityReserveExplain");
        this.shareholderEquity = jsonObject.optString("shareholderEquity");

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReserveAmount() {
        return reserveAmount;
    }

    public void setReserveAmount(int reserveAmount) {
        this.reserveAmount = reserveAmount;
    }

    public int getReservePeopleNum() {
        return reservePeopleNum;
    }

    public void setReservePeopleNum(int reservePeopleNum) {
        this.reservePeopleNum = reservePeopleNum;
    }

    public int getYetReservePropleNum() {
        return yetReservePropleNum;
    }

    public void setYetReservePropleNum(int yetReservePropleNum) {
        this.yetReservePropleNum = yetReservePropleNum;
    }

    public String getInvestCompany() {
        return investCompany;
    }

    public void setInvestCompany(String investCompany) {
        this.investCompany = investCompany;
    }

    public String getEquityReserveExplain() {
        return equityReserveExplain;
    }

    public void setEquityReserveExplain(String equityReserveExplain) {
        this.equityReserveExplain = equityReserveExplain;
    }

    public String getShareholderEquity() {
        return shareholderEquity;
    }

    public void setShareholderEquity(String shareholderEquity) {
        this.shareholderEquity = shareholderEquity;
    }
}
