package http.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A1 on 2017/3/16.
 */
public class UserCityEntity {
    private String name;
    private List list=new ArrayList();

    public UserCityEntity(JSONObject json){
        this.name=json.optString("province");
        if(!json.isNull("citys")){
            JSONArray array=json.optJSONArray("citys");
            for(int i=0;i<array.length();i++){
                try {
                    list.add((String)array.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
