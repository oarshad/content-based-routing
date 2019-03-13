package com.laldinsoft.cbr.config;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

class RabbitMqContainer extends GenericContainer<RabbitMqContainer> {

    private RabbitMqContainer() {
        super("rabbitmq:management");
    }

    static RabbitMqContainer build() {
        return new RabbitMqContainer()
                .withExposedPorts(5672, 15672)
                .waitingFor(
                        Wait.forLogMessage(".*Server startup complete.*\\n", 1)
                );
    }
}
