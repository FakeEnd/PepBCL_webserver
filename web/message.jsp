<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>results</title>




<script type="text/javascript">
	var time = 5;
 
	function returnUrlByTime() {
 
		window.setTimeout('returnUrlByTime()', 1000);
 
		time = time - 1;
 
		document.getElementById("layer").innerHTML = time;
	}
</script>




</head>
<body onload="returnUrlByTime()">
    <center>
        <h2>${message}</h2>
    </center>
    <br>
    
    <p align="center" ><h2  align="center" ><b  id="layer" style="color: red;font-size: 50px">5 </b> seconds later this webpage will be redirected to home webpage</h2></p>
<%
		//转向语句
		response.setHeader("Refresh", "5; URL=http://weilab.sducat.top/PepBCL/");
	%>


</body>
</html>