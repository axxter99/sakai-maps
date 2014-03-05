var EMarkerSelector = {
	
	icons: new Array(),
	
	init: function() {
		// add a listener to each of the selectors
		var selectors = document.getElementsByTagName("select");
		for(var i = 0; i < selectors.length; i++) {
			var selector = selectors[i];
			if (selector.className.indexOf("markerselector") != -1) {
				EEvent.addListener(selector, "change", EMarkerSelector.changeSelectorBackground);
			}
		}
		
		// Add listeners to the action buttons.
		EMarkerSelector.doButtons();
		
	},
	
	changeSelectorBackground: function(e) {
		var selector = EEvent.getEventSource(e);
		selector.style.backgroundColor = EMarkerSelector.icons[selector.selectedIndex].color;
		selector.blur();
		
		var td = selector.parentNode;
		while(td != null && td.nodeName.toLowerCase() != "td") {
			td = td.parentNode;
		}
		td.style.backgroundImage = "url(icons/"+EMarkerSelector.icons[selector.selectedIndex].image+")";
	},
	
	upClicked: function(e) {
		var tr = EMarkerSelector.getSourceRow(e);
		var other = tr.previousSibling;
		while(other.nodeType != 1 && other.nodeName.toLowerCase() != "tr") {
			other = other.previousSibling;
		}
 		tr.parentNode.insertBefore(tr, other);
 		
 		EMarkerSelector.doButtons();
	},
	
	downClicked: function(e) {
		var tr = EMarkerSelector.getSourceRow(e);
		var other = tr.nextSibling;
		while(other.nodeType != 1 && other.nodeName.toLowerCase() != "tr") {
			other = other.nextSibling;
		}
 		tr.parentNode.insertBefore(other, tr);
 		
 		EMarkerSelector.doButtons();
	},
	
	addClicked: function(e) {
		var tr = EMarkerSelector.getSourceRow(e);
		
		// First find out if a type name has been given.
		var nameInput = tr.getElementsByTagName("input")[1];
		if (!nameInput.value) {
			nameInput.focus();
			alert("Enter a marker type name first.");
			return;
		}
		var newTr = tr.cloneNode(true);
		tr.parentNode.appendChild(newTr);
		
		// erase any values copied from the cloned node.
		var inputs = newTr.getElementsByTagName("input");
		inputs[inputs.length] = newTr.getElementsByTagName("select")[0];
		for (var i = 0; i < inputs.length; i ++) {
			var input = inputs[i];
			if (input.className.indexOf("markerid") != -1) {
				input.value = "";
				// Get the span in this td
				var spans = input.parentNode.getElementsByTagName("span");
				if (spans.length > 0) {
					spans[0].title = "No id: not yet stored.";
					spans[0].innerHtml = "-";
				}
			}
			else if (input.className.indexOf("markername") != -1) {
				input.value = "";
			}
			else if (input.className.indexOf("markerselector") != -1) {
				input.selectedIndex = 0;
			}
		}

		// change the add button for a delete button on the last row that was clicked
		var tds = tr.getElementsByTagName("td");
		var td = tds[tds.length-1];
		var img = td.firstChild;

		var newTds = newTr.getElementsByTagName("td");
		var newTd = newTds[tds.length-1];
		var newImg = newTd.firstChild;
		
		newTd.appendChild(img);
		td.appendChild(newImg);
		
		newImg.className = "removebutton";
		newImg.src = "gfx/user-trash.png";
		newImg.title = "remove this marker type";

 		EMarkerSelector.doButtons();
	},
	
	removeClicked: function(e) {
		var tr = EMarkerSelector.getSourceRow(e);
		tr.parentNode.removeChild(tr);
 		
 		EMarkerSelector.doButtons();
	},
	
	getSourceRow: function(e) {
		var tr = EEvent.getEventSource(e);
		while(tr != null && tr.nodeName.toLowerCase() != "tr") {
			tr = tr.parentNode;
		}
		return tr;
	},
	
	/**
	 * Arrange the listsners and the proper images for the action buttons.
	 */
	doButtons: function() {
		var table = document.getElementById("markertypetable");
		var rows = table.tBodies[0].rows;
		
		for (var i = 0; i < rows.length; i++) {
			
			var buttons =  rows[i].getElementsByTagName("img");
			for (var j = 0; j < buttons.length; j++) {
				// do buttons and their listeners.
				if (buttons[j].className.indexOf("upbutton") != -1) {
					EEvent.removeListener(buttons[j], "click", EMarkerSelector.upClicked);
					if (i > 0 && i < rows.length -1) { // not on the first and last rows
						EEvent.addListener(buttons[j], "click", EMarkerSelector.upClicked);
						buttons[j].src = "gfx/go-up.png"
					}
					else {
						buttons[j].src = "gfx/go-up-grayedout.png"
					}
				}
				else if  (buttons[j].className.indexOf("downbutton") != -1) {
					EEvent.removeListener(buttons[j], "click", EMarkerSelector.downClicked);
					if (i < rows.length -2) { // not on the last row
						EEvent.addListener(buttons[j], "click", EMarkerSelector.downClicked);
						buttons[j].src = "gfx/go-down.png"
					}
					else {
						buttons[j].src = "gfx/go-down-grayedout.png"
					}
				}
				else if  (buttons[j].className.indexOf("addbutton") != -1) {
					EEvent.removeListener(buttons[j], "click", EMarkerSelector.addClicked);
					if (i == rows.length-1) { // only on the last row
						EEvent.addListener(buttons[j], "click", EMarkerSelector.addClicked);
					}
				}
				else if  (buttons[j].className.indexOf("removebutton") != -1) {
					EEvent.removeListener(buttons[j], "click", EMarkerSelector.removeClicked);
					if (i < rows.length-1) {
						EEvent.addListener(buttons[j], "click", EMarkerSelector.removeClicked);
					}
				}
				// else this is not a button
			}
			
			// Do the indices and register the listeners for the color dropdowns.
			var tds = rows[i].getElementsByTagName("td");
			for (var j = 0; j < tds.length; j++) {
				if (j == 0) {
					//var span =  tds[j].getElementsByTagName("span")[0];
					//span.innerHTML = i;
					var input = tds[j].getElementsByTagName("input")[0];
					input.name = "markertypes["+i+"].id";
					input.readonly = true;
				}
				else if (j == 1) {
					var input = tds[j].getElementsByTagName("input")[0];
					input.name = "markertypes["+i+"].name";
				}
				else if (j == 2){
					var select = tds[j].getElementsByTagName("select")[0];
					select.name = "markertypes["+i+"].image";
					EEvent.removeListener(select, "change", EMarkerSelector.changeSelectorBackground);
					EEvent.addListener(select, "change", EMarkerSelector.changeSelectorBackground);
					
					tds[j].style.backgroundImage = "url(icons/"+EMarkerSelector.icons[select.selectedIndex].image+")";
					select.style.backgroundColor = EMarkerSelector.icons[select.selectedIndex].color;
					select.blur();
				}
			}
		}
	}
	
};
EEvent.addListener(window, "load", EMarkerSelector.init);