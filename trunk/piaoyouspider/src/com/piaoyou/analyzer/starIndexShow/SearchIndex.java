package com.piaoyou.analyzer.starIndexShow;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import com.piaoyou.analyzer.index.ConstantsIndex;
import com.piaoyou.util.PageBean;

public class SearchIndex {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SearchIndex a=new SearchIndex();
		PageBean page=new PageBean();
		page.setPageIndex("1");
		page.setPageCount("8");
		a.GetShowByStarName("陈奕迅" ,1,page);
		System.out.println(a.getShowListTotal("张学友" ,1));
	}
	/*
	 * type=0 表示近期演出
	 * type=1 表示历史演出
	 * 
	 */
	public   List<StarIndexShow> GetShowByStarName(String name,int type,PageBean page){
		  List<StarIndexShow> list=new ArrayList<StarIndexShow>();
		  String  indexPath =ConstantsIndex.showInfoStar_index_path;
		  int pageIndex=Integer.parseInt(page.pageIndex);
		  int pageCount=Integer.parseInt(page.pageCount);
		  try{
			  IndexSearcher searcher = new IndexSearcher(indexPath);
			  Term term1 = new Term("showInfo",name);   
			  Term term2 = new Term("showName",name);
			  Term term3 =null;
			  BooleanQuery booleanQuery13 = new BooleanQuery();
			  BooleanQuery booleanQuery23 = new BooleanQuery();
			  if(type==2){
			     term3 = new Term("status",String.valueOf(type)); 
			     Query q3 = new TermQuery(term3);
			     booleanQuery13.add(q3,BooleanClause.Occur.MUST);
			     booleanQuery23.add(q3,BooleanClause.Occur.MUST);
			  }
			  else{
				  term3 = new Term("status",String.valueOf(2)); 
				  Query q3 = new TermQuery(term3);
				  booleanQuery13.add(q3,BooleanClause.Occur.MUST_NOT);
				  booleanQuery23.add(q3,BooleanClause.Occur.MUST_NOT);
			  }
			  Query q1 = new TermQuery(term1);   
			  Query q2 = new TermQuery(term2);
		      booleanQuery13.add(q1, BooleanClause.Occur.MUST); 
		      booleanQuery23.add(q2, BooleanClause.Occur.MUST); 
		      BooleanQuery booleanQuery = new BooleanQuery();
		      booleanQuery.add(booleanQuery13, BooleanClause.Occur.SHOULD);
		      booleanQuery.add(booleanQuery23, BooleanClause.Occur.SHOULD);
		      Hits hits=searcher.search(booleanQuery);
		      int length=hits.length();
		      int start=(pageIndex-1)*pageCount;
		      for(int i=start;i<((start+pageCount)<length?(start+pageCount):length);i++){
		    	  Document doc=hits.doc(i);
		    	  String siteName=doc.getField("siteName").stringValue();
		    	  String showName=doc.getField("showName").stringValue();
		    	  String showID=doc.getField("showId").stringValue();
		    	  String showTime=doc.getField("showTime").stringValue();
		    	  String showTypeName=doc.getField("showType").stringValue();
		    	  String cityName=doc.getField("cityName").stringValue();
		    	  StarIndexShow show=new StarIndexShow();
		    	  show.setSiteName(siteName);
		    	  show.setShowName(showName);
		    	  show.setShowID(showID);
		    	  show.setShowTime(showTime);
		    	  show.setCityName(cityName);
		    	  show.setShowTypeName(showTypeName);
		    	  list.add(show);
		      }
		  }
		  catch(Exception ee ){
		  }
          return list;
	}
	public int getShowListTotal(String name,int type){
		  int size=0;
		  String  indexPath =ConstantsIndex.showInfoStar_index_path;
		  try{
			  IndexSearcher searcher = new IndexSearcher(indexPath);
			  Term term1 = new Term("showInfo",name);   
			  Term term2 = new Term("showName",name); 
			  Term term3=null;
			  BooleanQuery booleanQuery13 = new BooleanQuery();
			  BooleanQuery booleanQuery23 = new BooleanQuery();
			  if(type==2){
			     term3 = new Term("status",String.valueOf(type)); 
			     Query q3 = new TermQuery(term3);
			     booleanQuery13.add(q3,BooleanClause.Occur.MUST);
			     booleanQuery23.add(q3,BooleanClause.Occur.MUST);
			  }
			  else{
				  term3 = new Term("status",String.valueOf(2)); 
				  Query q3 = new TermQuery(term3);
				  booleanQuery13.add(q3,BooleanClause.Occur.MUST_NOT);
				  booleanQuery23.add(q3,BooleanClause.Occur.MUST_NOT);
			  }
			  Query q1 = new TermQuery(term1);   
			  Query q2 = new TermQuery(term2);
		      booleanQuery13.add(q1, BooleanClause.Occur.MUST); 
		      booleanQuery23.add(q2, BooleanClause.Occur.MUST); 
		      BooleanQuery booleanQuery = new BooleanQuery();
		      booleanQuery.add(booleanQuery13, BooleanClause.Occur.SHOULD);
		      booleanQuery.add(booleanQuery23, BooleanClause.Occur.SHOULD);
		      Hits hits= searcher.search(booleanQuery);
		      size=hits.length();
		  }
		  catch(Exception ee ){
			  ee.printStackTrace();
		  }
		return size;
	}
}
