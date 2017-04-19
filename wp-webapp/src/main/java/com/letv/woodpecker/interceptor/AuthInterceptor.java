package com.letv.woodpecker.interceptor;

import com.alibaba.fastjson.JSON;
import com.letv.auth.core.Authentication;
import com.letv.woodpecker.auth.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
		Authentication auth = (Authentication) request.getSession().getAttribute(AuthConstant.AUTH_NAME);
		String path = request.getServletPath();
		Map parameterMap = request.getParameterMap();
		if (parameterMap == null) {
			parameterMap = new HashMap();
		}
		if(null != auth){
			log.info("operation info:path={},userName={},ip={},param={}",path,auth.getUsername(),getRemoteHost(request),JSON.toJSONString(parameterMap));
		}

		if( auth == null ){
			String contextPath = request.getContextPath();
			String unAuthUrl = contextPath + "/login";
			response.sendRedirect(unAuthUrl);
		}else{
			if(path.startsWith("woodpecker.")){//只需要登陆权限
				return true;
			}
		}
		return true ;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		if( modelAndView != null ){
			Authentication auth = (Authentication) request.getSession().getAttribute(AuthConstant.AUTH_NAME);
			if( auth != null ){
				String username = auth.getUsername();
				modelAndView.addObject("username", username) ;
			}
		}
	}

	public String getRemoteHost(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}

}
