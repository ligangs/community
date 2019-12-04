package com.gang.community.mapper;


import com.gang.community.model.Comment;

public interface CommentExtMapper {
    //增加评论数
    int incComment(Comment record);
}
