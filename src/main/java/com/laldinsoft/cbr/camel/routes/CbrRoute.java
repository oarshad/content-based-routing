package com.laldinsoft.cbr.camel.routes;

import org.apache.camel.builder.RouteBuilder;
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
                .removeHeaders("rabbitmq.*")
                .setHeader("rabbitmq.CONTENT_TYPE", () -> "application/json")
                .to("rabbitmq:cbr?queue=NOTIFICATION.LA0246.UC9999&routingKey=NOTIFICATION.LA0246.UC9999&autoDelete=true");

        //TODO: Add MongoDB route
    }

}
