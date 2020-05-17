package com.student.dao;

import com.student.bean.Grade;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GradeMapper {
    int addGrade(Grade grade);
    public  int updateGrade(Grade grade);
    public int deleteGrade(String ids);
    public List<Grade> getGradeList(Map<String,Object> map);
    public int getCount(Map<String,Object> map);
    public List<Grade> findAll();
}
