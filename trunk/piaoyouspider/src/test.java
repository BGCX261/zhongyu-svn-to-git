import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.piaoyou.dao.DataDao;




public class test{
	
	
	public static void main(String[] args) throws IOException {
		DataDao dao = new DataDao();
		StringBuffer sql = new StringBuffer();
		sql.append(" select show_id,show_id_cor from t_show_info where show_id_cor in( ");
		sql.append(" select tmp.show_id_cor from t_show_info t right join  ");
		sql.append(" (select distinct show_id_cor from t_show_info where show_id_cor is not null) tmp on t.show_id=tmp.show_id_cor ");
		sql.append(" where t.show_id is null) group by show_id_cor ");
		
		String update = "update t_show_info set show_id_cor=? where show_id_cor=?";
		
		List<Map<String,Object>> target = dao.searchBySQL(sql.toString());
		for(Map<String,Object> tmp:target){
			dao.executeSQL(update, tmp.get("show_id"),tmp.get("show_id_cor"));
		}
		
		dao.executeSQL("update t_show_info set show_id_cor=null where show_id_cor=show_id");

	}
}
