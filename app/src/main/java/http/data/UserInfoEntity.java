package http.data;/**
 * Created by A1 on 2017/8/2.
 */

import org.json.JSONException;
import org.json.JSONObject;

/**
 * created by kp at 2017/8/2
 * 个人信息
 */
public class UserInfoEntity {
    private String account;
    private String nickname;
    private String headpic;
    private String residence;//所在地
    private int sex;//性别：1男，0：女
    private int age;//年龄
    private String position;//职位
    private String annualincome;//年收入
    private String company;//公司
    private String signature;//个性签名
    private int auditstate;//审核状态：'状态：0删除，1待审，2审核通过，3审核不通过',
    private int investprojectcount;//想跟投的项目数量
    private int investprojectcollectcount;//收藏的项目数量
    private int  isinvestauthen;//是否是投资人  1：是  0 不是
    private int isTruenameAuthen;//实名认证  1：是  0 不是


    public UserInfoEntity(JSONObject json) {
        try {
            this.nickname = json.getString("nickname");
            this.headpic = json.getString("headpic");
            this.residence = json.getString("residence");
            this.age=json.optInt("age", 0);
            this.sex=json.optInt("sex", 0);
            this.position = json.getString("position");
            this.annualincome = json.getString("annualincome");
            this.company = json.getString("company");
            this.signature = json.getString("signature");
            this.auditstate=json.optInt("auditstate", 0);
            this.investprojectcount=json.optInt("investprojectcount", 0);
            this.investprojectcollectcount=json.optInt("investprojectcollectcount", 0);
            this.isinvestauthen = json.optInt("isinvestauthen",0);
            this.account = json.optString("account");
            this.isTruenameAuthen = json.optInt("isTruenameAuthen",-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAnnualincome() {
        return annualincome;
    }

    public void setAnnualincome(String annualincome) {
        this.annualincome = annualincome;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getAuditstate() {
        return auditstate;
    }

    public void setAuditstate(int auditstate) {
        this.auditstate = auditstate;
    }

    public int getInvestprojectcount() {
        return investprojectcount;
    }

    public void setInvestprojectcount(int investprojectcount) {
        this.investprojectcount = investprojectcount;
    }

    public int getInvestprojectcollectcount() {
        return investprojectcollectcount;
    }

    public void setInvestprojectcollectcount(int investprojectcollectcount) {
        this.investprojectcollectcount = investprojectcollectcount;
    }

    public int getIsinvestauthen() {
        return isinvestauthen;
    }

    public void setIsinvestauthen(int isinvestauthen) {
        this.isinvestauthen = isinvestauthen;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getIsTruenameAuthen() {
        return isTruenameAuthen;
    }

    public void setIsTruenameAuthen(int isTruenameAuthen) {
        this.isTruenameAuthen = isTruenameAuthen;
    }
}
