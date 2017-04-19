package com.letv.woodpecker.controller;

import com.letv.auth.core.Authentication;
import com.letv.auth.service.ISsoService;
import com.letv.woodpecker.auth.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhusheng on 17/3/29.
 */
@Controller
@Slf4j
public class LoginController extends BaseController{

	@Autowired
	private ISsoService ssoService;
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "cas_ticket", required = false, defaultValue = "") String cas_ticket,
                        @RequestParam(value = "url", required = false, defaultValue = "") String url, RedirectAttributes attr) {
		String host = request.getServerName();
		try {
			Authentication auth = ssoService.login(request, response, url, cas_ticket, 0);
			if(auth != null) {
				request.getSession().setAttribute(AuthConstant.AUTH_NAME, auth);
				return "redirect:/index";
			}
		} catch (Exception ex) {
			log.error("{}", ex);
		}
		return null;
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request, HttpServletResponse reponse , Model model){
		ssoService.logout(request, reponse);
		return "redirect:/login";
	}

}
