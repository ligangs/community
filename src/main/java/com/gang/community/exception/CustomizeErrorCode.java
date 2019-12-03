package com.gang.community.exception;

public enum  CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"您找的问题已经不存在了，要不换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002,"请选择问题或评论再进行回复吧~"),
    NO_LOGIN(2003,"检测到您还未曾登录，请先登录再进行操作吧~" ),
    SYSTEM_ERROR(2004,"服务器出错啦，请换个姿势！！！" ),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在，你真的有认真操作吗？" ),
    COMMENT_NOT_FOUND(2006,"该评论已经不存在啦，换个试试吧~" ),
    COMMENT_IS_EMPTY(2007,"评论的内容为不能空哦~" );

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

}
