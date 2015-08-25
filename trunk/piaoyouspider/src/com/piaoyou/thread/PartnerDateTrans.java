package com.piaoyou.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.dao.DataDao;

/**
 * 同步(已经审核的记录) 票商 2个小时同步一次 216
 * 
 * @author chow.kingbo
 * 
 */
public class PartnerDateTrans {
	private static final Log log = LogFactory.getLog(PartnerDateTrans.class);
	public static void main(String[] args) {
//		DataDao dao = new DataDao();
//		try {
//			List<String> partnerList = getPartnerList(dao);
//			for (int i = 0; i < partnerList.size(); i++) {
//				partnerDateTrans(partnerList.get(i), dao);
//			}
//		} catch (Exception e) {
//		} finally {
//		}
		log.info("同步合作商");
		synPartners();
	}

	@SuppressWarnings("finally")
	public static int synPartners() {
		int rint = 0;
		DataDao dao = null;
		try {
			List<String> partnerList = getPartnerList(dao);
			for (int i = 0; i < partnerList.size(); i++) {
				partnerDateTrans(partnerList.get(i), dao);
			}
			partnerList = getPartnerList2(dao);
			for (int i = 0; i < partnerList.size(); i++) {
				synPartner(partnerList.get(i), dao);
			}
		} catch (Exception e) {
			rint = -1;
		} finally {
			return rint;
		}

	}

	private static void partnerDateTrans(String agencyID, DataDao dao) {
		dao = new DataDao();
		String SQL = "INSERT INTO t_partner_show(partner_id,"
				+ "                           show_id,"
				+ "                           is_sell,"
				+ "                           page_view,syn_time)"
				+ "   SELECT ?,"
				+ "          tsi.show_id,"
				+ "          0,"
				+ "          0,"
				+ "          now()"
				+ "     FROM t_show_info tsi"
				+ "          JOIN t_site_info ts"
				+ "             ON ts.site_id = tsi.site_id"
				+ "          JOIN t_city tc"
				+ "             ON     tc.city_id = ts.city_id"
				+ "                AND find_in_set(tc.city_id,"
				+ "                                (SELECT friend_ticket_city"
				+ "                                   FROM t_agency_info"
				+ "                                  WHERE t_agency_info.agency_id = ?))"
				+ "                AND tsi.status != 2 AND tsi.status != 3"
				+ "                AND tsi.standard_agency_id IS NOT NULL"
//				+ "  				 AND TO_DAYS(crawled_time) = TO_DAYS(now())"
				+ "    WHERE NOT EXISTS"
				+ "             (SELECT tps.show_id"
				+ "                FROM t_partner_show tps"
				+ "               WHERE    tps.partner_id = ? AND tsi.show_id = tps.show_id)"
				/** 影响SQL执行效率 */
				+ "          AND (EXISTS"
				+ "                 (SELECT tst.show_id"
				+ "                    FROM    t_show_time tst"
				+ "                         JOIN"
				+ "                            t_agency_price tap"
				+ "                         where     tap.one_show_id = tst.one_show_id"
				+ "                            AND tsi.standard_agency_id = tap.agency_id"
				+ "                            AND tst.show_id = tsi.show_id)"
				+ "         OR EXISTS (SELECT tsd.show_id"
				+ "                FROM t_show_date tsd"
				+ "               WHERE tsd.show_id = tsi.show_id"
				+ "                     AND tsd.show_begin_date IS NOT NULL"
				+ "                     and tsd.agency_id = tsi.standard_agency_id"
				+ "				    	AND tsi.standard_agency_id IS NOT NULL)"
				+ "                     )";
		dao.executeSQL(SQL, agencyID, agencyID, agencyID);
	}

	private static void synPartner(String agencyID, DataDao dao) {
		System.err.println(agencyID);
		dao = new DataDao();
		String SQL = "update t_agency_price otap , (select H.showId as show_id,"
				+ "	H.showName as show_name,H.showTime as show_time,H.standardPrice as standard_price,H.isExist standard_is_exist,B.is_exist as is_exist,(select"
				+ "	site_name   from t_site_info where t_site_info.site_id"
				+ "	=H.siteId) as site_name,B.id as agency_price_id from"
				+ "	(SELECT A.show_id showId ,"
				+ "	A.show_name showName,"
				+ "	A.standard_agency_id agencyId,"
				+ "	B.one_show_id,"
				+ "	B.show_time showTime,"
				+ "	C.is_exist isExist,"
				+ "	C.standard_price standardPrice,"
				+ "	A.site_id siteId,"
				+ "	C.id priceId"
				+ "	FROM t_show_info A, t_show_time B,t_agency_price C,t_partner_show D"
				+ "	WHERE A.show_id = B.show_id AND A.standard_agency_id is not null"
				+ "	and A.standard_agency_id=C.agency_id"
				+ "	and B.one_show_id=C.one_show_id"
				+ "	and D.show_id=A.show_id" + "	and D.partner_id= ?"
				+ "	and D.is_sell=1" + "	and A.status!=2"
				+ "	) H,t_agency_price B,t_show_time C" + "	where"
				+ "	H.one_show_id=B.one_show_id" + "	and C.show_id=H.showId"
				+ "	and C.one_show_id=B.one_show_id" + "	and B.agency_id= ?"
				+ "	and H.standardPrice=B.standard_price"
				+ "	and H.isExist!=B.is_exist"
				+ "	and date(H.showTime)>=date(now())"
				+ "	and (B.status !=1 or B.status is null)" + "	order by H.showId ) tmp set otap.is_exist=tmp.standard_is_exist where otap.id = tmp.agency_price_id";//不同步忽略的
		dao.executeSQL(SQL, agencyID,agencyID);
	}

	private static List<String> getPartnerList(DataDao dao) {
		dao = new DataDao();
		String SQL = "select agency_id from t_agency_info where is_our_friend = 1";
		List<Map<String, Object>> partnerList = dao.searchBySQL(SQL);
		List<String> partner = new ArrayList<String>();
		for (int i = 0; i < partnerList.size(); i++) {
			partner.add(partnerList.get(i).get("agency_id") + "");
		}
		return partner;
	}
	private static List<String> getPartnerList2(DataDao dao) {
		dao = new DataDao();
		String SQL = "select agency_id from t_agency_info where is_our_friend = 1 and is_syn =1";
		List<Map<String, Object>> partnerList = dao.searchBySQL(SQL);
		List<String> partner = new ArrayList<String>();
		for (int i = 0; i < partnerList.size(); i++) {
			partner.add(partnerList.get(i).get("agency_id") + "");
		}
		return partner;
	}
}
