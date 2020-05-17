package com.student.service.impl;

import com.student.bean.Student;
import com.student.dao.StudentMapper;
import com.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student findByUsername(String username) {
        return studentMapper.findByUsername(username);
    }

    @Override
    public int addStudent(Student student) {
        return studentMapper.addStudent(student);
    }

    @Override
    public int updateStudent(Student student) {
        return studentMapper.updateStudent(student);
    }

    @Override
    public int deleteStudent(String ids) {
        return studentMapper.deleteStudent(ids);
    }

    @Override
    public List<Student> getStudentList(Map<String, Object> map) {
        return studentMapper.getStudentList(map);
    }

    @Override
    public int getCount(Map<String, Object> map) {
        return studentMapper.getCount(map);
    }

    @Override
    public List<Student> findAll() {
        return studentMapper.findAll();
    }
}
