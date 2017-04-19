package com.letv.woodpecker.controller;

import com.alibaba.fastjson.JSON;
import com.letv.auth.core.Authentication;
import com.letv.woodpecker.auth.AuthConstant;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhusheng on 17/3/27.
 */
public class BaseController {

    protected Map<String, Object> getSuccessMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "0");
        result.put("msg", "成功");
        return result;
    }

    protected Map<String, Object> getFailMap(String rsMsg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "A00001");
        result.put("msg", rsMsg);
        return result;
    }

    protected void printJSON(HttpServletResponse servletResponse, Object obj ) {
        PrintWriter writer = null;
        try {
            servletResponse.setContentType("application/json");
            writer = servletResponse.getWriter();
            writer.print(JSON.toJSONString(obj));
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if ( writer != null ) {
                writer.close();
            }
        }
    }
    protected void setResContent2Json(HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
    }

    protected String getUseId(HttpServletRequest request){
        Authentication auth = (Authentication) request.getSession().getAttribute(AuthConstant.AUTH_NAME);
        if(auth!=null){
            return auth.getUsername();
        }
        return "";
    }

    @Data
    protected class ResultBean{
        private int code;
        private String message;
        private Object data;
        public ResultBean(int code,String message){
            this.code = code;
            this.message = message;
        }
    }


}
