<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2020/4/14
  Time: 22:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%
    String path = request.getContextPath();
    request.setAttribute("path",path);
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${path}/resources/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${path}/resources/easyui/css/demo.css">
    <script type="text/javascript" src="${path}/resources/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${path}/resources/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${path}/resources/easyui/js/validateExtends.js"></script>
    <script type="text/javascript">
        $(function() {
            var table;
            //datagrid初始化 
            $('#dataList').datagrid({
                title:'用户列表',
                iconCls:'icon-more',//图标 
                border: true,
                collapsible:false,//是否可折叠的 
                fit: true,//自动大小 
                method: "post",
                /*这里有问题*/
                url:"${path}/user/getAdminList?t="+new Date().getTime(),
                idField:'id',
                singleSelect:false,//是否单选 
                pagination:true,//分页控件 
                rownumbers:true,//行号 
                sortName:'id',
                sortOrder:'asc',
                remoteSort: false,
                columns: [[
                    {field:'chk',checkbox: true,width:50},
                    {field:'id',title:'ID',width:50, sortable: true},
                    {field:'username',title:'姓名',width:150},
                    {field:'password',title:'密码',width:100},
                ]],
                toolbar: "#toolbar"
            });
            //设置分页控件 
            var p = $('#dataList').datagrid('getPager');
            $(p).pagination({
                pageSize: 10,//每页显示的记录条数，默认为10 
                pageList: [10,20,30,50,100],//可以设置每页记录条数的列表 
                beforePageText: '第',//页数文本框前显示的汉字 
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
            });
            //设置工具类按钮
            $("#add").click(function(){
                table = $("#addTable");
                $("#addDialog").dialog("open");
            });
            //修改
            $("#edit").click(function(){
                table = $("#editTable");
                var selectRows = $("#dataList").datagrid("getSelections");
                if(selectRows.length != 1){
                    $.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
                } else{
                    $("#editDialog").dialog("open");
                }
            });
            //删除
            $("#delete").click(function(){
                var selectRows = $("#dataList").datagrid("getSelections");
                var selectLength = selectRows.length;
                if(selectLength == 0){
                    $.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
                } else{
                    /*请选择*/
                    var ids = [];
                    $(selectRows).each(function(i, row){
                        ids[i] = row.id;
                    });
                    $.messager.confirm("消息提醒", "将删除与用户相关的所有数据，确认继续？", function(r){
                        if(r){
                            $.ajax({
                                type: "post",
                                url: "${path}/user/deleteUser",
                                data: {ids: ids},
                                dataType:"json",
                                success: function(data){
                                    if(data.type == "success"){
                                        $.messager.alert("消息提醒",data.msg,"info");
                                        //刷新表格
                                        $("#dataList").datagrid("reload");
                                        $("#dataList").datagrid("uncheckAll");
                                    } else{
                                        $.messager.alert("消息提醒",data.msg,"warning");
                                        return;
                                    }
                                }
                            });
                        }
                    });
                }
            });

            //设置添加窗口
            $("#addDialog").dialog({
                title: "添加用户",
                width: 350,
                height: 200,
                iconCls: "icon-add",
                modal: true,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                draggable: true,
                closed: true,
                buttons: [
                    {
                        text:'添加',
                        plain: true,
                        iconCls:'icon-user_add',
                        handler:function(){
                            var validate = $("#addForm").form("validate");
                            if(!validate){
                                $.messager.alert("消息提醒","请检查你输入的数据!","warning");
                                return;
                            } else{
                                var data = $("#addForm").serialize();
                                $.ajax({
                                    type: "post",
                                    url: "${path}/user/addUser",
                                    data: data,
                                    dataType:"json",
                                    success: function(data){
                                        if(data.type == "success"){
                                            $.messager.alert("消息提醒","添加成功!","info");
                                            //关闭窗口
                                            $("#addDialog").dialog("close");
                                            //清空原表格数据
                                            $("#add_username").textbox('setValue', "");
                                            $("#add_password").textbox('setValue', "");
                                            $(table).find(".chooseTr").remove();
                                            //重新刷新页面数据
                                            $('#dataList').datagrid("reload");

                                        } else{
                                            $.messager.alert("消息提醒",data.msg,"warning");
                                            return;
                                        }
                                    }
                                });
                            }
                        }
                    },
                ],
                /*当关闭时*/
                onClose: function(){
                    $("#add_username").textbox('setValue', "");
                    $("#add_password").textbox('setValue', "");
                    $(table).find(".chooseTr").remove();
                }
            });
            //编辑用户信息
            $("#editDialog").dialog({
                title: "修改用户信息",
                width: 350,
                height: 200,
                iconCls: "icon-edit",
                modal: true,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                draggable: true,
                closed: true,
                buttons: [
                    {
                        text:'提交',
                        plain: true,
                        iconCls:'icon-edit',
                        handler:function(){
                            var validate = $("#updateForm").form("validate");
                            if(!validate){
                                $.messager.alert("消息提醒","请检查你输入的数据!","warning");
                                return;
                            } else{
                                var data = $("#updateForm").serialize();
                             //   alert(data);
                                $.ajax({
                                    type: "post",
                                    url: "${path}/user/updateUser",
                                    data: data,
                                    dataType: "json",
                                    success: function(data){
                                        if(data.type == "success"){
                                            $.messager.alert("消息提醒",data.msg,"info");
                                            //关闭窗口
                                            $("#editDialog").dialog("close");
                                            //重新刷新页面数据
                                            $('#dataList').datagrid("reload");
                                            $('#dataList').datagrid("uncheckAll");

                                        } else{
                                            $.messager.alert("消息提醒",data.msg,"warning");
                                            return;
                                        }
                                    }
                                });
                            }
                        }
                    },
                ],
                /*打开之前*/
                onBeforeOpen: function(){
                    var selectRow = $("#dataList").datagrid("getSelected");
                    //设置值
                    $("#update_id").val(selectRow.id);
                    $("#update_username").textbox('setValue', selectRow.username);
                    $("#update_password").textbox('setValue', selectRow.password);
                },
            });
            /*给搜索添加事件*/
           $("#search-btn").click(function () {
                $('#dataList').datagrid('reload',{
                    username:$("#search-username").textbox('getValue')
                });
            });
        });
    </script>
