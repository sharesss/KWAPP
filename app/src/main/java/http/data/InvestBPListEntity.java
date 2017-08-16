package http.data;/**
 * Created by A1 on 2017/8/8.
 */

import org.json.JSONObject;

/**
 * created by kp at 2017/8/8
 * BP12项
 */
public class InvestBPListEntity {

    private int id;//id
    private int peopleNum;//人数
    private int  isScore;//当前用户是否评分
    private String  bpname;//项目名称
    private String  bpdeion;//BP项说明
    private int  score;//分数
    private int sumTotal;//总分数

    public InvestBPListEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.peopleNum = jsonObject.optInt("peopleNum");
        this.isScore = jsonObject.optInt("isScore");
        this.bpname = jsonObject.optString("bpname");
        this.bpdeion = jsonObject.optString("bpdeion");
        this.score = jsonObject.optInt("score");
        this.sumTotal = jsonObject.optInt("sumTotal");

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int isScore() {
        return isScore;
    }

    public void setIsScore(int isScore) {
        this.isScore = isScore;
    }

    public String getBpname() {
        return bpname;
    }

    public void setBpname(String bpname) {
        this.bpname = bpname;
    }

    public String getBpdeion() {
        return bpdeion;
    }

    public void setBpdeion(String bpdeion) {
        this.bpdeion = bpdeion;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(int sumTotal) {
        this.sumTotal = sumTotal;
    }
}
