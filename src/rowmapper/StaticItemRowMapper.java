package rowmapper;

import bean.InvItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StaticItemRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        InvItem invItem = new InvItem(UUID.randomUUID().toString(), "", "", "", "", resultSet.getString("custcode"),
                resultSet.getString("custname"), resultSet.getString("itemcode"),
                1, resultSet.getBigDecimal("price"), resultSet.getBigDecimal("price"), "");
        return invItem;
    }
}
