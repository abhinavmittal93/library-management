import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Created on May 11, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrator
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class LoginServlet extends HttpServlet {

	String driver;
	String url;
	String dname;
	String dpass;
	Connection con;
	String role = "Login.jsp";
	private String userIDChk="" ;

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext ctx = getServletContext();
		driver = ctx.getInitParameter("driver");
		url = ctx.getInitParameter("url");
		dname = ctx.getInitParameter("dname");
		dpass = ctx.getInitParameter("dpass");
		HttpSession session = request.getSession();

		System.out.println("-------- Oracle JDBC Connection Testing ------");

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, dname, dpass);

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String userid = request.getParameter("login");
			String pwd = request.getParameter("password");
			userIDChk=userid;
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select role from lms_login where user_id='"
							+ userid + "' and password='" + pwd + "'");

			if (rs.next()) {
				role = rs.getString("role");
				if (role.equals("admin"))
					role = "AdminHome.jsp";
				if (role.equals("student")) {
					System.out
							.println("Select return_date-2,sysdate,book_id from lms_books_issue where stu_id='"
									+ userid + "'");
					rs = st.executeQuery("Select return_date-2,sysdate,book_id from lms_books_issue where stu_id='"
							+ userid + "'");
					String bookid = "";
					while (rs.next()) {
						if (rs.getDate(1).equals(rs.getDate(2))) {
							bookid = rs.getString(3);
							System.out.println("bookid" + bookid);
							st.executeUpdate("insert into lms_mails values(msg_seg.nextval,'"
									+ userid
									+ "','Return date of bookid-"
									+ bookid
									+ " will be expire with in to days.')");
						}
					}
					role = "StudentHome.jsp";
				}
				if (role.equals("user"))
					role = "UserHome.jsp";
			} else
				role = "Login.jsp?status=Invalid Username and password";

			session.setAttribute("userid", userid);
			
			System.out.println("(String)session.getAttribute(userid)"
					+ (String) session.getAttribute("userid"));
		} catch (Exception e) {
			e.printStackTrace();
			role = "Login.jsp";
		}
		
		
		if(role!=null){
			Cookie loginCookie = new Cookie("user",userIDChk);
			//setting cookie to expiry in 30 mins
			loginCookie.setMaxAge(30*60);
			response.addCookie(loginCookie);
			response.sendRedirect(role);
		}else{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
			PrintWriter out= response.getWriter();
			out.println("<font color=red>Either user name or password is wrong.</font>");
			rd.include(request, response);
		}
		
	}
}
