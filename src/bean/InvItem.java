package bean;

import java.math.BigDecimal;

/**
 * 发票明细(销售项目)统计后的结果
 */
public class InvItem {
    private String pkid;                //主键
    private String invCode;             //发票代码
    private String invNo;               //发票号码
    private String taxCode;             //税控号
    private String invDate;             //开票日期
    private String custCode;            //客户代码
    private String custName;            //客户名称
    private String itemCode;            //项目代码
    private int num;                    //数量默认为1
    private BigDecimal price;           //单价
    private BigDecimal cash;            //金额
    private String summary;             //摘要

    public InvItem(String pkid, String invCode, String invNo, String taxCode, String invDate, String custCode, String custName, String itemCode, int num, BigDecimal price, BigDecimal cash, String summary) {
        this.pkid = pkid;
        this.invCode = invCode;
        this.invNo = invNo;
        this.taxCode = taxCode;
        this.invDate = invDate;
        this.custCode = custCode;
        this.custName = custName;
        this.itemCode = itemCode;
        this.num = num;
        this.price = price;
        this.cash = cash;
        this.summary = summary;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
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

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getInvDate() {
        return invDate;
    }

    public void setInvDate(String invDate) {
        this.invDate = invDate;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
