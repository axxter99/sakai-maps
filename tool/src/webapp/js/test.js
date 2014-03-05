function openWindow(sURL, sName, sFeatures, bReplace) {
	var newWindow = window.open(sURL, sName, sFeatures, bReplace);
	var newDocument = newWindow.document;
	
	addListener(newWindow, 'load', function(e) {
		var table = document.getElementsByTagName("table")[0];
		var rows = table.rows;
		var firstRow = rows[0];
		table.removeChild(firstRow);
	});
}

function addListener(element, type, listener) {
	
	if (element.addEventListener)	{ // Standard
		element.addEventListener(type, listener, false);
	}
	else if (element.attachEvent) { // IE
		element.attachEvent('on' + type, listener);
	} 
	else {
		element['on'+type] = listener;
	}
}