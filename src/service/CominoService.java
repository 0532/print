package service;

import bean.Comino;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import rowmapper.CominoMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CominoService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Comino> cominoqry(String comcod, String comnam) throws Exception {
        StringBuffer sb = new StringBuffer("select t.comcod,t.comnam,t.biznam,t.cmsnam,t.mngnam,t.compan,t.upddat from comino t where 1 = 1");
        if (!"".equals(comcod.trim())) {
            sb.append(" and t.comcod = '" + comcod + "'");
        }
        if (!"".equals(comnam.trim())) {
            sb.append(" and t.comnam like '%" + comnam + "%'");
        }
        return jdbcTemplate.query(sb.toString(), new CominoMapper());
    }

    public int cominodel(String comcod) {
        String sql = "DELETE FROM comino t WHERE  t.comcod = '" + comcod + "'";
        return jdbcTemplate.update(sql);
    }

    public int cominoadd(String comcod, String comnam) {
        String sql = "INSERT INTO comino t (t.comcod,t.comnam) VALUES ('" + comcod + "','" + comnam + "')";
        return jdbcTemplate.update(sql);
    }
}
