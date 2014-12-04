package action;

import bean.InvInvInfo;
import org.apache.log4j.Logger;
import service.RiService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@ManagedBean
@ViewScoped
public class RiAction {

    private static Logger logger = Logger.getLogger(RiAction.class);
    @ManagedProperty(value = "#{riService}")
    private RiService riService;

    private String itemType = "01";//业务类型
    private String invType = "3";//发票类型 3正常票 2退票
    private String invCode;//发票代码
    private String invNo;//业务号码
    private String fadeInvCode;//退票代码
    private String fadeInvNo;//退票号码
    private String custName;//客户名称
    private String taxCode;//税控号
    private String invDate;//开票日期
    private String txnDate;//交易日期
    private BigDecimal cash;//发票金额

    @PostConstruct
    public void init() {
    }

    /**
     * 手工录入发票信息以及发票明细信息
     */
    public void inputInvInfo() {
        InvInvInfo invInfo;

        boolean flag = riService.existInv(invCode, invNo);
        if (flag) {
            addMessage(FacesMessage.SEVERITY_ERROR, "此发票已存在，请重新输入！");
            return;
        }
        if ("3".equals(invType)) {
            if (cash.compareTo(new BigDecimal(0)) <= 0) {
                addMessage(FacesMessage.SEVERITY_ERROR, "金额应大于0！");
                return;
            }
            invInfo = new InvInvInfo(UUID.randomUUID().toString(), invCode, invNo, invDate, "", "", taxCode, invType, "", custName, "", "", "金融保险业", "海尔集团财务责任有限公司", cash, "1", txnDate + itemType,"");
        } else {
            if (!fadeInvCode.matches("\\d{20}") || !fadeInvNo.matches("\\d{8}")) {
                addMessage(FacesMessage.SEVERITY_ERROR, "退票代码或退票号码错误，请重新输入！");
                return;
            }
            InvInvInfo invInvInfo = riService.hasCspCommInv(fadeInvCode, fadeInvNo);
            if (invInvInfo == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "没有与之对应的补录正常票，请重新输入！");
                return;
            } else {
                if (invInvInfo.getPriceSum().compareTo(cash) != 0) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "金额不对应，请重新输入！");
                    return;
                }
                if (!invInvInfo.getCustName().equals(custName)) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "名称不对应，请重新输入！");
                    return;
                }
            }
            boolean invInfoExist = riService.getInvInfoByFadeInvNoAndCode(fadeInvCode, fadeInvNo);
            if (invInfoExist) {
                addMessage(FacesMessage.SEVERITY_ERROR, "此发票已退票!");
                return;
            }

            invInfo = new InvInvInfo(UUID.randomUUID().toString(), invCode, invNo, invDate, "", "", taxCode, invType, "", custName, fadeInvCode, fadeInvNo, "金融保险业", "海尔集团财务责任有限公司", cash, "1", txnDate + itemType,"");
        }
        try {
            int num = riService.addInvInvInfo(invInfo);
            if (num > 0) {
                addMessage(FacesMessage.SEVERITY_INFO, "录入成功！");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "录入失败！");
            logger.error(new Date().toString() + e.getMessage());
        }
    }


    /**
     * 添加前台页面显示内容
     *
     * @param msgStr
     */
    public void addMessage(FacesMessage.Severity severity, String msgStr) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msgStr, msgStr));
    }

    public RiService getRiService() {
        return riService;
    }

    public void setRiService(RiService riService) {
        this.riService = riService;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
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

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }
}
