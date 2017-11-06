package http.data;

import org.json.JSONObject;

/**
 * Created by kp on 2017/10/30.
 * 投资偏好
 */

public class InvestmentPreferenceEntity {
    private int id;
    private int userId;
    private int investSumMin;//最小值
    private int investSumMax;//最大值
    private int investMoneyUnit;//货币类型
    private int returnWay;//回报类型
    private int isFamousInvestor;//是否知名机构
    private String industryIds;//关注领域
    private String  raundIds;//关注轮次


    public InvestmentPreferenceEntity(JSONObject json) {
        this.id = json.optInt("id",0);
        this.userId = json.optInt("userId",0);
        this.investSumMin = json.optInt("investSumMin",0);
        this.investSumMax = json.optInt("investSumMax",0);
        this.investMoneyUnit = json.optInt("investMoneyUnit",0);
        this.returnWay = json.optInt("returnWay",0);
        this.isFamousInvestor = json.optInt("isFamousInvestor",0);
        this.industryIds = json.optString("industryIds");
        this.raundIds = json.optString("raundIds");

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getInvestSumMin() {
        return investSumMin;
    }

    public void setInvestSumMin(int investSumMin) {
        this.investSumMin = investSumMin;
    }

    public int getInvestSumMax() {
        return investSumMax;
    }

    public void setInvestSumMax(int investSumMax) {
        this.investSumMax = investSumMax;
    }

    public int getInvestMoneyUnit() {
        return investMoneyUnit;
    }

    public void setInvestMoneyUnit(int investMoneyUnit) {
        this.investMoneyUnit = investMoneyUnit;
    }

    public int getReturnWay() {
        return returnWay;
    }

    public void setReturnWay(int returnWay) {
        this.returnWay = returnWay;
    }

    public int getIsFamousInvestor() {
        return isFamousInvestor;
    }

    public void setIsFamousInvestor(int isFamousInvestor) {
        this.isFamousInvestor = isFamousInvestor;
    }

    public String getIndustryIds() {
        return industryIds;
    }

    public void setIndustryIds(String industryIds) {
        this.industryIds = industryIds;
    }

    public String getRaundIds() {
        return raundIds;
    }

    public void setRaundIds(String raundIds) {
        this.raundIds = raundIds;
    }
}
