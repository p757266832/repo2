<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
    <%
        String path = request.getContextPath();
        request.setAttribute("path",path);
    %>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link href="${path}/resources/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="${path}/resources/h-ui/css/H-ui.login.css" rel="stylesheet" type="text/css" />
<link href="${path}/resources/h-ui/lib/icheck/icheck.css" rel="stylesheet" type="text/css" />
<link href="${path}/resources/h-ui/lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css" href="${path}/resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${path}/resources/easyui/themes/icon.css">
<script type="text/javascript" src="${path}/resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${path}/resources/h-ui/js/H-ui.js"></script>
<script type="text/javascript" src="${path}/resources/h-ui/lib/icheck/jquery.icheck.min.js"></script>
<script type="text/javascript" src="${path}/resources/easyui/jquery.easyui.min.js"></script>

<title>登录|学生信息管理系统</title>
<meta name="keywords" content="学生信息管理系统">
</head>
<body>

<div class="header" style="padding: 0;">
	<h2 style="color: white; width: 400px; height: 60px; line-height: 60px; margin: 0 0 0 30px; padding: 0;">学生信息管理系统</h2>
</div>
<div class="loginWraper">
  <div id="loginform" class="loginBox">
    <form id="form" class="form form-horizontal" method="post">
      <div class="row cl">
        <label class="form-label col-3"><i class="Hui-iconfont">&#xe60d;</i></label>
        <div class="formControls col-8">
          <input id="username" name="username" type="text" placeholder="账户" class="input-text size-L">
        </div>
      </div>
      <div class="row cl">
        <label class="form-label col-3"><i class="Hui-iconfont">&#xe60e;</i></label>
        <div class="formControls col-8">
          <input id="password" name="password" type="password" placeholder="密码" class="input-text size-L">
        </div>
      </div>
      <div class="row cl">
        <div class="formControls col-8 col-offset-3">
          <input class="input-text size-L" id="vcode" name="vcode" type="text" placeholder="请输入验证码" style="width: 200px;">
          <img title="点击图片切换验证码" id="vcodeImg" src="${path}/admin/getCpacha?vl=4&width=160&height=40"></div>
      </div>
      
      <div class="mt-20 skin-minimal" style="text-align: center;">
		<div class="radio-box">
			<input type="radio" id="radio-2"  name="type" value="2" />
			<label for="radio-1">学生</label>
		</div>
		<div class="radio-box">
			<input type="radio" id="radio-1"  name="type" value="1" checked="checked"/>
			<label for="radio-2">管理员</label>
		</div>
	</div>
      
      <div class="row">
        <div class="formControls col-8 col-offset-3">
          <input id="submitBtn" type="button" class="btn btn-success radius size-L" value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;">
        </div>
      </div>
    </form>
  </div>
</div>
<div class="footer">Copyright &nbsp; gaowenbo @ bobo </div>


</body>
<script type="text/javascript">

   $(function () {
       //点击切换验证码
       $("#vcodeImg").click(function () {
           //通过获取点击时的当前时间并且拼接在url后面加载不同的图片（url参数不同，所以图片不同）
           this.src = "${path}/admin/getCpacha?vl=4&width=160&height=40&t="+new Date().getTime();
       })
       $("#submitBtn").click(function () {
      //序列化表单,显示key=value&key=value的形式
            var data = $("#form").serialize();
           $.ajax({
               type:"post",
               url:"${path}/admin/loginUser",
               //  url:"login",
               data:data,
               success:function (data) {
                 if(data.type=="success") {
                       window.parent.location.href="${path}/admin/index";
                   } else {
                      // console.log(data);
                       $.messager.alert("消息提示",data.msg,"warning");
                       //切换验证码
                       $("#vcodeImg").click();
                       //清空验证码输入框
                       $("input[name='vcode']").val("");
                   }
               },
               dataType:"JSON"
           });
       });
   })
</script>
</html>