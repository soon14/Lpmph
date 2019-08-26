< % @ page import="com.manfirst.framework.config.SysConfigHelper " %  >
< % @ page import="com.manfirst.hxcareer.tool.constants.CareerConstant" % > 
< % @ page import="com.manfirst.framework.session.SessionHolder"%>
<%@  page language="java" import="java.util.*" pageEncoding="UTF-8" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
   
<title>附件上传</title>
   
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->
<script type="text/javascript" src="${webroot}framework/jsLib/manage/extend/upload/js/AttachUpload.js"></script>
</head>
  
<body>
	<input type="hidden" id="sessionToken" value="< % = SessionHolder.getSession().getToken() % > ">
	<input type="hidden" id="fileUploaderUserNO" value="< % = SessionHolder.getUser().getUserNo() %>">
	<input type="hidden" id="fileUploaderServer" value="< % = SysConfigHelper.getInstance().getValue("FILEUPLOAD.SERVER") % >">
	<input type="hidden" id="fileUploaderUpFunction" value="< % = SysConfigHelper.getInstance().getValue("FILEUPLOAD.JFUP") % >">
	附件目录：<select id="imgUploadFolder" name="imgUploadFolder"></select>
	<div class="uploadifyMainBox" style="margin-top: 5px;">
		<div id="attachmentFileQueue" class="fileQueue" style="height: 300px;"></div>
		<input id="attachmentFileInput" type="file" name="attachmentFileInput" />
	</div>
	<div align="right" style="margin-top: 5px;">
		<input type="button" id="fileUploaderUpload" value="附件上传" />
	</div>
</body>
</html>
