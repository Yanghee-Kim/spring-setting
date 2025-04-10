<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<h2 class="mb-4">📁 파일 업로드 / 다운로드 </h2>

<h6>업로드</h6>
<h6>1. back : MultipartResolver 주입</h6>
<h6>2. front : Form + enctype="multipart/form-data"</h6>
<h6>3. front : Form 직접 제출 | Ajax 방식</h6>
<h6>4. back : MultipartFile | MultipartHttpServletRequest(동적으로 name이 달라질 수 있거나, 파일 개수가 유동적일 경우)</h6>

<h6>다운로드</h6>
<h6>1. Ajax + blob(파일로 인식하기 위해) + Common IO lib + a 태그</h6>

<br>

<h5>form 직접 제출</h5>
<form method="post" action="/upload" enctype="multipart/form-data">
  <div class="mb-3">
    <input type="file" id="file" name="file" class="form-control" style="width:500px;">
  </div>
  <button type="submit" class="btn btn-primary">업로드</button>
</form>

<h5>ajax 방식 + MultipartFile[]</h5>
<form id="uploadForm">
  <div class="mb-3">
    <input type="file" id="fileInput" name="fileInput" class="form-control" style="width:500px;" multiple>
  </div>
  <button type="button" class="btn btn-primary" onclick="uploadFiles1()">업로드</button>
</form>

<h5>ajax 방식 + MultipartHttpServletRequest</h5>
<form id="uploadForm2" enctype="multipart/form-data">
  <div class="mb-3">
    <input type="file" id="file1" name="file1" class="form-control" style="width:500px;">
    <input type="file" id="file2" name="file2" class="form-control" style="width:500px;">
    <input type="file" id="file3" name="file3" class="form-control" style="width:500px;">
  </div>
  <button type="button" class="btn btn-primary" onclick="uploadFiles2()">업로드</button>
</form>

<!-- 진행바 출력 영역 -->
<div id="progressBarContainer" class="mt-4"></div>

<!-- 템플릿: 숨겨놓고 복제용으로 사용 -->
<div id="progressTemplate" style="display: none;">
  <div class="mb-2">
    <label class="form-label file-label"></label>
    <div class="progress">
      <div class="progress-bar bg-success" role="progressbar" style="width: 0%;">0%</div>
    </div>
  </div>
</div>

<!-- 버튼 -->
<div class="d-flex justify-content-end mb-2">
  <button class="btn btn-primary me-2" onclick="downloadFile()">📥 다운로드</button>
  <button class="btn btn-primary me-2" onclick="downloadFileZip()">📥 다운로드(ZIP)</button>
  <button class="btn btn-danger" onclick="deleteFile()">🗑 삭제</button>
</div>

<!-- 그리드 -->
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

// 파일 삭제
function deleteFile() {
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

//파일 업로드 : ajax 방식 + MultipartFile[] 방식
function uploadFiles1() {
    var files = $("#fileInput")[0].files;
    
    if (files.length === 0) {
        alert("파일을 선택해주세요.");
        return;
    }
    
    // 파일 개수 제한
    if (files.length > 5) {
        return "파일은 최대 5개까지만 업로드할 수 있습니다.";
    }
    
    // 확장자 제한
    const allowExt = ['jpg', 'jpeg', 'png', 'gif', 'pdf'];
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const fileName = file.name;
        const ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        if (!allowExt.includes(ext)) {
            alert("허용되지 않은 확장자입니다: " + fileName);
            return; // 업로드 중단
        }
    }

    $('#progressBarContainer').empty(); // 진행바 영역 초기화

    for (let i = 0; i < files.length; i++) {
        const formData = new FormData();
        const file = files[i];
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
            url: "/fileUpload1",
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
            success: function (res) {
            	if(res == "success"){          		
	                $bar.removeClass("bg-success").addClass("bg-primary").text("완료");
	                selectFileList();
            	} else {
            		alert(res);
            	}
            },
            error: function () {
                $bar.removeClass("bg-success").addClass("bg-danger").text("실패");
            }
        });
    }
}

