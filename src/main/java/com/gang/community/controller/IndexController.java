package com.gang.community.controller;

import com.gang.community.dto.PageQuestionDTO;
import com.gang.community.dto.QuestionDTO;
import com.gang.community.mapper.QuestionMapper;
import com.gang.community.mapper.UserMapper;
import com.gang.community.model.Question;
import com.gang.community.model.User;
import com.gang.community.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(@RequestParam(name = "currentPage",defaultValue = "1")Integer currentPage,
                        @RequestParam(name="pageSize",defaultValue = "5")Integer pageSize,
                        Model model){
        //得到问题列表，回显首页
        PageQuestionDTO pageQuestionDTO = questionService.getPageQuestion(currentPage, pageSize);
        model.addAttribute("pageQuestionDTO", pageQuestionDTO);
        return "index";
    }
}
