package com.student.dao;

import com.student.bean.Clazz;
import com.student.bean.Grade;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClazzMapper {
    int addClazz(Clazz clazz);
    public  int updateClazz(Clazz clazz);
    public int deleteClazz(String ids);
    public List<Clazz> getClazzList(Map<String, Object> map);
    public int getCount(Map<String, Object> map);
    public List<Clazz> findAll();
}
