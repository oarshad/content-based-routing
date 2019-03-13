package com.laldinsoft.cbr.config;

import org.testcontainers.containers.GenericContainer;

class MongoDbContainer extends GenericContainer<MongoDbContainer> {

    private MongoDbContainer() {
        super("mongo");
    }

    static MongoDbContainer build() {
        return new MongoDbContainer()
                .withExposedPorts(27017);
    }
}
