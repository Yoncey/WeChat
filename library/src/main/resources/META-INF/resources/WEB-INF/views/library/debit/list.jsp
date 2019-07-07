<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>借阅列表</title>
<link href="/kemao_2/library/css/main.css" rel="stylesheet"/>
<script type="text/javascript" src="/kemao_2/library/js/library.js"></script>
</head>
<body>
	<c:forEach items="${debitList.books }" var="book">
		<div class="item">
			${book.name }
			<a href="/kemao/library/debit/remove/${book.id }" class="remove">删除</a>
		</div>
	</c:forEach>
	<a style="margin-top:10px;" href="/kemao/library">返回图书室</a>
	<button style="float:right;margin-top:10px;" onclick="saveborrow()">保存</button>
</body>
</html>