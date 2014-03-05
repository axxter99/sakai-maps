/**
 * This singleton contains usefull event processing methods 
 * for cross-browser interoperability.
 *
 * @author: Maarten van Hoof
 * @copy: Edia 2007
 */

var EEvent = {
	
	/**
	 * Add an event listener to an element in cross-browser fashion.
	 */
	addListener : function(element, type, listener) {
	
		if (element.addEventListener)	{ // Standard
			element.addEventListener(type, listener, false);
		}
		else if (element.attachEvent) { // IE
			element.attachEvent('on' + type, listener);
		} 
		else {
			element['on'+type] = listener;
		}
	},
	
	removeListener : function(element, type, listener) {
	
		if (element.removeEventListener)	{ // Standard
			element.removeEventListener(type, listener, false);
		}
		else if (element.detachEvent) { // IE
			element.detachEvent('on' + type, listener);
		} 
		else {
			element['on'+type] = null;
		}
	},
	
	/**
	 * Return the event object in cross-browser fashion.
	 *
	 * Returns window.event if that exists, 
	 * returns e itself on other browsers.
	 */
	getEvent : function(e) {
		if (window.event) { // IE
			return window.event;
		}
		else {
			return e;
		}
	},
	
	getEventSource: function(e) {
		e = EEvent.getEvent(e);
		var eventSource;
		if (e.target) {
			eventSource = e.target;
		}
		else if (e.srcElement) {
			eventSource = e.srcElement;
		}
		
		if (eventSource.nodeType == 3) {// defeat Safari bug
			eventSource = targ.parentNode;
		}
		
		return eventSource;
	},	
	
	/**
	 * Prevent the default action of the given event.
	 * 
	 * There's no need to call getEvent(e) first.
	 */
	preventDefault : function(e) {
		if (window.event) { // IE
			window.event.returnValue = false;
		}
		else if (e.preventDefault) { // mozilla
			e.preventDefault();
		}
	},
	
	/**
	 * Stop the propagation of the event up the DOM tree
	 * in cross-browser fashion.
	 */
	stopPropagation : function(e) {
		if (window.event) {
			window.event.cancelBubble;
		}
		else if (e.stopPropagation) {
			e.stopPropagation();
		}
	}

}