package com.piaoyou.analyzer.index;

import java.util.List;
/**
 * @author 作者 <a href="mailto:xiangwanli@yahoo.comcn">wanli</a>
 * @version 创建时间：Jun 19, 2011 2:09:26 PM
 * 类说明:创建索引类接口。
 *
 */
public interface ICreateIndex {
	/*
	 * 创建索引的单元
	 */
	public void CreateConfigDocAndIndex(String indexPath,List<IShow> showList,boolean isIncrement);
}
