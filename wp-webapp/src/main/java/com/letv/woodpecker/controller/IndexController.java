package com.letv.woodpecker.controller;

import com.letv.auth.core.Authentication;
import com.letv.woodpecker.auth.AuthConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhusheng on 17/3/29.
 */
@Controller
public class IndexController extends BaseController {

    @RequestMapping(value={"/","/index"})
    public String index(HttpServletRequest request, Model model){
        Authentication auth = (Authentication) request.getSession().getAttribute(AuthConstant.AUTH_NAME);
        if (auth == null) {
            return "redirect:/login";
        } else {
            String username = auth.getUsername();
            model.addAttribute("username", username);
            return "index" ;
        }
    }
}
