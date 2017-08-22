package http.data;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/21.
 */
public class WeiXinPayEntity {

    /**
     * return_code : SUCCESS
     * result_code : SUCCESS
     * return_msg : OK
     * appid : wx5ccd530cd75841dd
     * mch_id : 1286923901
     * device_info : WEB
     * nonce_str : K9R1yXkTMqfS9S5x
     * sign : 9886BF299FF4775B071454788CDA284B
     * prepay_id : wx20160421175003c8382c59e90511478742
     * trade_type : APP
     */

    private String return_code;
    private String result_code;
    private String return_msg;
    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String prepay_id;
    private String trade_type;

    public WeiXinPayEntity(JSONObject json) {
        this.return_code = json.optString("returnCode", "");
        this.result_code = json.optString("resultCode", "");
        this.return_msg = json.optString("returnMsg", "");
        this.appid = json.optString("appid", "");
        this.mch_id = json.optString("mchId", "");
        this.device_info = json.optString("deviceInfo", "");
        this.nonce_str = json.optString("nonceStr", "");
        this.sign = json.optString("sign", "");
        this.prepay_id = json.optString("prepayId", "");
        this.trade_type = json.optString("tradeType", "");
    }


    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
}
