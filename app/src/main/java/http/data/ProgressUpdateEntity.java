package http.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utils.DateFormatUtils;

/**
 * Created by kp on 2017/11/27.
 * 进度更新
 */

public class ProgressUpdateEntity {
     private int id;
    private int investId;
    private String participationTitle;
    private int totalInvestAmount;
    private int bonusShareAmount;
    private int annualizedReturn;
    private String  createTime;//发起时间
    private String  participationStartTime;//开始时间
    private String  participationEndTime;//结束时间
    private int  participationType;//类型1进度，2是分红
    private int  participationState;//类型1进度，2是分红
    private List<Ceil> ceils=new ArrayList<>();

    public ProgressUpdateEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id",0);
        this.investId = jsonObject.optInt("investId",0);
        this.participationTitle = jsonObject.optString("participationTitle");
        this.totalInvestAmount = jsonObject.optInt("totalInvestAmount",0);
        this.bonusShareAmount = jsonObject.optInt("bonusShareAmount",0);
        this.annualizedReturn = jsonObject.optInt("annualizedReturn",0);
        this.participationType = jsonObject.optInt("participationType",0);
        this.participationState = jsonObject.optInt("participationState",0);
        if (!jsonObject.isNull("participationStartTime")) {
            this.participationStartTime = DateFormatUtils.longToDate("yyyy-MM-dd", jsonObject.optLong("participationStartTime", 0));
        }
        if (!jsonObject.isNull("createTime")) {
            this.createTime = DateFormatUtils.longToDate("yyyy/MM/dd", jsonObject.optLong("createTime", 0));
        }
        if (!jsonObject.isNull("participationEndTime")) {
            this.participationEndTime = DateFormatUtils.longToDate("yyyy/MM/dd", jsonObject.optLong("participationEndTime", 0));
        }
        String participationDesc = jsonObject.optString("participationDesc");
        try {
            JSONObject bpdeionObj = new JSONObject(participationDesc);
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

    public String getParticipationTitle() {
        return participationTitle;
    }

    public void setParticipationTitle(String participationTitle) {
        this.participationTitle = participationTitle;
    }

    public int getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(int totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getParticipationType() {
        return participationType;
    }

    public void setParticipationType(int participationType) {
        this.participationType = participationType;
    }

    public int getParticipationState() {
        return participationState;
    }

    public void setParticipationState(int participationState) {
        this.participationState = participationState;
    }

    public List<Ceil> getCeils() {
        return ceils;
    }

    public void setCeils(List<Ceil> ceils) {
        this.ceils = ceils;
    }

    public String getParticipationStartTime() {
        return participationStartTime;
    }

    public void setParticipationStartTime(String participationStartTime) {
        this.participationStartTime = participationStartTime;
    }

    public String getParticipationEndTime() {
        return participationEndTime;
    }

    public void setParticipationEndTime(String participationEndTime) {
        this.participationEndTime = participationEndTime;
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
