package rowmapper;

import bean.InvItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvItemRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        InvItem invItem = new InvItem(resultSet.getString("pkid"), resultSet.getString("invcode"),
                resultSet.getString("invno"), resultSet.getString("taxcode"),
                resultSet.getString("invdate"), resultSet.getString("custcode"),
                resultSet.getString("custname"), resultSet.getString("itemcode"),
                resultSet.getInt("num"), resultSet.getBigDecimal("price"),
                resultSet.getBigDecimal("cash"), resultSet.getString("summary"));
        return invItem;
    }
}
