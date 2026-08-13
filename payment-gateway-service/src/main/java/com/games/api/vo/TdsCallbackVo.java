package com.games.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class TdsCallbackVo {

    private String threeDSServerTransID;

    private String acsTransID;

    private String messageType;

    private String messageVersion;

    private String transStatus;

    private List<MessageExtension> messageExtension;

    private String acsSignedContent;

    @Data
    public static class MessageExtension {
        private String name;
        private String id;
        private boolean criticalityIndicator;
        private ExtensionData data;
    }

    @Data
    public static class ExtensionData {
        private String valueOne;
        private String valueTwo;
    }
}