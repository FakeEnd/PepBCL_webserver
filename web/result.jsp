<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@  page import="java.io.*"%>
<%@  page import="java.util.ArrayList"%>
<%@  page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- Required meta tags always come first -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="x-ua-compatible" content="ie=edge">

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/custom.css">

<!-- Fonts -->
<link
	href='https://fonts.googleapis.com/css?family=Lato:400,700,900,300'
	rel='stylesheet' type='text/css'>
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic'
	rel='stylesheet' type='text/css'>
<link
	href='https://fonts.googleapis.com/css?family=Raleway:400,300,600,700,900'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
<title>PepBCL</title>
</head>
<body>



	<div class="container">
		<br> 
		<div class="">
			<h2>
				PepBCL<span class="pull-right label label-default"></span>
			</h2>
			<p>A webserver for predicting protein-peptide binding residues.</p>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="panel with-nav-tabs panel-default">
					<div class="panel-heading">
						<ul class="nav nav-tabs">

							<li class="active"><a href="#result" data-toggle="tab"><strong>Result</strong></a></li>

						</ul>
					</div>
					<div class="panel-body">
						<div class="tab-content">
							<div class="tab-pane fade in active" id="result">

								<div class="container" style="width:100%">
									<div class="row">
									<div class="col-lg-11">
										<%
					List<String> showlist1 = (List<String>) request.getAttribute("prelist");
//											String path = "data/Dataset2_train.tsv";
					String path = (String) request.getAttribute("download_result");
//										String Pep = (String) request.getAttribute("Pep");
				%>


										<strong><h3 align="center">Results</h3></strong>
										<%
											for (int i = 0; i < showlist1.size(); i++) {
												String[] show = showlist1.get(i).split("#####");
										%>

										<h4>><%=show[1]%></h4>


										<pre>><%=show[1]%>
<%=show[2]%>
<%=show[3]%></pre>

										<%
											}
										%>
<%--										<table class="table table-hover"   style="width:100%"--%>
<%--   >--%>
<%--											<tr class="info">--%>
<%--												<td align="center">No.</td>--%>
<%--												<td align="center">Residue</td>--%>
<%--												<td align="center">Binary</td>--%>
<%--												<td align="center">Propensity score</td>--%>
<%--											</tr>--%>

<%--												<%--%>
<%--						for (int i = 0; i < showlist1.size(); i++) {--%>
<%--								String[] show = showlist1.get(i).split(",");--%>
<%--					%>--%>
<%--											<tr class="info">--%>
<%--												<td align="center"><%=show[0]%></td>--%>
<%--												<td align="center"><%=show[1]%></td>--%>
<%--												<td align="center"><%=show[2]%></td>--%>
<%--												<td align="center"><%=show[3]%></td>--%>
<%--											</tr>--%>
<%--											<%--%>
<%--						}--%>
<%--					%>--%>
<%--										</table>--%>


										<br />
										<a href="http://120.24.47.30:8080/PepBCL">
										<button id="btn_back" type="button"
											class="btn btn-warning btn-center" style="width: 200px;">Back
										</button>
										</a>
										<a href=<%=path%> >
											<button id="btn_back" type="button"
													class="btn btn-info btn-center" style="width: 200px;">Download result
											</button></a>
									</div>
									</div>
								</div>
							</div>

							
						</div>
					</div>
				</div>


			</div>
		</div>

	</div>






	<div class="container">
		<footer class="footer">
			<br>
			<hr>
			<p>
				<p class="copyright"><a href="http://weilab.sducat.top/PepBCL/">Shandong University</a> &copy; 2021.<br /> If you have any comments, corrections or questions
				<a href="Contact.html" target="_top">contact us</a>.</p></span>
			</p>
		</footer>
	</div>	
<!-- JavaScripts -->
	<script src="js/jquery-2.1.1.min.js"></script>
	<script src="js/tether.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function() {

			jQuery('#btn_back').click(function(event) {
				window.location.href = 'home.html';

			});


		})
		console
	</script>    
    
</body>
</html>