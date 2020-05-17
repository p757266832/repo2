package com.student.service.impl;

import com.student.bean.Grade;
import com.student.dao.GradeMapper;
import com.student.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class GradeServiceImpl implements GradeService {
    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public int addGrade(Grade grade) {
        return gradeMapper.addGrade(grade);
    }

    @Override
    public int updateGrade(Grade grade) {
        return gradeMapper.updateGrade(grade);
    }

    @Override
    public int deleteGrade(String ids) {
        return gradeMapper.deleteGrade(ids);
    }

    @Override
    public List<Grade> getGradeList(Map<String, Object> map) {
        return gradeMapper.getGradeList(map);
    }

    @Override
    public int getCount(Map<String, Object> map) {
        return 0;
    }

    @Override
    public List<Grade> findAll() {
        return gradeMapper.findAll();
    }


}
