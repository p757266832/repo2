<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2020/4/14
  Time: 22:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    request.setAttribute("path",path);
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>班级列表</title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${path}/resources/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${path}/resources/easyui/css/demo.css">
    <script type="text/javascript" src="${path}/resources/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${path}/resources/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${path}/resources/easyui/js/validateExtends.js"></script>
    <script type="text/javascript">
       var clazzListJson = ${clazzListJson}
        $(function() {
            var table;
            //datagrid初始化 
            $('#dataList').datagrid({
                title:'班级列表',
                iconCls:'icon-more',//图标 
                border: true,
                collapsible:false,//是否可折叠的 
                fit: true,//自动大小 
                method: "post",
                /*这里有问题*/
                url:"${path}/student/getStudentList?t="+new Date().getTime(),
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
                    {field:'username',title:'学生姓名',width:150, sortable: true},
                    {field:'password',title:'登录密码',width:150, sortable: true},
                    {field:'photo',title:'图片',width:100,formatter:function (val,index,row) {
                            return '<img src = '+val+' width = "100px "/>';
                        }},
                    {field:'sex',title:'性别',width:150},
                    {field:'sn',title:'学号',width:150, sortable: true},
                    {field:'clazzId',title:'所属班级',width:150, sortable: true,
                        formatter:function (val,index,row) {
                            for(var i = 0; i < clazzListJson.length; i++) {
                                if(clazzListJson[i].id == val) {
                                    return clazzListJson[i].name;
                                }
                            }
                            return val;
                 }},
                    {field:'remark',title:'备注',width:200},
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
                    $.messager.confirm("消息提醒", "如果班级下存在学生信息将无法删除,请先删除学生信息", function(r){
                        if(r){
                            $.ajax({
                                type: "post",
                                url: "${path}/student/deleteStudent",
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
            <!--有错添加以后窗口关闭不了-->
            //设置添加窗口
            $("#addDialog").dialog({
                title: "添加班级",
                width: 450,
                height: 650,
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
                                    url: "${path}/student/addStudent",
                                    data: data,
                                    dataType:'json',
                                    success: function(data){
                                        if(data.type == "success"){
                                            $.messager.alert("消息提醒","添加成功!","info");
                                            //关闭窗口
                                            $("#addDialog").dialog("close");
                                            //清空原表格数据
                                            $("#add_username").textbox('setValue', "");
                                            $("#add_password").textbox('setValue', "");
                                            $("#add_clazzId").textbox('setValue', "");
                                            $("#add_sex").textbox('setValue', "");
                                            $("#add_remark").textbox('setValue', "");
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
                /*关闭的时候*/
                onClose: function(){
                    $("#add_username").textbox('setValue', "");
                    $("#add_password").textbox('setValue', "");
                    $("#add_clazzId").textbox('setValue', "");
                    $("#add_sex").textbox('setValue', "");
                    $("#add_remark").textbox('setValue', "");
                }
            });

            //编辑班级信息
            $("#editDialog").dialog({
                title: "修改班级信息",
                width: 450,
                height: 650,
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
                                    url: "${path}/student/updateStudent",
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
                    $("#update_clazzId").combobox('setValue',selectRow.clazzId);
                    $("#update_sex").textbox('setValue', selectRow.sex);
                    $("#update_remark").textbox('setValue', selectRow.remark);
                    $("#update-photo-preview").attr("src",selectRow.photo);
                    $("#update_photo").val(selectRow.photo);

                },
            });
            /*给搜索添加事件*/
           $("#search-btn").click(function () {
                $('#dataList').datagrid('reload',{
                    username:$("#search-name").textbox('getValue'),
                    clazzId:$("#search-clazzId").combobox('getValue')
                });
            });
            //上传图片按钮
            $("#upload-btn").click(function(){
                if($("#add-upload-photo").filebox("getValue") == ""){
                    $.messager.alert("消息提醒","请选择图片文件!","warning");
                    return;
                }
                $("#photoForm").submit();
            });
          $("#update-upload-btn").click(function(){
                if($("#update-upload-photo").filebox("getValue") == ""){
                    $.messager.alert("消息提醒","请选择图片文件!","warning");
                    return;
                }
                $("#updatephotoForm ").submit();
            });
        });
       function uploaded(e) {
           var data = $(window.frames["photo_target"].document).find("body pre").text();
           if(data == "") {
               return;
           }
           data = JSON.parse(data);
           if(data.type=="success") {
               $.messager.alert("消息提醒","图片上传成功!","info");
               $("#photo-preview").attr("src",data.src);
               $("#update-photo-preview").attr("src",data.src);

               $("#add-upload-photo").val(data.src);
               $("#update_photo").val(data.src);

            /*   $("#update-photo-preview").attr("src",selectRow.photo);
               $("#update_photo").val(selectRow.photo);*/

           } else {
               $.messager.alert("消息提醒",data.msg,"warning");
           }
       }
    </script>
</head>
<body>
<!-- 数据列表 -->
<table id="dataList" cellspacing="0" cellpadding="0">

</table>
<!-- 工具栏 -->
<div id="toolbar">
    <c:if test="${userType==1}">
    <div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
    <div style="float: left;" class="datagrid-btn-separator"></div>
    </c:if>
    <div style="float: left;"><a id="edit" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></div>
    <div style="float: left;" class="datagrid-btn-separator"></div>

    <div>
       <c:if test="${userType==1}">
        <a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a>
       </c:if>
        <%-- 这里加入了搜索框--%>
        学生名：<input id="search-name" class="easyui-textbox"/>
        所属年级：<select id="search-clazzId" class="easyui-combobox" style="width: 150px;">
                    <option value="">请选择</option>
                    <c:forEach items="${clazzList}" var = "clazz">
                        <option value="${clazz.id}">${clazz.name}</option>
                    </c:forEach>
         </select>
        <a id="search-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a>
    </div>
</div>
<!-- 添加窗口 -->
<div id="addDialog" style="padding: 10px;">
    <form id="photoForm" method="post" enctype="multipart/form-data" action="${path}/student/uploadPhoto" target="photo_target">
        <table id="addTable1" cellpadding="8">
            <tr >
                <td>预览头像:</td>
                <td>
                    <img id="photo-preview" alt="照片" style="max-width: 100px; max-height: 100px;" title="照片" src="${path}/resources/easyui/photo/student.jpg" />
                </td>
            </tr>
            <tr >
                <td>学生头像:</td>
                <td>
                    <input id="add-upload-photo" class="easyui-filebox" name="photo" data-options="prompt:'选择照片'" style="width:200px;">
                    <a id="upload-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">上传图片</a>
                </td>
            </tr>
        </table>
    </form>
    <form id="addForm" method="post">
        <table id="addTable" cellpadding="8">
            <input id="add_photo" type="hidden" name="photo" value="${path}/resources/easyui/photo/student.jpg"/>
            <tr>
                <td>学生姓名:</td>
                <td>
                    <input id="add_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请填写学生姓名'"  />
                </td>
            </tr>
            <tr>
                <td>登录密码:</td>
                <td>
                    <input id="add_password"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="password" data-options="required:true, missingMessage:'请填写登录密码'"  />
                </td>
            </tr>
            <tr >
                <td>所属班级:</td>
                <td>
                    <select id="add_clazzId"  class="easyui-combobox" style="width: 200px;"  name="clazzId" data-options="required:true, missingMessage:'请选择所属班级'">
                        <option>请选择</option>
                        <c:forEach items="${clazzList}" var = "clazz">
                            <option value="${clazz.id}">${clazz.name}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr >
                <td>学生性别:</td>
                <td>
                    <select id="add_sex"  class="easyui-combobox" style="width: 200px;" name="sex" data-options="required:true, missingMessage:'请选择学生性别'">
                        <option value="男">男</option>
                        <option value="女">女</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>备注:</td>
                <td><input id="add_remark" style="width: 256px; height: 180px;" class="easyui-textbox" type="text" name="remark" data-options="multiline:true"  /></td>
            </tr>
        </table>
    </form>
</div>

<!-- 修改窗口 -->
<div id="editDialog" style="padding: 10px">
    <form id="updatephotoForm" method="post" enctype="multipart/form-data" action="${path}/student/uploadPhoto" target="photo_target">
        <table id="editTable1" cellpadding="8">
            <tr >
                <td>预览头像:</td>
                <td>
                    <img id="update-photo-preview" alt="照片" style="max-width: 100px; max-height: 100px;" title="照片" src="${path}/resources/easyui/photo/student.jpg" />
                </td>
            </tr>
            <tr >
                <td>学生头像:</td>
                <td>
                    <input id="update-upload-photo" class="easyui-filebox" name="photo" data-options="prompt:'选择照片'" style="width:200px;">
                    <a id="update-upload-btn" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">上传图片</a>
                </td>
            </tr>
        </table>
    </form>
    <form id="updateForm" method="post">
        <input id="update_photo" type="hidden" name="photo" value="${path}/resources/easyui/photo/student.jpg"/>
        <table id="editTable2" border=0  cellpadding="8">
            <input type="hidden" name="id" id="update_id">
            <tr >
                <td>学生名:</td>
                <td>
                    <input id="update_username"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="username" data-options="required:true, missingMessage:'请填写班级名'"  />
                </td>
            </tr>
            <tr >
                <td>登录密码:</td>
                <td>
                    <input id="update_password"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="password" data-options="required:true, missingMessage:'请填写班级名'"  />
                </td>
            </tr>
            <tr >
                <td>所属班级:</td>
                <td>
                    <select id="update_clazzId"  class="easyui-combobox" style="width: 200px;"  name="clazzId" data-options="required:true, missingMessage:'请选择所属年级'">
                        <option>请选择</option>
                        <c:forEach items="${clazzList}" var = "clazz">
                            <option value="${clazz.id}">${clazz.name}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr >
                <td>学生性别:</td>
                <td>
                    <select id="update_sex"  class="easyui-combobox" style="width: 200px;" name="sex" data-options="required:true, missingMessage:'请选择学生性别'">
                        <option value="男">男</option>
                        <option value="女">女</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>备注:</td>
                <td><input id="update_remark" style="width: 256px; height: 180px;" class="easyui-textbox" type="text" name="remark" data-options="multiline:true"  /></td>
            </tr>
        </table>
    </form>
</div>
<!-- 提交表单处理iframe框架 -->
  <iframe id="photo_target" name="photo_target" onload="uploaded(this)"></iframe>
</body>
</html>

