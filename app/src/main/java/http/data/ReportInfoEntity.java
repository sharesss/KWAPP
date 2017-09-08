package http.data;

import org.json.JSONObject;

/**
 * Created by A1 on 2017/9/5.
 */

public class ReportInfoEntity {
    private int id;
    private String name;

    public ReportInfoEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id",0);
        this.name = jsonObject.optString("name");
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
}
