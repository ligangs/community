package com.gang.community.controller;

import com.gang.community.dto.QuestionDTO;
import com.gang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id,
                           Model model) {
        questionService.incView(id);
        QuestionDTO questionDTO=questionService.getQuestionDTOById(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
