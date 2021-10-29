package tool;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.servlet.http.*;
import javax.servlet.*; 
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Servlet implementation class Sendmail
 */
@WebServlet("/SendMail")
public class SendMail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMail() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String name=request.getParameter("name");
		name = toUTF8(name);  
		String email_acount=request.getParameter("email");
		email_acount=toUTF8(email_acount);
		String message=request.getParameter("message");
		message = toUTF8(message);
		String result;
		String send_message="name:"+name+"\n"+"email:"+email_acount+"\n"+"message:"+message;
		System.out.println(send_message);
		HtmlEmail email = new HtmlEmail();

		try {

		    // 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
		    email.setHostName("smtp.qq.com");
			email.setSmtpPort(80);
			email.setSSLOnConnect(true);

			// 字符编码集的设置
		    email.setCharset("utf-8");
		    // 收件人的邮箱
//		    email.addTo("1109837660@qq.com");
			email.addTo("1109837660@qq.com");
		    // 发送人的邮箱2
		    email.setFrom("1109837660@qq.com", "1109837660@qq.com");
//			email.setCc("954839106@qq.com");
		    // 如果需要认证信息的话，设置认证：用户名-密码     ***是你开启POP3服务时的授权码，不是登录密码
		    email.setAuthentication("1109837660@qq.com", "pgqbljvjxjkrjeee");
		    // 要发送的邮件主题
		    // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
		    email.setMsg(send_message);
		    // 发送
		    email.send();


		    System.out.println("sending messages successfully");
		    result = "Sent message successfully....";
		} catch (EmailException e) {
		    e.printStackTrace();
		    System.out.println("sending messages failure");
		    result = "Error: unable to send message....";
		}
		request.setAttribute("message", result);
		RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
		rd.forward(request, response);
	}
	

	public static String toUTF8(String str) {
		if (isEmpty(str)) {
			return "";
		}
		try {
			if (str.equals(new String(str.getBytes("GB2312"), "GB2312"))) {
				str = new String(str.getBytes("GB2312"), "utf-8");
				return str;
			}
		} catch (Exception exception) {
		}
		try {
			if (str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
				str = new String(str.getBytes("ISO-8859-1"), "utf-8");
				return str;
			}
		} catch (Exception exception1) {
		}
		try {
			if (str.equals(new String(str.getBytes("GBK"), "GBK"))) {
				str = new String(str.getBytes("GBK"), "utf-8");
				return str;
			}
		} catch (Exception exception3) {
		}
		return str;
	}

	public static boolean isEmpty(String str) {
		// 如果字符串不为null，去除空格后值不与空字符串相等的话，证明字符串有实质性的内容
		if (str != null && !str.trim().isEmpty()) {
			return false;// 不为空
		}
		return true;// 为空
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}


