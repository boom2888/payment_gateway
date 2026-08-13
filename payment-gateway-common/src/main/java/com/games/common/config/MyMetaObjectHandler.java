package com.games.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String CREATED_AT = "createdAt";
    private static final String UPDATED_AT = "updatedAt";

    /**
     * 插入填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
//        log.info("==========start insert fill ....");
        //setFieldvalByName(String fieldName,object fieldval,MetaObject metaObject)
        this.setFieldValByName(CREATE_TIME, new Date(), metaObject);
        this.setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        this.setFieldValByName(CREATED_AT, new Date(), metaObject);
        this.setFieldValByName(UPDATED_AT, new Date(), metaObject);
    }

    /**
     * 更新填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
//        log.info("=============start update fill ....");
        this.setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        this.setFieldValByName(UPDATED_AT, new Date(), metaObject);
    }
}
