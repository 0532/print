package service;

import bean.InvIntData;
import bean.InvInvInfo;
import bean.InvItem;
import bean.StaticInvItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import qrybean.InvInfoQryCond;
import qrybean.InvIntDataQryCond;
import rowmapper.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 从BI获取数据
     */
    public List<InvIntData> getBIData(String exportDataDate) throws Exception {
        String sqlStr = "select '' as pkid,t.csm_code AS custcode,t.csm_name AS custname,to_char(t.biz_date,'yyyy-mm-dd') AS txndate,t.interest AS intamt,t.业务类别代码 AS txntype,t.cur_code AS currencytype,t.lnci_no AS iounum from bi.v_ss_interest@haierbi t where to_char(t.biz_date,'yyyy-mm-dd') = '" + exportDataDate + "'";
        return jdbcTemplate.query(sqlStr, new InvIntDataRowMapper());
    }

    /**
     * 从bi获取数据并且导入到本地数据库
     */
    public int importToDB() throws Exception {
        //String sqlStr = "INSERT INTO inv_intdata (pkid,custcode,custname,txndate,intamt,syamt,txntype,currencytype,iounum) SELECT lpad(invrs_seq.nextval,15,'0'),t.csm_code,t.csm_name,to_char(t.biz_date,'yyyy-mm-dd'),t.interest,t.rtnaddpint,t.业务类别代码,t.cur_code,t.lnci_no FROM bi.v_ss_interest@haierbi t WHERE to_char(t.biz_date,'yyyy-mm-dd')  > (select max(t.txndate) from INV_INTDATA t)";
        String sqlStr = "INSERT INTO inv_intdata " +
                "  (pkid," +
                "   custcode," +
                "   custname," +
                "   txndate," +
                "   intamt," +
                "   syamt," +
                "   txntype," +
                "   currencytype," +
                "   iounum," +
                "   biznam," +
                "   cmsnam," +
                "   mngnam," +
                "   apndate," +
                "   creamt," +
                "   debamt," +
                "   contno, " +
                "   invrat) " +
                "  SELECT lpad" +
                "  (invrs_seq.nextval, 15, '0'), " +
                "        t.csm_code," +
                "        t.csm_name," +
                "        to_char(t.biz_date, 'yyyy-mm-dd')," +
                "        t.interest," +
                "        t.rtnaddpint," +
                "        t.txntype," +
                "        t.cur_code," +
                "        t.lnci_no," +
                "        t.biz_body_name," +
                "        t.csm_grp_name," +
                "        t.mng_name," +
                "        to_char(t.sidt,'yyyy-mm-dd')," +
                "        t.credit_amount," +
                "        t.debit_amount," +
                "        t.contract_no," +
                "        t.intrate" +
                "   FROM bi.v_ss_interest@haierbi t" +
                "   WHERE to_char(t.biz_date,'yyyy-mm-dd')  > (select max(t.txndate) from INV_INTDATA t)";
        return jdbcTemplate.update(sqlStr);
    }

    /**
     * 检索发票信息
     *
     * @param invInfoQryCond 检索条件封装类
     * @return
     */
    public List<InvInvInfo> onQueryInvInfo(InvInfoQryCond invInfoQryCond) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT t.pkid,t.invcode,t.invno,t.invdate,t.opter,t.maccode,t.taxcode,t.invtype,t.custcode,t.custname,t.fadeinvcode,t.fadeinvno,t.buskind,t.payee,t.pricesum,t.invver,t.remark,t.invremark FROM inv_invinfo t WHERE 1=1 ");
        if (!"".equals(invInfoQryCond.getInvCode().trim())) {
            sb.append(" AND t.invcode LIKE '%" + invInfoQryCond.getInvCode() + "%'");
        }
        if (!"".equals(invInfoQryCond.getInvNo().trim())) {
            sb.append(" AND t.invno LIKE '%" + invInfoQryCond.getInvNo() + "%'");
        }
        if (!"".equals(invInfoQryCond.getFadeInvCode().trim())) {
            sb.append(" AND t.fadeinvcode LIKE '%" + invInfoQryCond.getFadeInvCode() + "%'");
        }

        if (!"".equals(invInfoQryCond.getFadeInvNo().trim())) {
            sb.append(" AND t.fadeinvno LIKE '%" + invInfoQryCond.getFadeInvNo() + "%'");
        }

        if (!"".equals(invInfoQryCond.getCustName().trim())) {
            sb.append(" AND t.custname LIKE '%" + invInfoQryCond.getCustName() + "%'");
        }
        if (!"".equals(invInfoQryCond.getInvType())) {
            sb.append(" AND t.invtype = '" + invInfoQryCond.getInvType() + "'");
        }
        if (!"".equals(invInfoQryCond.getInvDateSta().trim())) {
            sb.append(" AND t.invdate >= '" + invInfoQryCond.getInvDateSta() + "'");
        }
        if (!"".equals(invInfoQryCond.getInvDateEnd().trim())) {
            sb.append(" AND t.invdate <= '" + invInfoQryCond.getInvDateEnd() + "'");
        }
        sb.append(" ORDER BY t.invno");
        return jdbcTemplate.query(sb.toString(), new InvInvInfoRowMapper());
    }

    /**
     * 根据发票号码取得发票明细的list集合
     *
     * @param invNo 发票号码
     * @return
     */
    public List<InvItem> getInvItemsByInvNo(String invCode, String invNo) throws Exception {
        String sqlStr = "SELECT t.pkid,t.invcode,t.invno,''as taxcode,''as invdate,t.custcode,t.custname,t.itemcode,t.num,t.price,t.cash,t.summary FROM inv_item t WHERE t.invcode = '" + invCode + "' AND t.invno = '" + invNo + "'";
        return jdbcTemplate.query(sqlStr, new InvItemRowMapper());
    }

    /**
     * 检索税控基础信息
     *
     * @param invIntDataQryCond 检索条件封装类
     * @return
     */
    public List<InvIntData> onQueryInvData(InvIntDataQryCond invIntDataQryCond) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT t.pkid,t.custcode,tab.custname as custname,t.txndate,t.intamt,t.txntype,t.currencytype,t.iounum FROM inv_intdata t,(select t.CUSTCODE,max(t.custname) as custname from INV_INTDATA t where t.txndate = (select max(txndate) from INV_INTDATA where custcode = t.CUSTCODE)  group by t.CUSTCODE) tab WHERE t.custcode = tab.custcode and  t.itemstate = '1' and rownum < 1000 ");
        if (!"".equals(invIntDataQryCond.getCustName().trim())) {
            String custNameTmp = invIntDataQryCond.getCustName().trim().replaceAll("\\s+", " ");
            if (custNameTmp.contains(" ")) {
                sb.append(" AND t.custcode in (select distinct ii.custcode from inv_intdata ii where ");
                StringBuffer sbo = new StringBuffer("");
                String[] custNames = custNameTmp.split(" ");
                for (int i = 0; i < custNames.length; i++) {
                    sbo.append(" ii.custname LIKE '%" + custNames[i] + "%' ");
                    if (i < custNames.length - 1) {
                        sbo.append(" OR ");
                    }
                }
                sbo.append(")");
                sb.append(sbo);
            } else {
                sb.append(" AND t.custcode in (select distinct ii.custcode from inv_intdata ii where ii.custname like '%" + custNameTmp + "%') ");
            }
        }
        int txnTypeNum = invIntDataQryCond.getSelectTxnTypes().size(); //查询时选中的交易类型个数
        if (txnTypeNum != 0 && txnTypeNum != 4) {
            if (txnTypeNum == 1) {
                sb.append(" AND t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(0) + "'");
            } else if (txnTypeNum == 2) {
                sb.append(" AND (t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(0) + "' OR t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(1) + "') ");
            } else if (txnTypeNum == 3) {
                sb.append(" AND (t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(0) + "' OR t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(1) + "' OR t.txntype='" + invIntDataQryCond.getSelectTxnTypes().get(2) + "') ");
            }
        }
        if (!"".equals(invIntDataQryCond.getTxnDateSta())) {
            sb.append(" AND t.txndate >= '" + invIntDataQryCond.getTxnDateSta() + "'");
        }
        if (!"".equals(invIntDataQryCond.getTxnDateEnd())) {
            sb.append(" AND t.txndate <= '" + chgDate(invIntDataQryCond.getTxnDateEnd()) + "'");
        }

        sb.append(" AND t.currencytype = '" + invIntDataQryCond.getCurrencyType() + "'");

        if (invIntDataQryCond.getIntAmtSta().compareTo(new BigDecimal(0)) > 0) {
            sb.append(" AND t.intamt >= " + invIntDataQryCond.getIntAmtSta());
        }
        if (invIntDataQryCond.getIntAmtEnd().compareTo(new BigDecimal(0)) > 0) {
            sb.append(" AND t.intamt < " + invIntDataQryCond.getIntAmtEnd());
        }

        if (!"".equals(invIntDataQryCond.getMngnam().trim())) {
            sb.append(" AND t.mngnam like '%" + invIntDataQryCond.getMngnam()+"%'");
        }
        if (!"".equals(invIntDataQryCond.getBiznam().trim())) {
            sb.append(" AND t.biznam like '%" + invIntDataQryCond.getBiznam()+"%'");
        }
        if (!"".equals(invIntDataQryCond.getCmsnam().trim())) {
            sb.append(" AND t.cmsnam like '%" + invIntDataQryCond.getCmsnam()+"%'");
        }
        sb.append(" and not exists (select t2.comcod from comino t2 where t.custcode=t2.comcod) ");
        sb.append(" ORDER BY t.custcode,t.txndate ");
        return jdbcTemplate.query(sb.toString(), new InvIntDataRowMapper());
    }

    /**
     * 统计得到发票明细所有集合
     *
     * @param invIntDataQryCond 检索条件封装类
     * @param curRat
     * @return
     */
    public List<InvItem> staticInvItems(InvIntDataQryCond invIntDataQryCond, BigDecimal curRat) {
        String txnDateEndTmp = chgDate(invIntDataQryCond.getTxnDateEnd());
        StringBuffer sb = new StringBuffer("SELECT t.custcode,tab.custname as custname,t.txntype AS itemcode," + curRat + "*sum(t.intamt + t.syamt) AS price  FROM inv_intdata t,(select t.CUSTCODE,max(t.custname) as custname from INV_INTDATA t where t.txndate = (select max(txndate) from INV_INTDATA where custcode = t.CUSTCODE)  group by t.CUSTCODE) tab  WHERE tab.custcode = t.custcode and  t.itemstate = '1' ");
        if (!"".equals(invIntDataQryCond.getCustName().trim())) {
            String custNameTmp = invIntDataQryCond.getCustName().trim().replaceAll("\\s+", " ");
            if (custNameTmp.contains(" ")) {
                sb.append(" AND t.custcode in (select distinct ii.custcode from inv_intdata ii where ");
                StringBuffer sbo = new StringBuffer("");
                String[] custNames = custNameTmp.split(" ");
                for (int i = 0; i < custNames.length; i++) {
                    sbo.append(" ii.custname LIKE '%" + custNames[i] + "%' ");
                    if (i < custNames.length - 1) {
                        sbo.append(" OR ");
                    }
                }
                sbo.append(")");
                sb.append(sbo);
            } else {
                sb.append(" AND t.custcode in (select distinct ii.custcode from inv_intdata ii where ii.custname like '%" + custNameTmp + "%')");
            }
        }

        int txnTypeNum = invIntDataQryCond.getSelectTxnTypes().size(); //查询时选中的交易类型个数
        if (txnTypeNum != 0 && txnTypeNum != 4) {
            if (txnTypeNum == 1) {
                sb.append(" AND t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(0) + "'");
            } else if (txnTypeNum == 2) {
                sb.append(" AND (t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(0) + "' OR t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(1) + "') ");
            } else if (txnTypeNum == 3) {
                sb.append(" AND (t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(0) + "' OR t.txntype = '" + invIntDataQryCond.getSelectTxnTypes().get(1) + "' OR t.txntype='" + invIntDataQryCond.getSelectTxnTypes().get(2) + "') ");
            }
        }

        if (!"".equals(invIntDataQryCond.getTxnDateSta())) {
            sb.append(" AND t.txndate >= '" + invIntDataQryCond.getTxnDateSta() + "'");
        }
        if (!"".equals(invIntDataQryCond.getTxnDateEnd())) {
            sb.append(" AND t.txndate <= '" + txnDateEndTmp + "'");
        }

        sb.append(" AND t.currencytype = '" + invIntDataQryCond.getCurrencyType() + "'");

        if (invIntDataQryCond.getIntAmtSta().compareTo(new BigDecimal(0)) > 0) {
            sb.append(" AND t.intamt >= " + invIntDataQryCond.getIntAmtSta());
        }
        if (invIntDataQryCond.getIntAmtEnd().compareTo(new BigDecimal(0)) > 0) {
            sb.append(" AND t.intamt < " + invIntDataQryCond.getIntAmtEnd());
        }
        if (!"".equals(invIntDataQryCond.getMngnam().trim())) {
            sb.append(" AND t.mngnam like '%" + invIntDataQryCond.getMngnam()+"%'");
        }
        if (!"".equals(invIntDataQryCond.getBiznam().trim())) {
            sb.append(" AND t.biznam like '%" + invIntDataQryCond.getBiznam()+"%'");
        }
        if (!"".equals(invIntDataQryCond.getCmsnam().trim())) {
            sb.append(" AND t.cmsnam like '%" + invIntDataQryCond.getCmsnam()+"%'");
        }
        sb.append(" AND not exists (select t2.comcod from comino t2 where t.custcode=t2.comcod)  ");
        sb.append(" GROUP BY tab.custname, t.custcode, t.txntype ORDER BY t.custcode");
        return jdbcTemplate.query(sb.toString(), new StaticItemRowMapper());
    }

    /**
     * 将发票信息插入到数据库中
     *
     * @param invInfo 发票信息
     */
    public void addInvInvInfo(InvInvInfo invInfo) throws Exception {
        String fadeCode = invInfo.getFadeInvCode() == null ? "" : invInfo.getFadeInvCode();
        String sqlStr = "INSERT INTO inv_invinfo (pkid,invcode,invno,invdate,opter,maccode,taxcode,invtype,custcode,custname,fadeinvcode,fadeinvno,buskind,payee,pricesum,invver,remark,invremark) VALUES ('" + invInfo.getPkid() + "','" + invInfo.getInvCode() + "','" + invInfo.getInvNo() + "','"
                + invInfo.getInvDate() + "','" + invInfo.getOpter() + "','" + invInfo.getMacCode() + "','" + invInfo.getTaxCode() + "','" + invInfo.getInvType() + "','" + invInfo.getCustCode() + "','" + invInfo.getCustName() + "','" + fadeCode + "','" + invInfo.getFadeInvNo() + "','" + invInfo.getBusKind() + "','" + invInfo.getPayee() + "'," + invInfo.getPriceSum() + ",'1','" + invInfo.getRemark() + "','" + invInfo.getInvRemark() + "')";
        jdbcTemplate.update(sqlStr);
    }


    /**
     * 每打印一张发票，将对应的发票明细插入到数据库中
     *
     * @param invItems 对应发票明细集合
     */
    public void addIntItems(List<InvItem> invItems) throws Exception {
        String params;
        String sqlStr = "INSERT INTO inv_item (pkid,invcode,invno,taxcode,invdate,custcode,custname,itemcode,num,price,cash,summary) VALUES (";
        String[] strings = new String[invItems.size()];
        int i = 0;
        for (InvItem invItem : invItems) {
            params = "'" + invItem.getPkid() + "','" + invItem.getInvCode() + "','" + invItem.getInvNo() + "','" + invItem.getTaxCode() + "','" + invItem.getInvDate() + "','" + invItem.getCustCode() + "','" + invItem.getCustName() + "','" + invItem.getItemCode() + "',1," + invItem.getPrice() + "," + invItem.getCash() + ",'" + invItem.getSummary() + "')";
            strings[i] = sqlStr + params;
            i++;
        }
        jdbcTemplate.batchUpdate(strings);
    }

    /**
     * 将基础数据置为已开票
     *
     * @param invIntDataQryCond
     * @param invItems
     */
    public void invalidInvIntData(InvIntDataQryCond invIntDataQryCond, List<InvItem> invItems) throws Exception {
        String prtdat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String sqlStr;
        String[] strings = new String[invItems.size()];
        int i = 0;
        StringBuffer sbTmp = new StringBuffer("");
        if (invIntDataQryCond.getCurrencyType() != null) {
            sbTmp.append(" AND currencytype = '" + invIntDataQryCond.getCurrencyType() + "' ");
        }
        if (invIntDataQryCond.getIntAmtSta().compareTo(new BigDecimal(0)) > 0) {
            sbTmp.append(" AND intamt >= " + invIntDataQryCond.getIntAmtSta());
        }
        if (invIntDataQryCond.getIntAmtEnd().compareTo(new BigDecimal(0)) > 0) {
            sbTmp.append(" AND intamt <= " + invIntDataQryCond.getIntAmtEnd());
        }
        if (!"".equals(invIntDataQryCond.getBiznam())&&invIntDataQryCond.getBiznam() != null) {
            sbTmp.append(" AND biznam = '" + invIntDataQryCond.getBiznam() + "' ");
        }
        if (!"".equals(invIntDataQryCond.getCmsnam())&&invIntDataQryCond.getCmsnam() != null) {
            sbTmp.append(" AND cmsnam = '" + invIntDataQryCond.getCmsnam() + "' ");
        }
        if (!"".equals(invIntDataQryCond.getMngnam())&&invIntDataQryCond.getMngnam() != null) {
            sbTmp.append(" AND mngnam = '" + invIntDataQryCond.getMngnam() + "' ");
        }
        sbTmp.append(" AND txndate >='" + invIntDataQryCond.getTxnDateSta() + "' AND txndate <= '" + chgDate(invIntDataQryCond.getTxnDateEnd()) + "'");
        StringBuffer sbTmp1 = new StringBuffer("");
        for (InvItem invItem : invItems) {
            sqlStr = "UPDATE inv_intdata SET itemstate = '2',prtdat = '"+ prtdat +"',invcode = '";  //添加打印日期
            sbTmp1.append(sbTmp);
            sbTmp1.append(" AND custcode = '" + invItem.getCustCode() + "'");
            sbTmp1.append(" AND txntype = '" + invItem.getItemCode() + "' ");
            sqlStr += invItem.getInvCode() + "',invno = '" + invItem.getInvNo() + "' where itemstate= '1' " + sbTmp1.toString();
            sbTmp1.delete(0, sbTmp1.length());
            strings[i] = sqlStr;
            i++;
        }
        jdbcTemplate.batchUpdate(strings);
    }

    /**
     * 将基础数据置为未开票
     *
     * @param invCode
     * @param invNo
     * @throws Exception
     */
    public void validInvIntData(String invCode, String invNo) throws Exception {
        String sqlStr = "UPDATE inv_intdata SET itemstate = '1' where invcode = '" + invCode + "' and invno = '" + invNo + "'";
        jdbcTemplate.update(sqlStr);
    }

    public boolean getInvInfoByFadeInvNoAndCode(String invCode, String invNo) {
        String sqlStr = "SELECT count(pkid) FROM inv_invinfo WHERE fadeinvcode = '" + invCode + "' AND fadeinvno = '" + invNo + "'";
        int num = jdbcTemplate.queryForInt(sqlStr);
        if (num > 0) {
            return true;
        }
        return false;
    }

    public void invalidInvIntData(InvIntData selectedInvIntData) throws Exception {
        String upStr = "UPDATE inv_intdata SET itemstate = '2' where pkid='" + selectedInvIntData.getPkid() + "'";
        jdbcTemplate.update(upStr);
    }

    public String chgDate(String inputStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(inputStr.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(inputStr.substring(5)));
        calendar.set(Calendar.DATE, 1);
        String txnDateEndTmp = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
        return txnDateEndTmp;
    }

    /**
     * 检索发票信息
     *
     * @param invInfoQryCond 检索条件封装类
     * @return
     */
    public List<StaticInvItem> qryExpData(InvInfoQryCond invInfoQryCond) throws Exception {
        StringBuffer sb = new StringBuffer("select t.INVCODE,t.INVNO,decode(ii.invtype,'1',t.CUSTNAME,'2','(退)'|| ii.fadeinvno,'3',t.CUSTNAME,'') as custname,decode(t.ITEMCODE,'01','贷款利息','02','贴现利息','03','个人','04','贷款利息','') as itemname,t.CASH,t.INVDATE,t.TAXCODE,t.SUMMARY from inv_item t,inv_invinfo ii where t.invcode = ii.invcode and t.invno = ii.invno ");
        if (!"".equals(invInfoQryCond.getInvCode().trim())) {
            sb.append(" AND t.invcode LIKE '%" + invInfoQryCond.getInvCode() + "%'");
        }
        if (!"".equals(invInfoQryCond.getInvNo().trim())) {
            sb.append(" AND t.invno LIKE '%" + invInfoQryCond.getInvNo() + "%'");
        }
        if (!"".equals(invInfoQryCond.getCustName().trim())) {
            sb.append(" AND t.custname LIKE '%" + invInfoQryCond.getCustName() + "%'");
        }
        if (!"".equals(invInfoQryCond.getInvDateSta().trim())) {
            sb.append(" AND t.invdate >= '" + invInfoQryCond.getInvDateSta() + "'");
        }
        if (!"".equals(invInfoQryCond.getInvDateEnd().trim())) {
            sb.append(" AND t.invdate <= '" + invInfoQryCond.getInvDateEnd() + "'");
        }
        sb.append(" ORDER BY t.invdate,t.invno");
        return jdbcTemplate.query(sb.toString(), new StaticInvItemRowMapper());
    }

    /**
     * 获取未抄票的正常发票的总金额
     *
     * @return
     */
    public BigDecimal getCurInvPriceSum() throws Exception {
        String qrySql = "SELECT SUM(t.pricesum) AS curinvpricesum  FROM inv_invinfo t WHERE t.INVTYPE = '1' AND t.INVVER = '1'";
        Map<String, BigDecimal> map = jdbcTemplate.queryForMap(qrySql);
        if (map.get("CURINVPRICESUM") == null) {
            return new BigDecimal(0);
        }
        return map.get("CURINVPRICESUM");
    }

    /**
     * 获取未抄票的正常发票的总金额
     *
     * @return
     */
    public BigDecimal getCurBackInvPriceSum() throws Exception {
        String qrySql = "SELECT SUM(t.pricesum) AS curbackinvpricesum  FROM inv_invinfo t WHERE t.INVTYPE = '2' AND t.INVVER = '1'";
        Map<String, BigDecimal> map = jdbcTemplate.queryForMap(qrySql);
        if (map.get("CURBACKINVPRICESUM") == null) {
            return new BigDecimal(0);
        }
        return map.get("CURBACKINVPRICESUM");
    }

    public boolean invalidInvInfo(String invType) throws Exception {
        String updSql = "UPDATE inv_invinfo t SET t.INVVER = '2' WHERE t.INVTYPE = '" + invType + "' ";
        int num = jdbcTemplate.update(updSql);
        if (num > 0) {
            return true;
        }
        return false;
    }

    public String getMaxInvNo() {
        String selSql = "select max(t.INVNO) from inv_invinfo t";
        return (String) jdbcTemplate.queryForObject(selSql, String.class);
    }

    public BigDecimal getCurRat(String curratDate, String curcde) throws Exception {
        String sqlStr = "select t.CURRAT from SBS_ACTCXR t where t.TXNDATE = '" + curratDate + "' and t.CURCDE = '" + curcde + "' and t.SECCCY = '001'";
        Map<String, BigDecimal> map = jdbcTemplate.queryForMap(sqlStr);
        if (map.get("CURRAT") == null) {
            return new BigDecimal(0);
        }
        return map.get("CURRAT");
    }
}
