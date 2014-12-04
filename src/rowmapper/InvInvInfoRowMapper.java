package rowmapper;

import bean.InvInvInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvInvInfoRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        InvInvInfo invInvInfo = new InvInvInfo(resultSet.getString("pkid"), resultSet.getString("invcode"),
                resultSet.getString("invno"), resultSet.getString("invdate"), resultSet.getString("opter"),
                resultSet.getString("maccode"), resultSet.getString("taxcode"), resultSet.getString("invtype"),
                resultSet.getString("custcode"), resultSet.getString("custname"), resultSet.getString("fadeinvcode"),
                resultSet.getString("fadeinvno"), resultSet.getString("buskind"), resultSet.getString("payee"),
                resultSet.getBigDecimal("pricesum"), resultSet.getString("invver"), resultSet.getString("remark"),resultSet.getString("invremark"));
        return invInvInfo;
    }
}
