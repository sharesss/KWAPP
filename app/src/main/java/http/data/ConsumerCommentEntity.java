package http.data;


import org.json.JSONObject;

import utils.DateFormatUtils;

/**
 * Created by A1 on 2017/2/22.
 */
public class ConsumerCommentEntity {
    private int id;
    private int userId;
    private int parentUserId;//回复某用户Id
    private int parentId;//父评论id 大于0表示回复的那条评论id
    private String parentUserName;//回复某用户昵称
    private String nickName;
    private String headPic;
    private String content;
    private int state;//评论状态：0删除，1正常
    private String time;
    private int likeCount;//该条评论点赞次数
    private int isLike;//当前用户是否点赞
    private int  isFounder;//评论人是否项目创始人
    private int  isInvestAuthen;//评论人是否投资认证



    public ConsumerCommentEntity(JSONObject jsonObj) {
        this.id = jsonObj.optInt("id", 0);
        this.userId = jsonObj.optInt("userId", 0);
        this.parentUserId = jsonObj.optInt("parentUserId", 0);
        this.parentId = jsonObj.optInt("parentId", 0);
        this.state = jsonObj.optInt("state", 0);

        this.parentUserName = jsonObj.optString("parentUserName", "");
        this.nickName = jsonObj.optString("nickName", "");
        this.headPic = jsonObj.optString("headPic", "");
        this.content = jsonObj.optString("content", "");
        if (!jsonObj.isNull("createTime")) {
            this.time = DateFormatUtils.longToDate("yyyy-MM-dd", jsonObj.optLong("createTime", 0));

        }
        this.likeCount = jsonObj.optInt("likeCount", 0);
        this.isLike = jsonObj.optInt("isLike", 0);
        this.isFounder = jsonObj.optInt("isFounder", 0);
        this.isInvestAuthen = jsonObj.optInt("isInvestAuthen", 0);
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

    public int getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(int parentUserId) {
        this.parentUserId = parentUserId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentUserName() {
        return parentUserName;
    }

    public void setParentUserName(String parentUserName) {
        this.parentUserName = parentUserName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getIsFounder() {
        return isFounder;
    }

    public void setIsFounder(int isFounder) {
        this.isFounder = isFounder;
    }

    public int getIsInvestAuthen() {
        return isInvestAuthen;
    }

    public void setIsInvestAuthen(int isInvestAuthen) {
        this.isInvestAuthen = isInvestAuthen;
    }
}
