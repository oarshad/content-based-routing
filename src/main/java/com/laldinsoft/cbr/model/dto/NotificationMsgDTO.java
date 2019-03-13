package com.laldinsoft.cbr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class NotificationMsgDTO {

    private String documentId;
    private String documentType;
    private String documentAuthority;
    private String documentContent;

}
