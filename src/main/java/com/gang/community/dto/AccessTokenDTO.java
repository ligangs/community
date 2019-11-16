package com.gang.community.dto;

import lombok.Data;

@Data
//封装发送post请时的参数
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;            //第一次访问，返回的code
    private String redirect_uri ;   //返回地址
    private String state;
}
