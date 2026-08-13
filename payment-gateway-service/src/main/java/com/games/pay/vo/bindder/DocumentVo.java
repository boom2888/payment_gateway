package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentVo {
    
    private String id;
    
    private Long entityId;
    
    private String fileName;
    
    private String format;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date createdAt;
    
    private UserVo uploader;
    
    private Boolean thumbnailExists;
    
    private String path;
    
    private DocumentTypeVo type;
}
