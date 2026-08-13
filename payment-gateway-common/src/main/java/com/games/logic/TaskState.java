package com.games.logic;

/**
 * 业务操作类型
 * 
 * @author lor
 */
public enum TaskState
{
    /** 状态，SENDING:发送中,AUTH:审核中,SUCCESS:成功,FAIL:失败   */
    /**
     * 其它
     */
    OTHER,
    WAIT,
    /**
     * 充值
     */
    SENDING,
    /**
     * 归集
     */
    SUCCESS,
    AUTH,
    FAIL,
}
