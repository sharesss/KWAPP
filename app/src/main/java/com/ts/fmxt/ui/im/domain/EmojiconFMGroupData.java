package com.ts.fmxt.ui.im.domain;


//疯蜜表情包
public class EmojiconFMGroupData {


    private static final EaseEmojiconGroupEntity DATA = createData();

    private static EaseEmojiconGroupEntity createData() {
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity();

//        String jsonStr = FMWession.getInstance().getFMEmojicon();
//        if (!StringUtils.isEmpty(jsonStr)) {
//            JSONObject json = null;
//            try {
//                json = new JSONObject(jsonStr);
//                JSONArray array = json.optJSONArray("phizs");
//                EaseEmojicon[] datas = new EaseEmojicon[array.length()];
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject itemObj = array.optJSONObject(i);
//                    datas[i] = new EaseEmojicon(itemObj.optInt("id"), null, EaseEmojicon.Type.FM_EXPRESSION);
//                    datas[i].setUrl(itemObj.optString("phizPng"));
//                    datas[i].setName(itemObj.optString("phizName"));
//                    datas[i].setIdentityCode(String.valueOf(0));
//                    datas[i].setSendGifUrl(itemObj.optString("phizGif"));
//                }
//                emojiconGroupEntity.setEmojiconList(Arrays.asList(datas));
//                emojiconGroupEntity.setType(EaseEmojicon.Type.BIG_EXPRESSION);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        return emojiconGroupEntity;
    }


    public static EaseEmojiconGroupEntity getData() {
        return DATA;
    }
}
