package com.gang.community.dto;

import com.gang.community.exception.CustomizeErrorCode;
import com.gang.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO {

    private Integer code;
    private String message;

    public static ResultDTO errorOf(Integer code,String message) {
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.code = code;
        resultDTO.message = message;
        return resultDTO;
    }
    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.code = 200;
        resultDTO.message = "操作成功";
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }
}
