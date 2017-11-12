package cn.e3mall.test.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.junit.Test;

public class ActiveMQTest {
	private static final String brokerURL = "tcp://localhost:61616";

	@Test
	public void testProducer() throws Exception {
		ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
		Connection connection = factory.createConnection();
		Session session = connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("test_queue");
		MessageProducer producer = session.createProducer(queue);
		String msg = System.currentTimeMillis() + "";
		TextMessage message = session.createTextMessage(msg);
		producer.send(message);
		System.out.println("SEND: " + msg);
	}

	@Test
	public void testConsumer() throws Exception {
		ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("test_queue");
		MessageConsumer consumer = session.createConsumer(queue);
		
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMsg = (TextMessage) message;
					String text = textMsg.getText();
					System.out.println("1RECIVED: " + text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.in.read();
	}
}
