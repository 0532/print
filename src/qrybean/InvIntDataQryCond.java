package qrybean;

import java.math.BigDecimal;
import java.util.List;

/**
 * ��ѯ��������ʱ�ļ���������װ��
 */
public class InvIntDataQryCond {
    private String custName;                //�ͻ�����
    private List<String> selectTxnTypes;    //ѡ�еĽ������ʹ���
    private String currencyType = "CNY";    //�ұ�
    private BigDecimal intAmtSta;           //��Χ��
    private BigDecimal intAmtEnd;           //��Χֹ
    private String txnDateSta;              //�����·���
    private String txnDateEnd;              //�����·�ֹ

    /**
     * add by wanglichao 2014-12-08
     */
    private String biznam;    //��Ӫ��
    private String cmsnam;    //С��Ӫ��
    private String mngnam;    //�ͻ�����
    private String txndat;    //�ͻ�����

    // = = = = = = = = = = = = = =  = = = = =

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public List<String> getSelectTxnTypes() {
        return selectTxnTypes;
    }

    public void setSelectTxnTypes(List<String> selectTxnTypes) {
        this.selectTxnTypes = selectTxnTypes;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public BigDecimal getIntAmtSta() {
        return intAmtSta;
    }

    public void setIntAmtSta(BigDecimal intAmtSta) {
        this.intAmtSta = intAmtSta;
    }

    public BigDecimal getIntAmtEnd() {
        return intAmtEnd;
    }

    public void setIntAmtEnd(BigDecimal intAmtEnd) {
        this.intAmtEnd = intAmtEnd;
    }

    public String getTxnDateSta() {
        return txnDateSta;
    }

    public void setTxnDateSta(String txnDateSta) {
        this.txnDateSta = txnDateSta;
    }

    public String getTxnDateEnd() {
        return txnDateEnd;
    }

    public void setTxnDateEnd(String txnDateEnd) {
        this.txnDateEnd = txnDateEnd;
    }

    public String getBiznam() {
        return biznam;
    }

    public void setBiznam(String biznam) {
        this.biznam = biznam;
    }

    public String getMngnam() {
        return mngnam;
    }

    public void setMngnam(String mngnam) {
        this.mngnam = mngnam;
    }

    public String getCmsnam() {
        return cmsnam;
    }

    public void setCmsnam(String cmsnam) {
        this.cmsnam = cmsnam;
    }

    public String getTxndat() {
        return txndat;
    }

    public void setTxndat(String txndat) {
        this.txndat = txndat;
    }
}
