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
<!--  -->
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
			    <h2 class="title">Issuing Book</h2>
		      </blockquote>
		  </blockquote>
	  </div>
    <FORM action="issuebook" name="issuebook" method="post" onsubmit="return fun()"> 
 <table align="center">
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
            ResultSet rs=st.executeQuery("select lbi.isbn_no,li.category,lbi.book_id, lb.book_title,lbi.issue_date,lbi.return_date from"+ 
            		                     " lms_books_issue lbi, lms_isbn li, lms_books lb where lbi.isbn_no=li.isbn_no and "+
            		                     " lbi.book_id=lb.book_id and lbi.stu_id='"+(String)session.getAttribute("userid")+"'");
            
     %>
       <td width="111" height="28" bgcolor="#B79A36" class="style1"><div align="center">ISBN No</div></td>
       <td width="111" height="28" bgcolor="#B79A36" class="style1"><div align="center">Category</div></td>
       <td width="111" height="28" bgcolor="#B79A36" class="style1"><div align="center">Book ID</div></td>
       <td width="134" height="28" bgcolor="#B79A36" class="style1"><div align="center">Book Name</div></td>
       <td width="142" height="28" bgcolor="#B79A36" class="style1"><div align="center">Issued Date</div></td>
       <td width="119" height="28" bgcolor="#B79A36" class="style1"><div align="center">Return Date</div></td>
   </tr>
       <%
         while(rs.next())
         {%>
        	 <tr>
        	 <td width="111" height="28" bgcolor="#DDD5B1" class="style1"><%= rs.getString(1)%></td>
             <td width="134" height="28" bgcolor="#DDD5B1" class="style1"><%= rs.getString(2) %></td>
             <td width="142" height="28" bgcolor="#DDD5B1" class="style1"><%= rs.getString(3)%></td>
             <td width="119" height="28" bgcolor="#DDD5B1" class="style1"><%= rs.getString(4)%></td></tr>
         <%
         }
       %>
     </table>
    </FORM>
                <%}
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
