/** The info window template loaded from infowindow_show.html. 
var infoWindowShow;*/
/** The info window template loaded from infowindow_edit.html. 
var infoWindowEdit;*/
/** Info window options: the maximum width of an info window. */
var infoWindowOptions = {maxWidth: 250};
/** Enable writing to the GLog. */
var logEnabled = false;
/** True if this user is allowed to create markers on this map. */
var canCreateMarkers = true;
/** Hold all markers on the map for lookup purposes. Stored by id. */
var allMarkers;

var idSerial = 0;

function initMapApp() {
	if (GBrowserIsCompatible()) {
		
		EMap.init(document.getElementById("map"));
		registerListeners();		
		allMarkers = new Array();
		loadMarkerTypes();
	}
}

function registerListeners() {

	GEvent.addListener(EMap.map, "click", function(marker, latlng) {
		if (EMap.map.getInfoWindow().isHidden() && !marker) {
			openMarkerEditWindow(null, latlng);			
		}
	});
	GEvent.addListener(EMap.map, "zoomend", function(oldLevel, newLevel) {
		for (var i = 0; i < allMarkers.length; i++) {
			var m = allMarkers[i];
			if (m) {
				var newMarker = createMarker(m.id, m.getLatLng(), m.title, m.description, m.url, m.typeId, m.canEdit, m.canDelete, m.resources, newLevel);
				EMap.map.removeOverlay(m);
				addMarker(newMarker);
			}
		}
	});
}

function loadMarkerTypes() {
	GDownloadUrl("listmarkertypes.xml?sakai.tool.placement.id=" + toolPlacementId + rnd(), function(data, responseCode) {
		if (responseCode == 200) {
			var xml = GXml.parse(data);
			if (isError(xml)) return;
			
			var markerTypes = xml.documentElement.getElementsByTagName("markertype");
			for (var i = 0; i < markerTypes.length; i++) {
				var markerType = markerTypes[i];
				
				var id = markerType.getAttribute("id");
				var image = markerType.getAttribute("image");
				var name = markerType.getAttribute("name");
				
				addMarkerType(id, image, name);
				
			}
			// Load markers from here to make sure the types are loaded
			// before..
			loadMarkers();	
		}
	});	
}

/**
 * Load all markers and add them to the map.
 * @param markerTypeId if given, load only markers with this id.
 */
function loadMarkers(markerTypeId) {

	// Download the data in data.xml and load it on the map. The format we expect is:
	// <markers>
	//   <marker lat="37.441" lng="-122.141" title="title" type="typeid" url="url">description</marker>
	//   <marker lat="37.322" lng="-121.213" title="title" type="typeid" url="url">description</marker>
	// </markers>
	
	var url = "listmarkers.xml?sakai.tool.placement.id=" + toolPlacementId;
	if (markerTypeId) {
		url += "&typeid="+markerTypeId;
	}
	url += rnd();
	
	GDownloadUrl(url, function(data, responseCode) {
		if (responseCode == 200) {
			var xml = GXml.parse(data);
			if (isError(xml)) return;

			// is this map editable for this user?
			canCreateMarkers = "true" == xml.documentElement.getAttribute("cancreate");
			
			var markers = xml.documentElement.getElementsByTagName("marker");

			for (var i = 0; i < markers.length; i++) {
				
				var id = markers[i].getAttribute("id");
				var point = new GLatLng(parseFloat(markers[i].getAttribute("lat")),
				                        parseFloat(markers[i].getAttribute("lng")));
				var title = markers[i].getAttribute("title");
				var typeId = markers[i].getAttribute("typeid");
				var url = markers[i].getAttribute("url");
				var canEdit = "true" == markers[i].getAttribute("canedit");
				var canDelete = "true" == markers[i].getAttribute("candelete");
				var description = "";
				if (markers[i].firstChild) {
					var description = markers[i].firstChild.nodeValue;
				}
				var resourceElms = markers[i].getElementsByTagName("resource");
				var resources = new Array();
				for (var j = 0; j < resourceElms.length; j++) {
					resources[j] = resourceElms[j].firstChild.nodeValue;
				}
				
				var marker = createMarker(id, point, title, description, url, typeId, canEdit, canDelete, resources, EMap.map.getZoom());
				addMarker(marker);
			}
		}
		else {
			alert(data);
		}
		if (currentMarkerId) {
			openMarkerEditWindow(allMarkers[currentMarkerId]);
			currentMarkerId = 0;
		}
		else if (currentLatLng) {
			openMarkerEditWindow(null, currentLatLng);
			currentLatLng = null;
		}
	});	
}

