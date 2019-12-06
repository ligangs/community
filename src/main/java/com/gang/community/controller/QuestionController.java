package com.gang.community.controller;

import com.gang.community.dto.CommentDTO;
import com.gang.community.dto.QuestionDTO;
import com.gang.community.enums.CommentTypeEnum;
import com.gang.community.model.Question;
import com.gang.community.service.CommentService;
import com.gang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Long id,
                           Model model) {
        QuestionDTO questionDTO=questionService.getQuestionDTOById(id);
        List<CommentDTO> commentDTOS=commentService.getCommentListByType(id, CommentTypeEnum.QUESTION);
        questionService.incView(id);
        List<Question> relatedQuestions = questionService.getRelatedQuestionList(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments", commentDTOS);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
