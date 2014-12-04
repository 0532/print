package bean;

import java.math.BigDecimal;

/**
 * 税控基础数据信息
 */
public class InvIntData {
    private String pkid;                    //主键
    private String invCode;                 //发票代码
    private String invNo;                   //发票号码
    private String custCode;                //客户代码
    private String custName;                //客户名称
    private String txnDate;                 //交易日期
    private BigDecimal intAmt;              //利息金额
    private String txnType;                 //业务类别   01：贷款 02：贴现 03：个人
    private String currencyType = "CNY";    //币别 CNY人民币 USD美元 HKD港币
    private String iouNum;                  //借据号
    private String itemState;               //状态   1未开票 2已开票

    public InvIntData(String pkid, String custCode, String custName, String txnDate, BigDecimal intAmt, String txnType, String currencyType, String iouNum) {
        this.pkid = pkid;
        this.custCode = custCode;
        this.custName = custName;
        this.txnDate = txnDate;
        this.intAmt = intAmt;
        this.txnType = txnType;
        this.currencyType = currencyType;
        this.iouNum = iouNum;
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

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public BigDecimal getIntAmt() {
        return intAmt;
    }

    public void setIntAmt(BigDecimal intAmt) {
        this.intAmt = intAmt;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getIouNum() {
        return iouNum;
    }

    public void setIouNum(String iouNum) {
        this.iouNum = iouNum;
    }

    public String getItemState() {
        return itemState;
    }

    public void setItemState(String itemState) {
        this.itemState = itemState;
    }
}
