<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
	color: #FF0000;
	font-weight: bold;
	font-size: 14px;
}

#name{
	padding:10px;
}
-->
</style>
</head>
<body>
	<%
		String userName = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user"))
					userName = cookie.getValue();
			}
		}
		if (userName == null)
			response.sendRedirect("Login.jsp");
		;
	%>
	<div id="header">
		<div id="logo">
			<h1><jsp:include page="header.html" />
			</h1>

		</div>
		<div id="menu">
			<jsp:include page="adminoptions.html" />
		</div>
	</div>
	<div id="page">
		<!-- end #content -->
		<div id="sidebar">
			<div id="news" class="boxed1">
				<blockquote>
					<blockquote>
						<h2 class="title">Registration</h2>
					</blockquote>
				</blockquote>
			</div>
			<form action="" method="post" name="login">
			&nbsp;
				<div id="name">Enter Name:&nbsp;<input type="text" name="Enter Name" />
				</div>

				<div align="center" class="style1">
					<%
						if (request.getParameter("status") != null) {
					%>
					<%=request.getParameter("status")%>
					<%
						}
					%>
				</div>
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
