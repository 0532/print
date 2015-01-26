package rowmapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Lichao.W
 * On 2014/12/10 At 9:30
 */
public class CmsLstMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        String invNos =  resultSet.getString("cmsnam");
        return invNos;
    }
}
