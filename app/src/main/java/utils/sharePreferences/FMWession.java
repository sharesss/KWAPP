package utils.sharePreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.thindo.base.Utils.sharepreferences.BaseSharePreferences;
import com.ts.fmxt.FmxtApplication;

import org.json.JSONObject;

import http.Logger;
import utils.StringUtils;

public class FMWession extends BaseSharePreferences {
    private static SharedPreferences preferences;
    private Logger logger = new Logger(this.getClass().getSimpleName());
    private final static String SHARE_NAME = "FMwession";
    private JSONObject jsonObject;

    public FMWession() {
        super(SHARE_NAME);
    }

    private static FMWession stWession = null;

    public static FMWession getInstance() {
        if (stWession == null)
            stWession = new FMWession();
        return stWession;
    }

    public boolean isGuide() {
        return readValue("Guide", true);
    }

    public void setGuide(boolean flg) {
        writeValue("Guide", flg);
    }

    public void setBuyInsuranceStatus(boolean flg) {
        writeValue("buyInsuranceStatus", flg);
    }

    public boolean getBuyInsuranceStatus() {
        return readValue("buyInsuranceStatus", false);
    }

    public void setLoginInfo(String json) {
        writeValue("login", json);
    }

    public String getLoginInfoJson() {
        return readValue("login", "");
    }

    public void setUserInfo(String json) {
        writeValue("user_info", json);
    }

//    public UserInfoEntity getUserInfo() {
//        UserInfoEntity info = null;
//        String json = readValue("user_info", "");
//        if (!StringUtils.isEmpty(json)) {
//            try {
//                JSONObject jsonObject = new JSONObject(json);
//                info = new UserInfoEntity(jsonObject.optJSONObject("userInfo"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return info;
//    }

    public void setToken(String token) {
//        FmxtApplication.token = token;
        writeValue("token", token);
    }

    public void setFMIdentity(String identity) {
        writeValue("fm_identity", identity);
    }

    public String getFMIdentity() {
        return readValue("fm_identity", "");
    }


    public boolean isLogin() {
        return StringUtils.isEmpty(getToken());
    }

    public String getToken() {
        return readValue("token", "");
    }


    public void setRefreshUser(boolean flg) {
        writeValue("isRefreshUser", flg);
    }


    public void setUserGuide(boolean flg) {
        writeValue("user_guide_" + FmxtApplication.APP_VERSION, flg);
    }


    public static void setCofigInt(Context context, int b, String key) {
        Tool(context).edit().putInt(key, b).commit();
    }

    @SuppressLint("CommitPrefEdits")
    public static void setCofigBoolean(Context context, boolean b, String key) {
        Tool(context).edit().putBoolean(key, b).commit();
    }

    public static boolean getConfigBoolean(Context context, String key) {
        return Tool(context).getBoolean(key, false);
    }

    public static void clear(Context context) {
        Tool(context).edit().clear();
    }

    public static SharedPreferences Tool(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("USER_CONFIG", Context.MODE_PRIVATE);
        }
        return preferences;
    }


    public boolean toDayActivity(String date) {
        String str = readValue("day_activity_list", "");
        if (Integer.valueOf(date) < 10)
            date = String.format("0%s", date);

        return str.indexOf(date) == -1;
    }


    public String getIMOtherUserName() {
        return readValue("other_user_name", "");
    }

    public void setIMOtherUserName(String name) {
        writeValue(String.format("other_user_name", name), name);
    }

    public String getIMOtherUserPath() {
        return readValue("other_user_path", "");
    }

    public void setIMOtherUserPath(String name) {
        writeValue("other_user_path", name);
    }

    public String getIM(String key) {//im_avatar",
        return readValue(key, "");
    }

    public void setIM(String key, String val) {//im_avatar",
        writeValue(key, val);
    }

    public void setIMPath(String iamge_uri) {
        writeValue("im_path", iamge_uri);
    }

    public void setOtherDistance(String distance) {//im_avatar",
        writeValue("user_distance", distance);
    }

    public String getOtherDistance() {//im_avatar",
        return readValue("user_distance", "");
    }

    public String getIMPath() {//im_avatar",
        return readValue("im_path", "");
    }

    public int getUserId() {
        return readValue("user_id", 0);
    }

    public void setUserId(int id) {
        writeValue("user_id", id);
    }

    public String getSendGifUri() {
        return readValue("send_gif_uri", "");
    }

    public String getSendGifName() {
        return readValue("send_gif_name", "");
    }

    public void setSendGifName(String name) {
        writeValue("send_gif_name", name);
    }

    public void setSendGifUri(String uri) {
        writeValue("send_gif_uri", uri);
    }

    public void setSendGifAnimUri(String uri) {
        writeValue("send_gif_anim_uri", uri);
    }

    public String getSendGifAnimUri() {
        return readValue("send_gif_anim_uri", "");
    }

    public void setSendGifNum(int num) {
        writeValue("send_gif_num", String.valueOf(num));
    }

    public String getSendGifNum() {
        return readValue("send_gif_num", "1");
    }

    public boolean getUserLikeFlg(int id) {
        boolean flg = true;
        String str = getUserLikeStr();
        if (!StringUtils.isEmpty(str)) {
            String[] ids = str.split(",");
            for (int i = 0; i < ids.length; i++) {
                String tempNum = ids[i];
                if (!StringUtils.isEmpty(tempNum)) {
                    if (Integer.valueOf(tempNum) == id) {
                        flg = false;
                        break;
                    }
                }

            }
        }
        return flg;
    }

    public void setIMOtherId(String uuId, String userId) {
        writeValue(uuId, userId);
    }

    public String getIMOtherId(String uuId) {
        return readValue(uuId, "0");
    }

    public String getUserLikeStr() {
        return readValue("user_like", "");
    }

    public void setUserLike(String id) {
        String str = getUserLikeStr() + "," + id;
        writeValue("user_like", str);
    }


    public void setwithdrawalsInfo(String wx, String phone) {
//        writeValue(String.format("%s_%s", "withdrawals", getUserInfo().getUuid()), String.format("%s_%s", wx, phone));
    }

