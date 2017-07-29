<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
String userid = (String)session.getAttribute("userid");
Cookie  userCookie = new Cookie("userid", userid);
userCookie. setMaxAge(60*60*24*365);
response.addCookie(userCookie);
if(userid!=null)
{
	response.sendRedirect("LoginServlet");
    return; //the return is important; forces redirect to go now
}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Digital Library System</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="default.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #333333;
	font-weight: bold;
}
.style2 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>
</head>
<body>
<div id="header">
	<div id="logo">
		<h1><jsp:include page="header.html"/> </h1>
		
  </div>
	<div id="menu">
		<jsp:include page="generaloptions.html"/>
	</div>
</div>
<div id="page">
	<div id="content">
		<div id="welcome" class="boxed2">
			<h1 class="title">Welcome to Library!</h1>
			<div class="content">
				<p align="center" class="style2">
				<%
				  if(request.getParameter("status")!=null)
				  {
				%>
				<%=request.getParameter("status")%>
                <%}%></p>
		  </div>
	  </div>
		</div>
	<!-- end #content -->
	<div id="sidebar1">
		<div id="news" class="boxed1">
			<blockquote>
			  <blockquote>
			    <h2 class="title">Login</h2>
		      </blockquote>
		  </blockquote>
	  </div>
      <form name="login" id="f1" action="Login" method="post"><br />
         <table width="231" border="0">
  <tr>
    <td width="84" height="34"><strong>Username</strong></td>
    <td width="137"><label>
      <input name="login" type="text" id="textfield" size="22" />
    </label></td>
  </tr>
  <tr>
    <td height="32"><strong>Password</strong></td>
    <td><input name="password" type="password" id="textfield2" size="22" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2">
    <div align="center">
      <input type="submit" name="button" id="button" value="Sign In" />
    </div>    </td>
    </tr>
</table>

      
      </form>
	</div>
<!-- end #sidebar -->
</div>
<!-- end #page -->
<div id="footer">
	<p>&nbsp;</p>
</div>
</body>
</html>
