package com.piaoyou.util.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public final class DBUtils {
	
	private static final Log log = LogFactory.getLog(DBUtils.class);
	private static BasicDataSource bds = new BasicDataSource();
	private final static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();

	static {
		initDataSource();
	}

	private static void initDataSource() {
		try {
			Document doc = new SAXReader().read(Thread.currentThread()
					.getContextClassLoader().getResource("DataSource.xml"));
			Element root = doc.getRootElement();

			bds.setDriverClassName(root.elementText("driverClassName")); // 数据库驱动程序
			bds.setUrl(root.elementText("url")); // 数据库url
			bds.setUsername(root.elementText("username")); // dba帐号
			bds.setPassword(root.elementText("password")); // 密码
			bds.setMaxActive(Integer.valueOf(root.elementText("maxActive"))); // 初始化连接数量
			bds.setMaxIdle(Integer.valueOf(root.elementText("maxIdle"))); // 最大idle数
			bds.setMaxWait(Integer.valueOf(root.elementText("maxWait"))); // 超时回收时间

			Connection conn = getConnection();
			DatabaseMetaData mdm = conn.getMetaData();
			System.out.println("Connected to " + mdm.getDatabaseProductName() + " "
					+ mdm.getDatabaseProductVersion());
		} catch (SQLException e) {
			log.error("initDataSource error:" + e);
		} catch (Exception e) {
			log.error("parse config error:" + e);
		}
	}

	public static Connection getConnection() throws SQLException {
		Connection conn = conns.get();
		if (conn == null || conn.isClosed()) {
			conn = bds.getConnection();
			conns.set(conn);
		}
		return conn;
	}

	public static void printDataSourceStats(DataSource ds) {
		BasicDataSource bds = (BasicDataSource) ds;
		log.info("NumActive: " + bds.getNumActive());
		log.info("NumIdle: " + bds.getNumIdle());
	}

	public static void shutdownDataSource(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		bds.close();
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Connection conn = DBUtils.getConnection();
						System.out.println(" : " + conn.getClass().getName()
								+ "@" + Integer.toHexString(conn.hashCode()));
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.error("close ResultSet error:" + e);
			}
		}
	}

	public static void closeStatement(PreparedStatement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				log.error("close statement error:" + e);
			}
		}
	}

	public static void closeConnection(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				conn.close();
			}
		} catch (SQLException e) {
			log.error("close Connection error:", e);
		}
		conns.set(null);
	}

}