//    public String[] getwithdrawalsInfo() {
//        String str = readValue(String.format("%s_%s", "withdrawals", getUserInfo().getUuid()), "");
//        String[] arrayList = new String[2];
//        if (!StringUtils.isEmpty(str)) {
//            arrayList = str.split("_");
//        }
//        return arrayList;
//    }

    public void clearUserLike() {
        writeValue("user_like", "");
    }

//    public void clearSearchHistory() {
//        writeValue(String.format("%s_%s", "search_history", getUserInfo().getUuid()), "");
//    }

//    public void clearSelectHistory() {
//        writeValue(String.format("%s_%s", "select_brand_history", getUserInfo().getUuid()), "");
//    }

    public void setUserAccount(String account) {
        writeValue("user_account", account);
    }

    public String getUserAccount() {
        return readValue("user_account", "");
    }

//    public void setSearchHistory(String keyworke) {
//        try {
//            keyworke = URLDecoder.decode(keyworke, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String historyStr = "";
//        List<Object> historyList = getSearchHistory();
//        if (historyList != null) {
//            historyStr = readValue(String.format("%s_%s", "search_history", getUserInfo().getUuid()), "");
//            if (!booleanKeyWord(keyworke))
//                historyStr = historyStr + "," + keyworke;
//        } else {
//            historyStr = keyworke;
//        }
//        writeValue(String.format("%s_%s", "search_history", getUserInfo().getUuid()), historyStr);
//
//    }

//    private boolean booleanKeyWord(String keyworke) {
//        boolean flg = false;
//        List<Object> historyList = getSearchHistory();
//        int tempI = 0;
//        for (int j = historyList.size() - 1; j >= 0; j--) {
//            if (tempI >= 4) break;
//            if (keyworke.equals(historyList.get(j).toString())) {
//                flg = true;
//            }
//            tempI++;
//        }
//        return flg;
//    }

//    private boolean booleanBrandWord(String keyworke) {
//        boolean flg = false;
//        List<Object> historyList = getSelectHistory();
//        int tempI = 0;
//        for (int j = historyList.size() - 1; j >= 0; j--) {
//            if (tempI >= 4) break;
//            if (keyworke.equals(historyList.get(j).toString())) {
//                flg = true;
//            }
//            tempI++;
//        }
//        return flg;
//    }

//    public List<Object> getSearchHistory() {
//        if (getUserInfo() == null)
//            return null;
//        ArrayList<Object> list = null;
//        String historyStr = readValue(String.format("%s_%s", "search_history", getUserInfo().getUuid()), "");
//        if (!StringUtils.isEmpty(historyStr)) {
//            list = new ArrayList<>();
//            String[] history = historyStr.split(",");
//            int tempI = 0;
//            for (int i = history.length - 1; i >= 0; i--) {
//                if (tempI >= 4) break;
//                list.add(history[i]);
//                tempI++;
//            }
//        }
//        return list;
//    }

    //获取品牌选择历史
