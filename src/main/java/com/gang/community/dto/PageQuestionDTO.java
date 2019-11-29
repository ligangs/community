package com.gang.community.dto;

import com.gang.community.model.Question;
import lombok.Data;

import java.util.List;

@Data
public class PageQuestionDTO {
    private List<QuestionDTO> pageQuestionDTOS;//当前页要显示的问题
    private int currentPage; //当前页
    private int totalPage;//总页数
    private int pageSize;//每页的记录数
    private int totalSize; //总的记录数
    private List<Integer> pages;//要显示的页码
    private Boolean flag=true;//标记是否有数据,默认true
}
