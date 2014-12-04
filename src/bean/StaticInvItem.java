package bean;

import java.math.BigDecimal;

public class StaticInvItem {
    private String invCode;             //��Ʊ����
    private String invNo;               //��Ʊ����
    private String custName;            //�ͻ�����
    private String itemName;            //��Ŀ����
    private BigDecimal cash;            //���
    private String invDate;             //��Ʊ����yyyymmdd
    private String taxCode;             //˰�غ�
    private String summary;             //ժҪ

    public StaticInvItem(String invCode, String invNo, String custName, String itemName, BigDecimal cash, String invDate, String taxCode, String summary) {
        this.invCode = invCode;
        this.invNo = invNo;
        this.custName = custName;
        this.itemName = itemName;
        this.cash = cash;
        this.invDate = invDate;
        this.taxCode = taxCode;
        this.summary = summary;
    }

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

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public String getInvDate() {
        return invDate;
    }

    public void setInvDate(String invDate) {
        this.invDate = invDate;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
