<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

    <title>Document</title>
    <script type="text/javascript">
    function init()
    {
    	if("${loginid}" == "${userid}")
    	{
    		
    	}
    	else
    	{
    		$("#editbtn").hide();
    		$(".deletebtn").hide();
    	}
    }
    
    </script>
    <style>
        .mytable
        {
            border: 1px solid black;
            width: 800px;
            margin:50px auto 0px auto;
        }
        
        .mytable .td1
        {
            width:100px;
        }
        .mytable .td2
        {
            width:700px;
        }
        .mytable .td3
        {
            vertical-align: 0px;
        }
        .mytable .td4
        {
            text-align: right;
        }

        .mytable .mytextarea
        {
            height:500px;
            resize: none;
        }
    </style>
</head>
<body onload="init()">
    <table class="mytable">
        <tr>
            <td class="td1">작성자</td>
            <td class="td2">${list[0].userid}</td>
        </tr>
        <tr>
            <td class="td1">제목</td>
            <td><input type="text" class="td2" name="title" readonly value="${list[0].title}"></td>
        </tr>
        <tr>
            <td class="td1 td3">내용</td>
            <td><textarea class="td2 mytextarea" name="mytextarea" readonly>${list[0].boardcontents}</textarea></td>
        </tr>
        <tr>
            <td class="td1">이미지</td>
            <td><img src="${list[0].fileurl}"></td>
        </tr>
        <tr>
            <td class="td1">다운로드</td>
            <td><a href="${list[0].fileurl}">파일 다운로드</a></td>
        </tr>
        <tr>
            <td colspan="2" class="td4">
                <!-- javascript로 사용자에게 안보여지게 처리필요. -->
                <a href="boardEdit.do?boardid=${list[0].boardid}" id="editbtn"><input type="button" value="수정"></a>
                <a href="boardRemove.do?boardid=${list[0].boardid}" class="deletebtn"><input type="button" value="삭제"></a>
                <a href="boardReply.do?boardid=${list[0].boardid}"><input type="button" value="답글"></a>
                <a href="boardList.do"><input type="button" value="목록보기"></a>
            </td>
        </tr>
    </table>
</body>
</html>
