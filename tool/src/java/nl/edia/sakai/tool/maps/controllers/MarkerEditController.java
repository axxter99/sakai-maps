/*
 * This Educational Community License (the "License") applies
 * to any original work of authorship (the "Original Work") whose owner
 * (the "Licensor") has placed the following notice immediately following
 * the copyright notice for the Original Work:
 * 
 * Copyright (c) 2007 Edia (www.edia.nl)
 * 
 * Licensed under the Educational Community License version 1.0
 * 
 * This Original Work, including software, source code, documents,
 * or other related items, is being provided by the copyright holder(s)
 * subject to the terms of the Educational Community License. By
 * obtaining, using and/or copying this Original Work, you agree that you
 * have read, understand, and will comply with the following terms and
 * conditions of the Educational Community License:
 * 
 * Permission to use, copy, modify, merge, publish, distribute, and
 * sublicense this Original Work and its documentation, with or without
 * modification, for any purpose, and without fee or royalty to the
 * copyright holder(s) is hereby granted, provided that you include the
 * following on ALL copies of the Original Work or portions thereof,
 * including modifications or derivatives, that you make:
 * 
 * - The full text of the Educational Community License in a location viewable to
 * users of the redistributed or derivative work.
 * 
 * - Any pre-existing intellectual property disclaimers, notices, or terms and
 * conditions.
 * 
 * - Notice of any changes or modifications to the Original Work, including the
 * date the changes were made.
 * 
 * 
 * Any modifications of the Original Work must be distributed in such a manner as
 * to avoid any confusion with the Original Work of the copyright holders.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * The name and trademarks of copyright holder(s) may NOT be used
 * in advertising or publicity pertaining to the Original or Derivative
 * Works without specific, written prior permission. Title to copyright in
 * the Original Work and any associated documentation will at all times
 * remain with the copyright holders. 
 * 
 */
package nl.edia.sakai.tool.maps.controllers;

import nl.edia.sakai.tool.maps.model.Marker;
import nl.edia.sakai.tool.maps.model.MarkerType;
import nl.edia.sakai.tool.maps.utils.StateManager;
import nl.edia.sakai.tool.maps.value.ResourceValue;
import nl.edia.sakai.tool.util.SakaiUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.api.ToolSession;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MarkerEditController extends MarkerWindowController {
	
	private StateManager stateManager;
	private Log log = LogFactory.getLog(MarkerEditController.class);
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    Map<String, Object> myModel = new HashMap<String, Object>();
	    
	    if (stateManager.getCurrentState().equals(StateManager.STATE_EDIT_MARKER)) {
	    	// This is not a normal request, but a user that has just finished attaching resources
	    	// to a marker. The client knows this and is asking us for the form state as it was when
	    	// the user clicked 'edit files'. The FilePickerProxyController has stored that in the session.
	    	ToolSession session = SakaiUtils.getToolSession();
	    	Marker marker = new Marker();

	    	String id = (String)session.getAttribute(StateManager.SESSION_ATTR_ID);
	    	if (id != null && id.length() != 0) {
		    	log.debug("Show marker edit window for marker " + id + " after file picker activity.");
	    		marker.setId(Long.valueOf(id));
	    	}
	    	else {
	    		log.debug("Show marker edit window for new marker after file picker activity.");
	    	}
	    	
	    	String type = (String)session.getAttribute(StateManager.SESSION_ATTR_TYPE);
	    	if (type != null && type.length() != 0) {
	    		marker.setType(markerService.readMarkerType(Long.valueOf(type)));
	    	}
	    	
	    	marker.setTitle((String)session.getAttribute(StateManager.SESSION_ATTR_TITLE));
	    	marker.setDescription((String)session.getAttribute(StateManager.SESSION_ATTR_DESCRIPTION));
	    	marker.setUrl((String)session.getAttribute(StateManager.SESSION_ATTR_URL));
	    	
	    	@SuppressWarnings("unchecked")
	    	List<String> resourceIds = (List<String>) session.getAttribute(StateManager.SESSION_ATTR_RESOURCES);
	    	List<ResourceValue> resources = getResourceValues(resourceIds);
	    	
	    	myModel.put("resources", resources);
	    	myModel.put(getCommandName(), marker);
	    	
	    	// return to normal state.
	    	stateManager.toNormalState();
	    }
	    else {
	    	// this is a normal request
	    	
	    	// Save the current map state to the session. We'll have to reproduce this if the 
	    	// user leaves the edit window to go to the file picker.
	    	stateManager.saveMapState(request);
	    	
		    Marker myMarker = null;
		    String myIdString = request.getParameter("id");
		    if (myIdString != null && myIdString.length() != 0) {
		    	log.debug("Show marker edit window for marker " + myIdString + ".");
		    	Long myId = new Long(myIdString);
		    	myMarker = markerService.readMarker(myId);
		    	
		    	// marker description may have html <br/> elements in it,
		    	// get these out for the edit. MvH
		    	String myDescription = myMarker.getDescription();
		    	if (myDescription != null) {
		    		myDescription = myDescription.replaceAll("\\<br/\\>", "\n");
		    	}
		    	myMarker.setDescription(myDescription);
		    }
		    // Create new, if none available
		    if (myMarker == null) {
		    	log.debug("Show marker edit window for new marker.");
		    	myMarker = new Marker();
		    }
		    
		    myModel.put(getCommandName(), myMarker);
		    
		    if (myMarker.getResources() != null) {
		    	List<ResourceValue> resources = getResourceValues(myMarker.getResources());
		    	myModel.put("resources", resources);
		    }
	    }

	    myModel.put("markerTypes", markerService.listMarkerTypes());
    	myModel.put("defaultMarkerType", getDefaultType());
	    
	    return new ModelAndView(view, myModel);
    }

	protected MarkerType getDefaultType() {
		List<MarkerType> myMarkerTypes = markerService.listMarkerTypes();
		if (myMarkerTypes.size() == 1) {
			return myMarkerTypes.get(0);
		}
		return null;
	}

	/**
	 * @return the stateManager
	 */
	public StateManager getStateManager() {
		return stateManager;
	}

	/**
	 * @param stateManager the stateManager to set
	 */
	public void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}
	
}
