<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="css/sakaimaps.css" type="text/css">
	<title><s:message code="page.markerview.title"/></title>
</head>
<body marginwidth="0" marginheight="0">
	<div class="infowindow">
		<h1>${marker.title}</h1>
		<c:if test="${not empty marker.type}">
			<p><b>Type:</b> ${marker.type.name}</p>
		</c:if>
		<p>${marker.description}</p>
		<c:forEach items="${resources}" var="resource">
			<span class="resourceSpan">
				<a href="${resource.url}" title="${resource.id}" target="_blank"><img border="0" src="/library/image/${resource.contentTypeImage}"/></a>
				<a href="${resource.url}" title="${resource.displayName}" target="_blank">${resource.displayName}</a>
			</span>
		</c:forEach>
		<a id="readmorelink${marker.id}" class="readmorelink" href="${marker.url}" title="${marker.url}" target="_blank">More info...</a>
		<c:if test="${marker.canEdit || marker.canDelete}">
			<button id="editbutton${marker.id}" class="infowindowbutton" ${(marker.canEdit ? "" : "disabled=\"disabled\"")}><s:message code="page.markerview.button.edit"/></button> &nbsp; 
			<button id="deletebutton${marker.id}" class="infowindowbutton" ${(marker.canDelete ? "" : "disabled=\"disabled\"")}><s:message code="page.markerview.button.delete"/></button>
		</c:if>
	</div>
</body>
</html>