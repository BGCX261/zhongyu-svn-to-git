package com.piaoyou.util;

/**
 * 
 */

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.alisoft.xplatform.asf.cache.memcached.client.MemCachedClient;
import com.alisoft.xplatform.asf.cache.memcached.client.SockIOPool;




/**
 * @author shen.hai
 * 
 */
public final class CacheUtil {
	private final static Logger log = Logger.getLogger(CacheUtil.class);

	private static CacheUtil instance = null;
	private SockIOPool pool;
	private MemCachedClient mcc;
	private CacheUtil() {
		this.init();
	}

	/**
	 * 
	 */
	private void init() {
		try {
			InputStream in = CacheUtil.class.getResourceAsStream("/p2p.properties");
			Properties prop = new Properties();
			prop.load(in);
			
			pool = SockIOPool.getInstance();
			// 开始设置pool属性
			String[] servers = prop.getProperty("memcached.server.list").trim().split(",");
			// grab an instance of our connection pool
			pool.setServers(servers);
			// set some basic pool settings
			pool.setInitConn(Integer.parseInt(prop.getProperty("memcached.init.conn", "10")));
			pool.setMinConn(Integer.parseInt(prop.getProperty("memcached.min.conn", "10")));
			pool.setMaxConn(Integer.parseInt(prop.getProperty("memcached.max.conn","800") ));
			pool.setMaxIdle(1000 * 60 * 60 * Integer.parseInt(prop.getProperty("memcached.max.idle.time", "6")));

			// set the sleep for the maint thread
			// it will wake up every x seconds and
			// maintain the pool size
			pool.setMaintSleep(30);

			// set some TCP settings
			// disable nagle
			// set the read timeout to 3 secs
			// and don't set a connect timeout
			pool.setNagle(false);
			pool.setSocketTO(3000);
			pool.setSocketConnectTO(0);

			// initialize the connection pool
			pool.initialize();

			// get client instance
			mcc = new MemCachedClient();
			// 开始设置 mcc属性
			// lets set some compression on for the client
			// compress anything larger than 64k
			mcc.setCompressEnable(true);
			mcc.setCompressThreshold(64 * 1024);
		} catch (Exception e) {
			log.error("memcache read file p2p.properties error:" + e);
		}
	}

	public static CacheUtil getInstance() {
		if (instance == null) {
			synchronized (log) {
				instance = new CacheUtil();
			}
		}
		return instance;
	}

	public String getCache(String key) {
		if(log.isDebugEnabled()){
			log.debug("memcache get key: "+key);
		}
		return (String) mcc.get(key);
	}
	public Object getCacheObject(String key) {
		if(log.isDebugEnabled()){
			log.debug("memcache get key: "+key);
		}
		return  mcc.get(key);
	}

	public boolean putCache(String key, String value,Date expireTime) {
		if (CacheKey.IS_START_CACHE==false){
			return false;
		}
		if(log.isDebugEnabled()){
			log.debug("memcache put key:"+key+" value:"+value);
		}
		return mcc.set(key, value,expireTime);
	}
	public boolean putCache(String key, Object value,Date expireTime) {
		if (CacheKey.IS_START_CACHE==false){
			return false;
		}
		if(log.isDebugEnabled()){
			log.debug("memcache put key:"+key+" value:"+value);
		}
		return mcc.set(key, value,expireTime);
	}
	
	public boolean putCache(String key, Object value) {
		if (CacheKey.IS_START_CACHE==false){
			return false;
		}
		if(log.isDebugEnabled()){
			log.debug("memcache put key:"+key+" value:"+value);
		}
		return mcc.set(key, value);
	}
	
	public boolean delCache(String key) {
		if (CacheKey.IS_START_CACHE==false){
			return false;
		}
		if(log.isDebugEnabled()){
			log.debug("memcache del key:"+key);
		}
		return mcc.delete(key);
	}
}
