package com.piaoyou.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.piaoyou.domain.User;

public class CommonMailService {
	private static final String URL = "http://127.0.0.1/pyh";
	//private static final String URL = "http://192.168.0.253/pyh";
	//private static final String URL = "http://www.piaoyouhui.com";
	private static final SimpleDateFormat FD = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	private static final SimpleDateFormat FD_CN = new SimpleDateFormat("yyyy年MM月dd日");
	private static final String LOGO = URL+"/public/images/register/emtipimg.jpg";
	
	private SendMail sm = new SendMail();
	private String subject;
	private String content;
	private String to;
	/**
	 * 发送验证新用户的邮箱连接
	 * @param user 验证的用户
	 * @return
	 */
	public boolean sendLinkForActivateNewUser(User user,String inviterId) {
		StringBuffer sb = new StringBuffer();
		String link = "ue="+TransferDES.encrypt(user.getUser_email())+"&up="+user.getUser_password();
		this.subject = "美娱美乐帐号激活";
		this.to = user.getUser_email();
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
		sb.append("<head>");
		sb.append("	<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />");
		sb.append("	<title>美娱美乐帐号激活</title>");
		sb.append("	<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"favicon.ico\" />");
		sb.append("	<style type=\"text/css\">");
		sb.append("	body{margin:0 auto;text-align:center;font-size:100%;font-family:'宋体';}");
		sb.append("#emtipscon{margin:86px auto 0 auto;padding:104px 40px 40px 40px;width:590px;height:300px;border:5px solid #d5d7db;text-align:left;background:#fff url("+LOGO+") center top no-repeat;color:#010101;}");
		sb.append(".thanks{padding:20px 0 12px 0;font-size:0.875em;font-weight:bold;}");
		sb.append(".dlming{margin-bottom:34px;font-size:0.875em;}");
		sb.append(".dlming span{color:#f92300;}");
		sb.append(".djjh{font-size:0.875em;}");
		sb.append("#linkjh{margin-bottom:28px;margin-top:28px;font-size:0.875em;}");
		sb.append("	#linkjh a{display:block;width:588px;color:#315f81;word-wrap:break-word;}");
		sb.append("#qtts{font-size:0.75em;color:#7c7c7c;}");
		sb.append("#qtts p{margin-bottom:15px;}");
		sb.append(".tdtix{font-size:0.75em;color:#7c7c7c;margin-top:20px;}");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div id=\"emtipscon\">");
		sb.append("<p class=\"thanks\">感谢您注册美娱美乐！</p>");
		sb.append("<p class=\"dlming\">你的登录名为：<span>"+this.to+"</span></p>");
		sb.append("<p class=\"djjh\">请点击以下链接，完成帐号激活！</p>");
		if(inviterId==null||inviterId.equals("")) {
			sb.append("<p id=\"linkjh\"><a href=\""+URL+"/user/activateNewUser.do?"+link+"\">"+URL+"/user/activateNewUser.do?"+link+"</a></p>");
		} else {
			inviterId = TransferDES.encrypt(inviterId);
			sb.append("<p id=\"linkjh\"><a href=\""+URL+"/user/activateNewUser.do?"+link+"&inviterId="+inviterId+"\">"+URL+"/user/activateNewUser.do?"+link+"&inviterId="+inviterId+"</a></p>");
		}
		sb.append("	<div id=\"qtts\">");
		sb.append("	<p>如果无法打开,请将链接复制到浏览器的地址栏中进行激活。</p>");
		sb.append("</div>");
		sb.append("	<p class=\"tdtix\">这只是一封系统自动发出的邮件，请不要直接回复。</p>");
		sb.append("	</div>");
		sb.append("</body>");
		sb.append("</html>");
		this.content = sb.toString();
		return this.sendMail();
	}
	
	public boolean sendLinkForInvitaion(String id,String name,String target) {
		StringBuffer sb = new StringBuffer();
		String link = "inviterId="+TransferDES.encrypt(id);
		this.subject = "美娱美乐用户邀请";
		this.to = target;
		if(name!=null&&name.trim().equals("")) {
			name = null;
		}
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
		sb.append("<head>");
		sb.append("	<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />");
		sb.append("	<title>美娱美乐用户邀请</title>");
		sb.append("	<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"favicon.ico\" />");
		sb.append("	<style type=\"text/css\">");
		sb.append("	body{margin:0 auto;text-align:center;font-size:100%;font-family:'宋体';}");
		sb.append("#emtipscon{margin:86px auto 0 auto;padding:104px 40px 40px 40px;width:590px;height:300px;border:5px solid #d5d7db;text-align:left;background:#fff url("+LOGO+") center top no-repeat;color:#010101;}");
		sb.append(".thanks{padding:20px 0 12px 0;font-size:0.875em;font-weight:bold;}");
		sb.append(".dlming{margin-bottom:34px;font-size:0.875em;}");
		sb.append(".dlming span{color:#f92300;}");
		sb.append(".djjh{font-size:0.875em;}");
		sb.append("#linkjh{margin-bottom:28px;margin-top:28px;font-size:0.875em;}");
		sb.append("	#linkjh a{display:block;width:588px;color:#315f81;word-wrap:break-word;}");
		sb.append("#qtts{font-size:0.75em;color:#7c7c7c;}");
		sb.append("#qtts p{margin-bottom:15px;}");
		sb.append(".tdtix{font-size:0.75em;color:#7c7c7c;margin-top:20px;}");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div id=\"emtipscon\">");
		sb.append("<p><br /><span>"+(name==null?"有人":name)+"</span>邀请您加入美娱美乐<br /><br />");
		sb.append("<p class=\"djjh\">我已加入美娱美乐网(www.lecc.cc)在这里有：中国娱乐前沿最新演出资讯，最酷最热的演唱会，<br />");
		sb.append("最幽默的话剧，最唯美的音乐会，犀利的剧评，国内第一演艺娱乐平台，期待与你一起分享高端娱乐生活！<br /></p>");
		sb.append("<p>你也来看看吧！点击进入：</p>");
		sb.append("<p id=\"linkjh\"><a href=\""+URL+"/user/doInviteFriends.do?"+link+"\">"+URL+"/user/doInviteFriends.do?"+link+"</a></p>");
		sb.append("	<div id=\"qtts\">");
		sb.append("	<p>如果无法打开,请将链接复制到浏览器的地址栏中进行激活。</p>");
		sb.append("</div>");
		sb.append("	<p class=\"tdtix\">这只是一封系统自动发出的邮件，请不要直接回复。</p>");
		sb.append("	</div>");
		sb.append("</body>");
		sb.append("</html>");
		this.content = sb.toString();
		return this.sendMail();
	}
	/**
	 * This method is used to validate the safe email whether the email is existence.
	 * @param user
	 * @return
	 */
	public boolean validateUserSafeEmail(User user) {
		StringBuffer sb = new StringBuffer();
		String link = "ue="+TransferDES.encrypt(user.getUser_email())+"&up="+user.getUser_password()+"&se="+TransferDES.encrypt(user.getSafe_email());
		this.subject = "美娱美乐安全邮箱验证";
		this.to = user.getSafe_email().substring(1);
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
		sb.append("<head>");
		sb.append("	<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />");
		sb.append("	<title>美娱美乐安全邮箱验证</title>");
		sb.append("	<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"favicon.ico\" />");
		sb.append("	<style type=\"text/css\">");
		sb.append("	body{margin:0 auto;text-align:center;font-size:100%;font-family:'宋体';}");
		sb.append("#emtipscon{margin:86px auto 0 auto;padding:104px 40px 40px 40px;width:590px;height:300px;border:5px solid #d5d7db;text-align:left;background:#fff url("+LOGO+") center top no-repeat;color:#010101;}");
		sb.append(".thanks{padding:20px 0 12px 0;font-size:0.875em;font-weight:bold;}");
		sb.append(".dlming{margin-bottom:34px;font-size:0.875em;}");
		sb.append(".dlming span{color:#f92300;}");
		sb.append(".djjh{font-size:0.875em;}");
		sb.append("#linkjh{margin-bottom:28px;margin-top:28px;font-size:0.875em;}");
		sb.append("	#linkjh a{display:block;width:588px;color:#315f81;word-wrap:break-word;}");
		sb.append("#qtts{font-size:0.75em;color:#7c7c7c;}");
		sb.append("#qtts p{margin-bottom:15px;}");
		sb.append(".tdtix{font-size:0.75em;color:#7c7c7c;margin-top:20px;}");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div id=\"emtipscon\">");
		sb.append("<p class=\"thanks\">"+user.getUser_nick()+"，您好！</p>");
		sb.append("<p class=\"djjh\">您已设置了安全邮箱。请点击以下链接，验证您的安全邮箱！</p>");
		sb.append("<p id=\"linkjh\"><a href=\""+URL+"/user/validateUserSafeEmail.do?"+link+"\">"+URL+"/user/validateUserSafeEmail.do?"+link+"</a></p>");
		sb.append("	<div id=\"qtts\">");
		sb.append("	<p>如果无法打开,请将链接复制到浏览器的地址栏中进行激活。</p>");
		sb.append("</div>");
		sb.append("	<p class=\"tdtix\">这只是一封系统自动发出的邮件，请不要直接回复。</p>");
		sb.append("	</div>");
		sb.append("</body>");
		sb.append("</html>");
		this.content = sb.toString();
		return this.sendMail();
	}
	
	public boolean sendLinkForFindPassword(User user) {
		String link = "ue="+TransferDES.encrypt(user.getUser_email())+"&up="+user.getUser_password();
		this.subject = "美娱美乐用户密码重置";
		//this.to = user.getUser_email();
		this.to = user.getSafe_email();
		//if the user didn't set safe email,then the default email is the email of register. 
		if(this.to==null||this.to.startsWith("#")) {
			this.to = user.getUser_email();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
		sb.append("<head>");
		sb.append("	<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />");
		sb.append("	<title>美娱美乐用户密码重置</title>");
		sb.append("	<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"favicon.ico\" />");
		sb.append("	<style type=\"text/css\">");
		sb.append("	body{margin:0 auto;text-align:center;font-size:100%;font-family:'宋体';}");
		sb.append("#emtipscon{margin:86px auto 0 auto;padding:104px 40px 40px 40px;width:590px;height:300px;border:5px solid #d5d7db;text-align:left;background:#fff url("+LOGO+") center top no-repeat;color:#010101;}");
		sb.append(".thanks{padding:20px 0 12px 0;font-size:0.875em;font-weight:bold;}");
		sb.append(".dlming{margin-bottom:34px;font-size:0.875em;}");
		sb.append(".dlming span{color:#f92300;}");
		sb.append(".djjh{font-size:0.875em;}");
		sb.append("#linkjh{margin-bottom:28px;margin-top:28px;font-size:0.875em;}");
		sb.append("	#linkjh a{display:block;width:588px;color:#315f81;word-wrap:break-word;}");
		sb.append("#qtts{font-size:0.75em;color:#7c7c7c;}");
		sb.append("#qtts p{margin-bottom:15px;}");
		sb.append(".tdtix{font-size:0.75em;color:#7c7c7c;margin-top:20px;}");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div id=\"emtipscon\">");
		sb.append("<p class=\"thanks\">"+user.getUser_nick()+"，您好！</p>");
		sb.append("<p class=\"djjh\">您提交了找回密码的申请，请点击以下链接重置你的登录密码：</p>");
		sb.append("<p id=\"linkjh\"><a href=\""+URL+"/user/resetUserPassword.do?"+link+"\">"+URL+"/user/resetUserPassword.do?"+link+"</a></p>");
		sb.append("	<div id=\"qtts\">");
		sb.append("<p>以上链接将在点击后会立即失效。如你未提交该申请或未注册票友会，请不要理会此邮件，对此为你带来的不便深表歉意。</p>");
		sb.append("	<p>如果无法打开,请将链接复制到浏览器的地址栏中进行激活。</p>");
		sb.append("</div>");
		sb.append("	<p class=\"tdtix\">这只是一封系统自动发出的邮件，请不要直接回复。</p>");
		sb.append("	</div>");
		sb.append("</body>");
		sb.append("</html>");
		this.content = sb.toString();
		return this.sendMail();
	}
	
	public boolean sendExample() {
		String content = "<a href='http://www.lecc.cc'>票友会</a><img src='http://www.piaoyouhui.com/public/images/logo.gif'/>";
		this.subject = "测试样例";
		this.to = "chaishu2005@163.com";
		this.content = content;
		return this.sendMail();
	}
	/**
	 * 格式化当前时间
	 * @return 格式化当前的时间
	 */
	public String FormatDate() {
		return FD.format(new java.util.Date());
	}
	/**
	 * 统一发送
	 * @return 是否发送成功
	 */
	private boolean sendMail() {
		this.sm.setSubject(sm.transferChinese(subject));
		this.sm.setTo(to);
		this.sm.setContent(content);
		return this.sm.sendMail();
	}
	public static void main(String[] args) {
		/*UserInfo ui = new UserInfo();
		ui.setUser_mail("346616085@qq.com");
		ui.setUser_password("6d150eef76150fd9");
		AllSendMailForService asmf = new AllSendMailForService();
		asmf.sendLinkForActivateNewAccount(ui);*/
		//User user = new User();
		//user.setUser_email("346616085@qq.com");
		//user.setUser_password("6d150eef76150fd9");
		//user.setUser_type("2");
		CommonMailService asmf = new CommonMailService();
		//asmf.sendLinkForAuth(user);
		asmf.sendLinkForInvitaion("66", "柴曙", "346616085@qq.com");
	}
}
