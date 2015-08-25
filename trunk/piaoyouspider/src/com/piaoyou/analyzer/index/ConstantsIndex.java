package com.piaoyou.analyzer.index;

/**
 * @author 作者 <a href="mailto:xiangwanli@yahoo.comcn">wanli</a>
 * @version 创建时间：Jun 19, 2011 2:09:26 PM
 * 类说明:数据库表对应索引名称。
 * 常量命名的规则 表名_index_path  目录的规则
 *
 */
public class ConstantsIndex {

//   public static String INDEX_ROOT="d://";
  // public static String INDEX_ROOT="opt/root/";

   public static String INDEX_ROOT="/opt/";
   
   public static String SHOWiNFO_IDPATH=ConstantsIndex.INDEX_ROOT+"data/tableIndex/showInfo/showInfo_storeId.txt";
   public static String SHOWINFO_INDEX_PATH=ConstantsIndex.INDEX_ROOT+"data/tableIndex/showInfo/showInfo_index";
   public static String showInfoStar_index_path =ConstantsIndex.INDEX_ROOT+"data/tableIndex/InfoStar/showInfo_index"; 
 

   //询结果高亮格式设置前缀
   public static String HighLighterHTMLFormatter_prefix = "<font color='red'><b>";
   //询结果高亮格式设置后缀
   public static String HighLighterHTMLFormatter_Suffix = "</b></font>";
   
   //创建二手票所需要的演出索引设置
   public static String showinfo_usedTicket_idPath = "D:/data/tableIndex/showInfo/showInfo_usedTicket_storeId.txt";
   public static String showInfo_uesdTicket_index_path = "D:/data/tableIndex/showInfo/showInfo_usedTicket_index";
   
   public static String SHOWINFO_USEDTICKET_IDPATH=ConstantsIndex.INDEX_ROOT+"data/tableIndex/showInfo/showInfo_usedTicket_storeId.txt";
   public static String SHOWINFO_USEDTICKET_INDEX_PATH=ConstantsIndex.INDEX_ROOT+"data/tableIndex/showInfo/showInfo_usedTicket_index";
  
  

}