/**
 * Create a marker. Change the way a markers looks and behaves here. 
 */
function createMarker(id, point, title, description, url, typeId, canEdit, canDelete, resources, zoomLevel) {
	logMsg("createMarker("+point+", "+title+", "+description+", "+url+", "+typeId+ ","+canEdit+","+canDelete+")");

	var icon = getIconForTypeIdAndZoomLevel(typeId, zoomLevel);
	var markerOptions = {icon: icon, title: title};
	var marker = new GMarker(point, markerOptions);
	
	if (id) {
		marker.id = id;
	}
	marker.title = title;
	marker.description = description;
	marker.url = url;
	marker.resources = resources;
	marker.typeId = typeId;
	marker.canEdit = canEdit;
	marker.canDelete = canDelete;
	
	return marker;
}

function cloneMarker(marker, title, typeId) {
	logMsg("cloneMarker("+marker.title+")");

	var icon = getIconForTypeIdAndZoomLevel(typeId, EMap.map.getZoom());
	var markerOptions = {icon: icon, title: title};
	var myMarker = new GMarker(marker.getPoint(), markerOptions);
	
	return myMarker;
}

/**
 * Add the given marker element to the map.
 */
function addMarker(marker) {
	logMsg("addMarker("+marker.title+")");
	
	// Add a listener for the click event.
	GEvent.addListener(marker, "click", function() {
		openMarkerInfoWindow(marker);
	});
	
	EMap.map.addOverlay(marker, 0);
	allMarkers[marker.id] = marker;
}

function addMarkerType(id, image, name) {
	EMap.registerMarkerType(id, image, name);
}

/**
 * Save the given marker and add it to the map if the save succeeds.
 */
function saveAndAddMarker(marker) {
	logMsg("saveAndAddMarker("+marker.title+")");
	var queryString = "?title=" + escape(marker.title) + 
			"&description=" + escape(marker.description);
	if (marker.resources) {
		queryString += "&resource.count=" + marker.resources.length;
		for (var i = 0; i < marker.resources.length; i++) {
			queryString += "&resource." + i + "=" + escape(marker.resources[i]);
		}
	}
	queryString +=	"&url=" + escape(marker.url) +
			"&typeid=" + marker.typeId +
			"&lat=" + marker.getPoint().lat() +
			"&lng=" + marker.getPoint().lng() +
			"&sakai.tool.placement.id=" + toolPlacementId + rnd();
			
	var url = "createmarker.xml" + queryString;
	GDownloadUrl(url, function(data, responseCode) {
		if (responseCode == 200) {
			var xml = GXml.parse(data);
			if (isError(xml)) return;
			marker.id = xml.documentElement.getAttribute("id");
			// TODO: to get the permissions really right, the 
			// whole marker should be re-read here.
			addMarker(marker);
		}
		else {
			logError("Could not create marker. Server replied " +responseCode);
			alert(data);
		}
	});
}

/**
 * Remove the given marker from the database and if that succeeds,
 * remove marker from the map. 
 */
function removeMarker(marker) {
	logMsg("removeMarker("+marker.title+")");
	
	var url = "deletemarker.xml?id="+marker.id + "&sakai.tool.placement.id=" + toolPlacementId + rnd();
	GDownloadUrl(url, function(data, responseCode) {
		if (responseCode == 200) {
			var xml = GXml.parse(data);
			if (!isError(xml)) {
				EMap.map.removeOverlay(marker);
				allMarkers[marker.id] = null;
			}
		}
		else {
			logError("Could not remove marker. Server replied " +responseCode);
			alert(data);
		}
	});
}

/**
 * Remove all markers from the map.
 */
function removeAllMarkers() {
	EMap.map.clearOverlays();
}

/**
 * Save button in infowindow_edit clicked.
 */
