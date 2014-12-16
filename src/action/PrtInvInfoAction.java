package action;

import bean.PrtInvInfo;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import service.CommonInrService;
import service.PrtInvInfoService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Create by Lichao.W On 2014/12/8
 * email:wanglichao@163.com
 */
@ManagedBean
@ViewScoped
public class PrtInvInfoAction {
    private static final Log logger = LogFactory.getLog(PrtInvInfoAction.class);
    @ManagedProperty(value = "#{prtInvInfoService}")
    private PrtInvInfoService prtInvInfoService;

    @ManagedProperty(value = "#{commonInrService}")
    private CommonInrService commonInrService;

    private String invcode = "00000000237021401105";
    private String invnum1 = "00980668";
    private String invnum2 = "00980757";
    private List<PrtInvInfo> prtInvList;

    public void invdataQry() {
        try {
            prtInvList = prtInvInfoService.getInvdatByInvNo(invcode, invnum1,invnum2);
            if (prtInvList.size() == 0) {
                addMessage(FacesMessage.SEVERITY_INFO, "查询数据为空！");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
        }
    }


    public void prtInvdata() {
        try {
            String sysdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            DecimalFormat df = new DecimalFormat("###,###,##0.00");
            int count = prtInvList.size(); //  总行数
            int pageCount = 30;             // 每页记录数
            int page = 0;                   // 总共页数
            /** 主要控制总共的页数*/
            if (count >= pageCount && count % pageCount == 0) {
                page = count / pageCount;
            } else {
                page = count / pageCount + 1;
            }
            InputStream is = null;
            ByteArrayOutputStream baos[] = new ByteArrayOutputStream[page];
            for (int item = 0; item < page; item++) {
                String item1 = item + 1 + "";
                String page1 = page + "";
                String curtyp = "";
                String templateFilePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("templates/invinfo.pdf");
                is = new FileInputStream(templateFilePath);
                baos[item] = new ByteArrayOutputStream();
                PdfReader reader = new PdfReader(is);
                PdfStamper ps = new PdfStamper(reader, baos[item]);
                AcroFields fields = ps.getAcroFields();
                fields.setField("cusnam", prtInvList.get(0).getCustName());
                fields.setField("opndat", sysdate);
                if ("USD".equals(prtInvList.get(0).getCurrencyType())){
                    curtyp = "美元";
                }else if ("HKD".equals(prtInvList.get(0).getCurrencyType())){
                    curtyp = "港币";
                }else if ("NZD".equals(prtInvList.get(0).getCurrencyType())){
                    curtyp = "新西兰元";
                }else {
                    curtyp = "人民币";
                }
                fields.setField("curtyp", curtyp);
                fields.setField("invcode", prtInvList.get(0).getInvCode());
                fields.setField("curev", "元");
                fields.setField("invno", prtInvList.get(0).getInvNo());
                fields.setField("item", item1);
                fields.setField("page", page1);
                int i = 0;
                int from = item * pageCount;
                int to = from + pageCount;
                if (to > count) {
                    to = count;
                }
                for (PrtInvInfo invInfo : prtInvList.subList(from, to)) {
                    for (int j = 0; j < 8; j++) {
                        switch (j) {
                            case 0:
                                String no = i + 1 + "";
                                fields.setField("num." + i + "." + j, no);
                                break;
                            case 1:
                                fields.setField("num." + i + "." + j, invInfo.getContno());
                                break;
                            case 2:
                                fields.setField("num." + i + "." + j, invInfo.getApndate() + "-" + invInfo.getTxnDate());
                                break;
                            case 3:
                                String intamt = df.format(invInfo.getIntAmt());
                                fields.setField("num." + i + "." + j, intamt);
                                break;
                            case 4:
                                fields.setField("num." + i + "." + j, invInfo.getCreamt());
                                break;
                            case 5:
                                fields.setField("num." + i + "." + j, "0.00674");//lilv
                                break;
                            case 6:
                                fields.setField("num." + i + "." + j, "20");//tianshu
                                break;
                            case 7:
                                fields.setField("num." + i + "." + j, invInfo.getCreamt());
                                break;
                            default:
                                break;
                        }
                    }
                    i++;
                }
                ps.setFormFlattening(true);
                ps.close();
            }

            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletResponse resp = (HttpServletResponse) ctx.getExternalContext().getResponse();
            Document document = new Document();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            PdfCopy pdfCopy = new PdfCopy(document, bao);
            document.open();
            PdfImportedPage impPage = null;
            /**取出之前保存的每页内容*/
            for (int i = 0; i < page; i++) {
                impPage = pdfCopy.getImportedPage(new PdfReader(baos[i]
                        .toByteArray()), 1);
                pdfCopy.addPage(impPage);
            }
            pdfCopy.addJavaScript("this.print({bUI: false,bSilent: true,bShrinkToFit: false});" + "\r\nthis.closeDoc();");
            document.close();
            resp.reset();
            ServletOutputStream out = resp.getOutputStream();
            resp.setContentType("application/pdf");
            resp.setHeader("Content-disposition", "inline");
            resp.setContentLength(bao.size());
            resp.setHeader("Cache-Control", "max-age=30");
            bao.writeTo(out);
            out.flush();
            out.close();
            ctx.responseComplete();
            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "发票明细打印出错");
            logger.error("发票明细打印出错");
        }
    }

