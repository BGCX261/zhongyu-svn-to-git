package com.piaoyou.util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * <p>Title: 使用javamail发送邮件</p>
 * <p>Description: 演示如何使用javamail包发送电子邮件。这个实例可发送多附件</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Filename: Mail.java</p>
 * @version 1.0
 */
public class NewSendMail {

    private String to = "";//收件人
    private String from = "notice@piaoyouhui.com";//发件人
    private String host = "smtp.ym.163.com";//smtp主机
    private String username = "notice@piaoyouhui.com";
    private String password = "notice";
//    private String from = "qingbao.zhou@pm800.com";//发件人
//    private String host = "smtp.ym.163.com";//smtp主机
//    private String username = "qingbao.zhou@pm800.com";
//    private String password = "18713027205";
    private String filename = "";//附件文件名
    private String prefix = "爬虫通知";
    private String subject = "";//邮件主题
    private Vector file = new Vector();//附件文件集合
    private String content = "";//邮件正文
    /**
     *<br>方法说明：默认构造器
     *<br>输入参数：
     *<br>返回类型：
     */
    public NewSendMail() {
    }

    /**
     *<br>方法说明：构造器，提供直接的参数传入
     *<br>输入参数：
     *<br>返回类型：
     */
    public NewSendMail(String to, String from, String smtpServer, String username, String password, String subject, String content) {
        this.to = to;
        this.from = from;
        this.host = smtpServer;
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.content = content;
    }
    public NewSendMail(String to,String content) {
    	this.to = to;
		subject = transferChinese(prefix);
    	this.content = content;
    }
    /**
     *<br>方法说明：设置邮件服务器地址
     *<br>输入参数：String host 邮件服务器地址名称
     *<br>返回类型：
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     *<br>方法说明：设置登录服务器校验密码
     *<br>输入参数：
     *<br>返回类型：
     */
    public void setPassWord(String pwd) {
        this.password = pwd;
    }

    /**
     *<br>方法说明：设置登录服务器校验用户
     *<br>输入参数：
     *<br>返回类型：
     */
    public void setUserName(String usn) {
        this.username = usn;
    }

    /**
     *<br>方法说明：设置邮件发送目的邮箱
     *<br>输入参数：
     *<br>返回类型：
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     *<br>方法说明：设置邮件发送源邮箱
     *<br>输入参数：
     *<br>返回类型：
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     *<br>方法说明：设置邮件主题
     *<br>输入参数：
     *<br>返回类型：
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     *<br>方法说明：设置邮件内容
     *<br>输入参数：
     *<br>返回类型：
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *<br>方法说明：把主题转换为中文
     *<br>输入参数：String strText
     *<br>返回类型：
     */
    public String transferChinese(String strText) {
        try {
        	  sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();    
              strText="=?utf-8?B?" + enc.encode(strText.getBytes("utf-8")) + "?=";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strText;
    }

    /**
     *<br>方法说明：往附件组合中添加附件
     *<br>输入参数：
     *<br>返回类型：
     */
    public void attachfile(String fname) {
        file.addElement(fname);
    }

    /**
     *<br>方法说明：发送邮件
     *<br>输入参数：
     *<br>返回类型：boolean 成功为true，反之为false
     */
    public boolean sendMail() {
        //构造mail session
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            //构造MimeMessage 并设定基本的值
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);

            //构造Multipart
            Multipart mp = new MimeMultipart();

            //向Multipart添加正文
            MimeBodyPart mbpContent = new MimeBodyPart();
            mbpContent.setText(content);
            //向MimeMessage添加（Multipart代表正文）
            mp.addBodyPart(mbpContent);

            //向Multipart添加附件
            Enumeration efile = file.elements();
            while (efile.hasMoreElements()) {

                MimeBodyPart mbpFile = new MimeBodyPart();
                filename = efile.nextElement().toString();
                FileDataSource fds = new FileDataSource(filename);
                mbpFile.setDataHandler(new DataHandler(fds));
                mbpFile.setFileName(fds.getName());
                //向MimeMessage添加（Multipart代表附件）
                mp.addBodyPart(mbpFile);

            }
            file.removeAllElements();
            //向Multipart添加MimeMessage
            msg.setContent(mp);
            msg.setSentDate(new Date());
            //发送邮件
            Transport.send(msg);

        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     *<br>方法说明：主方法，用于测试
     *<br>输入参数：
     *<br>返回类型：
     */
    public static void main(String[] args) {
        NewSendMail sendMail = new NewSendMail("398885143@qq.com","测试");
       /* sendmail.setHost("smtp.163.com");
        sendmail.setUserName("chaishu2005");
        sendmail.setPassWord("chai1989shu");
        sendmail.setTo("375321153@qq.com");
        sendmail.setFrom("chaishu2005@163.com");
        sendmail.setSubject("柴曙sendMail测试");
        sendmail.setContent("hello  world!柴曙的小测试，收到请回复，谢谢!");*/
        //Mail sendmail = new Mail("dujiang@sricnet.com","du_jiang@sohu.com","smtp.sohu.com","du_jiang","31415926"," 你好","胃，你好吗？");

//  sendmail.attachfile("DND.jar");
//        for (int i = 0; i < 5; i++) {
//            sendmail.attachfile("c://test.txt");
         // sendMail.sendMail();
//        }
        System.out.print("sucess!");
    }
}//end

