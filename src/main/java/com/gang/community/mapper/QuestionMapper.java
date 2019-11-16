package com.gang.community.mapper;

import com.gang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,tag,gmt_create,gmt_modified,creator)" +
            "values(#{title},#{description},#{tag},#{gmtCreate},#{gmtModified},#{creator})")
    public int insert(Question question);
}
