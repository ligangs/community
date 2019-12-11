package com.gang.community.controller;

import com.gang.community.dto.PaginationDTO;
import com.gang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(@RequestParam(name = "currentPage",defaultValue = "1")Integer currentPage,
                        @RequestParam(name="pageSize",defaultValue = "5")Integer pageSize,
                        Model model){
        //得到问题列表，回显首页
        PaginationDTO paginationDTO = questionService.getPageQuestion(currentPage, pageSize);
        model.addAttribute("pageInfo", paginationDTO);
        return "index";
    }
}
