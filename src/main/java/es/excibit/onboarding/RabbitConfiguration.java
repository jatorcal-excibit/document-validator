package es.excibit.onboarding;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

	@Value("${documentvalidator.queue.name}")
	private String queueName;

	@Value("${documentvalidator.exchange.name}")
	private String exchangeName;

	@Value("${rabbitmq.host}")
	private String host;

	@Value("${rabbitmq.port}")
	private Integer port;

	@Value("${rabbitmq.user}")
	private String user;

	@Value("${rabbitmq.password}")
	private String password;

	@Value("${rabbitmq.virtualhost}")
	private String virtualhost;

	@Bean
	public ConnectionFactory connectionFactory() throws Exception {
		final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(user);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost(virtualhost);
		return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() throws Exception {
		return new RabbitTemplate(connectionFactory());
	}

	@Bean
	public Queue queue() {
		return new Queue(queueName);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchangeName);
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}
}