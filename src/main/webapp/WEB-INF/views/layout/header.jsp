<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	        <span>
				<select id="langSelect" onchange="changeLang()">
					<option value="en" ${pageContext.response.locale.language == 'en' ? 'selected' : ''}>English</option>
					<option value="ko" ${pageContext.response.locale.language == 'ko' ? 'selected' : ''}>한국어</option>
				</select>
	        </span>
	        <span>${sessionScope.LOGIN_USER.username}&nbsp;<spring:message code="welcome"></spring:message></span>
	        <span><button onclick="location.href='/logout'">로그아웃</button></span>
	    </div>
	</div>
</body>
<script>
function changeLang() {
	var selectedLang = $("#langSelect").val();

	$.ajax({
		type: 'post',
		url: "/changeLang",
		data: { lang: selectedLang },
		success: function () {
			location.reload();
		}
	});
}
</script>
</html>