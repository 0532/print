package qrybean;

/**
 * ��ѯ��Ʊ����������װ��
 */
public class InvInfoQryCond {
    private String invCode;             //��Ʊ����
    private String invNo;               //��Ʊ����
    private String fadeInvCode;         //��Ʊ������
    private String fadeInvNo;           //��Ʊ��Ʊ����
    private String custName;            //�ͻ�����
    private String invType;             //��Ʊ����
    private String invDateSta;          //��Ʊ������
    private String invDateEnd;          //��Ʊ����ֹ


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
