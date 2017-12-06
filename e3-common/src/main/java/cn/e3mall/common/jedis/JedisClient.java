package cn.e3mall.common.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	Long del(String key);
	Boolean exists(String key);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	Long hdel(String key, String... field);
	Boolean hexists(String key, String field);
	Long incrBy(final String key, final long integer);
	Set<String> hkeys(String hash);
	String hmset(final String key, final Map<String, String> hash);
	List<String> hmget(final String key, final String... fields);
	List<String> hvals(String key);
}
