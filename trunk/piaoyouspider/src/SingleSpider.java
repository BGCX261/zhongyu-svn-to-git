import com.piaoyou.boot.SpiderRunner;
import com.piaoyou.dao.DataDao;


public class SingleSpider {
	
	public static void main(String[] args) {
		DataDao dao = new DataDao();
		for(int i=0;i<args.length;i++){
			SpiderRunner runner = new SpiderRunner("com.piaoyou.crawler.spider."+args[i], dao);
			runner.run();
		}
	}

}
