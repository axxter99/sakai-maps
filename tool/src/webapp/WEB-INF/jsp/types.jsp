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
	<title><s:message code="page.types.title"/></title>
	<link rel="stylesheet" href="css/markerselector.css"/>
	<script type="text/javascript" src="js/EEvent.js"></script>
	<script type="text/javascript" src="js/EMarkerSelector.js"></script>
	<script>
		EMarkerSelector.icons = 
			new Array(
				<c:forEach var="typeImage" items="${markerTypeImages}" varStatus="status">
				{image: "${typeImage.fileName}", color: "${typeImage.colorCode}"} ${fn:length(markerTypeImages) == status.count ? "" : ","}
				</c:forEach>
				)
	</script>

	<%=request.getAttribute("sakai.html.head")%>

</head>
<body onload="<%= request.getAttribute("sakai.html.body.onload") %>">
<div class="portletBody">
	<h2><s:message code="page.types.header" /></h2>
	<p><s:message code="page.types.help" /></p>
	<form method="POST">
	<table id="markertypetable">
		<thead>
			<tr>
				<th><s:message code="page.types.label.id"/></th>
				<th><s:message code="page.types.label.typename"/></th>
				<th><s:message code="page.types.label.markericon"/></th>
				<th colspan="3"><s:message code="page.types.label.actions"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="markertype" items="${markertypes}" varStatus="status">
				<tr>
					<td class="markerindex"><span title="markertype id">${markertype.id}</span><input class="markerid" type="hidden" name="markertypes[${status.index}].id" value="${markertype.id}"/></td>
					<td><input class="markername" type="text" name="markertypes[${status.index}].name" value="${markertype.name}"/></td>
					<td class="selectorcolumn">
						<select name="markertypes[${status.index+1}].image" class="markerselector">
							<c:forEach var="typeImage" items="${markerTypeImages}">
								<option value="${typeImage.fileName}" style="background-color: ${typeImage.colorCode};" ${markertype.image eq typeImage.fileName ? 'selected="selected"' : ''}>&nbsp;</option>
							</c:forEach>
						</select>
					</td>
					<td><img class="upbutton" title="<s:message code="page.types.button.moveup"/>" src="gfx/go-up.png"/></td>
					<td><img class="downbutton" title="<s:message code="page.types.button.movedown"/>" src="gfx/go-down.png"/></td>
					<c:choose>
						<c:when test="${markertype.canDelete}">
							<td><img class="removebutton" title="<s:message code="page.types.button.remove"/>" src="gfx/user-trash.png"/></td>
						</c:when>
						<c:otherwise>
							<td><img title="<s:message code="page.types.button.noremove"/>" src="gfx/user-trash-grayedout.png"/></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
			<tr>
				<td class="markerindex"><span title="no markertype id">-</span><input class="markerid" type="hidden" name="markertypes[].id" value=""/></td>
				<td><input class="markername" type="text" name="markertypes[].name" value=""/></td>
				<td class="selectorcolumn">
					<select name="markertypes[].image" class="markerselector">
						<c:forEach var="typeImage" items="${markerTypeImages}">
							<option value="${typeImage.fileName}" style="background-color: ${typeImage.colorCode};" ${markertype.image eq typeImage.fileName ? 'selected="selected"' : ''}>&nbsp;</option>
						</c:forEach>
					</select>
				</td>
				<td><img class="upbutton" title="<s:message code="page.types.button.moveup"/>" src="gfx/go-up.png"/></td>
				<td><img class="downbutton" title="<s:message code="page.types.button.movedown"/>" src="gfx/go-down.png"/></td>
				<td><img class="addbutton" title="<s:message code="page.types.button.add"/>" src="gfx/list-add.png"/></td>
			</tr>
			</tbody>
		</table>
		<div class="act">
			<input class="active" type="submit" value="<s:message code="page.types.submit"/>"/>
			<input type="button" onclick="javascript:document.location='index.spring';" value="<s:message  code="page.types.cancel"/>"/>
		</div>
	</form>
</div>
</body>
</html>