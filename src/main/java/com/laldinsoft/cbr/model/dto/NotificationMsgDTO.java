package com.laldinsoft.cbr.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class NotificationMsgDTO {

    private String documentId;
    private String documentType;
    private String documentAuthority;
    private String documentContent;

    @JsonIgnore
    public String getKey() {
        return "NOTIFICATION." + documentType + "." + documentAuthority;
    }

}
