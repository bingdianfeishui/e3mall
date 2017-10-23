package cn.e3mall.test.publish;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//不用tomcat启动dubbo服务
public class DubboPublishTest {

	//@Test
	public void publishDubbo() throws IOException{
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("stated");
		System.in.read();
		applicationContext.close();
		System.out.println("closed");
	}
}
