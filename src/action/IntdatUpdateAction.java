package action;

import bean.InvIntData;
import bean.InvInvInfo;
import bean.InvItem;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.RowEditEvent;
import qrybean.InvInfoQryCond;
import qrybean.InvIntDataQryCond;
import service.IntdatUpdateService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Author:Lichao.W
 * On 2014/12/15 At 15:36
 */
@ManagedBean
@ViewScoped
public class IntdatUpdateAction  {
    private static final Log logger = LogFactory.getLog(PrtInvInfoAction.class);

    @ManagedProperty(value = "#{intdatUpdateService}")
    private IntdatUpdateService intdatUpdateService;

    private InvIntDataQryCond invIntDataQryCond;
    private List<InvIntData> invIntDataList;
    private Map<String, String> txnTypeMaps;
    ActiveXComponent axc;
    Dispatch dispatch;

    @PostConstruct
    public void init() {
        axc = new ActiveXComponent("shuikong.skfun");
        dispatch = axc.getObject();
        invIntDataQryCond = new InvIntDataQryCond();
        txnTypeMaps = new HashMap<String, String>();
        txnTypeMaps.put("01", "贷款");
        txnTypeMaps.put("02", "贴现");
        txnTypeMaps.put("03", "个人");
        txnTypeMaps.put("04", "顺延");
    }
    public void onQueryInvData(){
        try {
            invIntDataList = intdatUpdateService.onQueryInvData(invIntDataQryCond);
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            logger.error(new Date().toString() + "数据库连接或SQL执行出现错误!");
        }
    }

    public void onRowEdit(RowEditEvent event){
        try {
            InvIntData invIntData = (InvIntData) event.getObject();
            intdatUpdateService.updateInvData(invIntData);
            addMessage(FacesMessage.SEVERITY_INFO, "该客户本月份修改成功");
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            logger.error(new Date().toString() + "数据库连接或SQL执行出现错误!");
        }
    }

    public void onRowCancel(RowEditEvent event){
        addMessage(FacesMessage.SEVERITY_INFO, "数据未修改");
        logger.error(new Date().toString() + "数据未修改");
    }

    public void addMessage(FacesMessage.Severity severity, String msgStr) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msgStr, msgStr));
    }
    // = = = = = = = = = = = = = get set = = = = = = = = = = = = =

    public IntdatUpdateService getIntdatUpdateService() {
        return intdatUpdateService;
    }

    public void setIntdatUpdateService(IntdatUpdateService intdatUpdateService) {
        this.intdatUpdateService = intdatUpdateService;
    }

    public InvIntDataQryCond getInvIntDataQryCond() {
        return invIntDataQryCond;
    }

    public void setInvIntDataQryCond(InvIntDataQryCond invIntDataQryCond) {
        this.invIntDataQryCond = invIntDataQryCond;
    }

    public List<InvIntData> getInvIntDataList() {
        return invIntDataList;
    }

    public void setInvIntDataList(List<InvIntData> invIntDataList) {
        this.invIntDataList = invIntDataList;
    }
}
