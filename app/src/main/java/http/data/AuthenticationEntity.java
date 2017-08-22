package http.data;

import org.json.JSONObject;

/**
 * Created by 认证 on 2017/8/19.
 */

public class AuthenticationEntity {
    private int id;//审核id
    private String propertyphoto;//投资认证图片
    private String  createtime;
    private String  auditor;
    private int auditstate;//审核状态：状态：0删除，1待审，2审核通过，
    private String auditdesc;//审核原因

    public AuthenticationEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.propertyphoto = jsonObject.optString("propertyphoto");
        this.createtime = jsonObject.optString("createtime");
        this.auditdesc = jsonObject.optString("auditdesc");
        this.auditstate = jsonObject.optInt("auditstate");

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPropertyphoto() {
        return propertyphoto;
    }

    public void setPropertyphoto(String propertyphoto) {
        this.propertyphoto = propertyphoto;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public int getAuditstate() {
        return auditstate;
    }

    public void setAuditstate(int auditstate) {
        this.auditstate = auditstate;
    }

    public String getAuditdesc() {
        return auditdesc;
    }

    public void setAuditdesc(String auditdesc) {
        this.auditdesc = auditdesc;
    }
}