function saveMarker(marker) {
	logMsg("saveMarker");
	
	var id = document.getElementById("cid_input").value;
	
	var title = document.getElementById("ctitle_input").value;
	if (title == "") {
		alert("Enter a title first.");
		document.getElementById("ctitle_input").focus();
		return;
	}
	var description = document.getElementById("cdesc_input").value;
	description = description.replace(/\r\n/g, "<br/>");
	description = description.replace(/\n/g, "<br/>");
	description = description.replace(/\r/g, "<br/>");
	
	var rescount = document.getElementById("crescount_input").value;
	var resources = new Array();
	for (var i = 0; i < rescount; i++) {
		resources[i] = document.getElementById("cres"+i+"_input").value;
	}
	
	var url = document.getElementById("curl_input").value;
	if ((url != "") && !(url.indexOf("http://") == 0 || url.indexOf("https://") == 0 || url.indexOf("ftp://") == 0)) {
		url = "http://" + url
	}
	
	var typeSelect = document.getElementById("ctype_input");
	var typeId = "";
	if (typeSelect) {
		typeId = typeSelect.options[typeSelect.selectedIndex].value;
	}
	var singleType = document.getElementById("singlemarkertype");
	if (typeId == "" && singleType) {
		typeId = singleType.value;
	}
	if (marker) {
		// Can't change the title or the icon of the actual marker, so we'll create a new one.
		if (typeId != marker.typeId || title != marker.title) {
			EMap.map.removeOverlay(marker);
			marker = cloneMarker(marker, title, typeId);
			addMarker(marker);
		}
		marker.id = id;
		marker.title = title;
		marker.description = description;
		marker.resources = resources;
		marker.url = url;
		marker.typeId = typeId;

		var url = "updatemarker.xml?id=" + marker.id +
				"&title=" + escape(marker.title) +
				"&description=" + escape(marker.description);
		if (marker.resources) {
			url += "&resource.count=" + marker.resources.length;
			for (var i = 0; i < marker.resources.length; i++) {
				url += "&resource." + i + "=" + escape(marker.resources[i]);
			}
		}
		url +=	"&url=" + escape(marker.url) +
				"&typeid=" + marker.typeId +
				"&sakai.tool.placement.id=" + toolPlacementId + rnd();

		GDownloadUrl(url, function(data, responseCode) {
			if (responseCode == 200) {
				var xml = GXml.parse(data);
				if (isError(xml)) return;
			}
			else {
				logError("Could not remove marker. Server replied " +responseCode);
				alert(data);
			}
		});
	}
	else {
		logMsg("saveMarker()");
		var newPoint = EMap.map.getInfoWindow().getPoint();
		// TODO:
		// the canDelete and canEdit are set to true, own markers can always
		// be edited / deleted, but this actually should be
		// dictated by the server response.
		var marker = createMarker(null, newPoint, title, description, url, typeId, true, true, resources, EMap.map.getZoom());
		saveAndAddMarker(marker);
	}
	
	
	EMap.map.getInfoWindow().hide();
	// Can't get this to work :-(
	//openMarkerInfoWindow(marker);
}

/**
 * Discard button in infowindow_edit clicked.
 */
function cancelClicked(e) {
	logMsg("cancel click");
	EMap.getInfoWindow().hide();
	EEvent.stopPropagation(e);
}

/**
 * Open an info window showing the details of the given marker.
 */
function openMarkerInfoWindow(marker) {
	logMsg("openMarkerInfoWindow("+marker.title+")");
	
	GDownloadUrl("markerview.spring?id=" + marker.id + rnd(), function(data, responseCode) {
		// would happen if the marker no longer exists (somebody removed it already. 
		if (responseCode != 200) {
			removeMarker(marker);
			EMap.getInfoWindow().hide();
			return;
		}

		// We need to add listeners on the buttons of the info window, but we can't do that untill
		// the window is actually open or we won't find the buttons using document.getELementById
		GEvent.clearListeners(EMap.map, "infowindowopen");
		GEvent.addListener(EMap.map, "infowindowopen", function() {
			var readMoreLink = document.getElementById("readmorelink"+marker.id);
			if (readMoreLink && marker.url && marker.url != "") {
				readMoreLink.style.display = "block";
			}
			
			var editButton = document.getElementById("editbutton"+marker.id);
			if (editButton) {
				EEvent.addListener(editButton, "click", function(e) {
					openMarkerEditWindow(marker);
				});
			} else logError("Can't find the Edit button to add a listener to.");
			
			var deleteButton = document.getElementById("deletebutton"+marker.id);
			if (deleteButton) {
				EEvent.addListener(deleteButton, "click", function(e) {
					removeMarker(marker);
					EMap.getInfoWindow().hide();
					EEvent.stopPropagation(e);
				});
			} else logError("Can't find the Delete button to add a listener to.");
		});
		
		marker.openInfoWindowHtml(data, infoWindowOptions);

	});
	
}

