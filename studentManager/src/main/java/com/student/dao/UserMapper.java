package com.student.dao;

import com.student.bean.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {
    public User findByAdminName(String username);
    int addAdmin(User user);
    public List<User> findAdminList(Map<String,Object> map);
    public int getTotal(Map<String,Object> map);
    public  int updateAdmin(User user);
    public int deleteAdmin(String ids);
}
