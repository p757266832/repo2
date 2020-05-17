package com.student.controller;

import com.google.gson.Gson;
import com.student.bean.Clazz;
import com.student.bean.Grade;
import com.student.bean.Pager;
import com.student.bean.Student;
import com.student.service.ClazzService;
import com.student.service.GradeService;
import com.student.service.StudentService;
import com.student.util.MyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
    @Autowired
    private ClazzService clazzService;

    @Autowired
    private StudentService studentService;
    /*
    *
    * 学生列表页
    * */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView list(ModelAndView model) {
        model.setViewName("student/student_list");
        List<Clazz> clazzList = clazzService.findAll();
        Gson gson = new Gson();
        model.addObject("clazzList",clazzList);
        model.addObject("clazzListJson",gson.toJson(clazzList));
        return model;
    }
    @RequestMapping(value = "/getStudentList",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getStudentList(@RequestParam(value = "username",required = false,defaultValue = "") String username, @RequestParam(value = "clazzId",required = false) Integer clazzId, HttpServletRequest request, Pager pager) {
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> querymap = new HashMap<>();
        querymap.put("username","%"+username+"%");
        Object userType = request.getSession().getAttribute("userType");
        if("2".equals(userType.toString())) {
            Student loginStudent = (Student) request.getSession().getAttribute("admin");
            querymap.put("username","%"+loginStudent.getUsername()+"%");
        }
        if(clazzId !=null) {
            querymap.put("clazzId",clazzId);
        }
        /*起始索引*/
        querymap.put("offset",pager.getOffset());
        /*每页显示的记录数*/
        querymap.put("pageSize",pager.getRows());
        /*显示的数据*/
        map.put("rows",studentService.getStudentList(querymap));
        /*总共多少条记录*/
        map.put("total",studentService.getCount(querymap));
        return map;
    }
    @RequestMapping(value="/uploadPhoto",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> uploadPhoto(MultipartFile photo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,String> map = new HashMap<>();
        if(photo == null) {
            map.put("type","error");
            map.put("msg","请选择文件");
            return map;
        }
        if(photo.getSize() > 10485760) {
            map.put("type","error");
            map.put("msg","上传的文件不能大于10M");
            return map;
        }
        //获取后缀
        String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".") + 1,photo.getOriginalFilename().length());
        System.out.println(suffix);
        if(!"jpg,png,gif,jpeg".contains(suffix.toLowerCase())) {
            map.put("type","error");
            map.put("msg","文件格式不正确,请上传jpg,png,gif,jpeg格式的图片");
            return map;
        }
        //在服务器保存的绝对路径
        String savePath = request.getServletContext().getRealPath("/")+"\\upload\\";
        System.out.println(savePath);
        File savePathFile = new File(savePath);
        if(!savePathFile.exists()) {
            savePathFile.mkdir();//如果没有此文件夹,则创建upload文件夹
        }
        String fileName = new Date().getTime() +"." + suffix;
        //把文件保存到指定路径
        photo.transferTo(new File(savePath + fileName));
        //项目请求的根路径
        String path = request.getServletContext().getContextPath();
        System.out.println("项目请求的根路径:"+path);
        map.put("type","success");
        map.put("msg","头像上传成功");
        map.put("src",request.getServletContext().getContextPath() + "/upload/" + fileName);
        return map;
    }
    @ResponseBody
    @RequestMapping(value="/addStudent",method = RequestMethod.POST)
    public Map<String,String> addStudent(Student student) {
        Map<String,String> map = new HashMap<>();
        //设置学号的生成
        student.setSn(MyUtil.genernateSn("s",""));
        if(StringUtils.isEmpty(student.getUsername())) {
            map.put("type","error");
            map.put("msg","学生名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(student.getPassword())) {
            map.put("type","error");
            map.put("msg","登录密码不能为空");
            return map;
        }
        if(student.getClazzId() == null) {
            map.put("type","error");
            map.put("msg","请选择所属年级");
            return map;
        }
        if(isExist(student.getUsername(),null)) {
            map.put("type","error");
            map.put("msg","用户名已存在");
            return map;
        }
        if(studentService.addStudent(student) < 0) {
            map.put("type","error");
            map.put("msg","添加学生失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","学生添加成功");
        return map;
    }
    /*
    判断是否存在
    * */
    public boolean isExist(String username,Integer id) {
        Student student = studentService.findByUsername(username);
        if(student != null) {
            if(id == null) {
                return true;//添加,说明已存在该用户
            }
            if(student.getId().intValue() != id.intValue()) {
                return true;//修改,说明已存在该用户
            }
        }
        return false;
    }
    @ResponseBody
    @RequestMapping(value="/updateStudent",method = RequestMethod.POST)
    public Map<String,Object> updateStudent(Student student) {
        Map<String,Object> map = new HashMap<>();
        //设置学号的生成
        student.setSn(MyUtil.genernateSn("s",""));
        if(StringUtils.isEmpty(student.getUsername())) {
            map.put("type","error");
            map.put("msg","学生名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(student.getPassword())) {
            map.put("type","error");
            map.put("msg","登录密码不能为空");
            return map;
        }
        if(student.getClazzId() == null) {
            map.put("type","error");
            map.put("msg","请选择所属班级");
            return map;
        }
        if(isExist(student.getUsername(),student.getId())) {
            map.put("type","error");
            map.put("msg","该用户已存在");
            return map;
        }
        if(studentService.updateStudent(student) < 0) {
            map.put("type","error");
            map.put("msg","修改学生信息失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","学生信息修改成功");
        return map;
    }
    @ResponseBody
    @RequestMapping(value = "/deleteStudent",method = RequestMethod.POST)
    public Map<String,Object> deleteStudent(@RequestParam(value = "ids[]") Integer[] ids) {
        Map<String,Object> map = new HashMap<>();
        if(ids == null || ids.length <=0) {
            map.put("type","error");
            map.put("msg","请至少选择一条数据");
            return map;
        }
        try {
            if(studentService.deleteStudent(MyUtil.joinString(Arrays.asList(ids),",")) < 0) {
                map.put("type","error");
                map.put("msg","学生信息删除失败");
                return map;
            }
        } catch (Exception e) {
            map.put("type","error");
            map.put("msg","该学生下存在其他信息请谨慎删除");
            return map;
        }
        map.put("type","success");
        map.put("msg","学生信息删除成功");
        return map;
    }
}
