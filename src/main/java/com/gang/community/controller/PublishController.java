package com.gang.community.controller;

import com.gang.community.mapper.QuestionMapper;
import com.gang.community.model.Question;
import com.gang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("publish")
    public String pushQuestion(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "tag", required = false) String tag,
                               HttpServletRequest request,
                               Model model) {
        //用于回显所填信息
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        //判断是否登录
        User user=(User)request.getSession().getAttribute("user");
        //用户未登录
        if(user==null) {
            model.addAttribute("error", "您还未登录，请先登录后再发表问题吧~");
            return "publish";
        }

        //判断是否有未填写完整的信息，给出错误提示
        if (title == null||title=="") {
            model.addAttribute("error", "标题不能不能为空哦~");
            return "publish";
        }
        if (description == null||description=="") {
            model.addAttribute("error", "问题补充不能为空哦~");
            return "publish";
        }
        if (tag == null||tag=="") {
            model.addAttribute("error", "标签不能为空哦~");
            return "publish";
        }

        //创建问题对象并保存进数据库
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.insert(question);
        return "redirect:/";
    }

}
