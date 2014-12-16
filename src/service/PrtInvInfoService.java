package service;

import bean.InvItem;
import bean.PrtInvInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import rowmapper.InvItemRowMapper;
import rowmapper.PrtInvInfoMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: Lichao.W
 * On 2014/12/9 At 16:40
 */
@Service
public class PrtInvInfoService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<PrtInvInfo> getInvdatByInvNo(String invcode, String invno1,String invno2) throws Exception {
        String sqlStr = "SELECT t.invCode, t.invNo,t.custCode,t.custName, t.txnDate ,t.intAmt ,t.txnType ,t.currencyType ,t.iouNum,t.itemState, t.apndate,t.creamt, t.debamt ,t.contno ,t.invrat ,t.prtdat FROM inv_intdata t WHERE t.invcode = '" + invcode + "' AND t.invno between '" + invno1 + "'"+" and '"+invno2+"'";
        return jdbcTemplate.query(sqlStr, new PrtInvInfoMapper());
    }
}
