package com.letv.woodpecker.controller;

import com.letv.auth.core.Authentication;
import com.letv.woodpecker.auth.AuthConstant;
import com.letv.woodpecker.model.AppInfo;
import com.letv.woodpecker.model.ExceptionInfo;
import com.letv.woodpecker.service.ApplicationService;
import com.letv.woodpecker.service.ExceptionService;
import com.letv.woodpecker.vo.ExceptionVo;
import com.mongodb.DBObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhusheng on 17/3/22.
 */
@Controller
@RequestMapping(value = "/woodpecker/exception")
public class ExceptionInfoController extends BaseController{
    @Resource
    private ExceptionService exceptionService;
    @Resource
    private ApplicationService applicationService;

    @RequestMapping("/toAllExceptionsPage")
    public String toAllExceptionsPage(ModelMap model, HttpServletRequest request){
        String userId = getUseId(request);
        List<AppInfo> appInfos = applicationService.queryAllApps(userId,0,Integer.MAX_VALUE);
        model.put("appInfos",appInfos);
        return "exception/exceptionlist";
    }

    @RequestMapping(value = "/queryAllExceptionsPage",method = RequestMethod.POST)
    public Map<String,Object> queryAllExceptionsPage(@RequestBody MultiValueMap<String,String> valueMap,HttpServletResponse response){
        Map<String,String> params = valueMap.toSingleValueMap();
        int pageStart = Integer.valueOf(params.get("iDisplayStart"));
        int pageSize = Integer.valueOf(params.get("iDisplayLength"));
        String userId = params.get("username");
        String appName = params.get("appName");
        String startTime = params.get("startTime");
        if(startTime == null){
            startTime = "";
        }
        String endTime = params.get("endTime");
        if(endTime == null){
            endTime = "";
        }
        List<ExceptionInfo> exceptionInfoList = exceptionService.queryAllExceptions(userId,appName,startTime,endTime,pageStart,pageSize);
        long count = exceptionService.getAllExceptionCount(userId,appName,startTime,endTime);
        Map<String,Object> result = getSuccessMap();
        setResContent2Json(response);
        result.put("iTotalRecords", count);
        result.put("iTotalDisplayRecords", count); //totalAfterFilter
        result.put("data", exceptionInfoList.toArray());
        return result;
    }

    @RequestMapping(value = "/toExceptionVersion2Page", method = RequestMethod.GET)
    public String toExceptionVersion2Page(ModelMap model,HttpServletRequest request){
        String userId = getUseId(request);
        List<AppInfo> appInfos = applicationService.queryAllApps(userId,0,Integer.MAX_VALUE);
        model.put("appInfos",appInfos);
        return "exception/exception_version2";
    }

    @RequestMapping(value = "/queryExceptionPage", method = RequestMethod.POST)
    public Map<String, Object> queryExceptionPage(@RequestBody MultiValueMap<String,String> valueMap,HttpServletResponse response){
        Map<String,String> params = valueMap.toSingleValueMap();
        int pageStart = Integer.valueOf(params.get("iDisplayStart"));
        int pageSize = Integer.valueOf(params.get("iDisplayLength"));
        String userId = params.get("username");
        String appName = params.get("appName");
        String startTime = params.get("startTime");
        if(startTime == null){
            startTime = "";
        }
        String endTime = params.get("endTime");
        if(endTime == null){
            endTime = "";
        }
        List<DBObject> dbObjects = exceptionService.queryListByScheduler(userId,appName,startTime,endTime);
        List<ExceptionVo> exceptionVos = new ArrayList<>();
        for(int i=pageStart; i<dbObjects.size() && i<pageStart+pageSize; i++){
            ExceptionVo vo = new ExceptionVo();
            vo.setAppName(dbObjects.get(i).get("appName").toString());
            vo.setExceptionType(dbObjects.get(i).get("exceptionType").toString());
            vo.setCount(Float.valueOf( dbObjects.get(i).get("count").toString() ).intValue());
            exceptionVos.add(vo);
        }
        Map<String,Object> result = getSuccessMap();
        setResContent2Json(response);
        result.put("iTotalRecords", dbObjects.size());
        result.put("iTotalDisplayRecords", dbObjects.size()); //totalAfterFilter
        result.put("data", exceptionVos.toArray());
        return result;
    }

    @RequestMapping(value = "/classifyByMd5", method = RequestMethod.POST)
    public Map<String,Object> classifyByMd5(@RequestBody MultiValueMap<String,String> valueMap,HttpServletResponse response){
        Map<String,String> params = valueMap.toSingleValueMap();
        int pageStart = Integer.valueOf(params.get("iDisplayStart"));
        int pageSize = Integer.valueOf(params.get("iDisplayLength"));
        String appName = params.get("appName");
        String startTime = params.get("startTime");
        String endTime = params.get("endTime");
        String exceptionType = params.get("exceptionType");
        List<DBObject> dbObjects = exceptionService.classifyByMd5(appName,startTime,endTime,exceptionType);
        List<ExceptionVo> exceptionVos = new ArrayList<>();
        for(int i = pageStart; i<dbObjects.size() && i<pageStart+pageSize; i++){
            ExceptionVo vo = new ExceptionVo();
            vo.setAppName(dbObjects.get(i).get("appName").toString());
            vo.setExceptionType(exceptionType);
            vo.setContentMd5(dbObjects.get(i).get("contentMd5").toString());
            String msg = dbObjects.get(i).get("msg").toString();
            msg = msg.substring(msg.indexOf(" - ")+3);
            vo.setContent(msg);
            vo.setCount(Float.valueOf( dbObjects.get(i).get("count").toString() ).intValue());
            exceptionVos.add(vo);
        }
        Map<String,Object> result = getSuccessMap();
        setResContent2Json(response);
        result.put("iTotalRecords", dbObjects.size());
        result.put("iTotalDisplayRecords", dbObjects.size()); //totalAfterFilter
        result.put("data", exceptionVos.toArray());
        return result;

    }

