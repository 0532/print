package rowmapper;

import bean.StaticInvItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StaticInvItemRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        StaticInvItem staticInvItem = new StaticInvItem(resultSet.getString("invcode"), resultSet.getString("invno"),
                resultSet.getString("custname"), resultSet.getString("itemname"),
                resultSet.getBigDecimal("cash"), resultSet.getString("invdate"),
                resultSet.getString("taxcode"), resultSet.getString("summary"));
        return staticInvItem;
    }
}
