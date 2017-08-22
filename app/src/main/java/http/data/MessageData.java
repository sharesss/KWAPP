package http.data;

/**
 * Created by kp on 2017/8/19.
 */

public class MessageData {
    private boolean flg;
    private static MessageData instance;
     private MessageData (){}
      public static MessageData getInstance() {
            if (instance == null) {
                    instance = new MessageData();
                }
             return instance;
             }

    public void setLeftItemDleTag(boolean flg) {
        this.flg = flg;
    }

    public boolean isDelTag() {
        return flg;
    }
 }


