package widget.picturebrows;

import android.os.Bundle;

import org.json.JSONObject;

import java.io.Serializable;

import utils.StringUtils;

public class PictureBrowsInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tag_desc;
    private String background;
    private String live_id;
    private String sn;
    private int type;
    private String replay_id;
    private String channel;
    private String usign;
    private int poster_foreign_id;
    private int poster_type;
    private int id;
    private Object imageUrl;
    private String urlName;
    private String advertisement_url;
    private String poster_name;
    private String poster_url;
    private String poster_desc;
    private String remark;
    private int state;//广告状态：-1下架，0待上架，1上架
    private Bundle var1;

    /**
     * type	否	Int	直播所需的参数
     * sn	否	String	直播所需的参数
     * background	否	String	直播所需的参数
     * live_id	否	String	直播所需的参数
     * replay_id	否	String	直播所需的参数
     * channel	否	String	直播所需的参数
     * usign	否	String	直播所需的参数
     *
     * @param json
     */
    public PictureBrowsInfo(JSONObject json) {

        this.type = json.optInt("type", 0);
        this.sn = json.optString("sn", "");
        this.background = json.optString("background", "");
        this.live_id = json.optString("live_id", "");
        this.replay_id = json.optString("replay_id", "");
        this.channel = json.optString("channel", "");
        this.usign = json.optString("usign", "");
        this.tag_desc = json.optString("tag_desc", "");
        initLiveInfo();
        this.id = json.optInt("id", 0);
        this.imageUrl = json.optString("poster_icon", "");
        this.poster_name = json.optString("poster_name", "");
        this.poster_url = json.optString("poster_url", "");
        this.poster_desc = json.optString("poster_desc", "");
        this.remark = json.optString("remark", "");
        this.poster_type = json.optInt("poster_type", 0);
        this.poster_foreign_id = json.optInt("poster_foreign_id", 0);


    }

    private void initLiveInfo() {
        if (StringUtils.isEmpty(this.live_id)) return;

        var1 = new Bundle();
        var1.putInt("type", this.type);
        var1.putString("sn", this.sn);
        var1.putString("background", this.background);
        var1.putString("liveId", this.live_id);
        var1.putString("replayId", this.replay_id);

        var1.putString("channel", this.channel);
        var1.putString("usign", this.usign);

    }

    public PictureBrowsInfo() {

    }

    public Bundle getVar1() {
        return var1;
    }

    public void setPoster_foreignId(int poster_foreignId) {
        this.poster_foreign_id = poster_foreignId;
    }

    public int getPoster_foreignId() {
        return poster_foreign_id;
    }

    public void setPoster_type(int poster_type) {
        this.poster_type = poster_type;
    }

    public int getPoster_type() {
        return poster_type;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdvertisement_url() {
        return advertisement_url;
    }

    public void setAdvertisement_url(String advertisement_url) {
        this.advertisement_url = advertisement_url;
    }

    public String getPoster_name() {
        return poster_name;
    }

    public void setPoster_name(String poster_name) {
        this.poster_name = poster_name;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getPoster_desc() {
        return poster_desc;
    }

    public void setPoster_desc(String poster_desc) {
        this.poster_desc = poster_desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTag_desc() {
        return tag_desc;
    }
}
