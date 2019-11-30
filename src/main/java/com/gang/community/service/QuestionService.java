package com.gang.community.service;

import com.gang.community.dto.PageQuestionDTO;
import com.gang.community.dto.QuestionDTO;
import com.gang.community.mapper.QuestionMapper;
import com.gang.community.mapper.UserMapper;
import com.gang.community.model.Question;
import com.gang.community.model.QuestionExample;
import com.gang.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
        QuestionExample questionExample = new QuestionExample();
        //****不确定是否是返回所有question
        questionExample.createCriteria().getAllCriteria();
        List<Question> questions=questionMapper.selectByExample(questionExample);
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
            questionDTOS.add(questionDTO);
        }
        return questionDTOS;
    }

    //组装一个pageQuestionDTO,用于首页分页显示所有提问
    public PageQuestionDTO getPageQuestion(int currentPage, int pageSize) {
        PageQuestionDTO pageQuestionDTO =new PageQuestionDTO();

        //得到总记录数
        int totalSize= (int)questionMapper.countByExample(new QuestionExample());
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
            List<Question> pageQuestions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds((currentPage-1)*pageSize,pageSize));
            //组装问题和作者，便于显示
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : pageQuestions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser( userMapper.selectByPrimaryKey(question.getCreator()));
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
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        int totalSize=(int)questionMapper.countByExample(questionExample);
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
            QuestionExample questionExample1 = new QuestionExample();
            questionExample.createCriteria().andCreatorEqualTo(userId);
            List<Question> pageQuestions = questionMapper.selectByExampleWithRowbounds(questionExample1,new RowBounds((currentPage-1)*pageSize,pageSize));
            //组装问题和作者，便于显示
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : pageQuestions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser( userMapper.selectByPrimaryKey(question.getCreator()));
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
        Question question=questionMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(question, questionDTO);
        User user =  userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void addOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.updateByPrimaryKeySelective(question);
        }
    }
}