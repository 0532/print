package service;

import bean.InvInvInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import rowmapper.InvInvInfoRowMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RiService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 将发票信息插入到数据库中
     *
     * @param invInfo 发票信息
     */
    public int addInvInvInfo(InvInvInfo invInfo) throws Exception {
        String fadeCode = invInfo.getFadeInvCode() == null ? "" : invInfo.getFadeInvCode();
        String sqlStr = "INSERT INTO inv_invinfo (pkid,invcode,invno,invdate,pricesum,opter,maccode,taxcode,invtype,custcode,custname,fadeinvcode,fadeinvno,buskind,payee,invver,remark) VALUES ('" + invInfo.getPkid() + "','" + invInfo.getInvCode() + "','" + invInfo.getInvNo() + "','"
                + invInfo.getInvDate() + "'," + invInfo.getPriceSum() + ",'" + invInfo.getOpter() + "','" + invInfo.getMacCode() + "','" + invInfo.getTaxCode() + "','" + invInfo.getInvType() + "','" + invInfo.getCustCode() + "','" + invInfo.getCustName() + "','" + fadeCode + "','" + invInfo.getFadeInvNo() + "','" + invInfo.getBusKind() + "','" + invInfo.getPayee() + "','" + invInfo.getInvVer() + "','" + invInfo.getRemark() + "')";
        return jdbcTemplate.update(sqlStr);
    }

    /**
     * 是否有与之对应的正常票
     *
     * @param fadeInvCode
     * @param fadeInvNo
     * @return
     */
    public InvInvInfo hasCspCommInv(String fadeInvCode, String fadeInvNo) {
        String sqlStr = "select * from inv_invinfo t where t.invcode = '" + fadeInvCode + "' and t.invno = '" + fadeInvNo + "' and t.INVVER = '1' and t.invtype = '3'";
        List<InvInvInfo> invInvInfos = jdbcTemplate.query(sqlStr, new InvInvInfoRowMapper());
        if (!invInvInfos.isEmpty()) {
            return invInvInfos.get(0);
        }
        return null;
    }

    public boolean getInvInfoByFadeInvNoAndCode(String fadeInvCode, String fadeInvNo) {
        String sqlStr = "select count(t.PKID) from inv_invinfo t where t.FADEINVCODE = '" + fadeInvCode + "' and t.FADEINVNO = '" + fadeInvNo + "'";
        int num = jdbcTemplate.queryForInt(sqlStr);
        if (num > 0) {
            return true;
        }
        return false;
    }

    public boolean existInv(String invCode, String invNo) {
        String sqlStr = "select count(t.PKID) from inv_invinfo t where t.invcode = '" + invCode + "' and t.invno = '" + invNo + "'";
        int num = jdbcTemplate.queryForInt(sqlStr);
        if (num > 0) {
            return true;
        }
        return false;
    }
}
