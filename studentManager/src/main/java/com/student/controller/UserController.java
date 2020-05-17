package com.student.controller;

import com.student.bean.User;
import com.student.bean.Pager;
import com.student.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    //用户管理列表页面
    @RequestMapping("/list")
    public String userList() {
        return "user/user_list";
    }
    /*
    * 获取管理员列表
    * */
    @RequestMapping(value = "/getAdminList",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getAdminList(@RequestParam(value = "username",required = false,defaultValue = "") String username, Pager pager) {
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("username","%"+username+"%");
        /*起始索引*/
        queryMap.put("offset",pager.getOffset());
        /*每页显示的记录数*/
        queryMap.put("pageSize",pager.getRows());
        /*查询的数据*/
        map.put("rows", userService.findAdminList(queryMap));
        /*总记录数*/
        map.put("total",userService.getTotal(queryMap));
        return map;
    }

    /*
    * 添加用户
    *
    * */
    @ResponseBody
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public Map<String,String> addUser(User user) {
        Map<String, String> map = new HashMap<>();
        if(user ==null) {
            map.put("type","error");
            map.put("msg","数据绑定失败请联系开发人员");
            return map;
        }
        if(StringUtils.isEmpty(user.getUsername())) {
            map.put("type","error");
            map.put("msg","用户名不能为空");
            return  map;
        }
        if(StringUtils.isEmpty(user.getUsername())) {
            map.put("type","error");
            map.put("msg","密码不能为空");
            return map;
        }
        //添加之前先判断是否存在该用户
        User existUser = userService.findByAdminName(user.getUsername());
        if(existUser !=null) {
            map.put("type","error");
            map.put("msg","用户名已存在");
            return map;
        }
       if(userService.addAdmin(user)<=0) {
            map.put("type","error");
            map.put("msg","添加失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","添加成功");
        return  map;
    }
    /*
     * 修改用户
     *
     * */
    @ResponseBody
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public Map<String,String> updateUser(User user) {
        Map<String, String> map = new HashMap<>();
        if(user ==null) {
            map.put("type","error");
            map.put("msg","数据绑定失败请联系开发人员");
            return map;
        }
        if(StringUtils.isEmpty(user.getUsername())) {
            map.put("type","error");
            map.put("msg","用户名不能为空");
            return  map;
        }
        if(StringUtils.isEmpty(user.getUsername())) {
            map.put("type","error");
            map.put("msg","密码不能为空");
            return map;
        }
        //添加之前先判断是否存在该用户
        User existUser = userService.findByAdminName(user.getUsername());
        if(existUser !=null) {
            if(user.getId() != existUser.getId()) {
                map.put("type","error");
                map.put("msg","用户名已存在");
                return map;
            }
        }
        if(userService.updateAdmin(user)<=0) {
            map.put("type","error");
            map.put("msg","修改失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","修改成功");
        return  map;
    }
    @ResponseBody
    @RequestMapping(value = "/deleteUser",method = RequestMethod.POST)
    public Map<String,Object> deleteUser(@RequestParam("ids[]") Integer[] ids) {
        Map<String,Object> map = new HashMap<>();
        if(ids == null) {
            map.put("type","error");
            map.put("msg","请至少选择一条数据");
            return map;
        }
        //map.put("ids",ids);
        String idString = "";
        for(Integer id:ids) {
            idString += id + ",";
        }
        idString = idString.substring(0,idString.length() - 1);
     //   System.out.println(idString);
        if(userService.deleteAdmin(idString) < 0) {
            map.put("type","error");
            map.put("msg","删除失败");
            return  map;
        }
        map.put("type","success");
        map.put("msg","删除成功");
        return  map;
    }
}
