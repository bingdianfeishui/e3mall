package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转Controller
 * <p>Title: PageController</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: NULL.Co</p>
 * @author Lee
 * @date 2017年10月22日下午8:03:03
 * @version 1.0
 */
@Controller
public class PageController {
	@RequestMapping(value = "/")
	public String showIndex() {
		return "index";
	}

	@RequestMapping(value = "/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
}
