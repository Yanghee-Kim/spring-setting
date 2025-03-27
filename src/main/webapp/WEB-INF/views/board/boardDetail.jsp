<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<form id="boardForm">
<input type="hidden" id="id" name="id">
<input type="hidden" id="view_count" name="view_count">
  <div class="mb-3">
    <label for="title" class="form-label">제목</label>
    <input type="text" class="form-control" id="title" name="title">
  </div>
  <div class="mb-3">
    <label for="writer" class="form-label">작성자</label>
    <input type="text" class="form-control" id="writer" name="writer"> <!-- 일단 readonly 빼고 등록으로 -->
  </div>
  <div class="mb-3">
    <label for="content" class="form-label">내용</label>
    <textarea class="form-control" id="content" name="content" rows="5"></textarea>
  </div>
  
  <button type="button" class="btn btn-primary" onclick="saveBoard()">💾 저장</button>
  <button type="button" class="btn btn-success" onclick="updateBoard()">✏️ 수정</button>
  <button type="button" class="btn btn-danger" onclick="deleteBoard()">🗑 삭제</button>
</form>


<script>
$(document).ready(function() {
	const boardId = JSON.parse(sessionStorage.getItem("boardId"));
	if(boardId){
		// 상세 조회
		$.ajax({
			url: "/boardList",
		    type: "post",
		    contentType: "application/json",  // 요청 데이터 형식
			dataType: "json",                 // 응답 데이터 형식
		    data: JSON.stringify({id : boardId}),
		    success: function(data) {
	    	  $("#id").val(data[0].id);
		      $("#title").val(data[0].title);		    
		      $("#writer").val(data[0].writer);
		      $("#content").val(data[0].content);
		      $("#view_count").val(data[0].view_count);
		      
		      updateViewCount();
		    },
		    error: function() {
		      alert("저장 실패");
		    }
  		});
	}
});

// 저장
function saveBoard() {
	if (confirm("저장하시겠습니까?")) {
		$.ajax({
			url: "/insertBoard",
		    type: "post",
		    data: $("#boardForm").serialize(),
		    success: function(res) {
		      alert("저장되었습니다.");
		      movePage("/boardPage");
		    },
		    error: function() {
		      alert("저장 실패");
		    }
  		});
	}
}

// 수정
function updateBoard() {
  $.ajax({
    url: "/updateBoard",
    type: "post",
    data: $("#boardForm").serialize(),
    success: function(res) {
      alert("수정되었습니다.");
      movePage("/boardPage");
    },
    error: function() {
      alert("수정 실패");
    }
  });
}

// 삭제
function deleteBoard() {
  if (confirm("정말 삭제하시겠습니까?")) {
    $.ajax({
      url: "/deleteBoard",
      type: "post",
      data: { id: $("#id").val() },
      success: function(res) {
        alert("삭제되었습니다.");
        movePage("/boardPage");
      },
      error: function() {
        alert("삭제 실패");
      }
    });
  }
}

//조회수 증가
function updateViewCount() {
	$.ajax({
		url: "/updateViewCount",
	    type: "post",
	    contentType: "application/json",  // 요청 데이터 형식
	    data: JSON.stringify({id : $("#id").val(), view_count : Number($("#view_count").val())}),
	    success: function(res) {
	    },
	    error: function() {
	      alert("조회수 저장 실패");
	    }
	});
}
</script>