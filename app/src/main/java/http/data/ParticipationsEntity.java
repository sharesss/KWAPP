package http.data;

import org.json.JSONObject;

/**
 * Created by kp on 2017/11/30.
 * 红包
 */

public class ParticipationsEntity {
    private int id;
    private int investId;//项目ID
    private int bonusShareAmount;//本次分红金额（万）
    private int annualizedReturn;//平均年回报率
    private String  investProjectName;//项目名称

    public ParticipationsEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id",0);
        this.investId = jsonObject.optInt("investId",0);
        this.bonusShareAmount = jsonObject.optInt("bonusShareAmount",0);
        this.annualizedReturn = jsonObject.optInt("annualizedReturn",0);
        this.investProjectName = jsonObject.optString("investProjectName");

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

    public int getBonusShareAmount() {
        return bonusShareAmount;
    }

    public void setBonusShareAmount(int bonusShareAmount) {
        this.bonusShareAmount = bonusShareAmount;
    }

    public int getAnnualizedReturn() {
        return annualizedReturn;
    }

    public void setAnnualizedReturn(int annualizedReturn) {
        this.annualizedReturn = annualizedReturn;
    }

    public String getInvestProjectName() {
        return investProjectName;
    }

    public void setInvestProjectName(String investProjectName) {
        this.investProjectName = investProjectName;
    }
}
