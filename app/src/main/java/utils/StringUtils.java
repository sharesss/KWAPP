package utils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验银行卡证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     * "^\\d{19}$"
     */
    public static boolean isBankCard(String idCard) {
        return Pattern.matches("^\\d{19}$", idCard);
    }


    /**
     * 是否为Empty
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        if (value == null)
            return true;
        if (value.trim().equals(""))
            return true;
        return false;
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 设置小数为两位
     *
     * @param price
     * @param decm
     * @return
     */
    public static String floattostring(float price, int decm) {
        String sb = "";
        DecimalFormat df;

        switch (decm) {
            case 0:
                df = new DecimalFormat("#");
                sb += df.format(price);
                break;
            case 1:
                df = new DecimalFormat("#0.0");
                sb += df.format(price);
                break;
            case 2:
                df = new DecimalFormat("#0.00");
                sb += df.format(price);
                break;
            case 3:
                df = new DecimalFormat("#0.000");
                sb += df.format(price);
                break;
            case 4:
                df = new DecimalFormat("#0.0000");
                sb += df.format(price);
                break;
            case 5:
                df = new DecimalFormat("#0.00000");
                sb += df.format(price);
                break;
            case 6:
                df = new DecimalFormat("#0.000000");
                sb += df.format(price);
                break;
        }
        return sb;
    }

    /**
     * 设置小数为两位
     *
     * @param price
     * @param decm
     * @return
     */
    public static String floattostring(Double price, int decm) {
        String sb = "";
        DecimalFormat df;

        switch (decm) {
            case 0:
                df = new DecimalFormat("#");
                sb += df.format(price);
                break;
            case 1:
                df = new DecimalFormat("#0.0");
                sb += df.format(price);
                break;
            case 2:
                df = new DecimalFormat("#0.00");
                sb += df.format(price);
                break;
            case 3:
                df = new DecimalFormat("#0.000");
                sb += df.format(price);
                break;
            case 4:
                df = new DecimalFormat("#0.0000");
                sb += df.format(price);
                break;
            case 5:
                df = new DecimalFormat("#0.00000");
                sb += df.format(price);
                break;
            case 6:
                df = new DecimalFormat("#0.000000");
                sb += df.format(price);
                break;
        }
        return sb;
    }


    /**
     * 字符串类型的转换成两位小数,正数是否有加号
     *
     * @param str
     * @param isplusign
     * @return
     */
    public static String floattostring(String str, boolean isplusign) {
        String tempStr = "";
        float tempFloat = 0.0f;
        if (str != null && !"".equals(str)) {
            tempFloat = Float.valueOf(str);
            tempStr = floattostring(tempFloat, 2);
            if (isplusign == true) {
                if (tempFloat > 0) {
                    tempStr = "+" + tempStr;
                }
            }
        }
        return tempStr;
    }

    /**
     * 字符串类型的转换成两位小数,正数是否有加号
     *
     * @param str
     * @param isplusign
     * @return
     */
    public static String floattostring(double str, boolean isplusign) {

        return floattostring(str, 2, isplusign);
    }

    /**
     * 字符串类型的转换成两位小数,正数是否有加号
     *
     * @param str
     * @param isplusign
     * @return
     */
    public static String floattostring(double str, int position, boolean isplusign) {
        String tempStr = "";
        tempStr = floattostring(str, position);
        if (isplusign == true) {
            if (str > 0)
                tempStr = "+" + tempStr;
        }
        return tempStr;
    }

    /**
     * double类型转换成int,正数是否有加号
     *
     * @param str
     * @param isplusign
     * @return
     */
    public static String inttostring(double str, boolean isplusign) {
        String tempStr = "";
        tempStr = floattostring(str, 0);
        if (isplusign == true) {
            if (str > 0) {
                tempStr = "+" + tempStr;
            }
        }
        return tempStr;
    }


    /**
     * float类型的转换成两位小数
     *
     * @param floatStr
     * @return
     */
    public static String floattostring(float floatStr) {
        return floattostring(floatStr, 2);
    }

    /**
     * float类型的转换成两位小数
     *
     * @param floatStr
     * @return
     */
    public static String floattostring(Double floatStr) {
        return floattostring(floatStr, 2);
    }

    /**
     * float类型的转换成两位小数
     *
     * @param floatStr
     * @return
     */
    public static String floattotoPercentStr(Double floatStr, boolean isPercent) {
        return isPercent ? floattostring(floatStr, 2) + "%" : floattostring(floatStr, 2);
    }

    /**
     * percent
     * 获取http请求返回的提示信息
     *
     * @param json
     * @param str
     * @return
     * @throws Exception
     */
    public static String getResultMessage(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject resultJsonObject = jsonObject.getJSONObject("result");
        String message = resultJsonObject.getString("message");
        return message;
    }


    public static String getByteToStr(byte[] value) {
        try {
            return new String(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    //回车换行隐性\n替换成显性的\字符串
    public static String replaceEnterContent(String content) {
        return content.replaceAll("\n", "\\\\n");
    }
}
