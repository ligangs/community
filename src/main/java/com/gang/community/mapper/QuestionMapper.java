package com.gang.community.mapper;

import com.gang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,tag,gmt_create,gmt_modified,creator)" +
            "values(#{title},#{description},#{tag},#{gmtCreate},#{gmtModified},#{creator})")
    int insert(Question question);

    @Select("select * from question order by gmt_create desc")
    List<Question> findAll();

    @Select("select * from question order by gmt_create desc limit #{currentPage},#{pageSize} ")
    List<Question> getPageQuestions(@Param("currentPage") int currentPage, @Param("pageSize") int pageSize);

    @Select("select count(1) from question")
    int getCount();

    @Select("select count(1) from question where creator=#{userId}")
    int getCountByUserId(Integer userId);

    @Select("select * from question where creator=#{userId} order by gmt_create desc limit #{offset},#{pageSize}")
    List<Question> getPageQuestionsByUserId(@Param("offset") Integer offset,@Param("pageSize") Integer pageSize, @Param("userId") Integer userId);

    @Select("select * from question where id=#{id}")
    Question getQuestionById(@Param("id") Integer id);
}
