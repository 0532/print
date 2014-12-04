package qrybean;

/**
 * 查询发票检索条件封装类
 */
public class InvInfoQryCond {
    private String invCode;             //发票代码
    private String invNo;               //发票号码
    private String fadeInvCode;         //退票发代码
    private String fadeInvNo;           //退票发票号码
    private String custName;            //客户名称
    private String invType;             //开票类型
    private String invDateSta;          //开票日期起
    private String invDateEnd;          //开票日期止


    public String getInvCode() {
        return invCode;
    }

    public void setInvCode(String invCode) {
        this.invCode = invCode;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public String getFadeInvCode() {
        return fadeInvCode;
    }

    public void setFadeInvCode(String fadeInvCode) {
        this.fadeInvCode = fadeInvCode;
    }

    public String getFadeInvNo() {
        return fadeInvNo;
    }

    public void setFadeInvNo(String fadeInvNo) {
        this.fadeInvNo = fadeInvNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public String getInvDateSta() {
        return invDateSta;
    }

    public void setInvDateSta(String invDateSta) {
        this.invDateSta = invDateSta;
    }

    public String getInvDateEnd() {
        return invDateEnd;
    }

    public void setInvDateEnd(String invDateEnd) {
        this.invDateEnd = invDateEnd;
    }
}
