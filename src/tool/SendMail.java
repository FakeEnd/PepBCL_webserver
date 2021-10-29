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

		    // ������SMTP���ͷ����������֣�163�����£�"smtp.163.com"
		    email.setHostName("smtp.qq.com");
			email.setSmtpPort(80);
			email.setSSLOnConnect(true);

			// �ַ����뼯������
		    email.setCharset("utf-8");
		    // �ռ��˵�����
//		    email.addTo("1109837660@qq.com");
			email.addTo("1109837660@qq.com");
		    // �����˵�����2
		    email.setFrom("1109837660@qq.com", "1109837660@qq.com");
//			email.setCc("954839106@qq.com");
		    // �����Ҫ��֤��Ϣ�Ļ���������֤���û���-����     ***���㿪��POP3����ʱ����Ȩ�룬���ǵ�¼����
		    email.setAuthentication("1109837660@qq.com", "pgqbljvjxjkrjeee");
		    // Ҫ���͵��ʼ�����
		    // Ҫ���͵���Ϣ������ʹ����HtmlEmail���������ʼ�������ʹ��HTML��ǩ
		    email.setMsg(send_message);
		    // ����
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
		// ����ַ�����Ϊnull��ȥ���ո��ֵ������ַ�����ȵĻ���֤���ַ�����ʵ���Ե�����
		if (str != null && !str.trim().isEmpty()) {
			return false;// ��Ϊ��
		}
		return true;// Ϊ��
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}


