package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;


	@Autowired
	private UserService userService;

	/**
	 * 前处理 <br>
	 * 返回值：true，不拦截；false，拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1 从Cookie取得token
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		// 2 判断token是否为空, 未登录则放行
		if (StringUtils.isBlank(token))
			return true;
		// 3 若取到，则去sso项目查询是否登录
		E3Result result = userService.getUserInfo(token);
		// 4 判断用户是否登录。 若未登录或登录过期，直接放行
		if (result.getStatus() != 200)
			return true;

		// 5 至此，用户已登录。将用户信息放入request
		TbUser user = JsonUtils.jsonToPojo(result.getData().toString(),TbUser.class);
		System.out.println("用户已登录：" + user.getUsername());
		request.setAttribute("user", user);
		
		return true;
	}

	/**
	 * 后处理
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 方法调用后，返回前
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
