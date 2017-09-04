package widget.weixinpay;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

import http.data.WeiXinPayEntity;

/**
 * Created by Administrator on 2016/4/25.
 */
public class playUitls {
    public static playUitls self;

    public static playUitls getInstance() {
        return self == null ? new playUitls() : self;
    }

    private playUitls() {

    }

    public void weixinPlay(WeiXinPayEntity info, Context context){
            PayReq req = new PayReq();
           IWXAPI weixinApi = WXAPIFactory.createWXAPI(context, null);
            req.appId = info.getAppid();
            req.partnerId = info.getMch_id();
            req.prepayId = info.getPrepay_id();
            req.packageValue = "Sign=WXPay";
            req.nonceStr = Util.genNonceStr();
            req.timeStamp = String.valueOf(Util.genTimeStamp());

            List<NameValuePair> signParams = new LinkedList<NameValuePair>();
            signParams.add(new BasicNameValuePair("appid", req.appId));
            signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
            signParams.add(new BasicNameValuePair("package", req.packageValue));
            signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
            signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
            signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

            req.sign = Util.genAppSign(signParams);
            weixinApi.registerApp(info.getAppid());
            weixinApi.sendReq(req);
        }

}
