package http.data;

import org.json.JSONObject;

/**
 * Created by A1 on 2017/10/23.
 */

public class UserIndustryEntity {
    private int id;
    private String name;
    private boolean isChox;

    public UserIndustryEntity(JSONObject json) {
        this.id = json.optInt("id", 0);
        this.name = json.optString("name", "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChox() {
        return isChox;
    }

    public void setChox(boolean chox) {
        isChox = chox;
    }
}
