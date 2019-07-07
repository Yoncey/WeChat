<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>图书馆首页</title>
<link href="/kemao_2/library/css/main.css" rel="stylesheet" />
</head>
<body>
	<form action="" method="get">
		<input name="keyword" placeholder="关键字" value="${param.keyword}" />
		<button>搜索</button>
	</form>
	<c:forEach items="${page.content }" var="book">
		<div class="item">
			<div class="col-1">
				<img src="/kemao_2/library/images/${book.image }" />
			</div>
			<div class="col-10">
				<span>${book.name }</span>
			</div>
			<div class="col-1 buttons">
				<a href="/kemao/library/debit?id=${book.id }" class="button">+</a>
			</div>
		</div>
	</c:forEach>
	<div>
		<c:if test="${page.number <= 0 }">
			<a>上一页</a>
		</c:if>
		<c:if test="${page.number > 0 }">
			<a href="?pageNumber=${page.number - 1 }&keyword=${param.keyword}">上一页</a>
		</c:if>
		<c:choose>
			<c:when test="${page.number >= page.totalPages - 1 }">
				<a>下一页</a>
			</c:when>
			<c:otherwise>
				<a href="?pageNumber=${page.number + 1 }&keyword=${param.keyword}">下一页</a>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>