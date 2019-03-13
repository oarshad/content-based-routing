package com.laldinsoft.cbr.listener;

import com.laldinsoft.cbr.model.dto.NotificationMsgDTO;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class NotificationQueueListener {

    public void receive(NotificationMsgDTO message) {
        log.debug("message received: " + message);
    }

}
