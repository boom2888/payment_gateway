package com.games.system.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.games.common.core.domain.BaseEntity;

/**
 * 通知公告表 sys_notice
 * 
 * @author lor
 */
public class SysNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Long noticeId;

    /** 公告标题 */
    private String noticeNo;
    private String noticeTitle;
    /** 公告内容 */
    private String noticeContent;

    private String noticeTitleZh;
    private String noticeContentZh;


    /** 公告类型（1通知 2公告） */
    private String noticeType;



    /** 公告状态（0、即将到来 1正在进行, 2已经卖完） */
    private String status;



    public Long getNoticeId()
    {
        return noticeId;
    }

    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }

    public void setNoticeTitle(String noticeTitle)
    {
        this.noticeTitle = noticeTitle;
    }

    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    public String getNoticeTitle()
    {
        return noticeTitle;
    }

    public void setNoticeType(String noticeType)
    {
        this.noticeType = noticeType;
    }

    public String getNoticeType()
    {
        return noticeType;
    }

    public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent()
    {
        return noticeContent;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getNoticeTitleZh() {
        return noticeTitleZh;
    }

    public void setNoticeTitleZh(String noticeTitleZh) {
        this.noticeTitleZh = noticeTitleZh;
    }

    public String getNoticeContentZh() {
        return noticeContentZh;
    }

    public void setNoticeContentZh(String noticeContentZh) {
        this.noticeContentZh = noticeContentZh;
    }
}
