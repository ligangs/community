package com.gang.community.controller;

import com.gang.community.dto.PageQuestionDTO;
import com.gang.community.model.User;
import com.gang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action")String action,
                          HttpServletRequest request,
                          @RequestParam(name = "currentPage",defaultValue = "1")Integer currentPage,
                          @RequestParam(name="pageSize",defaultValue = "5")Integer pageSize,
                          Model model){
        //从session 中得到user
        User user=(User)request.getSession().getAttribute("user");
        //用户未登录,重定向到首页
        if(user==null){
            return "redirect:/";
        }
        //得到对应用户的分页问题
        PageQuestionDTO pageQuestionDTO=questionService.getPageQuestionByUserId(currentPage,pageSize,user.getId());
        model.addAttribute("pageQuestionDTO", pageQuestionDTO);
        if ("questions".equals(action)) {
            model.addAttribute("section","questions");
            model.addAttribute("sectionName", "我的提问");

        } else if ("replies".equals(action)) {
            model.addAttribute("section","replies");
            model.addAttribute("sectionName", "我的回复");
        }
        return "profile";
    }
}
