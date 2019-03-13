package com.laldinsoft.cbr.camel.routes;

import com.laldinsoft.cbr.model.dto.NotificationMsgDTO;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class CbrRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("rabbitmq:cbr?queue=NOTIFICATION&routingKey=NOTIFICATION.#&autoDelete=true")
                .log("${body}")
                .to("direct:route")
                .end();

        from("direct:route")
                .unmarshal().json(JsonLibrary.Jackson, NotificationMsgDTO.class)
                .setProperty("key", simple("${body.getKey()}"))
                .removeHeaders("rabbitmq.*")
                .setHeader("rabbitmq.CONTENT_TYPE", () -> "application/json")
                .marshal().json(JsonLibrary.Jackson)
                .recipientList(simple("rabbitmq:cbr?queue=${property.key}&routingKey=${property.key}&autoDelete=true"));

        //TODO: Add MongoDB route
    }

}
