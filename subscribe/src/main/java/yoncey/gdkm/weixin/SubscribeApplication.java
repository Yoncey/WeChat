package yoncey.gdkm.weixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import yoncey.gdkm.weixin.domain.InMessage;
import yoncey.gdkm.weixin.domain.text.EventInMessage;
import yoncey.gdkm.weixin.processors.EventMessageProcessor;

@SpringBootApplication
@ComponentScan("yoncey.gdkm")
public class SubscribeApplication implements CommandLineRunner, DisposableBean, ApplicationContextAware, CommonsConfig {

	private static final Logger LOG = LoggerFactory.getLogger(SubscribeApplication.class);

	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

	@Bean
	public MessageListener messageListener(
			@Autowired @Qualifier("inMessageTemplate") RedisTemplate<String, ? extends InMessage> inMessageTemplate) {
		MessageListenerAdapter adapter = new MessageListenerAdapter(this, "handle");
		adapter.setSerializer(inMessageTemplate.getValueSerializer());
		return adapter;
	};

	public void handle(EventInMessage msg) {
		LOG.trace("处理信息： {}", msg);
		String id = msg.getEvent().toLowerCase() + "MessageProcessor";
		try {
			EventMessageProcessor mp = (EventMessageProcessor) ctx.getBean(id);
			if (mp != null) {
				mp.onMessage(msg);
			} else {
				LOG.error("利用Bean的ID {} 不能找到一个事件消息处理器!", id);
			}
		} catch (NoSuchBeanDefinitionException e) {
			LOG.trace("当前模块不适合处理 {} 消息，没有对应的处理器实现", msg.getEvent());
		} catch (Exception e) {
			LOG.error("无法处理事件：" + e.getLocalizedMessage(), e);
		}
	}

	@Bean
	public RedisMessageListenerContainer messageListenerContainer(@Autowired RedisConnectionFactory connectionFactory,
			@Autowired MessageListener messageListener) {
		RedisMessageListenerContainer c = new RedisMessageListenerContainer();
		c.setConnectionFactory(connectionFactory);
		Topic topic = new ChannelTopic("kemao_2_event");

		c.addMessageListener(messageListener, topic);

		return c;
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(SubscribeApplication.class, args);
	}
}
