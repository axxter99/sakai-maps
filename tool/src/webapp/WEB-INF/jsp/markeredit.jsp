<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="css/sakaimaps.css" type="text/css">
	<title>Edit marker properties</title>
</head>
<body marginwidth="0" marginheight="0">
	<div class="infowindow">
	<form id="poiform">
	
		<input id="cid_input" type="hidden" name="marker.id" value="${marker.id}"/>
		<h1><c:choose>
			<c:when test="${empty marker.id}"><s:message code="page.markeredit.title.add"/></c:when>
			<c:otherwise><s:message code="page.markeredit.title.edit"/></c:otherwise>
		</c:choose>
		</h1>
		
		<table>
			
			<tr>
				<th><label for="ctitle_input"><s:message code="page.markeredit.label.title"/></label></th>
				<td><input id="ctitle_input" type="text" name="marker.title" value="${marker.title}"/></td>
			</tr>
			
			<tr>
				<th><label for="cdesc_input"><s:message code="page.markeredit.label.text"/></label></th>
				<td><textarea id="cdesc_input" name="marker.description" rows="4">${marker.description}</textarea></td>
			</tr>
			
			<tr>
				<th><label><s:message code="page.markeredit.label.resources"/></label></th>
				<td>
					<input type="hidden" id="crescount_input" name="marker.resources.length" value="${fn:length(resources)}"/>
					<c:forEach items="${resources}" var="resource" varStatus="status">
						<input type="hidden" id="cres${status.index}_input" name="marker.resource.${status.index}" value="${resource.id}"/>
						<span class="resourceSpan">
							<a href="${resource.url}" title="${resource.id}" target="_blank"><img border="0" src="/library/image/${resource.contentTypeImage}"/></a>
							<a href="${resource.url}" title="${resource.displayName}" target="_blank">${resource.displayName}</a>
						</span>
					</c:forEach>
					<a class="editfileslink" href="javascript://" onclick="submitToFilePickerProxy(); return false;"><s:message code="page.markeredit.action.editresources"/></a>
				</td>
			</tr>
			
			<tr>
				<th><label for="curl_input"><s:message code="page.markeredit.label.url"/></label></th>
				<td><input id="curl_input" type="text" name="marker.url" value="${marker.url}"/></td>
			</tr>
			
			<c:choose>
				<c:when test="${fn:length(markerTypes) > 1}">
					<tr>
						<th><label for="ctype_input"><s:message code="page.markeredit.label.type"/></label></th>
						<td>
							<select id="ctype_input" name="marker.type">
								<c:forEach var="markerType" items="${markerTypes}">
									<option value="${markerType.id}" ${marker.type.id == markerType.id ? 'selected="selected"' : ''}>${markerType.name}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</c:when>
				<c:when test="${fn:length(markerTypes) == 1 && not empty marker.type}">
					<input id="singlemarkertype" name="marker.type" type="hidden" value="${marker.type.id}"/>
				</c:when>
				<c:otherwise>
					<input id="singlemarkertype" name="marker.type" type="hidden" value="${defaultMarkerType.id}"/>
				</c:otherwise>
			</c:choose>
			
			<tr>
				<th>&nbsp;</th>
				<td>
					<div id="buttonpanel">
						<input type="button" id="savebutton${marker.id}" class="infowindowbutton" value="<s:message code="page.markeredit.submit"/>"/> &nbsp; 
						<input type="button" id="cancelbutton${marker.id}" class="infowindowbutton" value="<s:message code="page.markeredit.cancel"/>"/>
					</div>
				</td>
			</tr>
		</table>
	</form>
	</div>
</body>
</html>