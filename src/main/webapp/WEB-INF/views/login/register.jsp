<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
    <h2>회원가입</h2>
    <form action="/register" method="post">
        <label>아이디: <input type="text" name="username" required></label><br>
        <label>비밀번호: <input type="password" name="password" required></label><br>
        <label>이메일: <input type="email" name="email" required></label><br>
        <button type="submit">가입하기</button>
    </form>
</body>
</html>