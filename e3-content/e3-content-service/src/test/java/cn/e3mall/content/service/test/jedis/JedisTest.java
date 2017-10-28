package cn.e3mall.content.service.test.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void testJedis() {
		// 创建一个jedis对象
		Jedis jedis = new Jedis("192.168.25.133", 6379);
		// 直接使用jedis对象操作redis
		jedis.set("str1", "testJedis");
		System.out.println("Jedis:" + jedis.get("str1"));
		// 关闭连接
		jedis.close();
	}

	@Test
	public void testJedisPool() {
		// 创建一个jedis连接池对象
		JedisPool jedisPool = new JedisPool("192.168.25.133", 6379);
		// 从连接池获得jedis连接
		Jedis jedis = jedisPool.getResource();
		// 直接使用jedis对象操作redis
		jedis.set("str2", "testJedisPool");
		System.out.println("Pool:" + jedis.get("str2"));
		// 每次使用完都必须关闭连接，让连接池回收资源
		jedis.close();
		// 关闭jedis连接池
		jedisPool.close();
	}

	@Test
	public void testJedisCluster() {
		// 创建集群ip端口set
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.133", 7001));
		nodes.add(new HostAndPort("192.168.25.133", 7002));
		nodes.add(new HostAndPort("192.168.25.133", 7003));
		nodes.add(new HostAndPort("192.168.25.133", 7004));
		nodes.add(new HostAndPort("192.168.25.133", 7005));
		nodes.add(new HostAndPort("192.168.25.133", 7006));
		// 创建一个jedisCluster对象
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("str3", "testJedisCluster");
		System.out.println("Cluster:" + cluster.get("str3"));
		// 关闭JedisCluster对象
		cluster.close();
	}

}
