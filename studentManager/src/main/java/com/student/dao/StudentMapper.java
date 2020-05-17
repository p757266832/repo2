package com.student.dao;

import com.student.bean.Student;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StudentMapper {
    public Student findByUsername(String username);
    int addStudent(Student student);
    public  int updateStudent(Student student);
    public int deleteStudent(String ids);
    public List<Student> getStudentList(Map<String, Object> map);
    public int getCount(Map<String, Object> map);
    public List<Student> findAll();
}
