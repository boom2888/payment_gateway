package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentPhotoVo {
    
    private String documentId;
    
    private String fileName;
    
    private String format;
    
    private Boolean thumbnailExists;
    
    private String notes;
    
    private Long originalFileSize;
    
    private String sideType;
}
