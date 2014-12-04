package action;

import bean.InvIntData;
import bean.InvInvInfo;
import bean.InvItem;
import bean.StaticInvItem;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.primefaces.event.SelectEvent;
import qrybean.InvInfoQryCond;
import qrybean.InvIntDataQryCond;
import service.CommonService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class CommonAction {

    private static Logger logger = Logger.getLogger(CommonAction.class);

    @ManagedProperty(value = "#{commonService}")
    private CommonService commonService;

    private InvIntDataQryCond invIntDataQryCond;        ////查询基础数据时的检索条件封装类
    private InvIntDataQryCond invIntDataQryCondTmp;        ////查询基础数据时的检索条件封装类z临时
    private InvInfoQryCond invInfoQryCond;              //查询发票时的检索条件封装类

    private String exportDataDate;//导入某天的数据

    private List<InvIntData> invIntDataList;
    private InvIntData selectedInvIntData;

    private List<InvInvInfo> invInvInfoList;
    private List<InvInvInfo> selectedInvInvInfos;
    private InvInvInfo selectedInvInvInfo;

    private List<InvItem> invItemList;

    Map<String, List<InvItem>> infoMaps;            //封装了发票信息 和每个发票的明细信息
    private String currentInvNo;                    //当前发票号号码
    private String invNOtmp;                        //累计当前发票号
    private String currentInvCode;                  //当前发票代码
    private BigDecimal currentInvPriceSum;          //当前未抄票的正常发票总金额


    ActiveXComponent axc;
    Dispatch dispatch;
    private Thread thread;
    private boolean flag = false;
    private int printedNum;
    private boolean printable = false;
    private String curRemark = "";
    private Map<String, String> txnTypeMaps;

    @PostConstruct
    public void init() {
        axc = new ActiveXComponent("shuikong.skfun");
        dispatch = axc.getObject();
        invInvInfoList = new ArrayList<InvInvInfo>();
        invIntDataQryCond = new InvIntDataQryCond();
        invInfoQryCond = new InvInfoQryCond();
        infoMaps = new LinkedHashMap<String, List<InvItem>>();
        invNOtmp = commonService.getMaxInvNo();
        txnTypeMaps = new HashMap<String, String>();
        txnTypeMaps.put("01", "贷款");
        txnTypeMaps.put("02", "贴现");
        txnTypeMaps.put("03", "个人");
        txnTypeMaps.put("04", "顺延");
    }

    /**
     * 从BI获取数据
     */
    public void getBIData() {
        if ("".equals(exportDataDate)) {
            addMessage(FacesMessage.SEVERITY_ERROR, "查询日期不能为空!");
            return;
        }
        try {
            invIntDataList = commonService.getBIData(exportDataDate);
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
        }
    }

    /**
     * 导入到本地数据库
     */
    public void importToDB() {
        int recordNum;
        try {
            recordNum = commonService.importToDB();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
            return;
        }
        if (recordNum > 0) {
            addMessage(FacesMessage.SEVERITY_INFO, "成功获取" + recordNum + "条数据!");
        } else {
            addMessage(FacesMessage.SEVERITY_INFO, "无最新数据!");
        }
    }

    /**
     * 检索发票信息
     */
    public void onQueryInvInfo() {
        try {
            invInvInfoList = commonService.onQueryInvInfo(invInfoQryCond);
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
        }
    }

    /**
     * 发票抄税
     */
    public void invalidInvInfo(String invType) {
        try {
            if (commonService.invalidInvInfo(invType)) {
                addMessage(FacesMessage.SEVERITY_INFO, "成功抄税!");
                return;
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
        }

    }

    /**
     * 导出发票信息
     */
    public void exportInvData() {
        try {
            InputStream is;
            Map<String, Object> attrsMaps = new HashMap<String, Object>();
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            ServletOutputStream os = response.getOutputStream();
            String templateFilePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("templates/invinfotemplate.xls");
            List<StaticInvItem> staticInvItems = qryExpData();
            attrsMaps.put("staticInvItems", staticInvItems);
            XLSTransformer transformer = new XLSTransformer();
            is = new BufferedInputStream(new FileInputStream(templateFilePath));
            HSSFWorkbook wb = transformer.transformXLS(is, attrsMaps);
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode("已开发票明细表.xls", "UTF-8"));
            response.setContentType("application/msexcel");
            wb.write(os);
            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
            logger.error(new Date().toString() + " 错误信息:" + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "错误信息:" + e.getMessage());
        }
    }

    /**
     * 获取发票明细
     */
    public List<StaticInvItem> qryExpData() throws Exception {
        return commonService.qryExpData(invInfoQryCond);
    }

    /**
     * 检索税控基础信息
     */
    public void onQueryInvData() {
        try {
            invIntDataList = commonService.onQueryInvData(invIntDataQryCond);
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
        }
    }

    /**
     * 获取汇率
     */
    public BigDecimal getCurRat() throws Exception {
        BigDecimal currat;
        if (invIntDataQryCond.getCurrencyType().equals("CNY")) {
            currat = new BigDecimal("1");
        } else {
            String ratYear = invIntDataQryCond.getTxnDateEnd().substring(0, 4);
            int ratMonth = Integer.parseInt(invIntDataQryCond.getTxnDateEnd().substring(5));
            String curratDate;
            if (ratMonth <= 3) {
                curratDate = ratYear + "-03-31";
            } else if (ratMonth > 3 && ratMonth <= 6) {
                curratDate = ratYear + "-06-30";
            } else if (ratMonth > 6 && ratMonth <= 9) {
                curratDate = ratYear + "-09-30";
            } else {
                curratDate = ratYear + "-12-31";
            }
            if (invIntDataQryCond.getCurrencyType().equals("HKD")) {
                currat = commonService.getCurRat(curratDate, "013");
            } else if (invIntDataQryCond.getCurrencyType().equals("USD")) {
                currat = commonService.getCurRat(curratDate, "014");
            } else {
                currat = commonService.getCurRat(curratDate, "087");
            }
        }
        return currat;
    }

    private BigDecimal curRat;

    /**
     * 开票预览
     */
    public void invPreview() {
        initCurrentData(); //得到当前发票号码
        //清空已存在的发票信息
        invInvInfoList.clear();
        infoMaps.clear();
        if (!invIntDataQryCond.getTxnDateSta().equals("") && !invIntDataQryCond.getIntAmtEnd().equals("")) {
            try {
                invIntDataQryCondTmp = (InvIntDataQryCond) BeanUtils.cloneBean(invIntDataQryCond);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                curRat = getCurRat();
            } catch (Exception e) {
                addMessage(FacesMessage.SEVERITY_ERROR, "汇率不存在!");
                logger.error(new Date().toString() + " 汇率不存在!");
                return;
            }
            List<InvItem> invItems = commonService.staticInvItems(invIntDataQryCond, curRat);
            if (!invItems.isEmpty()) {
                fillDatas(invItems);
            }
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "交易起始月份不得为空!");
            return;
        }
    }

    /**
     * 填充infoMaps 和 invInvInfoList
     *
     * @param invItems
     */
    private void fillDatas(List<InvItem> invItems) {
        List<InvItem> invItemsTmp = new ArrayList<InvItem>();
        String invDte = new SimpleDateFormat("yyyyMMdd").format(new Date());
        InvInvInfo invInfo = null;
        String custCodeTmp = "";
        String strTmp = invIntDataQryCond.getTxnDateSta().substring(0, 4) + "年";
        StringBuffer sb = new StringBuffer("");
        if (invIntDataQryCond.getTxnDateSta().equals(invIntDataQryCond.getTxnDateEnd())) {
            strTmp += Integer.parseInt(invIntDataQryCond.getTxnDateSta().substring(5)) + "月份";
        } else {
            strTmp += Integer.parseInt(invIntDataQryCond.getTxnDateSta().substring(5)) + "-" + Integer.parseInt(invIntDataQryCond.getTxnDateEnd().substring(5)) + "月份";
        }
        BigDecimal priceSum = new BigDecimal(0);
        BigDecimal _price = new BigDecimal(-1);
        String itemName;
        for (InvItem invItem : invItems) {
            itemName = txnTypeMaps.get(invItem.getItemCode());
            if (!custCodeTmp.equals(invItem.getCustCode())) {
                if (!invItemsTmp.isEmpty()) {
                    infoMaps.put(invInfo.getCustCode(), invItemsTmp);
                    invInfo.setPriceSum(priceSum);
                    invInfo.setRemark(strTmp + sb.toString() + "合计利息");
                    priceSum = priceSum.add(priceSum.multiply(_price));
                    sb.delete(0, sb.length());
                    invInvInfoList.add(invInfo);
                }
                priceSum = priceSum.add(invItem.getPrice());
                sb.append(itemName);
                invInfo = new InvInvInfo(UUID.randomUUID().toString(), invDte, "1", invItem.getCustCode(), invItem.getCustName(), "金融保险业", "海尔集团财务有限责任公司", "1");
                invItemsTmp = new ArrayList<InvItem>();
                custCodeTmp = invItem.getCustCode();
                invItem.setSummary(strTmp + itemName + "利息");
                invItem.setInvDate(invDte);
                invItemsTmp.add(invItem);
            } else {
                priceSum = priceSum.add(invItem.getPrice());
                sb.append(itemName);
                invItem.setSummary(strTmp + itemName + "利息");
                invItem.setInvDate(invDte);
                invItemsTmp.add(invItem);
            }
        }
        if (!invItemsTmp.isEmpty()) {
            infoMaps.put(invInfo.getCustCode(), invItemsTmp);
            invInfo.setPriceSum(priceSum);
            invInfo.setRemark(strTmp + sb.toString() + "合计利息");
            invInvInfoList.add(invInfo);
        }
    }

    /**
     * 批量开票
     */
    public void batPrint() {
        if (invInvInfoList.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "没有发票可打印!");
            return;
        }
        if (selectedInvInvInfos.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "请选中要打印的发票!");
            return;
        }
        if (!getInterfaceState()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "查看是否打开税控软件,并且设置联动!");
            return;
        }
        initCurrentData();
        BigDecimal price = new BigDecimal(0);
        for (InvInvInfo invInvInfo : selectedInvInvInfos) {
            if (invInvInfo.getPriceSum().compareTo(new BigDecimal(10000000)) <= 0) {
                price = price.add(invInvInfo.getPriceSum());
            }
        }
        price = price.add(currentInvPriceSum);
        if (price.compareTo(new BigDecimal("42000000")) > 0) {
            addMessage(FacesMessage.SEVERITY_ERROR, "发票总额为" + price + "，已超过42000000，请到税务局抄税!");
            return;
        }

        printedNum = 0;
        flag = true;
        if ("USD".equals(invIntDataQryCondTmp.getCurrencyType())) {
            curRemark = "美元";
        } else if ("HKD".equals(invIntDataQryCondTmp.getCurrencyType())) {
            curRemark = "港币";
        } else if ("NZD".equals(invIntDataQryCondTmp.getCurrencyType())) {
            curRemark = "新西兰元";
        } else {
            curRemark = "";
        }
        thread = new Thread(new PrintRunnable());
        thread.start();
        printable = true;
    }

    /**
     * 停止打印
     */
    public void stopPrint() {
        if (flag) {
            flag = false;
            printable = false;
            addMessage(FacesMessage.SEVERITY_INFO, "已发送" + printedNum + "条数据到打印机!");
            initCurrentData();
        }
    }

    /**
     * 更新数据
     */
    public void updateData() {
        if (thread != null) {
            if (thread.isAlive()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "打印正在进行，请稍后再更新!");
                return;
            }
        }
        initCurrentData();
        printable = false;

        if (invIntDataList != null) {
            try {
                invIntDataList = commonService.onQueryInvData(invIntDataQryCondTmp);
            } catch (Exception e) {
                addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
                logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
            }
        }
    }

    /**
     * 分批打印
     */
    public void printThread(InvInvInfo invInvInfo) {
        try {
            List<InvItem> invItemsTmp = infoMaps.get(invInvInfo.getCustCode());
            Document doc = DocumentHelper.createDocument();
            String xmlStr;
            doc.setXMLEncoding("UTF-8");
            Element root = doc.addElement("InvInfo");
            Element verElem;
            Element invTypeElem;
            Element payClientElem;
            Element itemElem;
            Element tradeDataInfoElem;
            XMLWriter xw;
            StringWriter sw = new StringWriter();
            OutputFormat opf;

            verElem = root.addElement("Version");
            verElem.addText("1.00000");

            invTypeElem = root.addElement("InvType");
            invTypeElem.addText("1");

            payClientElem = root.addElement("PayClient");
            payClientElem.addAttribute("Name", invInvInfo.getCustName());
            String itemName;
            for (InvItem invItem : invItemsTmp) {
                itemElem = root.addElement("Item");
                if ("04".equals(invItem.getItemCode())) {
                    itemName = "贷款利息";
                } else {
                    itemName = txnTypeMaps.get(invItem.getItemCode()) + "利息";
                }
                itemElem.addAttribute("name", itemName).addAttribute("num", "1").addAttribute("price", invItem.getPrice() + "").addAttribute("cash", "" + invItem.getCash()).addAttribute("taxItem", "2").addAttribute("exattrib1", "").addAttribute("exattrib2", invItem.getSummary());
            }
            tradeDataInfoElem = root.addElement("TradeData");
            tradeDataInfoElem.addAttribute("name", "BUS_BUSKIND").addAttribute("data", "金融保险业");
            tradeDataInfoElem = root.addElement("TradeData");
            if (!"".equals(curRemark)) {
                String invRemarkStr = invInvInfo.getPriceSum().divide(curRat).toPlainString();
                invInvInfo.setInvRemark(invRemarkStr + curRemark);
                tradeDataInfoElem.addAttribute("name", "BUS_REMARK").addAttribute("data", invRemarkStr + curRemark);
            } else {
                tradeDataInfoElem.addAttribute("name", "BUS_REMARK").addAttribute("data", "");
            }

            opf = OutputFormat.createCompactFormat();
            opf.setEncoding("UTF-8");
            xw = new XMLWriter(sw);
            xw.write(doc);
            xw.close();
            xmlStr = sw.toString();
            sw.close();
            Variant result = Dispatch.callN(dispatch, "PrntInv", new Variant(xmlStr));  //打印发票

            if (result.getInt() != 0) {
                String errInf = Dispatch.callN(dispatch, "GetErrInfExt").getString();
                logger.error(new Date().toString() + " 错误信息:" + errInf);
                return;
            }
            printedNum++;
            invInvInfoList.remove(invInvInfo);
            invNOtmp = currentInvNo;
            Variant result1 = Dispatch.callN(dispatch, "GetPrntReturnData"); //获取打印返回数据
            invInvInfo = readStringXml(result1.toString(), invInvInfo);
            for (int i = 0, j = invItemsTmp.size(); i < j; i++) {
                invItemsTmp.get(i).setInvNo(invInvInfo.getInvNo());
                invItemsTmp.get(i).setInvCode(invInvInfo.getInvCode());
                invItemsTmp.get(i).setTaxCode(invInvInfo.getTaxCode());
            }
            commonService.addInvInvInfo(invInvInfo);
            commonService.addIntItems(invItemsTmp);
            commonService.invalidInvIntData(invIntDataQryCondTmp, invItemsTmp);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            logger.error(new Date().toString() + " 错误信息:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 解析打印成功后返回的数据
     *
     * @param xml
     * @param invInfo
     * @return
     */
    public InvInvInfo readStringXml(String xml, InvInvInfo invInfo) {
        Document doc;
        try {
            doc = DocumentHelper.parseText(xml);
            Element rootElt = doc.getRootElement();
            Iterator iter = rootElt.elementIterator("InvInfo");
            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                invInfo.setInvCode(recordEle.attributeValue("InvCode").equals("") ? "null" : recordEle.attributeValue("InvCode"));
                invInfo.setInvNo(recordEle.attributeValue("InvNo"));
                invInfo.setMacCode(recordEle.attributeValue("MacCode"));
                invInfo.setOpter(recordEle.attributeValue("Opter"));
                invInfo.setTaxCode(recordEle.attributeValue("TaxCode"));
            }
        } catch (DocumentException e) {
            logger.error(new Date().toString() + " 字符解析错误!" + e.getMessage());
            e.printStackTrace();
        }
        return invInfo;
    }

    /**
     * 检查领用发票是否用完，若是则无法打印，否则可以打印
     *
     * @return
     */
    public boolean testPrintable() {
        initCurrentData();
        if (currentInvNo == null) {
            return false;
        }
        if (currentInvNo.equals(invNOtmp)) {
            return false;
        }
        return true;
    }

    /**
     * 解析得到税控器当前的发票号码
     *
     * @throws DocumentException
     */
    public void initCurrentData() {
        try {
            currentInvPriceSum = commonService.getCurInvPriceSum();
            Variant result = Dispatch.callN(dispatch, "GetCurInv");
            if (result.getString().equals("")) {
                addMessage(FacesMessage.SEVERITY_ERROR, "获取当前发票号失败!");
                return;
            }
            Document doc = DocumentHelper.parseText(result.getString());
            Element rootElt = doc.getRootElement();
            currentInvNo = rootElt.attributeValue("InvNo");
            currentInvCode = rootElt.attributeValue("InvCode");
        } catch (DocumentException e) {
            logger.error(new Date().toString() + " 字符解析错误!" + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "字符解析错误!");
        } catch (ComFailException ce) {
            logger.error(new Date().toString() + " jacob出现错误!" + ce.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "jacob出现错误!");
        } catch (Exception ef) {
            logger.error(new Date().toString() + " " + ef.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, ef.getMessage());
        }
    }

    /**
     * 获取组件、打印机的状态
     *
     * @return
     */
    public boolean getInterfaceState() {
        int result = Dispatch.callN(dispatch, "GetInterfaceState").getInt();
        if (result == 0) {
            return false;
        }
        return true;
    }

    /**
     * 将基础数据中超过10000000的数据屏蔽
     */
    public void invalidInvIntData() {
        try {
            if (selectedInvIntData.getIntAmt().compareTo(new BigDecimal(10000000)) > 0) {
                commonService.invalidInvIntData(selectedInvIntData);
                invIntDataList.remove(selectedInvIntData);
                if (invInvInfoList != null && !invIntDataList.isEmpty()) {
                    invPreview();
                }
            }
        } catch (Exception e) {
            logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
        }
    }

    /**
     * 预览发票明细
     */
    public void previewInvItem() {
        invItemList = infoMaps.get(selectedInvInvInfo.getCustCode());
    }

    /**
     * 选中数据触发的事件
     *
     * @param event
     */
    public void onRowSelected(SelectEvent event) {
        selectedInvInvInfo = (InvInvInfo) event.getObject();
    }

    /**
     * 查看发票明细
     */
    public void showInvItem() {
        if (selectedInvInvInfo != null) {
            try {
                invItemList = commonService.getInvItemsByInvNo(selectedInvInvInfo.getInvCode(), selectedInvInvInfo.getInvNo());
            } catch (Exception e) {
                logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
                addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
            }
        }
    }

    /**
     * 开退票
     */
    public void printBackInv() {
        if (selectedInvInvInfo == null) {
            return;
        }
        if (selectedInvInvInfo.getFadeInvNo() != null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "此发票为退回票!");
            return;
        }

        if (selectedInvInvInfo.getInvType().equals("3")) {
            addMessage(FacesMessage.SEVERITY_ERROR, "补录发票，无法退票!");
            return;
        }

        boolean invInfoExist = commonService.getInvInfoByFadeInvNoAndCode(selectedInvInvInfo.getInvCode(), selectedInvInvInfo.getInvNo());
        if (invInfoExist) {
            addMessage(FacesMessage.SEVERITY_ERROR, "此发票已退票!");
            return;
        }

        if (!getInterfaceState()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "查看是否打开税控软件,并且设置联动!");
            return;
        }

        try {
            BigDecimal curBIPS = commonService.getCurBackInvPriceSum();
            if (curBIPS != null) {
                curBIPS = curBIPS.multiply(new BigDecimal(-1));
                if (curBIPS.add(selectedInvInvInfo.getPriceSum()).compareTo(new BigDecimal(40000000)) > 0) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "当前未抄税的退票发票总额为" + curBIPS + "，此发票不能退票，请先到税务局抄税!");
                    return;
                }
            }
            //获取发票明细列表
            List<InvItem> backInvItems = commonService.getInvItemsByInvNo(selectedInvInvInfo.getInvCode(), selectedInvInvInfo.getInvNo());
            prntBackInvThread(backInvItems);
        } catch (Exception e) {
            logger.error(new Date().toString() + " 错误信息:" + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "错误信息:" + e.getMessage());
        }
    }

    /**
     * 退票实体方法
     *
     * @param backInvItems
     * @throws IOException
     */
    public void prntBackInvThread(List<InvItem> backInvItems) throws IOException {
        String invDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String invCode = selectedInvInvInfo.getInvCode();
        String invNo = selectedInvInvInfo.getInvNo();
        selectedInvInvInfo.setFadeInvCode(invCode);
        selectedInvInvInfo.setFadeInvNo(invNo);
        selectedInvInvInfo.setInvType("2");
        selectedInvInvInfo.setPkid(UUID.randomUUID().toString());
        selectedInvInvInfo.setInvDate(invDate);
        BigDecimal _priceSum = selectedInvInvInfo.getPriceSum().multiply(new BigDecimal(-1));
        selectedInvInvInfo.setPriceSum(_priceSum);
        Document doc = DocumentHelper.createDocument();
        String xmlStr;
        doc.setXMLEncoding("UTF-8");
        Element root = doc.addElement("InvInfo");
        Element invTypeElem;
        Element payClientElem;
        Element fadeInvElem;
        Element itemElem;
        Element tradeDataInfoElem;
        XMLWriter xw;
        StringWriter sw = new StringWriter();
        OutputFormat opf;

        invTypeElem = root.addElement("InvType");
        invTypeElem.addText("2");

        payClientElem = root.addElement("PayClient");
        payClientElem.addAttribute("Name", selectedInvInvInfo.getCustName());

        fadeInvElem = root.addElement("fadeInv");
        fadeInvElem.addAttribute("InvCode", selectedInvInvInfo.getInvCode() == null ? "" : selectedInvInvInfo.getInvCode()).addAttribute("InvNo", selectedInvInvInfo.getInvNo());

        String itemName;
        for (InvItem invItem : backInvItems) {
            itemName = txnTypeMaps.get(invItem.getItemCode()) + "利息";
            itemElem = root.addElement("Item");
            itemElem.addAttribute("name", itemName).addAttribute("num", "1").addAttribute("price", invItem.getPrice() + "").addAttribute("cash", "" + invItem.getCash()).addAttribute("taxItem", "2").addAttribute("exattrib1", "").addAttribute("exattrib2", invItem.getSummary());
        }
        tradeDataInfoElem = root.addElement("TradeData");
        tradeDataInfoElem.addAttribute("name", "BUS_BUSKIND").addAttribute("data", "金融保险业");
        tradeDataInfoElem.addAttribute("name", "BUS_REMARK").addAttribute("data", selectedInvInvInfo.getInvRemark());
        opf = OutputFormat.createCompactFormat();
        opf.setEncoding("UTF-8");
        xw = new XMLWriter(sw);
        xw.write(doc);
        xw.close();
        xmlStr = sw.toString();
        sw.close();

        Variant result = Dispatch.callN(dispatch, "PrntInv", new Variant(xmlStr));  //打印发票

        if (result.getInt() != 0) {
            String errInf = Dispatch.callN(dispatch, "GetErrInfExt").getString();
            logger.error(new Date().toString() + " 错误信息:" + errInf);
            addMessage(FacesMessage.SEVERITY_ERROR, "错误信息:" + errInf);
            return;
        }
        Variant result1 = Dispatch.callN(dispatch, "GetPrntReturnData"); //获取打印返回数据
        selectedInvInvInfo = readStringXml(result1.toString(), selectedInvInvInfo);
        for (int i = 0, j = backInvItems.size(); i < j; i++) {
            backInvItems.get(i).setPkid(UUID.randomUUID().toString());
            backInvItems.get(i).setInvCode(selectedInvInvInfo.getInvCode());
            backInvItems.get(i).setInvNo(selectedInvInvInfo.getInvNo());
            backInvItems.get(i).setTaxCode(selectedInvInvInfo.getTaxCode());
            backInvItems.get(i).setInvDate(invDate);
        }
        try {
            commonService.addInvInvInfo(selectedInvInvInfo);
            commonService.addIntItems(backInvItems);
            commonService.validInvIntData(invCode, invNo);
        } catch (Exception e) {
            logger.error(new Date().toString() + " 数据库连接或SQL执行出现错误!");
            addMessage(FacesMessage.SEVERITY_ERROR, "数据库连接或SQL执行出现错误!");
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

    public CommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    public InvIntDataQryCond getInvIntDataQryCond() {
        return invIntDataQryCond;
    }

    public void setInvIntDataQryCond(InvIntDataQryCond invIntDataQryCond) {
        this.invIntDataQryCond = invIntDataQryCond;
    }

    public InvInfoQryCond getInvInfoQryCond() {
        return invInfoQryCond;
    }

    public void setInvInfoQryCond(InvInfoQryCond invInfoQryCond) {
        this.invInfoQryCond = invInfoQryCond;
    }

    public List<InvIntData> getInvIntDataList() {
        return invIntDataList;
    }

    public void setInvIntDataList(List<InvIntData> invIntDataList) {
        this.invIntDataList = invIntDataList;
    }

    public InvIntData getSelectedInvIntData() {
        return selectedInvIntData;
    }

    public void setSelectedInvIntData(InvIntData selectedInvIntData) {
        this.selectedInvIntData = selectedInvIntData;
    }

    public List<InvInvInfo> getInvInvInfoList() {
        return invInvInfoList;
    }

    public void setInvInvInfoList(List<InvInvInfo> invInvInfoList) {
        this.invInvInfoList = invInvInfoList;
    }

    public InvInvInfo getSelectedInvInvInfo() {
        return selectedInvInvInfo;
    }

    public void setSelectedInvInvInfo(InvInvInfo selectedInvInvInfo) {
        this.selectedInvInvInfo = selectedInvInvInfo;
    }

    public List<InvInvInfo> getSelectedInvInvInfos() {
        return selectedInvInvInfos;
    }

    public void setSelectedInvInvInfos(List<InvInvInfo> selectedInvInvInfos) {
        this.selectedInvInvInfos = selectedInvInvInfos;
    }

    public List<InvItem> getInvItemList() {
        return invItemList;
    }

    public void setInvItemList(List<InvItem> invItemList) {
        this.invItemList = invItemList;
    }

    public String getCurrentInvNo() {
        return currentInvNo;
    }

    public void setCurrentInvNo(String currentInvNo) {
        this.currentInvNo = currentInvNo;
    }

    public String getCurrentInvCode() {
        return currentInvCode;
    }

    public void setCurrentInvCode(String currentInvCode) {
        this.currentInvCode = currentInvCode;
    }

    public String getExportDataDate() {
        return exportDataDate;
    }

    public void setExportDataDate(String exportDataDate) {
        this.exportDataDate = exportDataDate;
    }

    public BigDecimal getCurrentInvPriceSum() {
        return currentInvPriceSum;
    }

    public void setCurrentInvPriceSum(BigDecimal currentInvPriceSum) {
        this.currentInvPriceSum = currentInvPriceSum;
    }

    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    class PrintRunnable implements Runnable {
        @Override
        public void run() {
            for (InvInvInfo invInfo : selectedInvInvInfos) {
                if (!testPrintable()) {
                    logger.error(new Date().toString() + " 发票已用完!");
                    break;
                }
                if (flag) {
                    if (invInfo.getPriceSum().compareTo(new BigDecimal(10000000)) > 0) {
                        continue;
                    }
                    printThread(invInfo);
                }
            }
        }
    }
}

