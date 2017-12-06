package cn.e3mall.cart.service;

import java.util.Map;

import cn.e3mall.common.pojo.E3Result;

public interface CartService {
	E3Result addCart(Long userId, Long itemId, Integer num);

	E3Result addCart(Long userId, Map<Long, Integer> map);

	E3Result updateCart(Long userId, Long itemId, Integer num);

	E3Result deleteFromCart(Long userId, Long itemId);

	E3Result getCart(Long userId);
}
