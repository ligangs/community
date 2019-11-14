package com.gang.community.controller;

import com.alibaba.fastjson.JSON;
import com.gang.community.dto.AccessTokenDTO;
import com.gang.community.dto.GitHubUser;
import com.gang.community.provide.GitHubProvide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.websocket.server.PathParam;
import java.io.IOException;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvide gitHubProvide;

    @Value("${github.Client_id}")
    private String client_id;
    @Value("${github.Client_secret}")
    private String client_secret;
    @Value("${github.Redirect_uri}")
    private String redircet_uri;

    @GetMapping("/callback")
    public String callback(@PathParam("code") String code,
                           @PathParam("state") String state) {
        String post = null;
        String access_token = null;
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setRedirect_uri(redircet_uri);
        accessTokenDTO.setClient_secret(client_secret);

        try {
            //post=access_token=31acd25e400b583288e293cba945c4f5a3ae355d&scope=user&token_type=bearer
            post = gitHubProvide.post("https://github.com/login/oauth/access_token", JSON.toJSONString(accessTokenDTO));

            //得到access_token，没次的到的access_token不一样
            access_token = post.split("&")[0].split("=")[1];

            //发送get请求https://api.github.com/user，得到用户信息api
            String userAPI = gitHubProvide.run("https://api.github.com/user?" + post.toString());
            //将返回json 数据封装成对象
            GitHubUser gitHubUser = JSON.parseObject(userAPI, GitHubUser.class);
            System.out.println(gitHubUser);
        } catch (IOException e) {
        } finally {
            return "index";
        }
    }
}
