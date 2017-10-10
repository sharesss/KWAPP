package com.ts.fmxt.ui.im.domain;


import com.ts.fmxt.R;


public class EmojiconExampleGroupData {
    
    private static int[] icons = new int[]{
        R.mipmap.icon_002_cover,
        R.mipmap.icon_007_cover,
        R.mipmap.icon_010_cover,
        R.mipmap.icon_012_cover,
        R.mipmap.icon_013_cover,
        R.mipmap.icon_018_cover,
        R.mipmap.icon_019_cover,
        R.mipmap.icon_020_cover,
        R.mipmap.icon_021_cover,
        R.mipmap.icon_022_cover,
        R.mipmap.icon_024_cover,
        R.mipmap.icon_027_cover,
        R.mipmap.icon_029_cover,
        R.mipmap.icon_030_cover,
        R.mipmap.icon_035_cover,
        R.mipmap.icon_040_cover,
    };
    
    private static int[] bigIcons = new int[]{
            R.mipmap.icon_002,
        R.mipmap.icon_007,
        R.mipmap.icon_010,
        R.mipmap.icon_012,
        R.mipmap.icon_013,
        R.mipmap.icon_018,
        R.mipmap.icon_019,
        R.mipmap.icon_020,
        R.mipmap.icon_021,
        R.mipmap.icon_022,
        R.mipmap.icon_024,
        R.mipmap.icon_027,
        R.mipmap.icon_029,
        R.mipmap.icon_030,
        R.mipmap.icon_035,
        R.mipmap.icon_040,
    };

    private static String[] emojis = new String[]{
             "Tuzki_1",
             "Tuzki_2",
             "Tuzki_3",
             "Tuzki_4",
             "Tuzki_5",
             "Tuzki_6",
             "Tuzki_7",
             "Tuzki_8",
             "Tuzki_9",
             "Tuzki_10",
             "Tuzki_11",
             "Tuzki_12",
             "Tuzki_13",
             "Tuzki_14",
             "Tuzki_15",
             "Tuzki_16",
    };
    private static String[] code = new String[]{
             "Tuzki_1",
             "Tuzki_2",
             "Tuzki_3",
             "Tuzki_4",
             "Tuzki_5",
             "Tuzki_6",
             "Tuzki_7",
             "Tuzki_8",
             "Tuzki_9",
             "Tuzki_10",
             "Tuzki_11",
             "Tuzki_12",
             "Tuzki_13",
             "Tuzki_14",
             "Tuzki_15",
             "Tuzki_16",
    };

    
    private static final EaseEmojiconGroupEntity DATA = createData();
    
    private static EaseEmojiconGroupEntity createData(){
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity();
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], null, EaseEmojicon.Type.BIG_EXPRESSION);
            datas[i].setBigIcon(bigIcons[i]);
            //you can replace this to any you want
            datas[i].setName("");
            datas[i].setIdentityCode(String.valueOf(i));
        }
//        emojiconGroupEntity.setEmojiconList(Arrays.asList(datas));
//        emojiconGroupEntity.setIcon(R.mipmap.icon_kbj);
//        emojiconGroupEntity.setType(EaseEmojicon.Type.BIG_EXPRESSION);
        return emojiconGroupEntity;
    }
    
    
    public static EaseEmojiconGroupEntity getData(){
        return DATA;
    }
}
