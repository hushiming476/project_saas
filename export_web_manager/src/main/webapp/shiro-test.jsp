<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>通过shiro标签实现权限控制</title>
</head>
<body>
     <shiro:hasPermission name="用户管理">
         <a href="#">用户管理</a>
     </shiro:hasPermission>
     <shiro:hasPermission name="部门管理">
         <a href="#">部门管理</a>
     </shiro:hasPermission>
     <shiro:hasPermission name="角色管理">
         <a href="#">角色管理</a>
     </shiro:hasPermission>
     <shiro:hasPermission name="装箱管理">
         <a href="#">装箱管理</a>
     </shiro:hasPermission>











</body>
</html>
