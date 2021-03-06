import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*
 * Created on May 12, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ModifyBookServlet extends HttpServlet {

	String driver;
	String url;
	String dname;
	String dpass;
	Connection con;
	
	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext ctx=getServletContext();
        driver=ctx.getInitParameter("driver");
        url=ctx.getInitParameter("url");
        dname=ctx.getInitParameter("dname");
        dpass=ctx.getInitParameter("dpass");
        String target="AdminHome.jsp?status=Book%20Modification%20Failed";
        GetBooksBean bean;
        Vector v;
        HttpSession session=request.getSession();
        try
		{
        	System.out.println("request.getParameter(isbn)"+request.getParameter("isbn"));
            Class.forName(driver);
            con=DriverManager.getConnection(url,dname,dpass);
		    response.setContentType("text/html");
		    PrintWriter out = response.getWriter();
		    String isbn=request.getParameter("isbn");
		    PreparedStatement pst=con.prepareStatement("update lms_books set book_title=?,book_author=?,status='Available' where isbn_no=? and book_id=?");
		    pst.setString(1,request.getParameter("title"));
		    pst.setString(2,request.getParameter("author"));
		    pst.setString(3,request.getParameter("isbn"));
		    pst.setString(4,request.getParameter("bookid"));
		    int i=pst.executeUpdate();
		    if(i>0)
				target="AdminHome.jsp?status=Book%20Modification%20Success";
			else
				target="AdminHome.jsp?status=Book%20Modification%20Failed";
		}
        catch(Exception e)
		{
        	e.printStackTrace();
        	target="AdminHome.jsp?status=Book%20Modification%20Failed";
		}
        response.sendRedirect(target);
	}

}
