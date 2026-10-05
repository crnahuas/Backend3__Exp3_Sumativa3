package cl.duoc.ms_mensajeria.config;

import jakarta.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Configuration
public class JmsConsumerConfig {

    @Bean
    public ConnectionFactory connectionFactory(
            @Value("${spring.activemq.broker-url:tcp://localhost:61616}") String brokerUrl,
            @Value("${spring.activemq.user:admin}") String user,
            @Value("${spring.activemq.password:admin}") String password,
            @Value("${app.jms.retry.max-redeliveries:3}") int maxRedeliveries,
            @Value("${app.jms.retry.initial-delay-ms:1000}") long initialDelay,
            @Value("${app.jms.retry.backoff-multiplier:2.0}") double backoffMultiplier) {

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, brokerUrl);
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(maxRedeliveries);
        redeliveryPolicy.setInitialRedeliveryDelay(initialDelay);
        redeliveryPolicy.setUseExponentialBackOff(true);
        redeliveryPolicy.setBackOffMultiplier(backoffMultiplier);
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        return connectionFactory;
    }

    @Bean(name = "retiroJmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory retiroJmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            @Value("${app.jms.consumer.concurrency:1-3}") String concurrency) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSessionTransacted(true);
        factory.setConcurrency(concurrency);
        return factory;
    }
}
