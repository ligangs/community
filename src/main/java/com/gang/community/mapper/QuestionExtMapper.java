package com.gang.community.mapper;

import com.gang.community.model.Question;

public interface QuestionExtMapper {
    //阅读数加一
    int incView(Question record);
}
