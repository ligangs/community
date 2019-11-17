package com.gang.community.controller;

import com.gang.community.dto.QuestionDTO;
import com.gang.community.mapper.QuestionMapper;
import com.gang.community.mapper.UserMapper;
import com.gang.community.model.Question;
import com.gang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
        //得到问题列表，回显首页
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        List<Question> questions=questionMapper.findAll();
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(userMapper.findUserById(question.getCreator()));
            questionDTOS.add(questionDTO);
        }
        model.addAttribute("questionDTOS", questionDTOS);

        //判断是否登录
        Cookie[] cookies = request.getCookies();
        if(cookies==null)
            return "index";
        for(Cookie cookie:cookies) {
            if (cookie.getName().equals("token")) {
                User user = userMapper.findUserByToken(cookie.getValue());
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                    break;
                }
            }
        }
        return "index";
    }
}
