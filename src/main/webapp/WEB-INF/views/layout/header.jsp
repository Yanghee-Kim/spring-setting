<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>header</title>
</head>
<body>
	<div class="d-flex justify-content-between align-items-center">
	    <h2>header</h2>
	    <div class="text-end">
	        <span>${sessionScope.LOGIN_USER.username}님 환영합니다.</span>
	        <span><button onclick="location.href='/logout'">로그아웃</button></span>
	    </div>
	</div>
</body>
</html>