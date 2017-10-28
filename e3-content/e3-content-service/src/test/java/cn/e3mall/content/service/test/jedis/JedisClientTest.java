package cn.e3mall.content.service.test.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import cn.e3mall.common.jedis.JedisClientCluster;
import cn.e3mall.common.jedis.JedisClientPool;

@SuppressWarnings("resource")
public class JedisClientTest {

	@Test
	public void testJedisClientPool() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-jedis.xml", "classpath:spring/applicationContext-dao.xml");
		Resource resource = applicationContext.getResource("redis.cluster.s02.port");
		System.out.println(resource);

		JedisClientPool pool = applicationContext.getBean(JedisClientPool.class);
		pool.set("pool", "pool value");
		System.out.println(pool.get("pool"));
	}

	@Test
	public void testJedisClientCluster() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-jedis.xml", "classpath:spring/applicationContext-dao.xml");
		JedisClientCluster cluster = applicationContext.getBean(JedisClientCluster.class);
		cluster.set("cluster", "cluster value");
		System.out.println(cluster.get("cluster"));
	}

}
