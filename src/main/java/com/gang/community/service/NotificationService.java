package com.gang.community.service;

import com.gang.community.dto.NotificationDTO;
import com.gang.community.dto.PaginationDTO;
import com.gang.community.enums.NotificationStatusEnum;
import com.gang.community.enums.NotificationTypeEnum;
import com.gang.community.exception.CustomizeErrorCode;
import com.gang.community.exception.CustomizeException;
import com.gang.community.mapper.NotificationMapper;
import com.gang.community.model.Notification;
import com.gang.community.model.NotificationExample;
import com.gang.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    //得到分页的通知消息
    public PaginationDTO list(Integer currentPage, Integer pageSize, Long userId) {
        PaginationDTO<NotificationDTO> paginationDTO =new PaginationDTO();

        //得到总记录数
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        int totalSize=(int)notificationMapper.countByExample(notificationExample);
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
            notificationExample.setOrderByClause("gmt_create desc");
            List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample,new RowBounds((currentPage-1)*pageSize,pageSize));
            if (notifications.size() == 0) {
                return paginationDTO;
            }

            //组装问题和作者，便于显示
            List<NotificationDTO> notificationDTOS = new ArrayList<>();
            for (Notification notification : notifications) {
                NotificationDTO notificationDTO = new NotificationDTO();
                BeanUtils.copyProperties(notification,notificationDTO);
                notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
                notificationDTOS.add(notificationDTO);
            }
            paginationDTO.setData(notificationDTOS);

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

    //得到用户未读通知数
    public Integer unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(0);
        int unreadCount = (int)notificationMapper.countByExample(notificationExample);
        return unreadCount;
    }

    //读通知
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        notificationDTO.setType(notification.getType());
        return notificationDTO;
    }
}
