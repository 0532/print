package rowmapper;

import bean.InvIntData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvIntDataRowMapper implements RowMapper {
    @Override
     public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        InvIntData invIntData = new InvIntData(resultSet.getString("pkid"), resultSet.getString("custcode"),
                resultSet.getString("custname"), resultSet.getString("txndate"),
                resultSet.getBigDecimal("intamt"), resultSet.getString("txntype"),
                resultSet.getString("currencytype"), resultSet.getString("iounum"));
        return invIntData;
    }
}















