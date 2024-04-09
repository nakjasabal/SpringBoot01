<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
function validateForm(form) { 
    if (form.name.value == "") {
        alert("작성자를 입력하세요.");
        form.name.focus();
        return false;
    }
    if (form.title.value == "") {
        alert("제목을 입력하세요.");
        form.title.focus();
        return false;
    }
    if (form.content.value == "") {
        alert("내용을 입력하세요.");
        form.content.focus();
        return false;
    }
    /* if (form.pass.value == "") {
        alert("비밀번호를 입력하세요.");
        form.pass.focus();
        return false;
    } */
}
</script>
</head>
<body>
	<h2>게시판 수정(Mybatis)</h2>
	<form name="writeFrm" method="post"
		action="./edit.do" onsubmit="return validateForm(this);">
		
	<!-- 수정할 게시물의 일련번호를 전송해야 하므로 hidden 박스가 하나 필요하다. -->
	<input type="hid-den" name="idx" value="${boardDTO.idx }" />
	<table border="1" width="90%">
	    <tr>
	        <td>작성자</td>
	        <td>
	            <input type="text" name="name" style="width:150px;" 
	            	value="${boardDTO.name }" />
	        </td>
	    </tr>
	    <tr>
	        <td>제목</td>
	        <td>
	            <input type="text" name="title" style="width:90%;" 
	            	value="${boardDTO.title }"/>
	        </td>
	    </tr>
	    <tr>
	        <td>내용</td>
	        <td>
	            <textarea name="content" style="width:90%;
	            	height:100px;">${boardDTO.content }</textarea>
	        </td>
	    </tr>
	    <!-- <tr>
	        <td>비밀번호</td>
	        <td>
	            <input type="password" name="pass" style="width:100px;" />
	        </td>
	    </tr> -->
	    <tr>
	        <td colspan="2" align="center">
	            <button type="submit">작성 완료</button>
	            <button type="reset">RESET</button>
	            <button type="button" onclick="location.href='./list.do';">
	                목록 바로가기
	            </button>
	        </td>
	    </tr>
	</table>    
	</form>
</body>
</html>