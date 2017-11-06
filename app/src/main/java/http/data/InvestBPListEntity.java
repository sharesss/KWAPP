package http.data;/**
 * Created by A1 on 2017/8/8.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private int bpsort;
    private int bpstate;
    private String  bpphoto;//项目图片
    private List<Ceil> ceils=new ArrayList<>();


    public InvestBPListEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.peopleNum = jsonObject.optInt("peopleNum");
        this.isScore = jsonObject.optInt("isScore");
        this.bpname = jsonObject.optString("bpname");
        String bpdeion = jsonObject.optString("bpdeion");
        try {
            JSONObject bpdeionObj = new JSONObject(bpdeion);
            //里面是json
            Iterator<String> keys = bpdeionObj.keys();
            while (keys.hasNext()) {
                //cell0  cell1   cell2
                String key = keys.next();
                JSONObject value = bpdeionObj.getJSONObject(key);

                int imageWidth = value.optInt("imageWidth", 0);
                int imageHeight = value.optInt("imageHeight", 0);
                String imageUrl = value.optString("imageUrl", "");
                String text = value.optString("text", "");
                Ceil ceil = new Ceil();
                ceil.setImageHeight(imageHeight);
                ceil.setImageWidth(imageWidth);
                ceil.setImageUrl(imageUrl);
                ceil.setText(text);
                ceils.add(ceil);
            }

        } catch (JSONException e) {
            //里面是字符串
            this.bpdeion = bpdeion;
            Ceil ceil = new Ceil(bpdeion,0);
            ceils.add(ceil);
        }
        this.score = jsonObject.optInt("score");
        this.sumTotal = jsonObject.optInt("sumTotal");
        this.bpphoto = jsonObject.optString("bpphoto");

    }
    public InvestBPListEntity() {

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

    public int getIsScore() {
        return isScore;
    }

    public int getBpsort() {
        return bpsort;
    }

    public void setBpsort(int bpsort) {
        this.bpsort = bpsort;
    }

    public int getBpstate() {
        return bpstate;
    }

    public void setBpstate(int bpstate) {
        this.bpstate = bpstate;
    }

    public String getBpphoto() {
        return bpphoto;
    }

    public void setBpphoto(String bpphoto) {
        this.bpphoto = bpphoto;
    }

    public List<Ceil> getCeils() {
        return ceils;
    }

    public void setCeils(List<Ceil> ceils) {
        this.ceils = ceils;
    }

    public static class Ceil{
        private int   imageHeight;
        private int   imageWidth;
        private String   imageUrl;
        private String   text;
        private int type; // 0/1 文本/图片

        public Ceil(String text, int type) {
            this.text = text;
            this.type = type;
        }

        public Ceil() {
        }

        public int getImageHeight() {
            return imageHeight;
        }

        public void setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
        }

        public int getImageWidth() {
            return imageWidth;
        }

        public void setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
