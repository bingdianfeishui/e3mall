package cn.e3mall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("/page/register")
	public String showRegisterPage() {
		return "register";
	}

	@RequestMapping("/page/login")
	public String showLoginPage() {
		return "login";
	}
}
