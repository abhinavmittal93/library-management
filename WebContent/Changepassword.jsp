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
<style type="text/css">
<!--
.style7 {color: #000000; font-weight: bold; }
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
   <%
   if(request.getParameter("role").equals("admin"))
   {%>
 <jsp:include page="adminoptions.html"/> 
 <%}
 else if(request.getParameter("role").equals("user"))
   {%>
 <jsp:include page="useroptions.html"/> 
 <%}
 else if(request.getParameter("role").equals("student")){
 %>
 <jsp:include page="studentoptions.html"/>
 <%
 }%>
	</div>
</div>
<div id="page">
  <!-- end #content -->
<div id="sidebar">
		<div id="news" class="boxed1">
			<blockquote>
			  <blockquote>
			    <h2 class="title">Change Password</h2>
		      </blockquote>
		  </blockquote>
	  </div>
    <FORM action="changepass" name="renewbook" method="post">
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
            ResultSet rs=st.executeQuery("select reg_id from lms_registration");
            %>
            
            <td width="124"><span class="style7">Student ID</span></td>
<TD width="150">

<%if(session.getAttribute("userid")!=null)
 {%>
<input type="text" name="stuid" value="<%= (String)session.getAttribute("userid") %>" readonly/>
<%} %>


</TD>
     </tr><tr><td><span class="style7">Old Password</span></td>
<%if(session.getAttribute("userid")!=null)
                {
                     rs=st.executeQuery("select password from lms_login where user_id='"+(String)session.getAttribute("userid")+"'");
       				if(rs.next())
       				  {%>
       				 <td> <input type="text" name="oldpass" value="<%=rs.getString(1)%>" readonly/></td>
       				  <% 
                 }}
        else
        {%>
       				 <td width="142"><input type="password" name="oldpass" readonly/></td>
       				  <%
        }
        %>
        </tr>
        <tr>
        <TD><span class="style7">New Password</span></TD>
        <td><input type="password" name="newpass"/></td></tr>
        <tr>
        <td align="center" colspan="2">
        <input type="submit" value="Change"/></td>
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
