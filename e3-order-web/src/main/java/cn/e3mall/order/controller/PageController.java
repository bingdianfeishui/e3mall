package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

@Controller
@RequestMapping("/order")
public class PageController {
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/order-cart")
	public String orderCart(HttpServletRequest request, HttpServletResponse response, Model model) {
		// 取用户
		TbUser user = (TbUser) request.getAttribute("user");
		// 根据用户id取收货地址列表
		
		// 取支付方式列表
		
		// 取购物车商品列表
		E3Result result = cartService.getCartItemList(6L/*user.getId()*/);
		List<TbItem> list = (List<TbItem>) result.getData();
		//model.addAttribute("cartList", list);
		// 
		return "order-cart";
	}
	
	@RequestMapping("/success")
	public String orderSuccess(Model model) {
		
		return "success";
	}
}
