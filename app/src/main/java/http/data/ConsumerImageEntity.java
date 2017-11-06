package http.data;

/**
 * Created by A1 on 2017/2/16.
 */
public class ConsumerImageEntity {


    private String qiniuToken;
    private int position;
    private String url;
    private String context;
    private String locatstion;

    public String getQiniuToken() {
        return qiniuToken;
    }

    public void setQiniuToken(String qiniuToken) {
        this.qiniuToken = qiniuToken;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLocatstion() {
        return String.format("consumer_image_%s.jpg", String.valueOf(position));
    }
}
