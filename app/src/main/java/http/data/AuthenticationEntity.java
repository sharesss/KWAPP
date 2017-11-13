package http.data;

import org.json.JSONObject;

import utils.DateFormatUtils;

/**
 * Created by 认证 on 2017/8/19.
 */

public class AuthenticationEntity {
//        staff.put("caseFinancingState",investmentRound);
//        staff.put("caseInvestReward",returnMultiples);
    private int id;//审核id
    private String propertyphoto;//投资认证图片
    private String  createtime;
    private String  auditor;
    private int auditstate;//审核状态：状态：0删除，1待审，2审核通过，
    private String auditdesc;//审核原因
    private int investProjectSum;
    private int historyInvestSum;
    private int exitProjectSum;
    private String caseProjectName;
    private int caseIndustryId;
    private String caseIndustryName;
    private String caseProjectTime;
    private int caseInvestMoney;
    private int  caseInvestMoneyUnit;
    private String caseFinancingState;
    private Double  caseInvestReward;
    private String assetsPhotos;

    public AuthenticationEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.propertyphoto = jsonObject.optString("propertyphoto");
        if (!jsonObject.isNull("createtime")) {
            this.createtime = DateFormatUtils.longToDate("yyyy-MM", jsonObject.optLong("createtime", 0));

        }
        this.auditdesc = jsonObject.optString("auditdesc");
        this.auditstate = jsonObject.optInt("auditstate");
        this.investProjectSum = jsonObject.optInt("investProjectSum");
        this.historyInvestSum = jsonObject.optInt("historyInvestSum");
        this.exitProjectSum = jsonObject.optInt("exitProjectSum");
        this.caseProjectName = jsonObject.optString("caseProjectName");
        this.caseIndustryId = jsonObject.optInt("caseIndustryId");
        this.caseIndustryName = jsonObject.optString("caseIndustryName");
        this.caseProjectTime = jsonObject.optString("caseProjectTime");
        this.caseInvestMoney = jsonObject.optInt("caseInvestMoney");
        this.caseFinancingState = jsonObject.optString("caseFinancingState");
        this.caseInvestReward = jsonObject.optDouble("caseInvestReward");
        this.assetsPhotos = jsonObject.optString("assetsPhotos");
        this.caseInvestMoneyUnit = jsonObject.optInt("caseInvestMoneyUnit",0);




    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPropertyphoto() {
        return propertyphoto;
    }

    public void setPropertyphoto(String propertyphoto) {
        this.propertyphoto = propertyphoto;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public int getAuditstate() {
        return auditstate;
    }

    public void setAuditstate(int auditstate) {
        this.auditstate = auditstate;
    }

    public String getAuditdesc() {
        return auditdesc;
    }

    public void setAuditdesc(String auditdesc) {
        this.auditdesc = auditdesc;
    }

    public int getInvestProjectSum() {
        return investProjectSum;
    }

    public void setInvestProjectSum(int investProjectSum) {
        this.investProjectSum = investProjectSum;
    }

    public int getHistoryInvestSum() {
        return historyInvestSum;
    }

    public void setHistoryInvestSum(int historyInvestSum) {
        this.historyInvestSum = historyInvestSum;
    }

    public int getExitProjectSum() {
        return exitProjectSum;
    }

    public void setExitProjectSum(int exitProjectSum) {
        this.exitProjectSum = exitProjectSum;
    }

    public String getCaseProjectName() {
        return caseProjectName;
    }

    public void setCaseProjectName(String caseProjectName) {
        this.caseProjectName = caseProjectName;
    }

    public int getCaseIndustryId() {
        return caseIndustryId;
    }

    public void setCaseIndustryId(int caseIndustryId) {
        this.caseIndustryId = caseIndustryId;
    }

    public String getCaseIndustryName() {
        return caseIndustryName;
    }

    public void setCaseIndustryName(String caseIndustryName) {
        this.caseIndustryName = caseIndustryName;
    }

    public String getCaseProjectTime() {
        return caseProjectTime;
    }

    public void setCaseProjectTime(String caseProjectTime) {
        this.caseProjectTime = caseProjectTime;
    }

    public int getCaseInvestMoney() {
        return caseInvestMoney;
    }

    public void setCaseInvestMoney(int caseInvestMoney) {
        this.caseInvestMoney = caseInvestMoney;
    }

    public String getCaseFinancingState() {
        return caseFinancingState;
    }

    public void setCaseFinancingState(String caseFinancingState) {
        this.caseFinancingState = caseFinancingState;
    }

    public Double getCaseInvestReward() {
        return caseInvestReward;
    }

    public void setCaseInvestReward(double caseInvestReward) {
        this.caseInvestReward = caseInvestReward;
    }

    public String getAssetsPhotos() {
        return assetsPhotos;
    }

    public void setAssetsPhotos(String assetsPhotos) {
        this.assetsPhotos = assetsPhotos;
    }

    public int getCaseInvestMoneyUnit() {
        return caseInvestMoneyUnit;
    }

    public void setCaseInvestMoneyUnit(int caseInvestMoneyUnit) {
        this.caseInvestMoneyUnit = caseInvestMoneyUnit;
    }
}
