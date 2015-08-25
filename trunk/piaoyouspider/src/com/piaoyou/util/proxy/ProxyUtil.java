/**
 * 
 */
package com.piaoyou.util.proxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jinfeng.zhang
 * 
 */
public class ProxyUtil {

	/**
	 * @param args
	 */

	private final static Log logger = LogFactory.getLog(ProxyUtil.class);

	public static HashSet<String> proxySet = null;

	static final String classPath = ProxyUtil.class.getResource("config").getPath();
	
	public static void main(String[] args) {
		reloadProxy();
	}

	public static void reloadProxy() {
		
		proxySet = new HashSet<String>();
		try {
			String proxy[] = readFile(classPath+"/proxy.proxy", "UTF-8").split(
					",");
			for (int c = 0; c < proxy.length; c++){
				if (validProxy(proxy[c]))
					proxySet.add(proxy[c]);
				else
					proxySet.remove(proxy[c]);
			}
			StringBuilder sb = new StringBuilder();
			for(String key:proxySet)
				sb.append(key+",");
			writeFile(sb.toString(), classPath+"/proxy.proxy", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("reloadProxy over");
	}

	private static boolean validProxy(String proxy) {
		GetMethod method =  null;
		try {
			HttpClient hc = new HttpClient();
			Matcher m = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)")
					.matcher(proxy);
			if (m.find()) {
				hc.getHostConfiguration().setProxy(m.group(1),
						Integer.parseInt(m.group(2)));
			} else {
				throw new RuntimeException(" Wrong proxy format: " + proxy);
			}
			hc.getHttpConnectionManager().getParams().setConnectionTimeout(
					1000);
			hc.getHttpConnectionManager().getParams().setSoTimeout(
					1000);
			method = new GetMethod("http://www.baidu.com");
			int code = hc.executeMethod(method);
			System.out.println(method.getResponseBodyAsString());
			if(code==200)
			return true;
		} catch (Exception e) {
			logger.warn("proxy error:"+proxy);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}

		return false;
	}

	static{
		try {
			loadProxy();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean setProxyHost(HttpClient client) {

		try {
			String proxy[] = randomSet(proxySet).split(":");
			String ip = proxy[0];
			String port = proxy[1];
			client.getHostConfiguration().setProxy(ip, Integer.parseInt(port));
		} catch (Exception e) {
			logger.warn("set proxy host error for ", e);
			return false;
		}
		return true;
	}

	private static void loadProxy() throws FileNotFoundException, IOException {
		
		proxySet = new HashSet<String>();
		String proxy[] = readFile(classPath+"/proxy.proxy", "UTF-8").split(
				",");
		for (int c = 0; c < proxy.length; c++){
				proxySet.add(proxy[c]);
		}
		System.out.println("loadProxy over");
	}

	private static String randomSet(HashSet<String> proxySet2) {
		String proxy[] = proxySet2.toArray(new String[0]);
		return proxy[(int) (Math.random() * proxySet2.size())];
	}
	
	 public static String readFile(String fileName, String srcEncoding)
     	throws FileNotFoundException, IOException {

     File file = null;
     try {
         file = new File(fileName);
         if (file.isFile() == false) {
             throw new IOException("'" + fileName + "' is not a file.");
         }
     } finally {
         // we don't have to close File here
     }

     BufferedReader reader = null;
     try {
         StringBuffer result = new StringBuffer(1024);
         FileInputStream fis = new FileInputStream(fileName);
         reader = new BufferedReader(new InputStreamReader(fis, srcEncoding));

         char[] block = new char[512];
         while (true) {
             int readLength = reader.read(block);
             if (readLength == -1) break;// end of file
             result.append(block, 0, readLength);
         }
         return result.toString();
     } catch (FileNotFoundException fe) {
    	 logger.error("Error", fe);
         throw fe;
     } catch (IOException e) {
    	 logger.error("Error", e);
         throw e;
     } finally {
         try {
             if (reader != null) reader.close();
         } catch (IOException ex) {}
     }
 }
	 
	 /**
     * Write content to a fileName with the destEncoding
     *
     * @param content String
     * @param fileName String
     * @param destEncoding String
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeFile(String content, String fileName, String destEncoding)
        throws FileNotFoundException, IOException {

        File file = null;
        try {
            file = new File(fileName);
            if (file.isFile() == false) {
                throw new IOException("'" + fileName + "' is not a file.");
            }
            if (file.canWrite() == false) {
                throw new IOException("'" + fileName + "' is a read-only file.");
            }
        } finally {
            // we don't have to close File here
        }

        BufferedWriter out = null;
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            out = new BufferedWriter(new OutputStreamWriter(fos, destEncoding));

            out.write(content);
            out.flush();
        } catch (FileNotFoundException fe) {
            logger.error("Error", fe);
            throw fe;
        } catch (IOException e) {
            logger.error("Error", e);
            throw e;
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException ex) {}
        }
    }
}
