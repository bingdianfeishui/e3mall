package cn.e3mall.order.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	@Value("${SSO_URL}")
	private String SSO_URL;

	@Value("${CART_COOKIE_NAME}")
	private String CART_COOKIE_NAME;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1 从Cookie取得token
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		// 2 判断token是否为空, 未登录则去登录，并设置重定向连接
		if (StringUtils.isBlank(token)) {
			response.sendRedirect(SSO_URL + "?returnUrl=" + request.getRequestURI());
			return false;
		}
		// 3 若取到，则去sso项目查询是否登录
		E3Result result = userService.getUserInfo(token);
		// 4 判断用户是否登录。 若未登录或登录过期，则去登录，并设置重定向连接
		if (result.getStatus() != 200) {
			response.sendRedirect(SSO_URL + "?returnUrl=" + request.getRequestURL());
			return false;
		}

		// 5 至此，用户已登录。将用户信息放入request
		TbUser user = JsonUtils.jsonToPojo(result.getData().toString(), TbUser.class);
		System.out.println("用户已登录：" + user.getUsername());
		request.setAttribute("user", user);

		// 6 还需合并购物车
		// 判断cookie购物车是否存在
		Map<Long, Integer> cartMap = getCartFromCookie(request);
		if (cartMap.size() > 0) {
			// 若存在，则需合并
			cartService.addCart(user.getId(), cartMap);
			// 清空cookie
			CookieUtils.deleteCookie(request, response, CART_COOKIE_NAME);
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

	private Map<Long, Integer> getCartFromCookie(HttpServletRequest request) {
		String cartJson = CookieUtils.getCookieValue(request, CART_COOKIE_NAME);
		if (StringUtils.isNotBlank(cartJson)) {
			try {
				Map<Long, Integer> map = JsonUtils.getMapper().readValue(cartJson,
						new TypeReference<HashMap<Long, Integer>>() {
						});
				return map;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new HashMap<>();
	}
}
