package com.laldinsoft.cbr.config;

import com.laldinsoft.cbr.CbrApplication;
import com.laldinsoft.cbr.listener.NotificationQueueListener;
import lombok.var;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:/application.test.properties")
@SpringBootTest(classes = { CbrApplication.class })
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    private static RabbitMqContainer rabbitMqContainer = RabbitMqContainer.build();
    private static MongoDbContainer mongoDbContainer = MongoDbContainer.build();

    static {
        rabbitMqContainer.start();
        mongoDbContainer.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            TestPropertyValues values = TestPropertyValues.of(
                    "camel.component.rabbitmq.port-number=" + rabbitMqContainer.getMappedPort(5672),
                    "spring.rabbitmq.port=" + rabbitMqContainer.getMappedPort(5672),
                    "spring.data.mongodb.uri=mongodb://localhost:" + mongoDbContainer.getMappedPort(27017) + "/notifications"
            );
            values.applyTo(applicationContext);
        }
    }

    @TestConfiguration
    public static class TestConfig {

        @Bean
        public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
            return new RabbitAdmin(connectionFactory);
        }

        @Bean
        Queue queue() {
            return new Queue("NOTIFICATION.LA0246.UC9999", true, false, true);
        }

        @Bean
        DirectExchange exchange() {
            return new DirectExchange("cbr", true, true);
        }

        @Bean
        Binding binding(Queue queue, DirectExchange exchange) {
            return BindingBuilder.bind(queue).to(exchange).with("NOTIFICATION.LA0246.UC9999");
        }

        @Bean
        SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                 MessageListenerAdapter listenerAdapter) {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.setQueueNames("NOTIFICATION.LA0246.UC9999");
            container.setMessageListener(listenerAdapter);
            return container;
        }

        @Bean
        public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            factory.setMessageConverter(new Jackson2JsonMessageConverter());
            return factory;
        }

        @Bean("notificationOutQueue")
        NotificationQueueListener getNotificationOutQueue() {
            return new NotificationQueueListener();
        }

        @Bean
        MessageListenerAdapter listenerAdapter(NotificationQueueListener receiver) {
            MessageListenerAdapter receive = new MessageListenerAdapter(receiver, "receive");
            receive.setMessageConverter(new Jackson2JsonMessageConverter());
            return receive;
        }

        @Bean
        public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
            final var rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
            return rabbitTemplate;
        }

        @Bean
        public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
            return new Jackson2JsonMessageConverter();
        }

    }
}
