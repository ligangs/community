package com.gang.community.service;

import com.gang.community.mapper.UserMapper;
import com.gang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void addOrUpdate(User user) {
        User dbUser=userMapper.findUserByAccountId(user.getAccountId());
        if (dbUser == null) {
            userMapper.insertUser(user);
        } else {
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setGmtModified(user.getGmtCreate());
            dbUser.setName(user.getName());
            dbUser.setBio(user.getBio());
            userMapper.updateUser(dbUser);
        }
    }
}
