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
    <jsp:include page="adminoptions.html"/> 
	</div>
</div>
<div id="page">
  <!-- end #content -->
<div id="sidebar">
		<div id="news" class="boxed1">
			<blockquote>
			  <blockquote>
			    <h2 class="title">Modify Student</h2>
		      </blockquote>
		  </blockquote>
	  </div>
   <FORM action="./DeleteMember" name="deletemember" method="post">
 <table width="255" height="74" align="center">
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
            ResultSet rs=st.executeQuery("select distinct reg_id from lms_registration order by reg_id asc");
     %><td width="76"><span class="style1">Student ID</span></td>
<TD width="142"><select name="stuid">
             
                <option>--select--</option>
       <%
                
           while(rs.next())
           { 
	  %>
             <option value="<%=rs.getString(1)%>"><%=rs.getString(1)%></option>
      <%
            }
      %></select>
     </TD>
     </tr>
      <%
          }
   
         catch(Exception e)
         {
             e.printStackTrace();
         }
          
      %>                    
     <tr></tr>
     <tr></tr>
     <tr>
     <td align="center" colspan="2"><br><input type="submit" value="Delete Member"/></td>
     </tr>
     </table>
</FORM>
  </div>
<!-- end #sidebar -->
</div>
<!-- end #page -->
<div id="footer">
	<p>&nbsp;</p>
</div>
</body>
</html>
