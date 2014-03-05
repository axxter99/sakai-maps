<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://edia.nl/jsp/tags/edia-sakai-tags" prefix="est"%>
<html>
<head>
	<title>Maps</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=${config.googleMapsKey}" type="text/javascript"></script>
	<script src="js/mapiconmaker_packed.js" type="text/javascript"></script>
	<script src="js/EEvent.js" type="text/javascript"></script>
	<script src="js/EMap.js" type="text/javascript"></script>
	<script src="js/mapapp.js" type="text/javascript"></script>
	<%=request.getAttribute("sakai.html.head")%>
	<link rel="stylesheet" href="css/sakaimaps.css" type="text/css">
	
	<script type="text/javascript">

	var toolPlacementId = '<%=request.getParameter("sakai.tool.placement.id")%>';
	/** 
	 * If the user is returning to the map after editing the files that belong to a new marker,
	 * currentLatLng gets set so that an edit window can be popped up.
	 */
	var currentLatLng;
	/** 
	 * If the user is returning to the map after editing the files that belong to an existng marker,
	 * currentMarkerId gets set so that an edit window can be popped up with the right marker.
	 */
	var currentMarkerId;

	<c:if test="${state eq 'editmarker'}">
		currentLatLng = new GLatLng(${markerLat}, ${markerLng});
		<c:if test="${not empty markerId}">
			currentMarkerId = ${markerId};
		</c:if>
	</c:if>
	
	function bootstrap() {
		initMapApp();
		
		<c:choose>
			<c:when test="${state eq 'editmarker'}">
				loadMap(${centerLat}, ${centerLng}, ${zoom}, ${type});
			</c:when>
			
			<c:otherwise>
				<c:if test="${not empty config.defaultMap}">
					loadMap(${config.defaultMap.centerLat}, ${config.defaultMap.centerLng}, ${config.defaultMap.zoom} ${not empty config.defaultMap.type ? ", " : ""} ${config.defaultMap.type});
				</c:if>
			</c:otherwise>
		</c:choose>
		
		<%= request.getAttribute("sakai.html.body.onload") %>
	}
	
	EEvent.addListener(window, "load", bootstrap);
	EEvent.addListener(window, "unload", GUnload);

	</script>
</head>
<body>
<div class="portletBody">

<est:allowed permission="sakaimaps.admintypes">
	<div class="navIntraTool">
		<a href="types.spring"><s:message code="page.index.action.types"/></a>
		<est:allowed permission="sakaimaps.admintool">
			<a href="admin.spring"><s:message  code="page.index.action.admin"/></a>
		</est:allowed>
	</div>
</est:allowed>

<c:if test="${fn:length(config.markerTypes) > 1}">
	<div id="markerTypesSelector">
		<ul>
			<li><a href="" onclick="filterMarkers(0);return false;"><s:message code="page.index.showall"/></a></li>
			<c:forEach var="markerType" items="${config.markerTypes}" varStatus="varStatus">
				<li>|<a href="" onclick="filterMarkers(${markerType.id});return false;"><img src="icons/${markerType.image}" width="10" height="17" border="0"></a><a href="" onclick="filterMarkers(${markerType.id});return false;">${markerType.name}</a></li>
			</c:forEach>
		</ul>
	</div>
</c:if>

<div id="mapnav">
	<div id="map"></div>
	<div id="mapSelectButtons">
		<c:forEach var="val" items="${config.maps}" varStatus="varStatus">
			<c:set var="map" value="${config.maps[varStatus.index]}"/>
			<div class="mapSelectButton"><a href="" onclick="loadMap(${map.centerLat}, ${map.centerLng}, ${map.zoom} ${not empty map.type? ", " : ""} ${map.type});return false;"><img src="${(not empty map.image ? map.image : "gfx/empty_map.jpg")}" alt="Click here to select the map of ${map.name}, lat: ${map.centerLat}, lng: ${map.centerLng}" title="Click here to select the map of ${map.name}, lat: ${map.centerLat}, lng: ${map.centerLng}" border="0"/></a></div>
		</c:forEach>
	</div>
</div>
<div id="mapfooter">Maps tool powered by <a href="http://www.edia.nl" title="www.edia.nl" target="_blank">Edia: www.edia.nl</a>.</div>
</div>

</body>
</html>