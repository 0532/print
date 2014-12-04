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
}
