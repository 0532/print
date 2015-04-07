package rowmapper;

import bean.InvIntData;
import bean.PrtInvInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Lichao.W
 * On 2014/12/10 At 9:30
 */
public class PrtInvInfoMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        PrtInvInfo prtInvInfo = new PrtInvInfo(resultSet.getString("invCode"),resultSet.getString("invNo"),resultSet.getString("custCode"),
                resultSet.getString("custName"),resultSet.getString("txnDate"),resultSet.getBigDecimal("intAmt"),resultSet.getBigDecimal("syAmt"),
                resultSet.getString("txnType"),resultSet.getString("currencyType"),resultSet.getString("iouNum"),
                resultSet.getString("itemState"),resultSet.getString("apndate"),resultSet.getString("creamt"),
                resultSet.getString("debamt"),resultSet.getString("contno"),resultSet.getString("invrat"),resultSet.getString("prtdat"),resultSet.getString("enddat"));
        return prtInvInfo;
    }
}
