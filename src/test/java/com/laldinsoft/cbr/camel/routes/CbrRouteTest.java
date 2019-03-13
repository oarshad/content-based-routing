package com.laldinsoft.cbr.camel.routes;

import com.laldinsoft.cbr.config.AbstractIntegrationTest;
import com.laldinsoft.cbr.listener.NotificationQueueListener;
import com.laldinsoft.cbr.model.dto.NotificationMsgDTO;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


public class CbrRouteTest extends AbstractIntegrationTest {

    private static final String EXCHANGE = "cbr";
    private static final String QUEUE = "NOTIFICATION";
    private static final String ROUTING_KEY = "NOTIFICATION.#";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @SpyBean
    private NotificationQueueListener notificationQueueListener;

    @Test
    public void someTest() {

        // Given
        assertNotNull("NOTIFICATION Queue doesn't exist", rabbitAdmin.getQueueProperties("NOTIFICATION"));
        assertNotNull("NOTIFICATION.LA0246.UC9999 Queue doesn't exist",
                rabbitAdmin.getQueueProperties("NOTIFICATION.LA0246.UC9999"));

        NotificationMsgDTO message = new NotificationMsgDTO("32b568bc-4104-4f85-b675-165c5ed18733",
                "UC9999", "LA0246", "Lorem ipsum");

        // When
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);

        // Then
        verify(notificationQueueListener, timeout(1000L).times(1)).receive(message);

        //TODO: Test MongoDB Log entry
    }



}
