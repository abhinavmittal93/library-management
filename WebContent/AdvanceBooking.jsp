<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
String userName = null;
Cookie[] cookies = request.getCookies();
if(cookies !=null){
for(Cookie cookie : cookies){
	if(cookie.getName().equals("user")) userName = cookie.getValue();
}
}
if(userName == null) response.sendRedirect("Login.jsp");;
%>
<%@ page language="java" import="java.util.*,java.sql.*" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Digital Library System</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="default.css" rel="stylesheet" type="text/css" />
<!--  -->
<style type="text/css">
<!--
.style1 {
	color: #000000;
	font-weight: bold;
}
-->
</style>
</head>
<body>
<div id="header">
	<div id="logo">
		<h1><jsp:include page="header.html"/> 
	  </h1>
		
  </div>
	<div id="menu">
       <jsp:include page="studentoptions.html"/> 
	</div>
</div>
<div id="page">
  <!-- end #content -->
<div id="sidebar">
		<div id="news" class="boxed1">
			<blockquote>
			  <blockquote>
			    <h2 class="title">Advanace Booking</h2>
		      </blockquote>
		  </blockquote>
	  </div>
    <FORM action="advancebook" name="advancebook" method="post" onSubmit="return fun()">
 <table border="0" align="center">
   <tr>
     <%
        ServletContext ctx=getServletContext();
        String driver=ctx.getInitParameter("driver");
        String url=ctx.getInitParameter("url");
        String dname=ctx.getInitParameter("dname");
        String dpass=ctx.getInitParameter("dpass");
        try
		{
            Class.forName(driver);
            Connection con=DriverManager.getConnection(url,dname,dpass);
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select isbn_no,category from lms_isbn");
     %><td width="90" height="31"><span class="style1">ISBN No.</span></td>
     <TD width="160"><select name="isbn" onChange="change()">
     <%
                if(session.getAttribute("isbnno")!=null)
                {
     %>
               <option value="<%=(String)session.getAttribute("isbnno")%>"><%=(String)session.getAttribute("isbnno")%></option>
     <%       }
                else
                {
     %>
                <option>--select--</option>
     <%
                }
           while(rs.next())
           { 
	 %>
             <option value=<%=rs.getString(1)%>><%=rs.getString(1)%>-<%=rs.getString(2)%></option>
     <%
            }
     %>
     </select></TD>
     </tr>
     <tr>
     <td height="32"><span class="style1">Book ID</span></td>
     <TD><select name="bookid">
     <%        System.out.println((String)session.getAttribute("isbnno"));
               System.out.println("(String)session.getAttribute(userid)"+(String)application.getAttribute("userid"));
                
                if(session.getAttribute("isbnno")!=null)
                {
                     System.out.println("values are going to be shown here");
                     System.out.println("select bo.book_id from lms_books_issue bo where bo.stu_id!='"+(String)application.getAttribute("userid")+"' and  bo.isbn_no='"+(String)session.getAttribute("isbnno")+"' and bo.book_id not in (select distinct(book_id) from lms_booking where stu_id='"+(String)application.getAttribute("userid")+"')");
                     
//                     System.out.println("select bo.book_id from lms_books_issue bo where bo.stu_id!='"+(String)application.getAttribute("userid")+"' and  bo.isbn_no='"+(String)session.getAttribute("isbnno")+"' and bo.stu_id not in(select stu_id from lms_booking) and bo.book_id not in (select book_id from lms_booking)");
                     rs=st.executeQuery("select bo.book_id from lms_books_issue bo where bo.stu_id!='"+(String)application.getAttribute("userid")+"' and  bo.isbn_no='"+(String)session.getAttribute("isbnno")+"' and bo.book_id not in (select distinct(book_id) from lms_booking where stu_id='"+(String)application.getAttribute("userid")+"')");   				 
           			//rs=st.executeQuery("select book_id from lms_books where isbn_no='"+(String)session.getAttribute("isbnno")+"' and status='Issued'");
           			 String bookid="";
           			 while(rs.next())
           			 {
                        bookid=rs.getString(1);
     %>
           				<option value=<%=bookid%>><%=bookid%></option>
     <%             
        			    }
                }
     %>
     </select></TD>
     </tr><tr><td height="32"><span class="style1">Student ID</span></td>
     <td><input type="text" name="stuid" value="<%=(String)session.getAttribute("userid")%>" readonly/></td>
     </tr>
     
     <tr>
     <td colspan="2" align="center"> <div align="center"><input type="submit" value="Advance Book"/>      
        &nbsp; <input type="reset" value="Clear"/>
         </div></td>
     </tr>
     </table>
    </FORM>
     <%
     }
     catch(Exception e)
     {
         e.printStackTrace();
     }
     %>
  </div>
<!-- end #sidebar -->
</div>
<!-- end #page -->
<div id="footer">
	<p>&nbsp;</p>
</div>
</body>
</html>
