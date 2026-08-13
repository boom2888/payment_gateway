package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookResVo {
    private Long entityId;
    private String status;
    private List<VerificationVo> verifications;
    private Boolean ongoingMonitoring;
    private Boolean pending;
    private Boolean screeningHasRun;
}

