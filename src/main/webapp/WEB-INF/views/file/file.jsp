<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<h2 class="mb-4">📁 파일 업로드 (다중)</h2>

<!-- 업로드 폼 -->
<form id="uploadForm" enctype="multipart/form-data">
  <div class="mb-3">
    <input type="file" id="fileInput" name="fileInput" multiple class="form-control" style="width:500px;">
  </div>
  <button type="button" class="btn btn-primary" onclick="uploadFiles()">업로드</button>
</form>

<!-- 진행바 출력 영역 -->
<div id="progressBarContainer" class="mt-4"></div>

<!-- 템플릿: 숨겨놓고 복제용으로 사용 -->
<div id="progressTemplate" style="display: none;">
  <div class="mb-2">
    <label class="form-label file-label">파일명</label>
    <div class="progress">
      <div class="progress-bar bg-success" role="progressbar" style="width: 0%;">0%</div>
    </div>
  </div>
</div>

<div class="d-flex justify-content-end mb-2">
  <button class="btn btn-primary me-2" onclick="downloadSelectedFiles()">📥 다운로드</button>
  <button class="btn btn-danger" onclick="deleteSelectedFiles()">🗑 삭제</button>
</div>

<div id="realgridContainer" style="width:100%; height:500px;"></div>

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
            fieldName: "original_name",
            dataType: "text",
        },
        {
            fieldName: "file_size",
            dataType: "text",
        },
        {
            fieldName: "upload_date",
            dataType: "text",
        },
        {
            fieldName: "saved_name",
            dataType: "text",
        }
    ];
    const columns = [
        {
            name: "id",
            fieldName: "id",
            visible : false
        },
        {
            name: "original_name",
            fieldName: "original_name",
            width: "100",
            header: {
                text: "파일명",
            },
            editable : false
        },
        {
            name: "file_size",
            fieldName: "file_size",
            width: "100",
            header: {
            	text: "파일크기"
            },
            renderer: {
				type: "html",
				callback: function (grid, cell) {
					const size = cell.value;
					if (!size) return "0";

					if (size >= 1024 * 1024) {
						return (size / (1024 * 1024)).toFixed(1) + " MB";
					} else if (size >= 1024) {
						return (size / 1024).toFixed(1) + " KB";
					} else {
						return size + " B";
					}
				}
			}
        },
        {
            name: "upload_date",
            fieldName: "upload_date",
            width: "100",
            header: {
                text: "업로드날짜",
            },
            editable : false
        },
        {
            name: "saved_name",
            fieldName: "saved_name",
            visible : false
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
	
	gridView.onCellItemClicked = function (grid, index, clickData) {
	  alert(clickData.value + " 버튼이 클릭 되었습니다.")
	  return true;
	}
}

//파일 조회
function selectFileList() {
	$.ajax({
		url: "/fileList",
        type: "post",
        dataType: "json",
        success: function (data) {
            provider.setRows(data);
        },
        error: function () {
        	alert("fileList를 불러올 수 없습니다.");
        }
	});
}

// 파일 업로드
function uploadFiles() {
    var files = $("#fileInput")[0].files;
    
    if (files.length === 0) {
        alert("파일을 선택해주세요.");
        return;
    }

    $('#progressBarContainer').empty(); // 진행바 영역 초기화

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const formData = new FormData();
        formData.append('files', file);

        // 템플릿 복제
        const $template = $('#progressTemplate').clone().removeAttr('id').show();
        const $label = $template.find('.file-label').text(file.name);
        const $bar = $template.find('.progress-bar');

        // 고유 ID 설정
        const barId = 'progressBar_' + i;
        $bar.attr('id', barId);

        $('#progressBarContainer').append($template);

        $.ajax({
            url: "/fileUpload",
            type: "post",
            data: formData,
            processData: false,
            contentType: false,
            xhr: function () { // progress bar 생성
                var xhr = new XMLHttpRequest();
                xhr.upload.onprogress = function (e) {
                    if (e.lengthComputable) {
                        var percent = (e.loaded / e.total) * 100;
                        $bar.css('width', percent + '%').text(percent.toFixed(2) + '%');
                    }
                };
                return xhr;
            },
            success: function () {
                $bar.removeClass("bg-success").addClass("bg-primary").text("완료");
                selectFileList();
            },
            error: function () {
                $bar.removeClass("bg-success").addClass("bg-danger").text("실패");
            }
        });
    }
}

// 파일 삭제
function deleteSelectedFiles() {
	const checkedIndexes = gridView.getCheckedRows();
	const selectedRows = checkedIndexes.map(index => provider.getJsonRow(index));

	if (selectedRows.length === 0) {
		alert("삭제할 파일을 선택해주세요.");
		return;
	}

	if (confirm("정말 삭제하시겠습니까?")) {
		$.ajax({
			url: "/fileDelete",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(selectedRows), // 선택된 파일 정보 전달
			success: function (res) {
				alert("삭제되었습니다.");
				selectFileList();
			},
			error: function () {
				alert("파일 삭제에 실패했습니다.");
			}
		});
	};
}

//파일 다운로드
//function downloadSelectedFiles() {
//const checkedIndexes = gridView.getCheckedRows(); // index list
//const selectedRows = checkedIndexes.map(index => provider.getJsonRow(index)); // 실제 row 데이터;

//if (selectedRows.length === 0) {
//  alert("다운로드할 파일을 선택해주세요.");
//  return;
//}

//selectedRows.forEach(row => {
//  const fileId = row.file_id; // 또는 실제 파일명
//  const link = document.createElement("a");
//  link.href = `/fileDownload?fileId=${fileId}`; // 백엔드 파일 다운로드 URL
//  link.click();
//});
//}

$(document).ready(function() {
	initGrid();
	selectFileList();
});
</script>