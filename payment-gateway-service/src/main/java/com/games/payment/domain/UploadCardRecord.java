package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 上传卡记录对象 upload_card_record
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("upload_card_record")
public class UploadCardRecord  {

private static final long serialVersionUID=1L;


    /** ID */
    @TableId(value = "id")
    private Long id;

    /** 文件存储桶地址 */
    @Excel(name = "文件存储桶地址")
    private String fileUrl;

    /** 创建时间 */
    @Excel(name = "创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 状态(1-上传中，2-上传完成) */
    @Excel(name = "状态(1-上传中，2-上传完成)")
    private Long state;

    /** 类型(1-白名单，2-黑名单) */
    @Excel(name = "类型(1-白名单，2-黑名单)")
    private Long type;

}