    @RequestMapping(value = "/exceptionListByDetail", method = RequestMethod.POST)
    public Map<String,Object> exceptionListByDetail(@RequestBody MultiValueMap<String,String> valueMap,HttpServletResponse response){
        Map<String,String> params = valueMap.toSingleValueMap();
        int pageStart = Integer.valueOf(params.get("iDisplayStart"));
        int pageSize = Integer.valueOf(params.get("iDisplayLength"));
        String appName = params.get("appName");
        String startTime = params.get("startTime");
        String endTime = params.get("endTime");
        String exceptionType = params.get("exceptionType");
        String contentMd5 = params.get("contentMd5");
        List<ExceptionInfo> results = exceptionService.queryList(appName,exceptionType,contentMd5,startTime,endTime,pageStart,pageSize);
        long count = exceptionService.getCountByDetail(appName,exceptionType,contentMd5,startTime,endTime);
        Map<String,Object> result = getSuccessMap();
        setResContent2Json(response);
        result.put("iTotalRecords", count);
        result.put("iTotalDisplayRecords", count); //totalAfterFilter
        result.put("data", results.toArray());
        return result;
    }

    @RequestMapping(value = "/toExceptionChartsPage",method = RequestMethod.GET)
    public String toExceptionChartsPage(ModelMap modelMap){


        return "exception/exception_charts";
    }

    @RequestMapping(value = "/exceptionNumChart")
    public Map<String,Object> exceptionNumChart(@RequestBody MultiValueMap<String,String> valueMap,HttpServletResponse response,HttpServletRequest request){
        Map<String,String> params = valueMap.toSingleValueMap();
        String userId = getUseId(request);
        String startTime = params.get("startTime");
        if(startTime==null || "".equals(startTime)){
            startTime = nowTime(true);
        }
        String endTime = params.get("endTime");
        if(endTime==null || "".equals(endTime)) {
            endTime = nowTime(false);
        }
        List<DBObject> exceptionNums = exceptionService.findExceptionNumByApp(userId,startTime,endTime);
        List<ExceptionVo> vos = new ArrayList<>();
        for(int i = 0;i<exceptionNums.size(); i++){
            ExceptionVo vo = new ExceptionVo();
            vo.setAppName(exceptionNums.get(i).get("appName").toString());
            vo.setCount(Float.valueOf( exceptionNums.get(i).get("count").toString() ).intValue());
            vos.add(vo);
        }
        Map<String,Object> result = getSuccessMap();
        setResContent2Json(response);
        result.put("data",vos.toArray());
        return result;
    }

    @RequestMapping(value = "/exceptionNumPieChart")
    public Map<String,Object> exceptionNumPieChart(@RequestBody MultiValueMap<String,String> valueMap,HttpServletResponse response,HttpServletRequest request){
        Map<String,String> params = valueMap.toSingleValueMap();
        String userId = getUseId(request);
        String startTime = params.get("startTime");
        if(startTime==null || "".equals(startTime)){
            startTime = nowTime(true);
        }
        String endTime = params.get("endTime");
        if(endTime==null || "".equals(endTime)) {
            endTime = nowTime(false);
        }
        List<DBObject> exceptionNums = exceptionService.findExceptionNumByApp(userId,startTime,endTime);
        List<ExceptionVo> vos = new ArrayList<>();
        for(int i = 0;i<exceptionNums.size(); i++){
            ExceptionVo vo = new ExceptionVo();
            vo.setAppName(exceptionNums.get(i).get("appName").toString());
            vo.setCount(Float.valueOf( exceptionNums.get(i).get("count").toString() ).intValue());
            vos.add(vo);
        }
        //按异常类型分组
        List<DBObject> dbObjects = exceptionService.queryListByScheduler(userId,null,startTime,endTime);
        List<ExceptionVo> exceptionVos = new ArrayList<>();
        for(int i = 0;i<dbObjects.size(); i++){
            ExceptionVo vo = new ExceptionVo();
            vo.setAppName(dbObjects.get(i).get("appName").toString());
            vo.setExceptionType(dbObjects.get(i).get("exceptionType").toString());
            vo.setCount(Float.valueOf( dbObjects.get(i).get("count").toString() ).intValue());
            exceptionVos.add(vo);
        }
        Map<String,Object> result = getSuccessMap();
        setResContent2Json(response);
        result.put("data",vos.toArray());
        result.put("exceptionType",exceptionVos.toArray());
        return result;
    }

    /**
     * 获取当前时间
     * @param isZeroHour  true : 返回今日零点，false:返回当前时间
     *
     * */
    private String nowTime(boolean isZeroHour){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(isZeroHour){
            format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        }
        Date now = new Date();
        return format.format(now);
    }




}
