package com.student.service;

import com.student.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

//service的作用是定义这个实体将有哪些操作
@Service
public interface UserService {
    //根据adminName查询admin
    public User findByAdminName(String username);
    //添加admin
    int addAdmin(User user);
    //查询admin列表
    public List<User> findAdminList(Map<String,Object> map);
    //获取总记录数
    public int getTotal(Map<String,Object> map);
    //修改admin
    public  int updateAdmin(User user);
    //删除admin
    public int deleteAdmin(String ids);
}
