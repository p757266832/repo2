package com.student.controller;

import com.student.bean.Grade;
import com.student.bean.Pager;
import com.student.dao.GradeMapper;
import com.student.service.GradeService;
import com.student.util.MyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/grade")
@Controller
public class GraderController {
    @Autowired
    private GradeService gradeService;
    /*
    *
    * 年级列表页
    * */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView list(ModelAndView model) {
        model.setViewName("grade/grade_list");
        return model;
    }
    /*
    * 查询年级信息
    * */
    @RequestMapping(value="/gradeList",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getGradeList(@RequestParam(value = "name",required = false,defaultValue = "") String name, Pager pager) {
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> querymap = new HashMap<>();
        /*年级名*/
        querymap.put("name","%"+name+"%");
        /*起始索引*/
        querymap.put("offset",pager.getOffset());
        /*每页显示的多少条数据*/
        querymap.put("pageSize",pager.getRows());
        /*每页显示的记录*/
        map.put("rows", gradeService.getGradeList(querymap));
        /*总共有多少条记录*/
        map.put("total",gradeService.getCount(querymap));
        return map;
    }
    /*
    * 增加年级信息
    * */
    @RequestMapping(value = "/addGrade",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> addGrade(Grade grade) {
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isEmpty(grade.getName())) {
            map.put("type","error");
            map.put("msg","年级不能为空");
            return map;
        }
        if(StringUtils.isEmpty(grade.getRemark())) {
            map.put("type","error");
            map.put("msg","备注不能为空");
            return map;
        }
        if(gradeService.addGrade(grade) < 0) {
            map.put("type","error");
            map.put("msg","添加年级失败");
            return map;
        }
        map.put("type","error");
        map.put("msg","添加年级成功");
        return map;
    }
    /*
    * 修改年级信息
    * */
    @ResponseBody
    @RequestMapping(value = "/updateGrade",method = RequestMethod.POST)
    public Map<String,String> updateGrade(Grade grade) {
        Map<String,String> map = new HashMap<>();
        if(grade == null) {
            map.put("type","error");
            map.put("msg","请联系开发人员");
            return map;
        }
        if(StringUtils.isEmpty(grade.getName())) {
            map.put("type","error");
            map.put("msg","年级名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(grade.getRemark())) {
            map.put("type","error");
            map.put("msg","备注不能为空");
            return map;
        }
        if(gradeService.updateGrade(grade) < 0) {
            map.put("type","error");
            map.put("msg","修改失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","修改成功");
        return map;
    }
    /*
    * 删除年级信息
    *
    * */
    @ResponseBody
    @RequestMapping(value = "/deleteGrade",method = RequestMethod.POST)
    public Map<String,Object> deleteGrade(@RequestParam(value = "ids[]") Integer[] ids) {
        Map<String,Object> map = new HashMap<>();
        if(ids == null) {
            map.put("type","error");
            map.put("msg","请至少选择一条数据");
            return map;
        }
      /*  String idString = "";
        for(Integer id:ids) {
            idString += id +",";
        }
        idString = idString.substring(0,idString.length() - 1);*/
        try {
            if(gradeService.deleteGrade(MyUtil.joinString(Arrays.asList(ids),",")) <= 0){
                map.put("type","error");
                map.put("msg","删除失败");
                return map;
            }
        } catch (Exception e) {
            map.put("type","error");
            map.put("msg","年级下存在班级信息,请勿冲动");
            return map;
        }
        map.put("type","success");
        map.put("msg","年级删除成功");

        return map;
    }
}
