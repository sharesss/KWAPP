package http.data;/**
 * Created by A1 on 2017/8/4.
 */

import org.json.JSONObject;

/**
 * created by kp at 2017/8/4
 * 发现首页
 */
public class ConsumerEntity {

    private int id;//id
    private int investInitiator;//投资发起人UserId
    private String  investName;//投资项目名称
    private String  investPhoto;//图片
    private String  investDeion;//简介
    private String  createTime;//发起时间
    private Double  exponent;//可投指数
    private int  dokels;//值得投票数
    private int   notDokels;//不值得投票数
    private int   isVote;//0表示当前用户并未投票，大于0表示当成用户
    private int   isCollect;//是否收藏：0否 ，1是
    private int   voteNum;//投票总数

    public ConsumerEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id",0);
        this.investInitiator = jsonObject.optInt("investInitiator", 0);
        this.investName = jsonObject.optString("investName");
        this.investPhoto = jsonObject.optString("investPhoto");
        this.investDeion = jsonObject.optString("investDeion");
        this.createTime = jsonObject.optString("createTime");
        this.exponent = jsonObject.optDouble("exponent");
        this.dokels = jsonObject.optInt("dokels");
        this.notDokels = jsonObject.optInt("notDokels");
        this.isVote = jsonObject.optInt("isVote");
        this.isCollect = jsonObject.optInt("isCollect");
        this.voteNum = jsonObject.optInt("voteNum");

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvestInitiator() {
        return investInitiator;
    }

    public void setInvestInitiator(int investInitiator) {
        this.investInitiator = investInitiator;
    }

    public String getInvestName() {
        return investName;
    }

    public void setInvestName(String investName) {
        this.investName = investName;
    }

    public String getInvestPhoto() {
        return investPhoto;
    }

    public void setInvestPhoto(String investPhoto) {
        this.investPhoto = investPhoto;
    }

    public String getInvestDeion() {
        return investDeion;
    }

    public void setInvestDeion(String investDeion) {
        this.investDeion = investDeion;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getExponent() {
        return exponent;
    }

    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

    public void setExponent(Double exponent) {
        this.exponent = exponent;
    }

    public int getDokels() {
        return dokels;
    }

    public void setDokels(int dokels) {
        this.dokels = dokels;
    }

    public int getNotDokels() {
        return notDokels;
    }

    public void setNotDokels(int notDokels) {
        this.notDokels = notDokels;
    }

    public int getIsVote() {
        return isVote;
    }

    public void setIsVote(int isVote) {
        this.isVote = isVote;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
