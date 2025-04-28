<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>default</title>
</head>
<body>
	<h2><spring:message code="default"></spring:message></h2>

	<!-- db에 있는 메시지 -->
	<h2><spring:message code="hello"></spring:message></h2>
	<h2><spring:message code="thanks"></spring:message></h2>
	<!-- 프로퍼티 파일에 있는 메시지 -->
	<h2><spring:message code="test"></spring:message></h2>
	<!-- 둘 다 없는 메시지 -->
	<h2><spring:message code="test1"></spring:message></h2>
</body>
</html>