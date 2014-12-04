package bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 开票数据表
 */
public class InvInvInfo implements Serializable {
    private String pkid;            //主键
    private String invCode;         //发票代码
    private String invNo;           // 发票号码
    private String invDate;         //开票日期yyyymmdd
    private String opter;           //开票人
    private String macCode;         //机器编码
    private String taxCode;         //税控号
    private String invType;         // 开票类型1、正常票 2、退票 3、补录发票
    private String custCode;        //客户代码
    private String custName;        //客户名称
    private String fadeInvCode = "";//退票发票代码
    private String fadeInvNo = "";  //退票发票号码
    private String busKind;         //行业类型 固定为：金融保险业
    private String payee;           //收款单位
    private BigDecimal priceSum;    //发票合计金额
    private String invVer;          //版本号   1:未抄票 2:已抄票
    private String remark;          //备注
    private String invRemark = "";       //发票备注

    public InvInvInfo(String pkid, String invDate, String invType, String custCode, String custName, String busKind, String payee, String invVer) {
        this.pkid = pkid;
        this.invDate = invDate;
        this.invType = invType;
        this.custCode = custCode;
        this.custName = custName;
        this.busKind = busKind;
        this.payee = payee;
        this.invVer = invVer;
    }

    public InvInvInfo(String pkid, String invCode, String invNo, String invDate, String opter, String macCode, String taxCode, String invType, String custCode, String custName, String fadeInvCode, String fadeInvNo, String busKind, String payee, BigDecimal priceSum, String invVer, String remark,String invRemark) {
        this.pkid = pkid;
        this.invCode = invCode;
        this.invNo = invNo;
        this.invDate = invDate;
        this.opter = opter;
        this.macCode = macCode;
        this.taxCode = taxCode;
        this.invType = invType;
        this.custCode = custCode;
        this.custName = custName;
        this.fadeInvCode = fadeInvCode;
        this.fadeInvNo = fadeInvNo;
        this.busKind = busKind;
        this.payee = payee;
        this.priceSum = priceSum;
        this.invVer = invVer;
        this.remark = remark;
        this.invRemark = invRemark;
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

    public String getInvDate() {
        return invDate;
    }

    public void setInvDate(String invDate) {
        this.invDate = invDate;
    }

    public String getOpter() {
        return opter;
    }

    public void setOpter(String opter) {
        this.opter = opter;
    }

    public String getMacCode() {
        return macCode;
    }

    public void setMacCode(String macCode) {
        this.macCode = macCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
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

    public String getBusKind() {
        return busKind;
    }

    public void setBusKind(String busKind) {
        this.busKind = busKind;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public BigDecimal getPriceSum() {
        return priceSum;
    }

    public void setPriceSum(BigDecimal priceSum) {
        this.priceSum = priceSum;
    }

    public String getInvVer() {
        return invVer;
    }

    public void setInvVer(String invVer) {
        this.invVer = invVer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInvRemark() {
        return invRemark;
    }

    public void setInvRemark(String invRemark) {
        this.invRemark = invRemark;
    }
}
