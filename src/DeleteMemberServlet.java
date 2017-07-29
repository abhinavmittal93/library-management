import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

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
public class DeleteMemberServlet extends HttpServlet {

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
        String target="AdminHome.jsp?status=Member Deletion Is Invalid";
        HttpSession session=request.getSession();
        try
		{
        	//System.out.println("request.getParameter(isbn)"+request.getParameter("isbn"));
            Class.forName(driver);
            con=DriverManager.getConnection(url,dname,dpass);
            con.setAutoCommit(false);
		    response.setContentType("text/html");
		    PrintWriter out = response.getWriter();
		    String stuid=request.getParameter("stuid");
		    //Statement st=con.createStatement();
		    //Statement st1=con.createStatement();
		    PreparedStatement pst=con.prepareStatement("delete from lms_registration where reg_id=?");
		    pst.setString(1,stuid );
		    PreparedStatement pst1=con.prepareStatement("delete from lms_login where user_id=");
		    pst1.setString(1,stuid );
		   // int i=pst.executeUpdate("delete from lms_registration where reg_id='"+stuid+"'");
		    //int j=pst1.executeUpdate("delete from lms_login where user_id='"+stuid+"'");
		    
		    int i=pst.executeUpdate();
		    int j=pst1.executeUpdate();
		    if(i>0 && j>0)
		    {
		    	//con.commit();
				target="AdminHome.jsp?status=Member Deletion Success";
		    }
			else
			{
				//con.rollback();
				target="AdminHome.jsp?status=Member Deletion Failed";
			}
		}
        catch(Exception e)
		{
		   e.printStackTrace();
		   target="AdminHome.jsp?status=Member Deletion Failed";
		}
        
        finally{
        	try{
        		if(con!=null)
        		con.close();
        	}catch(Exception e){e.printStackTrace();}
        	
        	
        }
        
        
       response.sendRedirect(target);
	}

}
