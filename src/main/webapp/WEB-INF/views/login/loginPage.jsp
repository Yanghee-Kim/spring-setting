<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<!-- jquery -->
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
	<meta charset="UTF-8">
	<title>Insert title here</title>
</head>
<body>	
<!--     <form id="loginForm" name="loginForm" method="post" action="/login"> -->
<!--         <label>Username:</label> -->
<!--         <input type="text" id="username" name="username" required /> -->
<!--         <label>Password:</label> -->
<!--         <input type="password" id="password" name="password" required /> -->
<!--         <button type="submit">Login</button> -->
<!--     </form> -->

    <form id="loginForm">
        <label>Username:</label>
        <input type="text" id="username" name="username" required />
        <label>Password:</label>
        <input type="password" id="password" name="password" required />
        <button type="button" onclick=login()>Login</button>
    </form>
    
    <button onclick=goToRegister()>회원가입</button>
</body>
<script>
function goToRegister() {
    window.location.href = "/registerPage";
}

function login() {
	$.ajax({
		url: '/login',
		type: 'post',
		contentType: 'application/json',
		data: JSON.stringify({
			username: $('#username').val(),
			password: $('#password').val()
		}),
		success: function(result) {
			if (result.errorMsg) {
				alert(result.errorMsg);
			} else {
				window.location.href = '/'; // 로그인 성공 → 메인 페이지 이동
			}
		},
		error: function(xhr, status, err) {
			alert("서버 오류가 발생했습니다.");
		}
	});
}
</script>
</html>