package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.sso.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result userRegister(String username, String password, String phone) throws Exception {
		return userService.register(username, password, phone);
	}

	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkParam(@PathVariable String param, @PathVariable int type) throws Exception {

		return userService.checkUserInfo(param, type);
	}

	@RequestMapping(value = "/user/login")
	@ResponseBody
	public E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		// // 获取cookie中的token
		// String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		//
		// // 若存在token，则更新session的过期时间
		// if(StringUtils.isNotBlank(token)) {
		//
		// }
		E3Result result = userService.login(username, password);

		if (result.getStatus() == 200) {
			// 将token写入cookie,不设置过期时间（关闭浏览器失效），不编码（token都是字母数字）
			String token = result.getData().toString();
			System.out.println("=====================" + TOKEN_KEY);
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}

		return result;
	}

	/*
	 * @RequestMapping(value="/user/query/{token}",
	 * produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	 * 
	 * @ResponseBody public String queryUserInfo(@PathVariable String token, String
	 * callback) { E3Result result = userService.getUserInfo(token);
	 * 
	 * // 若为jsonp if (StringUtils.isNotBlank(callback)) { return callback + "(" +
	 * JsonUtils.objectToJson(result) + ");"; }
	 * 
	 * return JsonUtils.objectToJson(result); }
	 */

	@RequestMapping(value = "/user/query/{token}")
	@ResponseBody
	public Object queryUserInfo(@PathVariable String token, String callback) {
		E3Result result = userService.getUserInfo(token);

		// 若为jsonp
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mapping = new MappingJacksonValue(result);
			mapping.setJsonpFunction(callback);
			return mapping;
		}

		return result;
	}

	@RequestMapping(value = "/user/logout")
	@ResponseBody
	public Object logout(HttpServletRequest request, HttpServletResponse response, String callback) {
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		CookieUtils.deleteCookie(request, response, TOKEN_KEY);
		E3Result result = userService.logout(token);
		CookieUtils.deleteCookie(request, response, TOKEN_KEY);
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mapping = new MappingJacksonValue(result);
			mapping.setJsonpFunction(callback);
			return mapping;
		}
		return result;
	}
}
