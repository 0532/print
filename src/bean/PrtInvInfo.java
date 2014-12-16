package bean;

import java.math.BigDecimal;

/**
 * Author: Lichao.W
 * On 2014/12/9 At 17:24
 */
public class PrtInvInfo {
    private String invCode;                 //发票代码
    private String invNo;                   //发票号码
    private String custCode;                //客户代码
    private String custName;                //客户名称
    private String txnDate;                 //交易日期
    private BigDecimal intAmt;              //利息金额
    private String txnType;                 //业务类别   01：贷款 02：贴现 03：个人
    private String currencyType;           //币别 CNY人民币 USD美元 HKD港币
    private String iouNum;                  //借据号
    private String itemState;               //状态   1未开票 2已开票
    private String apndate;                  //起息日
    private String creamt;                   //发放金额
    private String debamt;                   //还款金额
    private String contno;                   //合同号
    private String invrat;                   //利率
    private String prtdat;                   //发票打印日期


    public PrtInvInfo(String invCode, String invNo, String custCode, String custName,
                      String txnDate, BigDecimal intAmt, String txnType, String currencyType,
                      String iouNum, String itemState, String apndate, String creamt,
                      String debamt, String contno,String invrat,String prtdat) {
        this.invCode = invCode;
        this.invNo = invNo;
        this.custCode = custCode;
        this.custName = custName;
        this.txnDate = txnDate;
        this.intAmt = intAmt;
        this.txnType = txnType;
        this.currencyType = currencyType;
        this.iouNum = iouNum;
        this.itemState = itemState;
        this.apndate = apndate;
        this.creamt = creamt;
        this.debamt = debamt;
        this.contno = contno;
        this.contno = invrat;
        this.contno = prtdat;
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

    public String getApndate() {
        return apndate;
    }

    public void setApndate(String apndate) {
        this.apndate = apndate;
    }

    public String getCreamt() {
        return creamt;
    }

    public void setCreamt(String creamt) {
        this.creamt = creamt;
    }

    public String getDebamt() {
        return debamt;
    }

    public void setDebamt(String debamt) {
        this.debamt = debamt;
    }

    public String getContno() {
        return contno;
    }

    public void setContno(String contno) {
        this.contno = contno;
    }

    public String getInvrat() {
        return invrat;
    }

    public void setInvrat(String invrat) {
        this.invrat = invrat;
    }

    public String getPrtdat() {
        return prtdat;
    }

    public void setPrtdat(String prtdat) {
        this.prtdat = prtdat;
    }
}
