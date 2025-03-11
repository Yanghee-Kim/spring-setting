<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>	
    <form action="/login" method="post">
        <label>Username:</label>
        <input type="text" name="username" required />
        <label>Password:</label>
        <input type="password" name="password" required />
        <button type="submit">Login</button>
    </form>

    <!-- 로그인 실패 메시지 표시 -->
    <c:if test="${param.error != null}">
        <p style="color:red;">아이디 또는 비밀번호가 올바르지 않습니다.</p>
    </c:if>

    <!-- 로그아웃 메시지 표시 -->
<%--     <c:if test="${param.logout != null}"> --%>
<!--         <p style="color:green;">로그아웃 되었습니다.</p> -->
<%--     </c:if> --%>
</body>
</html>