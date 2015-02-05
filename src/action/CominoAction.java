package action;

import bean.Comino;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import service.CominoService;
import service.CommonInrService;
import service.CommonService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by Lichao.W On 2014/12/8
 * email:wanglichao@163.com
 */
@ManagedBean
@ViewScoped
public class CominoAction {
    private static final Log logger = LogFactory.getLog(CominoAction.class);

    @ManagedProperty(value = "#{cominoService}")
    private CominoService cominoService;
    @ManagedProperty(value = "#{commonInrService}")
    private CommonInrService commonInrService;

    private String comcod = "";
    private String comnam = "";

    private List<Comino> comList = new ArrayList<Comino>();


    public void qrycod(){
        try {
            List<String> slist = commonInrService.onQueryCod(comnam);
            if (slist.size() > 0){
                comcod = slist.get(0);
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "���ݿ����ӻ�SQLִ�г��ִ���!");
            logger.error(new Date().toString() + " ���ݿ����ӻ�SQLִ�г��ִ���!");
        }
    }

    public void cominoQry() {
        try {
            comList = cominoService.cominoqry(comcod, comnam);
            if (comList.size() == 0) {
                addMessage(FacesMessage.SEVERITY_INFO, "��ѯ����Ϊ�գ�");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "���ݿ����ӻ�SQLִ�г��ִ���!");
            logger.error(new Date().toString() + " ���ݿ����ӻ�SQLִ�г��ִ���!");
        }
    }
    public void cominoAdd() {
        try {
            int i = 0;
            i  = cominoService.cominoadd(comcod, comnam);
            if (i > 0) {
                addMessage(FacesMessage.SEVERITY_INFO, "��ӳɹ���");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "���ݿ����ӻ�SQLִ�г��ִ���!");
            logger.error(new Date().toString() + " ���ݿ����ӻ�SQLִ�г��ִ���!");
        }
    }
    public void comiondel(Comino comino){
        try {
            int i = 0;
            i = cominoService.cominodel(comino.getComcod());
            if (i>0){
                comList.remove(comino);
                addMessage(FacesMessage.SEVERITY_INFO, "�ͻ���Ϣ���³ɹ�");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "���ݿ����ӻ�SQLִ�г��ִ���!");
            logger.error(new Date().toString() + " ���ݿ����ӻ�SQLִ�г��ִ���!");
        }
    }

    public void addMessage(FacesMessage.Severity severity, String msgStr) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msgStr, msgStr));
    }
    //= = = = = = = = = = = = = = = = = = get set = = = = = = = = = = = = = = = = =

    public CominoService getCominoService() {
        return cominoService;
    }

    public void setCominoService(CominoService cominoService) {
        this.cominoService = cominoService;
    }

    public CommonInrService getCommonInrService() {
        return commonInrService;
    }

    public void setCommonInrService(CommonInrService commonInrService) {
        this.commonInrService = commonInrService;
    }

    public String getComcod() {
        return comcod;
    }

    public void setComcod(String comcod) {
        this.comcod = comcod;
    }

    public String getComnam() {
        return comnam;
    }

    public void setComnam(String comnam) {
        this.comnam = comnam;
    }

    public List<Comino> getComList() {
        return comList;
    }

    public void setComList(List<Comino> comList) {
        this.comList = comList;
    }
}
