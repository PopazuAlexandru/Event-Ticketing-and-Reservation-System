package com.example.ticketing.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Declares the shared RabbitMQ topology and JSON message conversion.
 */
@Configuration
public class RabbitMqConfiguration {

    @Bean
    public TopicExchange ticketExchange() {
        return new TopicExchange(TicketingMessaging.EXCHANGE, true, false);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(TicketingMessaging.NOTIFICATION_QUEUE).build();
    }

    @Bean
    public Queue expirationQueue() {
        return QueueBuilder.durable(TicketingMessaging.EXPIRATION_QUEUE).build();
    }

    @Bean
    public Binding createdNotificationBinding(
            @Qualifier("notificationQueue") Queue notificationQueue, TopicExchange ticketExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(ticketExchange).with(TicketingMessaging.RESERVATION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding paymentNotificationBinding(
            @Qualifier("notificationQueue") Queue notificationQueue, TopicExchange ticketExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(ticketExchange).with(TicketingMessaging.PAYMENT_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public Binding expirationBinding(
            @Qualifier("expirationQueue") Queue expirationQueue, TopicExchange ticketExchange) {
        return BindingBuilder.bind(expirationQueue)
                .to(ticketExchange).with(TicketingMessaging.RESERVATION_EXPIRED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonMessageConverter);
        return rabbitTemplate;
    }
}
