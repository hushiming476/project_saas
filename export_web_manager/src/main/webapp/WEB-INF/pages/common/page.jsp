<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<div class="pull-left">
    <div class="form-group form-inline">
        总共${pageInfo.pages} 页，共${pageInfo.total} 条数据。
    </div>
</div>

<div class="box-tools pull-right">
    <ul class="pagination" style="margin: 0px;">
        <li >
            <a href="javascript:goPage(1)" aria-label="Previous">首页</a>
        </li>
        <li><a href="javascript:goPage(${pageInfo.prePage})">上一页</a></li>

         <%--给样式颜色： ${pageInfo.pageNum==i ? 'active':''}--%>
         <%--遍历每个页面：<c:forEach begin="1" end="${pageInfo.pages}" var="i">--%>

        <c:forEach
                begin="${pageInfo.pageNum-5 > 0 ? pageInfo.pageNum-5 : 1}"
                end="${pageInfo.pageNum+5 < pageInfo.pages ? pageInfo.pageNum+5 : pageInfo.pages}" var="i">
            <li class="paginate_button ${pageInfo.pageNum==i ? 'active':''}"><a href="javascript:goPage(${i})">${i}</a></li>
        </c:forEach>

        <li><a href="javascript:goPage(${pageInfo.nextPage})">下一页</a></li>

        <li>
            <a href="javascript:goPage(${pageInfo.pages})" aria-label="Next">尾页</a>
        </li>
    </ul>
</div>
     <%--EL内置表，内置对象 "${param.pageUrl}"：作为表单提交地址(dept-list.jsp)--%>
     <%--name = pageNum(要和控制器请求的参数一样)--> 否则分页按钮点击失败 --%>
<form id="pageForm" action="${param.pageUrl}" method="post">
    <input type="hidden" name="pageNum" id="pageNum">
</form>
<script>
    function goPage(pageNum) {
        document.getElementById("pageNum").value = pageNum;
        document.getElementById("pageForm").submit()
    }
</script>
</body>
</html>
