package cn.e3mall.test.activemq;

import java.io.IOException;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class SpringActiveMQ {
	static int count = 1;

	// @Test
	public void testProducer() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-mq.xml");
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		Destination destination = (Destination) applicationContext.getBean("topicDestination");
		for (int i = 0; i < 5; i++) {
			jmsTemplate.send(destination, new MessageCreator() {

				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage message = session.createTextMessage(count + "");
					count++;
					return message;
				}
			});
		}
		applicationContext.close();
	}

	// @Test
	public void testConsumer() throws IOException {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-mq.xml");
		System.in.read();
		applicationContext.close();
	}
}
