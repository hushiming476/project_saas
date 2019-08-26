<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../base.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <base href="${ctx}/">
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 页面meta /-->
    <link rel="stylesheet" href="/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="/plugins/ztree/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="/plugins/ztree/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="/plugins/ztree/js/jquery.ztree.excheck-3.5.js"></script>

    <SCRIPT type="text/javascript">
        var setting = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };

        $(document).ready(function(){
            // 获取角色id
            var roleId = $("#roleId").val();
            $.post("/system/role/getZtreeNodes.do",{"roleId":roleId},
                function(jsonData){
                    initTree(jsonData);
                }
            );
        });

        var treeObj;
        // 初始化树
        function initTree(zNodes){
            treeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }

        // 获取选中的节点并提交
        function submitCheckedNodes(){
            // 获取选中的节点Array集合
            var nodes = treeObj.getCheckedNodes(true);

            // 定义权限字符串，保存所有选中的节点
            var moduleIds = "";

            // 遍历选中的节点
            for(var i=0; i<nodes.length; i++){
                //nodes[i] 就是节点对象，就是json对象
                //nodes[i] = {id:1,pId:0,name:''}
                moduleIds += nodes[i].id + ","
            }
            // 去最后的逗号
            moduleIds = moduleIds.substring(0,moduleIds.length-1);

            // 把获取的选中的权限id设置到表单中
            $("#moduleIds").val(moduleIds);

            // 提交表单
            // document.forms[0] 获取页面第一个表单对象
            document.forms[0].submit();
        }

    </SCRIPT>
</head>

<body style="overflow: visible;">
<div id="frameContent" class="content-wrapper" style="margin-left:0px;height: 1200px" >
    <section class="content-header">
        <h1>
            菜单管理
            <small>菜单列表</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
        </ol>
    </section>
    <!-- 内容头部 /-->

    <!-- 正文区域 -->
    <section class="content">

        <!-- .box-body -->
        <div class="box box-primary">
            <div class="box-header with-border">
                <h3 class="box-title">角色 [${role.name}] 权限列表</h3>
            </div>

            <div class="box-body">

                <!-- 数据表格 -->
                <div class="table-box">
                    <!--工具栏-->
                    <div class="box-tools text-left">
                        <button type="button" class="btn bg-maroon" onclick="submitCheckedNodes();">保存</button>
                        <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
                    </div>
                    <!--工具栏/-->
                    <!-- 树菜单 -->
                    <form name="icform" method="post" action="/system/role/updateRoleModule.do">
                        <input type="hidden" id="roleId" name="roleId" value="${role.id}"/>
                        <input type="hidden" id="moduleIds" name="moduleIds" value=""/>
                        <div class="content_wrap">
                            <div class="zTreeDemoBackground left" style="overflow: visible">
                                <ul id="treeDemo" class="ztree"></ul>
                            </div>
                        </div>

                    </form>
                    <!-- 树菜单 /-->

                </div>
                <!-- /数据表格 -->

            </div>
            <!-- /.box-body -->
        </div>
    </section>
</div>
</body>
</html>