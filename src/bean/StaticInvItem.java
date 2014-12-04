package bean;

import java.math.BigDecimal;

public class StaticInvItem {
    private String invCode;             //发票代码
    private String invNo;               //发票号码
    private String custName;            //客户名称
    private String itemName;            //项目名称
    private BigDecimal cash;            //金额
    private String invDate;             //开票日期yyyymmdd
    private String taxCode;             //税控号
    private String summary;             //摘要

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
