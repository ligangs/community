package com.gang.community.mapper;

import com.gang.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified,avatar_url) values(#{accountId}, #{name}, #{token}, #{gmtCreate},#{gmtModified},#{avatarUrl})")
    int insertUser(User user);

    @Select("select * from user where token=#{token}")
    User findUserByToken(@Param("token") String token);

    @Select("select * from user where id=#{id}")
    User findUserById(@Param("id")int id);

    @Select("select * from user where account_id=#{accountId}")
    User findUserByAccountId(@Param("accountId")String accountId);

    @Update(" update user set name=#{name}, token=#{token},gmt_modified=#{gmtModified},bio=#{bio},avatar_url=#{avatarUrl}  where account_id=#{accountId}")
    int updateUser(User dbUser);
}
