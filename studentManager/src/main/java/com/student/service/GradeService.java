package com.student.service;

import com.student.bean.Grade;

import java.util.List;
import java.util.Map;

public interface GradeService {
    int addGrade(Grade grade);
    public  int updateGrade(Grade grade);
    public int deleteGrade(String ids);
    public List<Grade> getGradeList(Map<String,Object> map);
    public int getCount(Map<String,Object> map);
    public List<Grade> findAll();
}
