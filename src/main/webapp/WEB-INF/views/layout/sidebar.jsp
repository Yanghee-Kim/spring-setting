<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>sidebar</title>
	<style>
		button {
			border : 0;
			background-color : white;
		}
	</style>
</head>
<body>
	<div class="p-3">
	  <h5 class="text-white">📂 메뉴</h5>
	  <ul class="list-group">
	    <li class="list-group-item">
	      <button onclick="movePage('/boardPage')">📝 <spring:message code="board"></spring:message></button>
	    </li>
	    <li class="list-group-item">
	      <button onclick="movePage('/filePage')">📝 <spring:message code="download"></spring:message></button>
	    </li>
	  </ul>
	</div>
</body>
</html>