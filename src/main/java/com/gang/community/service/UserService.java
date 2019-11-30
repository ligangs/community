package com.gang.community.service;

import com.gang.community.mapper.UserMapper;
import com.gang.community.model.User;
import com.gang.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void addOrUpdate(User user) {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());

        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            userMapper.insert(user);
        } else {
            User dbUser = users.get(0);

            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setGmtModified(user.getGmtCreate());
            dbUser.setName(user.getName());
            dbUser.setBio(user.getBio());

            userMapper.updateByPrimaryKey(dbUser);
        }
    }
}