    public String cruRatQry() {
        String currat = "";
        try {
            if (prtInvList.get(0).getCurrencyType().equals("CNY")) {
                currat = "1";
            } else {
                String ratdat = prtInvList.get(0).getTxnDate();
                String curratDate = ratdat;

                if (prtInvList.get(0).getCurrencyType().equals("HKD")) {
                    currat = commonInrService.getCurRat(curratDate, "013").toString();
                } else if (prtInvList.get(0).getCurrencyType().equals("USD")) {
                    currat = commonInrService.getCurRat(curratDate, "014").toString();
                } else {
                    currat = commonInrService.getCurRat(curratDate, "087").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取汇率出错");
        }
        return currat;
    }
   /* public void tmpPath() {
        PdfReader reader = null;
        String path = PrtInvInfoAction.class.getClassLoader().getResource("/").getPath();
        String path2 = FacesContext.class.getResource("/").getPath();
        String templateFilePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("templates/invinfotemplate.xls");
        InputStream isr = FacesContext.class.getResourceAsStream("templates/invinfotemplate.xls");
        System.out.println(path+"====="+path2 + "======"+templateFilePath+"==="+isr);
        try {
            InputStream is = new FileInputStream(templateFilePath);
            System.out.println(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
    public void addMessage(FacesMessage.Severity severity, String msgStr) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msgStr, msgStr));
    }
    // = = = = = = = = = get set = = = = = = =  = = = = = = =

    public CommonInrService getCommonInrService() {
        return commonInrService;
    }

    public void setCommonInrService(CommonInrService commonInrService) {
        this.commonInrService = commonInrService;
    }

    public PrtInvInfoService getPrtInvInfoService() {
        return prtInvInfoService;
    }

    public void setPrtInvInfoService(PrtInvInfoService prtInvInfoService) {
        this.prtInvInfoService = prtInvInfoService;
    }

    public String getInvcode() {
        return invcode;
    }

    public void setInvcode(String invcode) {
        this.invcode = invcode;
    }

    public String getInvnum1() {
        return invnum1;
    }

    public void setInvnum1(String invnum1) {
        this.invnum1 = invnum1;
    }

    public String getInvnum2() {
        return invnum2;
    }

    public void setInvnum2(String invnum2) {
        this.invnum2 = invnum2;
    }

    public List<PrtInvInfo> getPrtInvList() {
        return prtInvList;
    }

    public void setPrtInvList(List<PrtInvInfo> prtInvList) {
        this.prtInvList = prtInvList;
    }
}
