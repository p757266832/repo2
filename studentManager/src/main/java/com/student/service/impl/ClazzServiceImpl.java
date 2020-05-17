package com.student.service.impl;

import com.student.bean.Clazz;
import com.student.dao.ClazzMapper;
import com.student.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;
    @Override
    public int addClazz(Clazz clazz) {
        return clazzMapper.addClazz(clazz);
    }

    @Override
    public int updateClazz(Clazz clazz) {
        return clazzMapper.updateClazz(clazz);
    }

    @Override
    public int deleteClazz(String ids) {
        return clazzMapper.deleteClazz(ids);
    }

    @Override
    public List<Clazz> getClazzList(Map<String, Object> map) {
        return clazzMapper.getClazzList(map);
    }

    @Override
    public int getCount(Map<String, Object> map) {
        return clazzMapper.getCount(map);
    }

    @Override
    public List<Clazz> findAll() {
        return clazzMapper.findAll();
    }
}
