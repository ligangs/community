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
}
