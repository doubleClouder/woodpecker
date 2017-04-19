package com.letv.woodpecker.controller;


import com.letv.woodpecker.model.AppInfo;
import com.letv.woodpecker.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by zhusheng on 17/3/29.
 */
@Slf4j
@Controller
@RequestMapping("/woodpecker/application")
public class ApplicationController extends BaseController {

    @Resource
    private ApplicationService applicationService;

    @RequestMapping("/toAppListPage")
    public String queryAllApps(ModelMap model){
        return "application/application";
    }

    @RequestMapping(value = "/queryAppPage",method = RequestMethod.POST)
    public Map<String,Object> queAppPage(@RequestBody MultiValueMap<String,String> valueMap,HttpServletResponse response){
        Map<String, String> params = valueMap.toSingleValueMap();
        String userId = params.get("username");
        int pageStart = Integer.valueOf(params.get("iDisplayStart"));
        int pageSize = Integer.valueOf(params.get("iDisplayLength"));
        List<AppInfo> results = applicationService.queryAllApps(userId,pageStart,pageSize);
        long count = applicationService.queryAppsCount(userId);
        Map<String,Object> result = getSuccessMap();
        setResContent2Json(response);
        result.put("iTotalRecords", count);
        result.put("iTotalDisplayRecords", count);
        result.put("data", results.toArray());
        return result;
    }

    @RequestMapping("/toAppInfoAddPage")
    public String addAppInfoPage(ModelMap model){
        return "application/application_new";
    }

    @RequestMapping(value = "/deleteApp/{userId}/{appName}", method = RequestMethod.DELETE)
    public void deleteApp(@PathVariable("userId") String userId, @PathVariable("appName") String appName,HttpServletResponse response){
        ResultBean resultBean = new ResultBean(0,"success");
        try{
            applicationService.deleteApp(userId,appName);
        }catch (Exception e){
            resultBean.setCode(1);
            resultBean.setMessage("fail!");
            log.error("toAppInfoAddPage fail,{}",e);
        }
        printJSON(response,resultBean);
    }

    @RequestMapping(value = "/saveAppInfo",method = RequestMethod.POST)
    public void saveAppInfo(AppInfo appInfo,HttpServletResponse response){
        ResultBean result = new ResultBean(0,"success");
        try{
            applicationService.saveAppInfo(appInfo);
        }catch (Exception e){
            result.setCode(1);
            result.setMessage("fail!");
            log.error("saveAppInfo fail,{}",e);
        }
        printJSON(response,result);
    }

    @RequestMapping(value = "getIpByAppName/{username}/{appName}",method = RequestMethod.GET)
    public void getIpByAppName(@PathVariable("username") String userId,@PathVariable("appName")String appName,HttpServletResponse response){
        ResultBean result = new ResultBean(0,"success");
        try{
            AppInfo appInfo = applicationService.getIpByAppName(userId,appName);
            String[] ip = appInfo.getIp().split(";");
            result.setData(ip);
        }catch (Exception e){
            result.setCode(1);
            result.setMessage("fail!");
            log.error("saveAppInfo fail,{}",e);
        }
        printJSON(response,result);
    }
}
