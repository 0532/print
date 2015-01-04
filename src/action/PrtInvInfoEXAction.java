package action;

import bean.PrtInvInfo;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.collection.PdfCollection;
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
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
public class PrtInvInfoEXAction {
    private static final Log logger = LogFactory.getLog(PrtInvInfoEXAction.class);
    @ManagedProperty(value = "#{prtInvInfoService}")
    private PrtInvInfoService prtInvInfoService;

    @ManagedProperty(value = "#{commonInrService}")
    private CommonInrService commonInrService;

    private String invcode = "";
    private String invnum1 = "";
    private String invnum2 = "";
    private List<PrtInvInfo> prtInvList;

    public void invdataQry() {
        try {
            prtInvList = prtInvInfoService.getInvdatByInvNo2(invcode, invnum1, invnum2);
            if (prtInvList.size() == 0 || prtInvList.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_INFO, "查询数据为空！");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
        }
    }

    public void prtInvdata() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //String sysdate = sdf.format(new Date());
            DecimalFormat df = new DecimalFormat("###,###,##0.00");
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletResponse resp = (HttpServletResponse) ctx.getExternalContext().getResponse();
            Document document = new Document();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            PdfCopy pdfCopy = new PdfCopy(document, bao);
            List<String> invList = prtInvInfoService.invListByInvNo(invcode, invnum1, invnum2);
            List<PrtInvInfo> prtInvInfos;
            for (String invno : invList) {
                prtInvInfos = prtInvInfoService.getInvdatByInvNo(invcode, invno);
                int count = prtInvInfos.size();
                int pageCount = 35;             // 每页记录数
                int page = 0;                   // 总共页数
                int nonum = 1;                  //序号
                String tointamt ="0";                //利息金额合计
                BigDecimal bdtoamt = new BigDecimal("0");
                /** 主要控制总共的页数*/
                if (count >= pageCount && count % pageCount == 0) {
                    page = count / pageCount;
                } else {
                    page = count / pageCount + 1;
                }
                InputStream is = null;
                ByteArrayOutputStream baos[] = new ByteArrayOutputStream[page];
                for (int item = 0; item < page; item++) {
                    String item1 = item + 1 + "";   //页面页数
                    String page1 = page + "";       //页面页数
                    String curtyp = "";
                    String templateFilePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("templates/invinfo.pdf");
                    is = new FileInputStream(templateFilePath);
                    baos[item] = new ByteArrayOutputStream();
                    PdfReader reader = new PdfReader(is);
                    PdfStamper ps = new PdfStamper(reader, baos[item]);
                    AcroFields fields = ps.getAcroFields();
                    fields.setField("cusnam", prtInvInfos.get(0).getCustName());
                    fields.setField("opndat", prtInvInfos.get(0).getPrtdat());
                    if ("USD".equals(prtInvInfos.get(0).getCurrencyType())) {
                        curtyp = "美元";
                    } else if ("HKD".equals(prtInvInfos.get(0).getCurrencyType())) {
                        curtyp = "港币";
                    } else if ("NZD".equals(prtInvInfos.get(0).getCurrencyType())) {
                        curtyp = "新西兰元";
                    } else {
                        curtyp = "人民币";
                    }
                    fields.setField("curtyp", curtyp);
                    fields.setField("invcode", prtInvInfos.get(0).getInvCode());
                    fields.setField("curev", "元");
                    fields.setField("invno", prtInvInfos.get(0).getInvNo());
                    fields.setField("item", item1);
                    fields.setField("page", page1);
                    int i = 0;
                    int from = item * pageCount;
                    int to = from + pageCount;
                    if (to > count) {
                        to = count;
                    }
                    for (PrtInvInfo invInfo : prtInvInfos.subList(from, to)) {
                        for (int j = 0; j < 8; j++) {
                            switch (j) {
                                case 0:
                                    String no = nonum + "";
                                    fields.setField("num." + i + "." + j, no);
                                    break;
                                case 1:
                                    fields.setField("num." + i + "." + j, invInfo.getContno());
                                    break;
                                case 2:
                                    fields.setField("num." + i + "." + j, invInfo.getApndate() + "/" + invInfo.getTxnDate());
                                    break;
                                case 3:
                                    BigDecimal bdcre = new BigDecimal(invInfo.getDebamt());
                                    String creamt = df.format(bdcre);
                                    fields.setField("num." + i + "." + j, creamt);     //发放金额
                                    break;
                                case 4:
                                    BigDecimal bddeb = new BigDecimal(invInfo.getDebamt());
                                    String debamt = df.format(bddeb);
                                    fields.setField("num." + i + "." + j, debamt);    //还款金额
                                    break;
                                case 5:
                                    fields.setField("num." + i + "." + j, invInfo.getInvrat());     //利率
                                    break;
                                case 6:
                                    long datbeg = sdf.parse(invInfo.getApndate()).getTime();//开始日期
                                    long datend = sdf.parse(invInfo.getTxnDate()).getTime();//开始日期
                                    long tt = (datend - datbeg) / (1000 * 60 * 60 * 24);
                                    String datnum = Long.toString(tt);
                                    fields.setField("num." + i + "." + j, datnum);//天数
                                    break;
                                case 7:
                                    BigDecimal bd = invInfo.getIntAmt();
                                    bdtoamt = bdtoamt.add(bd);
                                    String intamt = df.format(invInfo.getIntAmt());       //利息金额
                                    fields.setField("num." + i + "." + j, intamt);
                                    tointamt = df.format(bdtoamt);
                                    break;
                                default:
                                    break;
                            }
                        }
                        i++;
                        nonum++;
                    }
                    fields.setField("tointamt", tointamt);
                    ps.setFormFlattening(true);
                    ps.close();
                }
                document.open();
                PdfImportedPage impPage = null;
                /**取出之前保存的每页内容*/
                for (int i = 0; i < page; i++) {
                    impPage = pdfCopy.getImportedPage(new PdfReader(baos[i]
                            .toByteArray()), 1);
                    pdfCopy.addPage(impPage);
                }
                if (is != null) {
                    is.close();
                }
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
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "发票明细打印出错");
            logger.error("发票明细打印出错");
        }
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