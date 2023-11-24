<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--<head>--%>
<%--    <meta charset="UTF-8">--%>
<%--    <title>fastcampus</title>--%>
<%--</head>--%>

<%--        <table>--%>
<%--            <tr>--%>
<%--                <th class="no">id</th>--%>
<%--                <th class="title">제목</th>--%>
<%--                <th class="writer">내용</th>--%>
<%--                <th class="regdate">등록일</th>--%>
<%--            </tr>--%>
<%--            <c:forEach var="article" items="${articles}">--%>
<%--                <tr>--%>
<%--                    <td class="no">${article.id}</td>--%>
<%--                    <td class="title">${article.title}</td>--%>
<%--                    <td class="writer">${article.content}</td>--%>
<%--                    <td class="regdate">${article.createdAt}</td>--%>
<%--                </tr>--%>
<%--            </c:forEach>--%>
<%--        </table>--%>
<%--</body>--%>
<%--</html>--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<body>

<div class="container">
    <table class="table table-hover table table-striped">
        <tr>
            <th>번호</th>
            <th>작성자</th>
            <th>제목</th>

    </table>
</div>

</body>
</html>