package qrybean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 查询基础数据时的检索条件封装类
 */
public class InvIntDataQryCond {
    private String custName;                //客户名称
    private List<String> selectTxnTypes;    //选中的交易类型代码
    private String currencyType = "CNY";    //币别
    private BigDecimal intAmtSta;           //金额范围起
    private BigDecimal intAmtEnd;           //金额范围止
    private String txnDateSta;              //交易月份起
    private String txnDateEnd;              //交易月份止

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
