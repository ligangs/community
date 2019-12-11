package com.gang.community.service;

import com.gang.community.dto.PaginationDTO;
import com.gang.community.dto.QuestionDTO;
import com.gang.community.exception.CustomizeErrorCode;
import com.gang.community.exception.CustomizeException;
import com.gang.community.mapper.QuestionExtMapper;
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
    @Autowired
    QuestionExtMapper questionExtMapper;

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
    public PaginationDTO getPageQuestion(int currentPage, int pageSize) {
        PaginationDTO<QuestionDTO> paginationDTO =new PaginationDTO<>();

        //得到总记录数
        int totalSize= (int)questionMapper.countByExample(new QuestionExample());
        if (totalSize == 0) {
            paginationDTO.setFlag(false);
        }
        if (paginationDTO.getFlag()){
            //计算总页数
            int totalPage=totalSize%pageSize==0?totalSize/pageSize:totalSize/pageSize+1;
            //处理当前页，上下页，会导致当前页为0 或大于总页数
            if(currentPage<1)
                currentPage=1;
            if(currentPage>totalPage)
                currentPage=totalPage;
            //设置当前页
            paginationDTO.setCurrentPage(currentPage);

            //得到当前页需要显示的问题
            QuestionExample questionExample = new QuestionExample();
            questionExample.setOrderByClause("gmt_create desc");
            List<Question> pageQuestions = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds((currentPage-1)*pageSize,pageSize));
            //组装问题和作者，便于显示
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : pageQuestions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser( userMapper.selectByPrimaryKey(question.getCreator()));
                questionDTOS.add(questionDTO);
            }
            paginationDTO.setData(questionDTOS);

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
            paginationDTO.setPages(pages);
        }
        return paginationDTO;
    }

    //组装用于显示个人所有提问的分页数据信息
    public PaginationDTO getPageQuestionByUserId(Integer currentPage,
                                                 Integer pageSize,
                                                 Long userId) {
        PaginationDTO<QuestionDTO> paginationDTO =new PaginationDTO<>();

        //得到总记录数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        int totalSize=(int)questionMapper.countByExample(questionExample);
        //如果有提问
        if (totalSize == 0) {
            paginationDTO.setFlag(false);
        }
        //问题为空，才封装数据
        if (paginationDTO.getFlag()){
            //计算总页数
            int totalPage=totalSize%pageSize==0?totalSize/pageSize:totalSize/pageSize+1;
            //处理当前页，上下页，会导致当前页为0 或大于总页数
            if(currentPage<1)
                currentPage=1;
            if(currentPage>totalPage)
                currentPage=totalPage;
            //设置当前页
            paginationDTO.setCurrentPage(currentPage);

            //得到当前页需要显示的问题
            questionExample.setOrderByClause("gmt_create desc");
            List<Question> pageQuestions = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds((currentPage-1)*pageSize,pageSize));
            //组装问题和作者，便于显示
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : pageQuestions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser( userMapper.selectByPrimaryKey(question.getCreator()));
                questionDTOS.add(questionDTO);
            }
            paginationDTO.setData(questionDTOS);

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
            paginationDTO.setPages(pages);
        }
        return paginationDTO;
    }

    public QuestionDTO getQuestionDTOById(Long id) {
        QuestionDTO questionDTO=new QuestionDTO();
        Question question=questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
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
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.insert(question);
        } else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            int update=questionMapper.updateByPrimaryKeySelective(question);
            if (update!=1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
    public void incView(Long id) {
        Question question=questionMapper.selectByPrimaryKey(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    //得到相关问题列表
    public List<Question> getRelatedQuestionList(Long id) {

        Question dbQuestion = questionMapper.selectByPrimaryKey(id);
        Question question = new Question();
        question.setId(id);
        //将tag字符串转变成正则表达式
        String regexpTag = dbQuestion.getTag().replace(',', '|');
        question.setTag(regexpTag);
        List<Question> relatedQuestions =questionExtMapper.getRelatedQuestionList(question);

        return relatedQuestions;
    }

}
