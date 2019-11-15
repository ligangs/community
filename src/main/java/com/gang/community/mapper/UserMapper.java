package com.gang.community.mapper;

import com.gang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified) values(#{account_id}, #{name}, #{token}, #{gmt_create},#{gmt_modified})")
    int insertUser(User user);

}
