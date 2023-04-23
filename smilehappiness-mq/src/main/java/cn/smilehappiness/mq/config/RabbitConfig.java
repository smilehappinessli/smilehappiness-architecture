package cn.smilehappiness.mq.config;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.Data;
import lombok.ToString;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @ClassName RabbitConfig
 * @Description rabbitMqProfile reading 
 * @author
 * @Date 2021/10/16 13:36
 * @Version 1.0
 **/
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "smilespring.rabbit", ignoreUnknownFields = false)
@ConditionalOnClass({RabbitTemplate.class, Channel.class})
public class RabbitConfig {


    /**
     *IPAddress/domain name 
     */
    public String host;

    /**
     *port 
     */
    public Integer port;

    /**
     * user name 
     */
    public String username;

    /**
     * password 
     */
    public String password;

    /**
     * Virtual host name 
     */
    public String virtualHost;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(host);
        cachingConnectionFactory.setPort(port);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setVirtualHost(virtualHost);
        return cachingConnectionFactory;
    }

    /**
     * ordinary rabbitTemplate
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    /**
     * FastJson rabbitTemplate
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate jsonRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new SelfConverter());
        return rabbitTemplate;
    }

    public static class SelfConverter extends AbstractMessageConverter {
        @Override
        protected Message createMessage(Object object, MessageProperties messageProperties) {
            messageProperties.setContentType("application/json");
            return new Message(JSON.toJSONBytes(object), messageProperties);
        }

        @Override
        public Object fromMessage(Message message) throws MessageConversionException {
            return JSON.parse(message.getBody());
        }
    }

    /**
     * JacksonJSON rabbitTemplate
     * rabbitDefault json structure 
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate jacksonRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitTemplate ackRabbitTemplate() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        // Set ack to true
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate= new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }


    /**
     * Configure rabbitmq transaction manager 
     * @param connectionFactory
     * @return
     */
    @Bean("rabbitTransactionManager")
    @ConditionalOnProperty(value = "zfspring.rabbit.openTx",havingValue = "true")
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "zfspring.rabbit.openTx",havingValue = "true")
    public RabbitTemplate transactionRabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

}