/**
 * Open an info window in which to create a new marker or edit an existing one.
 * @param marker the marker to edit or null.
 * @param point the point at which to create the new marker or null.
 */
function openMarkerEditWindow(marker, latlng) {
	logMsg("openMarkerEditWindow(" + (marker ? marker.title : latlng.lat()+":"+latlng.lng()) + ")");
	var markerId = "";
	if (marker) {
		markerId = marker.id;
	}

	GDownloadUrl("markeredit.spring?id=" + markerId + mapState((marker ? marker.getPoint() : latlng)) + rnd(), function(data, responseCode) {
		// would happen if the marker no longer exists (somebody removed it already. 
		if (responseCode != 200) {
			removeMarker(marker);
			EMap.getInfoWindow().hide();
			EEvent.stopPropagation(e);
			return;
		}
		
		// We need to add listeners on the buttons of the info window, but we can't do that untill
		// the window is actually open or we won't find the buttons using document.getELementById
		GEvent.clearListeners(EMap.map, "infowindowopen");
		GEvent.addListener(EMap.map, "infowindowopen", function() {
			var saveButton = document.getElementById("savebutton"+markerId);
			if (saveButton) {
				EEvent.addListener(saveButton, "click", function(e) {
					saveMarker(marker);
					EEvent.stopPropagation(e);
				});
			}
			else logError("Can't find the Save button to add a listener to.");
			
			var cancelButton = document.getElementById("cancelbutton"+markerId);
			if (cancelButton) {
				EEvent.addListener(cancelButton, "click", cancelClicked);
			}
			else logError("Can't find the Cancel button to add a listener to.");
		});

		var infoWindowHtml = data;
		if (marker) {
			marker.openInfoWindowHtml(infoWindowHtml, infoWindowOptions);
		}
		else {
			if (!canCreateMarkers) return;
			EMap.map.openInfoWindowHtml(latlng, infoWindowHtml, infoWindowOptions);
		}
	
	});
	
}

function mapState(latlng) {
	var center = EMap.map.getCenter();
	var zoom = EMap.map.getZoom();

	var type = null;
	if (EMap.map.getCurrentMapType() == G_SATELLITE_MAP) {
		type = "G_SATELLITE_MAP";
	}
	else if (EMap.map.getCurrentMapType() == G_HYBRID_MAP) {
		type = "G_HYBRID_MAP";
	}
	else if (EMap.map.getCurrentMapType() == G_PHYSICAL_MAP) {
		type = "G_PHYSICAL_MAP";
	}
	else {
		type = "G_NORMAL_MAP";
	}
	
	return "&centerLat="+center.lat()+"&centerLng="+center.lng()+"&zoom="+zoom+"&type="+type+"&markerLat="+latlng.lat()+"&markerLng="+latlng.lng();
}

/**
 * Check if the given xml document is an error and is it is, alert it's message.
 */
function isError(xml) {
	if (xml.documentElement.nodeName == "error") {
		alert(xml.documentElement.getAttribute("type")+":\n"+xml.documentElement.getAttribute("message"));
		return true;
	}
	return false;
}

function getIconForTypeIdAndZoomLevel(typeId, zoomLevel) {
	var icon = EMap.getIcon(typeId, zoomLevel);
	return icon;
}

function loadMap(lat, lng, zoom, type) {
	EMap.map.setCenter(new GLatLng(lat, lng), zoom);
	if (type) {
		EMap.map.setMapType(type);
	}
}

function filterMarkers(markerTypeId) {
	currentLatLng = null;
	currentMarkerId = 0;
	removeAllMarkers();
	loadMarkers(markerTypeId);
}

/*
 * Generate a random get attribute, including the & at the beginning.
 */
function rnd() {
	// very old-school way of disabling the browsers cache
	return "&rnd="+Math.random();
}

/**
 * Log the given message in green.
 */
function logMsg(message) {
	log(message, "green");
}

/**
 * Log the given message in red.
 */
function logError(message) {
	log(message, "red");
}

/**
 * Log the given message in the given color.
 */
function log(message, color) {
	if (logEnabled) {
		GLog.write(message, color);
	}
}

/**
 * This is called from inside the edit window.
 */
function submitToFilePickerProxy() {
	var form = document.getElementById('poiform');
	form.action='filepickerproxy.spring';
	form.method='post';
	form.submit();
}
