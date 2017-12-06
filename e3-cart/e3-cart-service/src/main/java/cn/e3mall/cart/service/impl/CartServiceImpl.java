package cn.e3mall.cart.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;

/**
 * 购物车业务逻辑
 * 
 * @author youngcoding <br>
 *         ProjectName: e3-cart-service <br>
 *         Date: 2017年12月6日 下午10:14:33
 * @version 1.0
 */
@Service
public class CartServiceImpl implements CartService {

	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;

	@Autowired
	private JedisClient jedisClient;

	@Override
	public E3Result addCart(Long userId, Long itemId, Integer num) {
		// 查询商品是否存在
		String key = REDIS_CART_PRE + userId;
		incrCartNum(key, itemId, num);
		return E3Result.ok();
	}

	private void incrCartNum(String key, Long itemId, Integer num) {
		String field = String.valueOf(itemId);
		Boolean isExist = jedisClient.hexists(key, field);
		// 若存在，数量相加,不存在则写入。也可以用hincrBy，需修改common
		Integer oldNum = isExist ? Integer.valueOf(jedisClient.hget(key, field)) : 0;
		jedisClient.hset(key, field, String.valueOf(oldNum + num));
	}

	@Override
	public E3Result addCart(Long userId, Map<Long, Integer> map) {
		String key = REDIS_CART_PRE + userId;
		for (Map.Entry<Long, Integer> entry : map.entrySet()) {
			incrCartNum(key, entry.getKey(), entry.getValue());
		}
		return E3Result.ok();
	}

	@Override
	public E3Result getCart(Long userId) {
		String key = REDIS_CART_PRE + userId;
		Set<String> hkeys = jedisClient.hkeys(key);
		Map<Long, Integer> map = new HashMap<>();
		for (String k : hkeys) {
			Integer num = Integer.valueOf(jedisClient.hget(key, k));
			map.put(Long.valueOf(k), num);
		}
		return E3Result.ok(map);
	}

	@Override
	public E3Result updateCart(Long userId, Long itemId, Integer num) {
		String key = REDIS_CART_PRE + userId;
		String field = String.valueOf(itemId);
		if (jedisClient.hexists(key, field))
			jedisClient.hset(key, field, num + "");
		return E3Result.ok();
	}

	@Override
	public E3Result deleteFromCart(Long userId, Long itemId) {
		String key = REDIS_CART_PRE + userId;
		String field = String.valueOf(itemId);
		if (jedisClient.hexists(key, field))
			jedisClient.hdel(key, field);
		return E3Result.ok();
	}

}
