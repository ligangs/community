package com.gang.community.mapper;

import com.gang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified) values(#{account_id}, #{name}, #{token}, #{gmt_create},#{gmt_modified})")
    int insertUser(User user);

    @Select("select * from user where token=#{token}")
    User findUserByToken(@Param("token") String token);
}
