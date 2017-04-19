package com.letv.woodpecker.controller;

import com.letv.woodpecker.model.AlarmConfig;
import com.letv.woodpecker.model.AppInfo;
import com.letv.woodpecker.service.AlarmConfigService;
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
@RequestMapping("/woodpecker/alarmconfig")
public class AlarmConfigController extends BaseController{
    @Resource
    private AlarmConfigService alarmConfigService;
    @Resource
    private ApplicationService applicationService;

    @RequestMapping("/toAlarmConfigPage")
    public String alarmConfigList(ModelMap model){
        return "alarmconfig/alarmconfig";
    }

    @RequestMapping("/toAlarmConfigAddPage")
    public String addAlarmConfigPage(String username,ModelMap model){
        List<AppInfo> apps = applicationService.queryAllApps(username,0,Integer.MAX_VALUE);
        model.put("appInfos",apps);
        return "alarmconfig/alarmconfig_new";
    }

    @RequestMapping(value = "/queryAlarmConfigPage",method = RequestMethod.POST)
    public Map<String,Object> queAppPage(@RequestBody MultiValueMap<String,String> valueMap, HttpServletResponse response){
        Map<String, String> params = valueMap.toSingleValueMap();
        String userId = params.get("username");
        int pageStart = Integer.valueOf(params.get("iDisplayStart"));
        int pageSize = Integer.valueOf(params.get("iDisplayLength"));
        List<AlarmConfig> results = alarmConfigService.queryAlarmConfigs(userId,pageStart,pageSize);
        long count = alarmConfigService.getConfigsCount(userId);
        Map<String,Object> result = getSuccessMap();
        setResContent2Json(response);
        result.put("iTotalRecords", count);
        result.put("iTotalDisplayRecords", count);
        result.put("data", results.toArray());
        return result;
    }

    @RequestMapping(value = "/deleteConfig/{userId}/{appName}/{ip}/{exceptionType}", method = RequestMethod.DELETE)
    public void deleteConfig(@PathVariable("userId") String userId, @PathVariable("appName") String appName,
                          @PathVariable("ip") String ip,@PathVariable("exceptionType") String exceptionType, HttpServletResponse response){
        ResultBean resultBean = new ResultBean(0,"success");
        AlarmConfig config = new AlarmConfig();
        config.setUserId(userId);
        config.setAppName(appName);
        config.setIp(ip);
        config.setExceptionType(exceptionType);
        try{
            alarmConfigService.deleteConfig(config);
        }catch (Exception e){
            resultBean.setCode(1);
            resultBean.setMessage("fail!");
        }
        printJSON(response,resultBean);
    }

    @RequestMapping(value = "/saveAlarmConfig",method = RequestMethod.POST)
    public void saveAlarmConfig(AlarmConfig alarmConfig, HttpServletResponse response){
        ResultBean result = new ResultBean(0,"success");
        try{
            alarmConfigService.saveAlarmConfig(alarmConfig);
        }catch (Exception e){
            result.setCode(1);
            result.setMessage("fail!");
        }
        printJSON(response,result);
    }

}
