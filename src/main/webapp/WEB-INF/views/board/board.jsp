<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
  <div class="d-flex justify-content-between align-items-center mb-2">
    <h4>📝 게시판</h4>
    <button class="btn btn-primary" onclick="movePage('/boardDetail')">✏️ 글작성</button>
  </div>
  <div id="realgridContainer" style="width:100%; height:500px;"></div>
</div>

<script>
var provider;
var gridView;

function initGrid() {
	// provider, gridView 생성
    provider = new RealGrid.LocalDataProvider();
    gridView = new RealGrid.GridView("realgridContainer");
    gridView.setDataSource(provider);
    
	// field, column 생성
    const fields = [
        {
            fieldName: "id",
            dataType: "text",
        },
        {
            fieldName: "title",
            dataType: "text",
        },
        {
            fieldName: "content",
            dataType: "text",
        },
        {
            fieldName: "writer",
            dataType: "text",
        },
        {
            fieldName: "reg_dtm",
            dataType: "text",
        },
        {
            fieldName: "upd_dtm",
            dataType: "text",
        },
        {
            fieldName: "view_count",
            dataType: "text",
        }
    ];
    const columns = [
        {
            name: "id",
            fieldName: "id",
            width: "50",
            header: {
                text: "No.",
            },
            editable : false
        },
        {
            name: "title",
            fieldName: "title",
            width: "200",
            header: {
                text: "제목",
            },
            editable : false
        },
        {
            name: "writer",
            fieldName: "writer",
            width: "100",
            header: {
                text: "작성자",
            },
            editable : false
        },
        {
            name: "reg_dtm",
            fieldName: "reg_dtm",
            width: "200",
            header: {
                text: "등록일자",
            },
            editable : false
        },
        {
            name: "view_count",
            fieldName: "view_count",
            width: "80",
            header: {
                text: "조회수",
            },
            editable : false
        }
    ];
    provider.setFields(fields);
    gridView.setColumns(columns);
    
    // option
    var options = {
	    display: {
	    	fitStyle : "even",
	    },
	    footer : {
	    	visible : false
	    }
	};
	gridView.setOptions(options);
    
    gridView.onCellDblClicked = function (grid, clickData) {
		const row = clickData.dataRow;
		sessionStorage.setItem("boardId", JSON.stringify(provider.getValue(row, "id"))); // boardId 적용
    	movePage("/boardDetail");
    }
}

function boardList() {
	$.ajax({
    	url: "/boardList",
    	type: "post",
		dataType: "json", // 응답을 JSON으로 받음
    	success : function(data) {
   	     	provider.setRows(data);
    	},
        error: function (xhr, status, error) {
			alert("boardList를 불러올 수 없습니다.");
        	console.error("AJAX Error:", status, error);
		}
    });
}

$(document).ready(function() {
	initGrid();
	boardList();
});
</script>