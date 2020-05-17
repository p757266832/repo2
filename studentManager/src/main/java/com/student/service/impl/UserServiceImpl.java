package com.student.service.impl;

import com.student.bean.User;
import com.student.dao.UserMapper;
import com.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired//作用从容器中拿出这个类
    private UserMapper userMapper;
    @Override
    public User findByAdminName(String username) {
        //具体实现是由adminMapper来完成
        return userMapper.findByAdminName(username);
    }

    @Override
    public int addAdmin(User user) {
        return userMapper.addAdmin(user);
    }

    @Override
    public List<User> findAdminList(Map<String, Object> map) {
        return userMapper.findAdminList(map);
    }

    @Override
    public int getTotal(Map<String, Object> map) {
        return userMapper.getTotal(map);
    }

    @Override
    public int updateAdmin(User user) {
        return userMapper.updateAdmin(user);
    }

    @Override
    public int deleteAdmin(String ids) {
        return userMapper.deleteAdmin(ids);
    }
}
