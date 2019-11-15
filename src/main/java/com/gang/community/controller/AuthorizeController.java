package com.gang.community.controller;

import com.alibaba.fastjson.JSON;
import com.gang.community.dto.AccessTokenDTO;
import com.gang.community.dto.GitHubUser;
import com.gang.community.mapper.UserMapper;
import com.gang.community.model.User;
import com.gang.community.provide.GitHubProvide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvide gitHubProvide;

    @Autowired
    private UserMapper userMapper;


    @Value("${github.Client_id}")
    private String client_id;
    @Value("${github.Client_secret}")
    private String client_secret;
    @Value("${github.Redirect_uri}")
    private String redirect_uri;

    @GetMapping("/callback")
    public String callback(@PathParam("code") String code,
                           @PathParam("state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        String post = null;
        String access_token = null;
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setClient_secret(client_secret);

        GitHubUser gitHubUser = null;
        try {
            //post=access_token=31acd25e400b583288e293cba945c4f5a3ae355d&scope=user&token_type=bearer
            post = gitHubProvide.post("https://github.com/login/oauth/access_token", JSON.toJSONString(accessTokenDTO));

            //得到access_token，没次的到的access_token不一样
            access_token = post.split("&")[0].split("=")[1];

            //发送get请求https://api.github.com/user，得到用户信息api
            String userAPI = gitHubProvide.run("https://api.github.com/user?" + post.toString());
            //System.out.println(post);
            //将返回json 数据封装成对象
            gitHubUser = JSON.parseObject(userAPI, GitHubUser.class);
        } catch (IOException e) {
        } finally {
            if (gitHubUser != null) {
                //登录成功
                User user = new User();
                user.setAccount_id(String.valueOf(gitHubUser.getId()));
                user.setName(gitHubUser.getName());
                user.setToken(UUID.randomUUID().toString());
                user.setGmt_create(System.currentTimeMillis());
                user.setGmt_modified(user.getGmt_create());

                Cookie tokenCookie = new Cookie("token", user.getToken());
                //可以通过设置Cookie的MaxAge，设置cookie的有效时间，默认有效时间为一次会话
                //tokenCookie.setMaxAge(60*1);
                response.addCookie(tokenCookie);

                userMapper.insertUser(user);
                //将的到的用户信息存入Session
                request.getSession().setAttribute("user", user);
                return "redirect:/";
            } else {
                //登录失败
                return "redirect:/";
            }

        }
    }
}
