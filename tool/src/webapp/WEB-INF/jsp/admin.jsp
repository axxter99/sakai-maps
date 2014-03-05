<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<html>
<head>
	<title><s:message  code="page.admin.title"/></title>
	<%=request.getAttribute("sakai.html.head")%>
</head>
<body text="#000000" onload="<%= request.getAttribute("sakai.html.body.onload") %>">
	<div class="portletBody">
	<h2><s:message  code="page.admin.header" /></h2>
	<form method="post">
	<s:bind path="config.maps">
		<h3><s:message code="page.heading.maps"/></h3>
		<p><s:message code="page.help.maps"/></p>
		<c:forEach var="val" items="${status.value}" varStatus="varStatus">
			<h4><s:message code="page.label.map" arguments="${varStatus.count}"/></h4>
			<s:bind path="config.maps[${varStatus.index}].name">
				<p class="shorttext">
					<label for="${status.expression}">
						<s:message  code="page.label.name" />
					</label>
					<input type="text" name="${status.expression}" class="req" id="${status.expression}" maxlength="255" size="20" value="${status.value}" />
				</p>
			</s:bind>
			<s:bind path="config.maps[${varStatus.index}].centerLat">
				<p class="shorttext">
					<label for="${status.expression}">
						<s:message code="page.label.centerLat" />
					</label>
					<input type="text" name="${status.expression}" class="req" id="${status.expression}" maxlength="20" size="20" value="${status.value}" />
				</p>
			</s:bind>
			<s:bind path="config.maps[${varStatus.index}].centerLng">
				<p class="shorttext">
					<label for="${status.expression}">
						<s:message  code="page.label.centerLng" />
					</label>
					<input type="text" name="${status.expression}" class="req" id="${status.expression}" maxlength="20" size="20" value="${status.value}" />
				</p>
			</s:bind>
			<s:bind path="config.maps[${varStatus.index}].zoom">
				<p class="shorttext">
					<label for="${status.expression}">
						<s:message  code="page.label.zoom" />
					</label>
					<select name="${status.expression}" id="${status.expression}">
						<c:forEach var="zoomLevel" begin="1" step="1" end="20">
							<c:choose>
								<c:when test="${zoomLevel eq status.value}">
									<option selected="selected">${zoomLevel}</option>
								</c:when>
								<c:otherwise>
									<option>${zoomLevel}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</p>
			</s:bind>
			<s:bind path="config.maps[${varStatus.index}].type">
				<p class="shorttext">
					<label for="${status.expression}">
						<s:message  code="page.label.type" />
					</label>
					<select name="${status.expression}" id="${status.expression}">
						<option value="G_NORMAL_MAP" ${(status.value eq "G_NORMAL_MAP" ? "selected=\"selected\"" : "")}><s:message code="page.label.type.normal" /></option>
						<option value="G_SATELLITE_MAP" ${(status.value eq "G_SATELLITE_MAP" ? "selected=\"selected\"" : "")}><s:message code="page.label.type.satellite" /></option>
						<option value="G_HYBRID_MAP" ${(status.value eq "G_HYBRID_MAP" ? "selected=\"selected\"" : "")}><s:message code="page.label.type.hybrid" /></option>
					</select>
				</p>
			</s:bind>
			<s:bind path="config.maps[${varStatus.index}].image">
				<p class="shorttext">
					<label for="${status.expression}">
						<s:message code="page.label.image" />
					</label>
					<input type="text" name="${status.expression}" class="req" id="${status.expression}" maxlength="255" size="20" value="${status.value}" />
				</p>
			</s:bind>
		</c:forEach>
	</s:bind>
	
	<br/>
	
	<h3><s:message code="page.heading.defaultmap"/></h3>
	<p><s:message code="page.help.defaultmap"/></p>
	<p class="shorttext">
		<label for="defaultMapSelector"><s:message code="page.label.defaultmap"/></label>
		<select name="defaultMapSelector" id="defaultMapSelector">
			<option value="-1"><s:message code="page.value.none"/></option>
			<c:forEach var="map" items="${config.maps}" varStatus="varStatus">
				<option value="${varStatus.index}" ${(config.defaultMap eq map ? "selected=\"selected\"" : "")}>${map.name}</option>
			</c:forEach>
		</select>
	</p>

	<s:bind path="config.googleMapsKeys">
		<h3><s:message code="page.heading.keys"/></h3>
		<p><s:message code="page.help.keys"/></p>
		<c:forEach var="gmk" items="${status.value}" varStatus="varStatus">
			<div id="googleMapsKey.${varStatus.count}">
				<h4><s:message  code="page.heading.googleMapKey" arguments="${varStatus.count}"/></h4>
				<s:bind path="config.googleMapsKeys[${varStatus.index}].url">
					<p class="shorttext">
						<label for="${status.expression}" ${status.error ? "style=\"color:red;\"" : ""}>
							<s:message  code="page.label.url" />
						</label>
						<input type="text" name="${status.expression}" id="${status.expression}" maxlength="255" size="50" value="${status.value}" />
					</p>
				</s:bind>
				<s:bind path="config.googleMapsKeys[${varStatus.index}].key">
					<p class="shorttext">
						<label for="${status.expression}" ${status.error ? "style=\"color:red;\"" : ""}>
							<s:message code="page.label.key" />
						</label>
						<input type="text" name="${status.expression}" id="${status.expression}" maxlength="255" size="50" value="${status.value}" />
					</p>
				</s:bind>
				<p class="shorttext">
					<label for="${status.expression}">
						<s:message code="page.label.deletekey" />
					</label>
					<input type="submit" id="deleteGoogleMapsKey.${varStatus.index}" name="deleteGoogleMapsKey.${varStatus.index}" value="<s:message code="page.label.delete"/>"/>
				</p>
			</div>
		</c:forEach>
		<br/>
		<p class="shorttext">
			<label for="${status.expression}">
				<s:message code="page.label.addkey" />
			</label>
			<input type="submit" id="addGoogleMapsKey" name="addGoogleMapsKey" value="<s:message code="page.label.add"/>"/>
		</p>
	</s:bind>
	<br/>

	<s:bind path="config.creationDate">
		<p class="shorttext">
			<label for="${status.expression}">
				<s:message  code="page.label.${status.expression}" />
			</label>
			<input type="text" name="${status.expression}" class="req" id="${status.expression}" maxlength="255" size="20" value="<fmt:formatDate type="both" value="${status.value}"/>" disabled="disabled"/>
		</p>
	</s:bind>
	<s:bind path="config.modificationDate">
		<p class="shorttext">
			<label for="${status.expression}">
				<s:message  code="page.label.${status.expression}" />
			</label>
			<input type="text" name="${status.expression}" class="req" id="${status.expression}" maxlength="255" size="20" value="<fmt:formatDate type="both" value="${status.value}"/>" disabled="disabled"/>
		</p>
	</s:bind>		
	<br/>	
	<div class="act">
		<input type="submit" class="active" value="<s:message  code="page.admin.submit" />"  accesskey="s" />
		<input type="button" onclick="javascript:document.location='index.spring';" value="<s:message  code="page.admin.cancel"/>"/>
	</div>
	</form>
	</div>
</body>
</html>