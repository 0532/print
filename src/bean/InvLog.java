package bean;

import java.io.Serializable;

/**
 * 日志表
 * nanmeiying
 */
public class InvLog implements Serializable {

    private String pkid;        //主键
    private String operName;    //操作人名称
    private String operTime;    //操作时间
    private String invNo;       //发票号码
    private String operInfo;    //操作信息

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public String getOperInfo() {
        return operInfo;
    }

    public void setOperInfo(String operInfo) {
        this.operInfo = operInfo;
    }
}
