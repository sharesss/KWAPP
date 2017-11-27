package http.data;/**
 * Created by A1 on 2017/8/4.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utils.DateFormatUtils;

/**
 * created by kp at 2017/8/4
 * 发现首页
 */
public class ConsumerEntity {

    private int id;//id
    private String headPic;
    private String nickName;
    private int investInitiator;//投资发起人UserId
    private String  investName;//投资项目名称
    private String  investPhoto;//图片
    private String  investDeion;//简介
    private String  createTime;//发起时间
    private Double  exponent;//可投指数
    private Double  holdRatios;//预约比例
    private int  dokels;//值得投票数
    private int   notDokels;//不值得投票数
    private int   isVote;//0表示当前用户并未投票，大于0表示当成用户
    private int   isCollect;//是否收藏：0否 ，1是
    private int   voteNum;//投票总数
    private String amount;
    private Long  reserveFinishTime;//预约截止时间
    private int reservedAmount;//已预约金额
    private int reserveTargetAmount;//目标金额
    private int reservedPeopleNum;//已预约人数
    private List<Ceil> ceils=new ArrayList<>();
    private List<Ceil> ceis=new ArrayList<>();

    public ConsumerEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id",0);
        this.nickName = jsonObject.optString("nickName");
        this.headPic  =jsonObject.optString("headPic");
        this.investInitiator = jsonObject.optInt("investInitiator", 0);
        this.investName = jsonObject.optString("investName");
        this.investPhoto = jsonObject.optString("investPhoto");
        this.investDeion = jsonObject.optString("investDeion");
        if (!jsonObject.isNull("createTime")) {
            this.createTime = DateFormatUtils.longToDate("yyyy-MM-dd", jsonObject.optLong("createTime", 0));
        }
        this.exponent = jsonObject.optDouble("exponent");
        this.holdRatios= jsonObject.optDouble("holdRatios");
        this.reserveFinishTime = jsonObject.optLong("reserveFinishTime");
        this.dokels = jsonObject.optInt("dokels");
        this.notDokels = jsonObject.optInt("notDokels");
        this.isVote = jsonObject.optInt("isVote");
        this.isCollect = jsonObject.optInt("isCollect");
        this.voteNum = jsonObject.optInt("voteNum");
        this.reservedAmount = jsonObject.optInt("reservedAmount",0);
        this.reserveTargetAmount = jsonObject.optInt("reserveTargetAmount",0);
        this.reservedPeopleNum = jsonObject.optInt("reservedPeopleNum",0);
        String projectReward = jsonObject.optString("projectReward");
        String projectStory = jsonObject.optString("projectStory");
        try {
            JSONObject bpdeionObj = new JSONObject(projectReward);
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
//            Ceil ceil = new Ceil(projectReward,0);
//            ceils.add(ceil);
        }

        try {
            JSONObject bpdeionObj = new JSONObject(projectStory);
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
                ceis.add(ceil);
            }

        } catch (JSONException e) {
            //里面是字符串
//            Ceil ceil = new Ceil(projectReward,0);
//            ceils.add(ceil);
        }

    }
    public ConsumerEntity(JSONObject jsonObject,String amount) {
        this.amount = amount;
        this.id = jsonObject.optInt("id",0);
        this.nickName = jsonObject.optString("nickName");
        this.headPic  =jsonObject.optString("headPic");
        this.investInitiator = jsonObject.optInt("investInitiator", 0);
        this.investName = jsonObject.optString("investName");
        this.investPhoto = jsonObject.optString("investPhoto");
        this.investDeion = jsonObject.optString("investDeion");
        if (!jsonObject.isNull("createTime")) {
            this.createTime = DateFormatUtils.longToDate("yyyy-MM-dd", jsonObject.optLong("createTime", 0));
        }
        this.exponent = jsonObject.optDouble("exponent");
        this.holdRatios= jsonObject.optDouble("holdRatios");
        this.reserveFinishTime = jsonObject.optLong("reserveFinishTime");
        this.dokels = jsonObject.optInt("dokels");
        this.notDokels = jsonObject.optInt("notDokels");
        this.isVote = jsonObject.optInt("isVote");
        this.isCollect = jsonObject.optInt("isCollect");
        this.voteNum = jsonObject.optInt("voteNum");
        this.reservedAmount = jsonObject.optInt("reservedAmount",0);
        this.reserveTargetAmount = jsonObject.optInt("reserveTargetAmount",0);
        this.reservedPeopleNum = jsonObject.optInt("reservedPeopleNum",0);
        String projectReward = jsonObject.optString("projectReward");
        String projectStory = jsonObject.optString("projectStory");
        try {
            JSONObject bpdeionObj = new JSONObject(projectReward);
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
//            Ceil ceil = new Ceil(projectReward,0);
//            ceils.add(ceil);
        }
        try {
            JSONObject bpdeionObj = new JSONObject(projectStory);
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
                ceis.add(ceil);
            }

        } catch (JSONException e) {
            //里面是字符串
//            Ceil ceil = new Ceil(projectReward,0);
//            ceils.add(ceil);
        }



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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Double getHoldRatios() {
        return holdRatios;
    }

    public void setHoldRatios(Double holdRatios) {
        this.holdRatios = holdRatios;
    }

    public List<Ceil> getCeils() {
        return ceils;
    }

    public Long getReserveFinishTime() {
        return reserveFinishTime;
    }

    public void setReserveFinishTime(Long reserveFinishTime) {
        this.reserveFinishTime = reserveFinishTime;
    }


    public void setCeils(List<Ceil> ceils) {
        this.ceils = ceils;
    }

    public int getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(int reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public int getReserveTargetAmount() {
        return reserveTargetAmount;
    }

    public void setReserveTargetAmount(int reserveTargetAmount) {
        this.reserveTargetAmount = reserveTargetAmount;
    }

    public int getReservedPeopleNum() {
        return reservedPeopleNum;
    }


    public void setReservedPeopleNum(int reservedPeopleNum) {
        this.reservedPeopleNum = reservedPeopleNum;
    }

    public List<Ceil> getCeis() {
        return ceis;
    }

    public void setCeis(List<Ceil> ceis) {
        this.ceis = ceis;
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
