import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Created on May 12, 2004
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
public class AddBookServlet extends HttpServlet {

	String driver;
	String url;
	String dname;
	String dpass;
	Connection con;

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
		String target = "AdminHome.jsp?status=Book%20Insertion%20Failed";
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, dname, dpass);
			con.setAutoCommit(false); // Set auto commit as false.

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String isbn = request.getParameter("isbn");
			String bookid = request.getParameter("bookid");
			String title = request.getParameter("title");
			String author = request.getParameter("author");
			int bookprice = Integer.parseInt(request.getParameter("bookprice"));
			PreparedStatement pst = con.prepareStatement("insert into lms_books values(?,?,?,?,?,?)");
			pst.setString(1, isbn);
			pst.setString(2, bookid);
			pst.setString(3, title);
			pst.setString(4, author);
			pst.setString(5, "Available");
			pst.setInt(6, bookprice);
			int i = pst.executeUpdate();
			//con.commit();
			Statement st = con.createStatement();

			ResultSet rs = st
					.executeQuery("select user_id from lms_login where role='user'");
			// pst.executeUpdate("insert into lms_mails
			// values(msg_seg.nextval,?,'The
			// bookid-"+request.getParameter("bookid")+" is Newly Added into the
			// Catalog')");
			while (rs.next()) {
				st.executeUpdate("insert into lms_mails values(msg_seg.nextval,'"+ rs.getString(1) + "','The bookid-" + request.getParameter("bookid")
						+ " is Newly Added into the Catalog')");
				// pst.setString(1,rs.getString(1));
				// pst.addBatch();
				con.commit(); // If there is no error.
			}
			// pst.executeBatch();

			if (i > 0)
				target = "AdminHome.jsp?status=Book%20Inserted";
			else
				target = "AdminHome.jsp?status=Book%20Insertion%20Failed";

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			target = "AdminHome.jsp?status=Book%20Insertion%20Failed";
		}
		response.sendRedirect(target);
	}

}
