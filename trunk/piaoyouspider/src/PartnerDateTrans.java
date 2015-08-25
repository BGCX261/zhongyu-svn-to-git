import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.piaoyou.dao.DataDao;


public class PartnerDateTrans {

	public static void main(String[] args) {
		List<String> partnerList = getPartnerList();
		for(int i=0;i<partnerList.size();i++){
			partnerDateTrans(partnerList.get(i));
		}

	}
	private static void partnerDateTrans(String agencyID){
		DataDao dao = new DataDao();
		String SQL = 
			"INSERT INTO t_partner_show(partner_id,"+
			"                           show_id,"+
			"                           is_sell,"+
			"                           page_view)"+
			"   SELECT ?,"+
			"          tsi.show_id,"+
			"          0,"+
			"          0"+
			"     FROM t_show_info tsi"+
			"          JOIN t_site_info ts"+
			"             ON ts.site_id = tsi.site_id"+
			"          JOIN t_city tc"+
			"             ON     tc.city_id = ts.city_id"+
			"                AND find_in_set(tc.city_id,"+
			"                                (SELECT friend_ticket_city"+
			"                                   FROM t_agency_info"+
			"                                  WHERE t_agency_info.agency_id = ?))"+
			"                AND tsi.status != 2 and tsi.status!=3 "+
			"                AND tsi.standard_agency_id IS NOT NULL"+
			"    WHERE NOT EXISTS"+
			"             (SELECT tps.show_id"+
			"                FROM t_partner_show tps"+
			"               WHERE tps.partner_id = ? AND tsi.show_id = tps.show_id)"+
			"          AND (EXISTS"+
			"                 (SELECT tst.show_id"+
			"                    FROM    t_show_time tst"+
			"                         JOIN"+
			"                            t_agency_price tap"+
			"                         where     tap.one_show_id = tst.one_show_id"+
			"                            AND tsi.standard_agency_id = tap.agency_id"+
			"                            AND tst.show_id = tsi.show_id)"+
			"         OR EXISTS (SELECT tsd.show_id"+
			"                FROM t_show_date tsd"+
			"               WHERE tsd.show_id = tsi.show_id"+
			"                     AND tsd.show_begin_date IS NOT NULL"+
			"                     and tsd.agency_id = tsi.standard_agency_id)"+
			"                     )";
		dao.executeSQL(SQL, agencyID,agencyID,agencyID);
	}
	
	private static List<String> getPartnerList(){
		DataDao dao = new DataDao();
		String SQL =  "select agency_id from t_agency_info where is_our_friend = 1";
		List<Map<String,Object>> partnerList = dao.searchBySQL(SQL);
		List<String> partner = new ArrayList<String>();
		for(int i=0;i<partnerList.size();i++){
			partner.add(partnerList.get(i).get("agency_id")+"");
		}
		return partner;
	}

}