</head>
<body>
<!-- 数据列表 -->
<table id="dataList" cellspacing="0" cellpadding="0">

</table>
<!-- 工具栏 -->
<div id="toolbar">
    <div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
    <div style="float: left;" class="datagrid-btn-separator"></div>
    <div style="float: left;"><a id="edit" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
    <div style="float: left;" class="datagrid-btn-separator"></div>
    <div>
        <a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a>
        <%-- 这里加入了搜索框--%>
        用户名：<input id="search-username" class="easyui-textbox" />
        <a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
    </div>
</div>

<!-- 添加窗口 -->
<div id="addDialog" style="padding: 10px;">
    <form id="addForm" method="post">
        <table id="addTable" cellpadding="8">
            <tr>
                <td>姓名:</td>
                <td>
                    <input id="add_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请输入用户名'" />
                </td>
            </tr>
            <tr>
                <td>密码:</td>
                <td><input id="add_password" style="width: 200px; height: 30px;" class="easyui-textbox" type="password" name="password" data-options="required:true, missingMessage:'请输入密码'" /></td>
            </tr>
        </table>
    </form>
</div>
<!-- 修改窗口 -->
<div id="editDialog" style="padding: 10px">
    <form id="updateForm" method="post">
        <table id="editTable" border=0  cellpadding="8">
            <input type="hidden" name="id" id="update_id">
            <tr>
                <td>姓名:</td>
                <td>
                    <input id="update_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请输入用户名'" />
                </td>
            </tr>
            <tr>
                <td>密码:</td>
                <td><input id="update_password" style="width: 200px; height: 30px;" class="easyui-textbox" type="password" name="password" data-options="required:true, missingMessage:'请输入密码'" /></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>

