package com.games.system.service.impl;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.games.common.constant.Constants;
import com.games.system.domain.SysNotice;
import com.games.system.domain.TourState;
import com.games.system.mapper.SysNoticeMapper;
import com.games.system.service.ISysConfigService;
import com.games.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公告 服务层实现
 * 
 * @author lor
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService
{
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private SysNoticeMapper noticeMapper;

    @Override
    @Transactional
    public SysNotice getSellingTour(boolean withHis) {
        SysNotice notice = new SysNotice();
        notice.setStatus(String.valueOf(TourState.SELLING.ordinal()));
        List<SysNotice> sellingList = noticeMapper.selectNoticeList(notice);
        if(!sellingList.isEmpty()){
            return sellingList.get(0);
        }
        if(withHis){
            String LAST_TOUR = sysConfigService.selectConfigByKey(Constants.LAST_TOUR);
            if(StrUtil.isNotBlank(LAST_TOUR)){
                SysNotice notice1 = noticeMapper.selectNoticeById(Long.parseLong(LAST_TOUR));
                if(notice1 != null)return notice1;
            }
            List<SysNotice> allList = noticeMapper.selectNoticeList(new SysNotice());
            if(!allList.isEmpty()){
                return allList.get(0);
            }
        }
        return null;
    }

    @Override
    public void sellOutAllOther() {
        noticeMapper.sellOutAllOther();
    }

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertNotice(SysNotice notice)
    {
        if(notice.getStatus().equals(String.valueOf(TourState.SELLING.ordinal()))){
            noticeMapper.sellOutAllOther();
        }
        int row = noticeMapper.insertNotice(notice);
        if(notice.getStatus().equals(String.valueOf(TourState.SELLING.ordinal()))) {
            sysConfigService.updateConfigValue(Constants.LAST_TOUR, String.valueOf(notice.getNoticeId()));
        }
        return row;
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateNotice(SysNotice notice)
    {
        if(notice.getStatus().equals(String.valueOf(TourState.SELLING.ordinal()))){
            noticeMapper.sellOutAllOther();
            sysConfigService.updateConfigValue(Constants.LAST_TOUR, String.valueOf(notice.getNoticeId()));
        }
        return noticeMapper.updateNoticeData(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId)
    {
        return noticeMapper.deleteNoticeById(noticeId);
    }

    /**
     * 批量删除公告信息
     * 
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds)
    {
        return noticeMapper.deleteNoticeByIds(noticeIds);
    }
}
