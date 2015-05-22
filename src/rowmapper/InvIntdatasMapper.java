package rowmapper;

import bean.InvIntData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Lichao.W At 2015/5/19 10:01
 * wanglichao@163.com
 * 主要用在从临时表复制到intdata表
 */
public class InvIntdatasMapper implements RowMapper {
    @Override
     public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        InvIntData invIntData = new InvIntData(resultSet.getString("pkid"), resultSet.getString("custcode"),
                resultSet.getString("custname"), resultSet.getString("txndate"),
                resultSet.getBigDecimal("intamt"),resultSet.getBigDecimal("syamt"), resultSet.getString("txntype"),
                resultSet.getString("currencytype"),resultSet.getString("iouNum"),
                resultSet.getString("biznam"),resultSet.getString("cmsnam"),resultSet.getString("mngnam"),
                resultSet.getString("compan"),resultSet.getString("apndate"),resultSet.getString("creamt"),
                resultSet.getString("debamt"),resultSet.getString("contno"),resultSet.getString("invrat"),resultSet.getString("enddat"));
        return invIntData;
    }
}
