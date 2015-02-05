package rowmapper;

import bean.Comino;
import bean.InvIntData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CominoMapper implements RowMapper {
    @Override
     public Object mapRow(ResultSet resultSet, int i) throws SQLException {
         Comino cominoList = new Comino(resultSet.getString("comcod"), resultSet.getString("comnam"),
                 resultSet.getString("biznam"),resultSet.getString("cmsnam"),
                 resultSet.getString("mngnam"),resultSet.getString("compan"),resultSet.getDate("upddat"));
        return cominoList;
    }
}















