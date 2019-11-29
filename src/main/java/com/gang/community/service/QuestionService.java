package com.gang.community.service;

import com.gang.community.dto.PageQuestionDTO;
import com.gang.community.dto.QuestionDTO;
import com.gang.community.mapper.QuestionMapper;
import com.gang.community.mapper.UserMapper;
import com.gang.community.model.Question;
import com.gang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionMapper questionMapper;

    //得到所有问题对象的DTO，将question和user组装
    public List<QuestionDTO> getList() {

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        List<Question> questions=questionMapper.findAll();
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(userMapper.findUserById(question.getCreator()));
            questionDTOS.add(questionDTO);
        }
        return questionDTOS;
    }

    //组装一个pageQuestionDTO,用于首页分页显示所有提问
    public PageQuestionDTO getPageQuestion(int currentPage, int pageSize) {
        PageQuestionDTO pageQuestionDTO =new PageQuestionDTO();

        //得到总记录数
        int totalSize=questionMapper.getCount();
        if (totalSize == 0) {
            pageQuestionDTO.setFlag(false);
        }
        if (pageQuestionDTO.getFlag()){
            //计算总页数
            int totalPage=totalSize%pageSize==0?totalSize/pageSize:totalSize/pageSize+1;
            //处理当前页，上下页，会导致当前页为0 或大于总页数
            if(currentPage<1)
                currentPage=1;
            if(currentPage>totalPage)
                currentPage=totalPage;
            //设置当前页
            pageQuestionDTO.setCurrentPage(currentPage);

            //得到当前页需要显示的问题
            List<Question> pageQuestions = questionMapper.getPageQuestions((currentPage-1)*pageSize, pageSize);
            //组装问题和作者，便于显示
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : pageQuestions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser(userMapper.findUserById(question.getCreator()));
                questionDTOS.add(questionDTO);
            }
            pageQuestionDTO.setPageQuestionDTOS(questionDTOS);

            //计算出需要显示的页码，开始和结束
            int start;
            int end;
            if (totalPage < pageSize) {
                start = 1;
                end = totalPage;
            } else {
                start=currentPage-2;
                end=currentPage+2;
                if(start<1){
                    start=1;
                    end=pageSize;
                }
                if (end > totalPage) {
                    end=totalPage;
                    start=end-pageSize+1;
                }
            }
            List<Integer> pages = new ArrayList<>();
            //将页码放入页码数组
            for (int i = start; i <= end; i++) {
                pages.add(i);
            }
            pageQuestionDTO.setPages(pages);
        }
        return pageQuestionDTO;
    }

    //组装用于显示个人所有提问的分页数据信息
    public PageQuestionDTO getPageQuestionByUserId(Integer currentPage,
                                                   Integer pageSize,
                                                   Integer userId) {
        PageQuestionDTO pageQuestionDTO =new PageQuestionDTO();

        //得到总记录数
        int totalSize=questionMapper.getCountByUserId(userId);
        //如果有提问
        if (totalSize == 0) {
            pageQuestionDTO.setFlag(false);
        }
        //问题为空，才封装数据
        if (pageQuestionDTO.getFlag()){
            //计算总页数
            int totalPage=totalSize%pageSize==0?totalSize/pageSize:totalSize/pageSize+1;
            //处理当前页，上下页，会导致当前页为0 或大于总页数
            if(currentPage<1)
                currentPage=1;
            if(currentPage>totalPage)
                currentPage=totalPage;
            //设置当前页
            pageQuestionDTO.setCurrentPage(currentPage);

            //得到当前页需要显示的问题
            int offset = (currentPage - 1) * pageSize;
            List<Question> pageQuestions = questionMapper.getPageQuestionsByUserId(offset, pageSize,userId);
            //组装问题和作者，便于显示
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : pageQuestions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser(userMapper.findUserById(question.getCreator()));
                questionDTOS.add(questionDTO);
            }
            pageQuestionDTO.setPageQuestionDTOS(questionDTOS);

            //计算出需要显示的页码，开始和结束
            int start;
            int end;
            if (totalPage < pageSize) {
                start = 1;
                end = totalPage;
            } else {
                start=currentPage-2;
                end=currentPage+2;
                if(start<1){
                    start=1;
                    end=pageSize;
                }
                if (end > totalPage) {
                    end=totalPage;
                    start=end-pageSize+1;
                }
            }
            List<Integer> pages = new ArrayList<>();
            //将页码放入页码数组
            for (int i = start; i <= end; i++) {
                pages.add(i);
            }
            pageQuestionDTO.setPages(pages);
        }
        return pageQuestionDTO;
    }

    public QuestionDTO getQuestionDTOById(Integer id) {
        QuestionDTO questionDTO=new QuestionDTO();
        Question question=questionMapper.getQuestionById(id);
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findUserById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
