package cn.e3mall.cart.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Value("${CART_COOKIE_EXPIRE}")
	private Integer CART_COOKIE_EXPIRE;
	@Value("${CART_COOKIE_NAME}")
	private String CART_COOKIE_NAME;

	@Autowired
	private ItemService itemService;

	@RequestMapping("/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 获取cookie中原来的购物车
		Map<Long, Integer> cartMap = getCartFromCookie(request);
		// 判断商品是否存在
		boolean exists = cartMap.containsKey(itemId);
		// 有则数量相加，没有则根据Id查询商品信息
		int cartNum = exists ? cartMap.get(itemId) + num : num;
		// 把商品添加到商品列表
		cartMap.put(itemId, cartNum);
		// 写入cookie
		CookieUtils.setCookie(request, response, CART_COOKIE_NAME, JsonUtils.objectToJson(cartMap), CART_COOKIE_EXPIRE);
		// 返回结果
		return "cartSuccess";
	}

	private Map<Long, Integer> getCartFromCookie(HttpServletRequest request) {
		String cartJson = CookieUtils.getCookieValue(request, CART_COOKIE_NAME);
		if (StringUtils.isNotBlank(cartJson)) {
			try {
				Map<Long, Integer> map = JsonUtils.getMapper().readValue(cartJson,
						new TypeReference<LinkedHashMap<Long, Integer>>() {
						});
				return map;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new LinkedHashMap<>();
	}

	@RequestMapping("/cart")
	public String cart(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<Long, Integer> cartMap = getCartFromCookie(request);
		List<TbItem> cartList = new ArrayList<>();
		for (Map.Entry<Long, Integer> entry : cartMap.entrySet()) {
			TbItem item = itemService.getItemById(entry.getKey());
			if (item != null) {
				item.setNum(entry.getValue());
				String image = item.getImage();
				if (StringUtils.isNotBlank(image))
					item.setImage(item.getImage().split(",")[0]);
				cartList.add(item);
			}
		}
		model.addAttribute("cartList", cartList);
		return "cart";
	}

	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateItemNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取cookie中原来的购物车
		Map<Long, Integer> cartMap = getCartFromCookie(request);
		// 更新购物车数量
		cartMap.put(itemId, num);
		// 写入cookie
		CookieUtils.setCookie(request, response, CART_COOKIE_NAME, JsonUtils.objectToJson(cartMap), CART_COOKIE_EXPIRE);

		return E3Result.ok();
	}

	@RequestMapping("/delete/{itemId}")
	public String deleteItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 获取cookie中原来的购物车
		Map<Long, Integer> cartMap = getCartFromCookie(request);
		// 删除对应商品
		cartMap.remove(itemId);
		// 写入cookie
		CookieUtils.setCookie(request, response, CART_COOKIE_NAME, JsonUtils.objectToJson(cartMap), CART_COOKIE_EXPIRE);
		return "redirect:/cart/cart.html";
	}
}
