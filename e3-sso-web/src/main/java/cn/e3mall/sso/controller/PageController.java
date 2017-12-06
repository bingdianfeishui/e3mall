package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("/page/register")
	public String showRegisterPage(String returnUrl, Model model) {
		//设置返回url
		if(StringUtils.isNotBlank(returnUrl))
			model.addAttribute("redirect", returnUrl);
		return "register";
	}

	@RequestMapping("/page/login")
	public String showLoginPage(String returnUrl, Model model) {
		//设置返回url
		if(StringUtils.isNotBlank(returnUrl))
			model.addAttribute("redirect", returnUrl);
		return "login";
	}
}
