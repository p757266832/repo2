package com.student.interceptor;

import com.google.gson.Gson;
import com.student.bean.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/*
* 自定义登录拦截器
* */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
   //     System.out.println("进入拦截器uri： "+requestURI);
        //获取session中的admin
        Object admin = request.getSession().getAttribute("admin");
        if(admin ==null) {
            //未登录成功或登录状态失效
            System.out.println("未登录或登录失效uri： "+requestURI);
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                //ajax请求
                Map<String,String> map = new HashMap<>();
                map.put("type","error");
                map.put("msg","登录状态已失效请重新登录");
                response.getWriter().write(new Gson().toJson(map));
                return false;
            }
            //转向登录页面
            response.sendRedirect(request.getContextPath()+"/admin/login");
            return false;
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
