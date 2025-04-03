<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Main</title>
    <!-- jquery -->
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <!-- realgrid-style -->
	<link href="https://cdn.wooritech.com/realgrid/realgrid.2.8.3/realgrid-style.css" rel="stylesheet" />
    <!-- realgrid -->
    <script src="https://cdn.wooritech.com/realgrid/realgrid.2.8.3/realgrid.2.8.3.min.js"></script>
    
    <!-- realgrid license Key -->
    <script>
		RealGrid.setLicenseKey(
	        "upVcPE+wPOmtLjqyBIh9RkM/nBOseBrflwxYpzGZyYm9cY8amGDkiMnVeQKUHJDjW2y71jtk+wteqHQ1mRMIXzEcGIrzZpzzNTakk0yR9UcO/hzNodVsIiqQNVtxmmYt"
	    );
    </script>
    
    <!-- bootstrap -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
	
	<script>
		// center 영역 jsp 이동 함수
		function movePage(jspPath) {
		    $.ajax({
		    	url: jspPath,
		    	type: "get",
		    	dataType: "html",
		    	success : function(html) {
		    		$("#centerArea").html(html);
		    	},
		        error: function (xhr, status, error) {
					alert("페이지 이동 중 시스템 오류가 발생하였습니다.");
	            	console.error("AJAX Error:", status, error);
				}
		    });
		}
	</script>
	
	<style>
	html, body {
	  height: 100%;
	  margin: 0;
	}
	
	#centerArea {
	  height: calc(100vh - 100px);
	  padding: 20px;
	}
	</style>
</head>
<body>
<div class="container-fluid">
    <!-- Header -->
    <div class="row bg-dark text-white">
        <div class="col-12">
            <%@ include file="/WEB-INF/views/layout/header.jsp" %>
        </div>
    </div>

    <!-- Content Area -->
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-2 bg-secondary text-white">
            <%@ include file="/WEB-INF/views/layout/sidebar.jsp" %>
        </div>

        <!-- Center -->
        <div class="col-md-10 bg-white" id="centerArea" style="overflow-y: scroll;">
            <%@ include file="/WEB-INF/views/layout/default.jsp" %>
        </div>
    </div>

    <!-- Footer -->
    <div class="row bg-dark text-white">
        <div class="col-12">
            <%@ include file="/WEB-INF/views/layout/footer.jsp" %>
        </div>
    </div>
</div>
</body>
</html>