//    public List<Object> getSelectHistory() {
//        if (getUserInfo() == null)
//            return null;
//        ArrayList<Object> list = null;
//        String historyStr = readValue(String.format("%s_%s", "select_brand_history", getUserInfo().getUuid()), "");
//        if (!StringUtils.isEmpty(historyStr)) {
//            list = new ArrayList<>();
//            String[] history = historyStr.split(",");
//            int tempI = 0;
//            for (int i = history.length - 1; i >= 0; i--) {
//                if (tempI >= 4) break;
//                list.add(history[i]);
//                tempI++;
//            }
//        }
//        return list;
//    }

//    public void setSelectHistory(String brand) {
//        try {
//            brand = URLDecoder.decode(brand, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String historyStr = "";
//        List<Object> historyList = getSelectHistory();
//        if (historyList != null) {
//            historyStr = readValue(String.format("%s_%s", "select_brand_history", getUserInfo().getUuid()), "");
//            if (!booleanBrandWord(brand))
//                historyStr = historyStr + "," + brand;
//        } else {
//            historyStr = brand;
//        }
//        writeValue(String.format("%s_%s", "select_brand_history", getUserInfo().getUuid()), historyStr);
//    }


    public boolean isWexLogin() {
        return readValue("wx_login", false);
    }

    public void setWxLogin(boolean flg) {
        writeValue("wx_login", flg);
    }

    public boolean isLuckyUser() {
        return readValue("lucky_user", false);
    }

    public void setLuckyUser(boolean key) {
        writeValue("lucky_user", key);
    }

//    public boolean isShowSearchHistory() {
//        return getSearchHistory() == null;
//    }

//    public boolean isShowSelectHistory() {
//        return getSelectHistory() == null;
//    }

    public void setLeftItemDleTag(boolean flg) {
        writeValue("del_flg", flg);
    }

    public boolean isDelTag() {
        return readValue("del_flg", false);
    }

    public boolean isOpenLocation() {
        return readValue("is_open_location", true);
    }

    public void setOpenLocation(boolean flg) {
        writeValue("is_open_location", flg);
    }

    //****************消费版本*******************
    public void setBrandRate(String brandRate) {
        writeValue("brandRate", brandRate);
    }

    public double getBrandRateStr() {
        String str = readValue("brandRate", "0");
        return Double.valueOf(str);
    }

    public void setPlayTourMax(String playTourMax) {
        writeValue("play_turmax", playTourMax);
    }

    public double getPlayTourMax() {
        return Double.valueOf(readValue("play_turmax", "0"));
    }
    public void setEndorsementFeeMax(String endorsementFeeMax) {
        writeValue("endorsementFeeMax", endorsementFeeMax);
    }

    public double getEndorsementFeeMax() {
        return Double.valueOf(readValue("endorsementFeeMax", "1"));
    }

    public String getBrandRate() {
        String str = readValue("brandRate", "0");
        return StringUtils.floattostring(Double.valueOf(str), 0);
    }

    public void setCnsumerSmall(int cnsumerVal) {
        writeValue("cnsumer_small", cnsumerVal);
    }

    public int getCnsumerSmall() {
        return readValue("cnsumer_small", 0);
    }

    public void setSortLetters(String str) {
        writeValue("sortLetters", str);
    }

    public String getSortLetters() {
        return readValue("sortLetters", "");
    }

    public void setBrandFlg(boolean flg) {
        writeValue("brand_flg", flg);
    }

    public boolean getBrandFlg() {
        return readValue("brand_flg", false);
    }

    public void setDelConsumerFlg(boolean flg) {
        writeValue("del_consumer_flg", flg);
    }

    public boolean getDelConsumerFlg() {
        return readValue("del_consumer_flg", false);
    }

    public void setConsumerLocation(String location) {
        writeValue("consumer_location_name", location);
    }

    public String getConsumerLocation() {
        return readValue("consumer_location_name", "");
    }

    public void setConsumerLocationXY(String location) {
        writeValue("consumer_location_name_xy", location);
    }

    public String getConsumerLocationXY() {
        return readValue("consumer_location_name_xy", "");
    }

    public void setFMEmojicon(String json) {
        writeValue("emojicon_fm", json);
    }

    public String getFMEmojicon() {
        return readValue("emojicon_fm", "");
    }
    //****************消费版本*******************


    //******************百度地图缓存***************************
    public void setBaiDuAPILocation(String json) {
        writeValue("baidu_location", json);
    }

//    public BaiDuLocationEntity getBaiDuAPILocation() {
//        BaiDuLocationEntity info = null;
//        String jsonStr = readValue("baidu_location", "");
//        if (!StringUtils.isEmpty(jsonStr)) {
//            try {
//                info = new BaiDuLocationEntity(new JSONObject(jsonStr));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return info == null ? new BaiDuLocationEntity() : info;
//    }
    //*************************************************************************************

}
