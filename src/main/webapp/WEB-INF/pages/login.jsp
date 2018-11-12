<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<c:if test="${pageContext.request.userPrincipal.name != null}">
  <% response.sendRedirect("welcome");%>
</c:if>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>CAS </title>

    <!-- Bootstrap core CSS -->

    <link href="resources/css/bootstrap.min.css" rel="stylesheet">

    <link href="resources/fonts/css/font-awesome.min.css" rel="stylesheet">
    <link href="resources/css/animate.min.css" rel="stylesheet">

    <!-- Custom styling plus plugins -->
    <link href="resources/css/custom.css" rel="stylesheet">
    <link href="resources/css/icheck/flat/green.css" rel="stylesheet">
   <script src="resources/js/jquery.min.js"></script>
</head>

<body style="background:#F7F7F7;"> 
    <div class="">
        <a class="hiddenanchor" id="tologin"></a>
   <div id="wrapper">
            <div id="login" class="animate form">
                <section class="login_content">
                	<c:if test="${not empty error}">
            			<div class="alert alert-danger alert-dismissible fade in" role="alert"> ${error} </div>
        			</c:if>
			        <c:if test="${not empty msg}">
			            <div class="msg">${msg}</div>
			        </c:if>
                   <form name='loginForm' action="<c:url value='j_spring_security_check' />" method='POST'>
                        <h1>Login Form</h1>
                        <div>
                            <input type="text" class="form-control" placeholder="Username" name="username" required="" />
                        </div>
                        <div>
                            <input type="password" class="form-control" placeholder="Password" name="password" required="" />
                        </div>
                        
                        <div>
                        <input name="submit" class="btn btn-default submit" type="submit" value="submit" />
                         </div>
                        <div class="clearfix"></div>
                        <div class="separator">
                            <div class="clearfix"></div>
                            <br />
                            <div>
                                 <h1><i class="fa fa-paw" style="font-size: 26px;"></i> CAS</h1>
                                <p>©2018 All Rights Reserved.Acquiro Payments.</p>
                            </div>
                        </div>  
           			 <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />   
                    </form>
                    <!-- form -->
                </section>
                <!-- content -->
            </div>
          
        </div>
    </div>

</body>

</html>