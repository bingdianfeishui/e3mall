package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// 打印控制台
		ex.printStackTrace();
		// 写日志
		logger.error("系统发生异常", ex);
		
		// jmail发邮件、发短信
		
		//显示错误页面
		ModelAndView mv = new ModelAndView();
		mv.setViewName("error/exception");
		return mv;
	}

}