//파일 업로드 : ajax 방식 + MultipartHttpServletRequest 방식
function uploadFiles2() {
	const form = $("#uploadForm2");
    const files = form.find('input[type="file"]');
    const formData = new FormData();
    
    var hasFile = false;
    
    for (let i = 0; i < files.length; i++) {
    	if (!files[i].files[0]) continue;
        hasFile = true;
        
        formData.append(files[i].name, files[i].files[0]);
    }
    
    if (!hasFile) {
        alert("파일을 선택해주세요.");
        return;
    }

    $('#progressBarContainer').empty(); // 진행바 영역 초기화
    const $template = $('#progressTemplate').clone().removeAttr('id').show();
    const $bar = $template.find('.progress-bar');
    $('#progressBarContainer').append($template);
    
    $.ajax({
        url: "/fileUpload2",
        type: "post",
        data: formData,
        processData: false, // FormData는 가공하지 않고 그대로 전송
        contentType: false, // 브라우저가 자동으로 multipart/form-data + boundary 설정하도록 함
        xhr: function () {
            const xhr = new XMLHttpRequest();
            xhr.upload.onprogress = function (e) {
                if (e.lengthComputable) {
                    const percent = (e.loaded / e.total) * 100;
                    $bar.css('width', percent + '%').text(percent.toFixed(2) + '%');
                }
            };
            return xhr;
        },
        success: function (res) {
        	if(res == "success"){
        		$bar.removeClass("bg-danger").addClass("bg-primary").text("완료");
        		selectFileList();
        	}
        },
        error: function () {
        	$bar.removeClass("bg-primary").addClass("bg-danger").text("실패");
        }
    });
}

// [1] 사용자가 버튼 클릭
// ↓
// [2] 프론트에서 Ajax 요청
// ↓
// [3] 서버는 파일 경로 찾아서 → OutputStream으로 응답에 write (복사)
// ↓
// [4] 프론트는 blob 응답을 받고
// ↓
// [5] 가짜 <a> 태그 만들어 click() → 브라우저가 파일 다운로드

//파일 다운로드
function downloadFile() {
	const checkedIndexes = gridView.getCheckedRows();
	const selectedRows = checkedIndexes.map(index => provider.getJsonRow(index));
	
	if (selectedRows.length === 0) {
		alert("다운로드할 파일을 선택해주세요.");
		return;
	}
	
	if(selectedRows.length > 1){
		alert("다운로드할 파일은 하나만 선택가능합니다.");
		return;
	}
	
	$.ajax({
		url: "/fileDownload",
		method: 'post',
		contentType: 'application/json',
		data: JSON.stringify(selectedRows[0]),
		xhrFields: { responseType: "blob" },
		success: function(blob, status, xhr) {
			// a 태그를 만들어 다운로드 트리거 발생
			// 브라우저가 인식되게 하기 위해
			
			var filename = "downloaded_file"; // 기본값
			
			const header = xhr.getResponseHeader("Content-Disposition");
			if (header && header.includes("filename=")) {
				// filename decode
				filename = decodeURIComponent(header.split("filename=")[1].replaceAll('"', ''));
			}
			
			// 2. Blob을 가상의 URL로 변환
			const url = window.URL.createObjectURL(blob);
			
			// 3. 가짜 a 태그 생성 후 강제 클릭
			const a = document.createElement("a");
			a.href = url;
			a.download = filename;
			document.body.appendChild(a);
			a.click();
			a.remove();
			
			// 4. URL 객체 메모리 해제
			window.URL.revokeObjectURL(url);
		},
		error: function(xhr, status, error) {
			alert("파일 다운로드 중 오류가 발생했습니다.");
		}
	});
}

// 파일 다운로드 Zip
function downloadFileZip() {
	const checkedIndexes = gridView.getCheckedRows();
	const selectedRows = checkedIndexes.map(index => provider.getJsonRow(index));
	
	if (selectedRows.length === 0) {
		alert("다운로드할 파일을 선택해주세요.");
		return;
	}
	
	$.ajax({
		url: "/fileDownloadZip",
		method: 'post',
		contentType: 'application/json',
		data: JSON.stringify(selectedRows),
		xhrFields: { responseType: "blob" },
		success: function(blob) {
			const url = window.URL.createObjectURL(blob);
		      
			const a = document.createElement("a");
			a.href = url;
			a.download = "files.zip";
			document.body.appendChild(a);
			a.click();
			a.remove();
			
			window.URL.revokeObjectURL(url);
		},
		error: function(xhr, status, error) {
			alert("파일 다운로드 중 오류가 발생했습니다.");
		}
	});
}

$(document).ready(function() {
	initGrid();
	selectFileList();
});
</script>