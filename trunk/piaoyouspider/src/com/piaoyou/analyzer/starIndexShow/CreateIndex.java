package com.piaoyou.analyzer.starIndexShow;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import com.piaoyou.analyzer.index.ConstantsIndex;
import com.piaoyou.analyzer.index.IShow;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.CacheUtil;
import com.piaoyou.util.db.DBUtils;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

public class CreateIndex {
	
	public static  Connection getConn(){
		try {
			InputStream in = CacheUtil.class.getResourceAsStream("/init.properties");
			Properties prop = new Properties();
			try {
				prop.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return DriverManager.getConnection(prop.getProperty("jdbc.url.user"), prop.getProperty("jdbc.username.user"),prop.getProperty("jdbc.url.user"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		creatStarIndex();
	}
	public static void creatStarIndex(){
		String sql=" select * from ( SELECT A.show_id showId,A.show_name_clean showName, A.show_info showInfo, B.show_time showTime,  C.site_name siteName ,A.status status , (select t_show_type.en_name from t_show_type where t_show_type.show_type_id =A.show_type_id ) as showType,  (select t_city.en_name from t_city where t_city.city_id=C.city_id) as cityName FROM t_show_info A,(SELECT show_id,  (case when min(show_time)=max(show_time)  then  min(show_time) else concat(min(show_time), '至', max(show_time))  end) as show_time  FROM t_show_time  GROUP BY show_id) B,t_site_info C  WHERE     A.show_id = B.show_id   AND A.site_id = C.site_id  AND A.show_id_cor IS NULL AND A.standard_agency_id IS NOT NULL and  A.status !=3 UNION SELECT A.show_id showId, A.show_name_clean showName,   A.show_info showInfo,  B.show_time showTime,  C.site_name siteName,A.status status ,(select t_show_type.en_name from t_show_type where t_show_type.show_type_id =A.show_type_id ) as showType,(select t_city.en_name from t_city where t_city.city_id=C.city_id) as cityName FROM t_show_info A, (SELECT show_id,  (case when min(show_begin_date)=max(show_end_date) then    min(show_begin_date)  else concat(min(show_begin_date), '至', max(show_end_date)) end )  AS show_time FROM t_show_date  WHERE (show_begin_date IS NOT NULL AND show_end_date IS NOT NULL)  GROUP BY show_id) B,  t_site_info C  WHERE     A.show_id = B.show_id AND A.site_id = C.site_id  AND A.standard_agency_id IS NOT NULL and  A.status !=3) z group by showId order by null";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			PaodingAnalyzer analyzer = new PaodingAnalyzer();
			String indexPath=ConstantsIndex.showInfoStar_index_path;
			IndexWriter writer = new IndexWriter(indexPath,analyzer, true);
			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			int ia=0;
			while(rs.next()){
				ia++;
				Document doc=new Document();
				Field showId=new Field("showId",rs.getString("showId")!=null ? rs.getString("showId"):"",Field.Store.YES,Field.Index.UN_TOKENIZED);
				doc.add(showId);
				Field showName=new Field("showName",rs.getString("showName")!=null ? rs.getString("showName"):"",Field.Store.YES,Field.Index.TOKENIZED);
				doc.add(showName);
				Field showInfo=new Field("showInfo",rs.getString("showInfo")!=null ? rs.getString("showInfo"):"",Field.Store.YES,Field.Index.TOKENIZED);
				doc.add(showInfo);
				Field showTime=new Field("showTime",rs.getString("showTime")!=null ? rs.getString("showTime"):"",Field.Store.YES,Field.Index.TOKENIZED);
				doc.add(showTime);
				Field siteName=new Field("siteName",rs.getString("siteName")!=null ? rs.getString("siteName"):"",Field.Store.YES,Field.Index.TOKENIZED);
				doc.add(siteName);
				Field status=new Field("status",rs.getString("status")!=null ? rs.getString("status"):"",Field.Store.YES,Field.Index.UN_TOKENIZED);
				doc.add(status);
				Field showType=new Field("showType",rs.getString("showType")!=null ? rs.getString("showType"):"",Field.Store.YES,Field.Index.UN_TOKENIZED);
				doc.add(showType);
				Field cityName=new Field("cityName",rs.getString("cityName")!=null ? rs.getString("cityName"):"",Field.Store.YES,Field.Index.UN_TOKENIZED);
				doc.add(cityName);
				writer.addDocument(doc);
			}
			System.out.println("共创建"+ia+"条记录");
			writer.optimize();
			writer.close();
		}catch(Exception ee){
			ee.printStackTrace();
		}
		finally{
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(stmt);
			DBUtils.closeConnection(conn);
		}
	}
	
	
	/*
	 * @param indexPath 索引文件位置 
	 * @param List<IShowIndex> 需要建立所以的数据
	 * @param isIncrement 建立索引的类型 true表示覆盖原来已经创建的索引，如果是false表示不覆盖，而是继续添加索引（即增量索引）
	 * 说明：对传入的List链表数据。解析其对象的属性、获取属性值。将对应的属性及值放到Document
	 *      将相关数据以庖丁分词工具进行分词,建立成索引
	 */
	public void CreateConfigDocAndIndex(String indexPath,List<IShow> showList,boolean isIncrement){
		try {
			PaodingAnalyzer analyzer = new PaodingAnalyzer();
			//true表示覆盖原来已经创建的索引,如果是false表示不覆盖，而是继续添加索引 区别就在此!
			IndexWriter writer = new IndexWriter(indexPath,analyzer, isIncrement); 
			for(IShow s:showList){
				String[] propertys = s.getPropertyName();
				Document doc = new Document();
				for(String temp : propertys){
					//java.lang.reflect.Field o = s.getClass().getField(temp);
					java.lang.reflect.Field o = s.getClass().getDeclaredField(temp);//用于获取私有属性
					o.setAccessible(true);//设置私有属性范围，允许访问。
					Object temp_o = o.get(s);
					if(null == temp_o)temp_o="";
					//System.out.println(temp_o+"="+temp_o.toString());
					doc.add(new Field(temp,temp_o.toString(),Field.Store.YES,Field.Index.TOKENIZED)); 
				}
				writer.addDocument(doc);
			}
			writer.optimize(); //优化索引文件
			writer.close();
		} catch (Exception e) {
			System.out.println("创建索引时出现error:"+e.getMessage());
		} finally{
			System.out.println("创建索引建立完毕");
		}
	}

	
	
}
