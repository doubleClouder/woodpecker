<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>新增告警配置</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<div class="container-fluid">
    <!-- BEGIN PAGE CONTENT LIST-->
    <div id="contentList" class="row-fluid">
        <!-- BEGIN FORM-->
        <form id="form_alarmconfig" action="#" class="form-horizontal">
             <div class="control-group">
                <label class="control-label">用户ID<span class="required">*</span></label>
                <div class="controls">
                    <input type="text" name="userId" class="span6 m-wrap" value="${username}" readonly="readonly" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">应用名称<span class="required">*</span></label>
                <div class="controls">
                    <select name="appName" class="m-wrap span6" id="appName">
                            <option value="0">应用名称</option>
                            <c:forEach items="${appInfos}" var="appInfo">
                                <option value="${appInfo.appName}">${appInfo.appName}</option>
                            </c:forEach>
                      </select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">IP<span class="required">*</span></label>
                <div class="controls">
                    <select name="ip" class="m-wrap span6" id="ip">
                            <option value="0">IP</option>
                       </select>
                 </div>
             </div>
            <div class="control-group">
                <label class="control-label">异常类型<span class="required">*</span></label>
                <div class="controls">
                    <input type="text" name="exceptionType" class="span6 m-wrap" value="all"/>
                </div>
           </div>
           <div class="control-group">
               <label class="control-label">阀值<span class="required">*</span></label>
               <div class="controls">
                   <input type="text" name="threshold" class="span6 m-wrap" value=""/>
               </div>
           </div>
           <div class="control-group">
               <label class="control-label">email<span class="required">*</span></label>
               <div class="controls">
                   <input type="text" name="email" class="span6 m-wrap" value=""/><a><font color="red">多个邮箱用;分隔</font></a>
               </div>
           </div>
           <div class="form-actions">
              <button id="btn_alarmconfig_save_new" type="button" class="btn green">保存</button>
              <button id="btn_alarmconfig_back_new" type="button" class="btn">返回</button>
          </div>
        </form>
        <!-- END FORM-->
    </div>
    <!-- END PAGE CONTENT LIST-->

</div>
<!-- END PAGE CONTAINER-->
</body>
</html>