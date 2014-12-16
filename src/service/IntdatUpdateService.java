package service;

import bean.InvIntData;
import bean.InvItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import qrybean.InvIntDataQryCond;
import rowmapper.InvIntDataPrtMapper;
import rowmapper.InvIntDataRowMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Author:Lichao.W
 * On 2014/12/15 At 15:37
 */
@Service
public class IntdatUpdateService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<InvIntData> onQueryInvData(InvIntDataQryCond invIntDataQryCond) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT t.* FROM inv_intdata t,(select t.CUSTCODE,max(t.custname) as custname from INV_INTDATA t where t.txndate = (select max(txndate) from INV_INTDATA where custcode = t.CUSTCODE)  group by t.CUSTCODE) tab WHERE t.custcode = tab.custcode and  t.itemstate = '1' and rownum < 1000 ");
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
        sb.append(" AND (t.compan is null or t.mngnam is null) ");
        sb.append(" ORDER BY t.custcode,t.txndate ");
        return jdbcTemplate.query(sb.toString(), new InvIntDataPrtMapper());
    }
    public void updateInvData(InvIntData invIntData) throws Exception {
        StringBuffer sb = new StringBuffer("UPDATE inv_intdata t SET t.compan = '" + invIntData.getCompan()+"' , "+"t.mngnam = '"+ invIntData.getMngnam()+"' where t.custname = '" +invIntData.getCustName()+"' and t.txndate like '"+chgDate2(invIntData.getTxnDate())+"%'");
        //sqlStr = "UPDATE inv_intdata t SET t.compan =" +invIntData.getCompan()+" t.mngnam = " + invIntData.getMngnam();
        jdbcTemplate.update(sb.toString());
    }
    public String chgDate(String inputStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(inputStr.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(inputStr.substring(5)));
        calendar.set(Calendar.DATE, 1);
        String txnDateEndTmp = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
        return txnDateEndTmp;
    }
    public String chgDate2(String inputStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(inputStr.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(inputStr.substring(5,7)));
        calendar.set(Calendar.DATE, 0);
        String txnDateTmp = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
        return txnDateTmp;
    }
}
