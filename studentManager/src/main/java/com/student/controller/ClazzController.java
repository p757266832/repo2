package com.student.controller;

import com.google.gson.Gson;
import com.student.bean.Clazz;
import com.student.bean.Grade;
import com.student.bean.Pager;
import com.student.dao.GradeMapper;
import com.student.service.ClazzService;
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
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/clazz")
@Controller
public class ClazzController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private ClazzService clazzService;
    /*
    *
    * 年级列表页
    * */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView list(ModelAndView model) {
        model.setViewName("clazz/clazz_list");
        List<Grade> list = gradeService.findAll();
        Gson gson = new Gson();
        String sJson = gson.toJson(list);
        model.addObject("gradeList",list);
        model.addObject("gradeListJson",sJson);
        return model;
    }
    @ResponseBody
    @RequestMapping(value = "/getClazzList",method = RequestMethod.POST)
    public Map<String,Object> getClazzList(@RequestParam(value = "name",required = false,defaultValue = "") String name,
                                           @RequestParam(value="gradeId",required = false) Integer gradeId,
                                           Pager pager) {
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> querymap = new HashMap<>();
        /*班级名*/
        querymap.put("name","%"+name+"%");
        if(gradeId != null) {
            querymap.put("gradeId",gradeId);
        }
        /*起始索引*/
        querymap.put("offset",pager.getOffset());
        /*每页显示大小*/
        querymap.put("pageSize",pager.getRows());
        /*显示的数据*/
        map.put("rows",clazzService.getClazzList(querymap));
        /*总共多少条记录*/
        map.put("total",clazzService.getCount(querymap));
        return map;
    }
    @ResponseBody
    @RequestMapping(value="/addClazz",method = RequestMethod.POST)
    public Map<String,String> addClazz(Clazz clazz) {
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isEmpty(clazz.getName())) {
            map.put("type","error");
            map.put("msg","班级不能为空");
            return map;
        }
        if(clazz.getGradeId() == null) {
            map.put("type","error");
            map.put("msg","请选择所属年级");
            return map;
        }
        if(StringUtils.isEmpty(clazz.getRemark())) {
            map.put("type","error");
            map.put("msg","备注不能为空");
            return map;
        }
        if(clazzService.addClazz(clazz) < 0) {
            map.put("type","error");
            map.put("msg","添加失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","添加成功");
        return map;

    }
    @ResponseBody
    @RequestMapping(value="/updateClazz",method = RequestMethod.POST)
    public Map<String,String> updateClazz(Clazz clazz) {
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isEmpty(clazz.getName())) {
            map.put("type","error");
            map.put("msg","班级不能为空");
            return map;
        }
        if(clazz.getGradeId() == null) {
            map.put("type","error");
            map.put("msg","请选择所属年级");
            return map;
        }
        if(StringUtils.isEmpty(clazz.getRemark())) {
            map.put("type","error");
            map.put("msg","备注不能为空");
            return map;
        }
        if(clazzService.addClazz(clazz) < 0) {
            map.put("type","error");
            map.put("msg","班级修改失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","班级成功");
        return map;
    }
    @RequestMapping(value="/deleteClazz",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteClazz(@RequestParam(value="ids[]") Integer[] ids) {
        Map<String,Object> map = new HashMap<>();
        if(ids == null || ids.length ==0) {
            map.put("type","error");
            map.put("msg","请至少选择一条数据");
            return map;
        }
        try {
            if(clazzService.deleteClazz(MyUtil.joinString(Arrays.asList(ids),",")) < 0) {
                map.put("type","error");
                map.put("msg","删除失败");
                return map;
            }
        } catch (Exception e) {
            map.put("type","error");
            map.put("msg","班级下存在学生信息,请谨慎删除");
            return map;
        }
        map.put("type","success");
        map.put("msg","班级删除成功");
        return map;
    }
}
