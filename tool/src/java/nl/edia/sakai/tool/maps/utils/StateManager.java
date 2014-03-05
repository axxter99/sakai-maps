/*
 * Copyright (c) 2008 Edia (www.edia.nl)
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
package nl.edia.sakai.tool.maps.utils;

import nl.edia.sakai.tool.util.SakaiUtils;

import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.tool.api.ToolSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Class StateManager is a single entry point to the tool session state of the maps
 * application. It's purpose is to keep all tool session state management in a single
 * place to minimize the risk of typos and session pollution.
 * 
 * @author Maarten van Hoof
 * 
 */
public class StateManager {
	
	public static final String STATE_NORMAL = "normal";
	public static final String STATE_EDIT_RESOURCES = "editresources";
	public static final String STATE_EDIT_MARKER = "editmarker";
	
	public static final String SESSION_ATTR_STATE = "nl.edia.sakai.maps.state";
	/** Current marker id. */
	public static final String SESSION_ATTR_ID = "nl.edia.sakai.maps.curmarker.id";
	/** Current marker title. */
	public static final String SESSION_ATTR_TITLE = "nl.edia.sakai.maps.curmarker.title";
	/** Current marker description. */
	public static final String SESSION_ATTR_DESCRIPTION = "nl.edia.sakai.maps.curmarker.descr";
	/** Current marker url. */
	public static final String SESSION_ATTR_URL = "nl.edia.sakai.maps.curmarker.url";
	/** Current marker type. */
	public static final String SESSION_ATTR_TYPE = "nl.edia.sakai.maps.curmarker.type";
	/** Current marker resources. */
	public static final String SESSION_ATTR_RESOURCES = "nl.edia.sakai.maps.curmarker.resources";
	
	/** lat of center of current map. */
	public static final String SESSION_ATTR_MAPSTATE_CENTERLAT = "nl.edia.sakai.maps.mapstate.centerlat";
	/** lng of center of current map. */
	public static final String SESSION_ATTR_MAPSTATE_CENTERLNG = "nl.edia.sakai.maps.mapstate.centerlng";
	/** Current map zoom level. */
	public static final String SESSION_ATTR_MAPSTATE_ZOOM = "nl.edia.sakai.maps.mapstate.zoom";
	/** Current map type. */
	public static final String SESSION_ATTR_MAPSTATE_TYPE = "nl.edia.sakai.maps.mapstate.type";
	/** x coordinate of the current marker on the map. */
	public static final String SESSION_ATTR_MAPSTATE_MARKERLAT = "nl.edia.sakai.maps.mapstate.markerlat";
	/** y coordinate of the current marker on the map. */
	public static final String SESSION_ATTR_MAPSTATE_MARKERLNG = "nl.edia.sakai.maps.mapstate.markerlng";
	
	private EntityManager entityManager;
	
	public StateManager() {
	}
	
	public String getCurrentState() {
		ToolSession toolSession = SakaiUtils.getToolSession();
		String state = (String)toolSession.getAttribute(SESSION_ATTR_STATE);
		return (state != null ? state : STATE_NORMAL);
	}
	
	public void toEditResourcesState(HttpServletRequest request) {
		ToolSession toolSession = SakaiUtils.getToolSession();
		
		// Here's what we'll need to reproduce when we get back from the file picker
		toolSession.setAttribute(SESSION_ATTR_ID, request.getParameter("marker.id"));
		toolSession.setAttribute(SESSION_ATTR_TITLE, request.getParameter("marker.title"));
		toolSession.setAttribute(SESSION_ATTR_DESCRIPTION, request.getParameter("marker.description"));
		toolSession.setAttribute(SESSION_ATTR_URL, request.getParameter("marker.url"));
		toolSession.setAttribute(SESSION_ATTR_TYPE, request.getParameter("marker.type"));
		
		// This is what we'll feed the attachment process with.
		int resourceCount = Integer.parseInt(request.getParameter("marker.resources.length"));
		List<Reference> references = new ArrayList<Reference>(resourceCount);
		for (int i = 0; i < resourceCount; i++) {
			String resourceId = request.getParameter("marker.resource."+i);
			Reference reference = entityManager.newReference(resourceId);
			references.add(reference);
		}
		
		// Fill the tool session with data for the file picker.
		toolSession.setAttribute(FilePickerHelper.FILE_PICKER_ATTACH_LINKS, "true");
		toolSession.setAttribute(FilePickerHelper.FILE_PICKER_MAX_ATTACHMENTS, FilePickerHelper.CARDINALITY_MULTIPLE);
		toolSession.setAttribute(FilePickerHelper.FILE_PICKER_SHOW_WORKSPACE, "true");
		//toolSession.setAttribute(FilePickerHelper.FILE_PICKER_TITLE_TEXT, "a.code");
		//toolSession.setAttribute(FilePickerHelper.FILE_PICKER_INSTRUCTION_TEXT, "another.code");
		//toolSession.setAttribute(FilePickerHelper.FILE_PICKER_RESOURCE_FILTER, null); // can we filter on anything?
		toolSession.setAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS, references);
		
		SakaiUtils.startHelper(request, "sakai.filepicker", null);
		
		// update state
		toolSession.setAttribute(SESSION_ATTR_STATE, STATE_EDIT_RESOURCES);
	}
	
	public void toEditMarkerState() {
		ToolSession toolSession = SakaiUtils.getToolSession();
		
		// Get the resources that the user picked and put them back in the session as a list of strings.
		@SuppressWarnings("unchecked")
		List<Reference> references = (List<Reference>)toolSession.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
		List<String> resources = new ArrayList<String>(references.size());
		for (Reference reference : references) {
			resources.add(reference.getReference());
		}
		toolSession.setAttribute(SESSION_ATTR_RESOURCES, resources);
		
		// now clean up the session state for the file picker
		toolSession.removeAttribute(FilePickerHelper.FILE_PICKER_ATTACH_LINKS);
		toolSession.removeAttribute(FilePickerHelper.FILE_PICKER_MAX_ATTACHMENTS);
		toolSession.removeAttribute(FilePickerHelper.FILE_PICKER_SHOW_WORKSPACE);
		toolSession.removeAttribute(FilePickerHelper.FILE_PICKER_TITLE_TEXT);
		toolSession.removeAttribute(FilePickerHelper.FILE_PICKER_INSTRUCTION_TEXT);
		toolSession.removeAttribute(FilePickerHelper.FILE_PICKER_RESOURCE_FILTER);
		toolSession.removeAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);

		// update state
		toolSession.setAttribute(SESSION_ATTR_STATE, STATE_EDIT_MARKER);
	}
	
	public void toNormalState() {
		ToolSession toolSession = SakaiUtils.getToolSession();
		
		// clean up the current marker state.
		toolSession.removeAttribute(SESSION_ATTR_ID);
		toolSession.removeAttribute(SESSION_ATTR_TITLE);
		toolSession.removeAttribute(SESSION_ATTR_DESCRIPTION);
		toolSession.removeAttribute(SESSION_ATTR_URL);
		toolSession.removeAttribute(SESSION_ATTR_TYPE);
		toolSession.removeAttribute(SESSION_ATTR_RESOURCES);
		
		// update state.
		toolSession.removeAttribute(SESSION_ATTR_STATE);
	}
	
	/**
	 * Store the map state that the client put in the request.
	 * The map state is needed only after a user has used the file picker
	 * to restore the map in the exact state that it was in when the user
	 * clicked 'edit files'.
	 * Note that this state is stored every time the user edits a marker, but only
	 * used when the user changes the files associated with the marker.
	 * @param request
	 */
	public void saveMapState(HttpServletRequest request) {
		ToolSession toolSession = SakaiUtils.getToolSession();
		toolSession.setAttribute(SESSION_ATTR_MAPSTATE_CENTERLAT, request.getParameter("centerLat"));
		toolSession.setAttribute(SESSION_ATTR_MAPSTATE_CENTERLNG, request.getParameter("centerLng"));
		toolSession.setAttribute(SESSION_ATTR_MAPSTATE_ZOOM, request.getParameter("zoom"));
		toolSession.setAttribute(SESSION_ATTR_MAPSTATE_TYPE, request.getParameter("type"));
		toolSession.setAttribute(SESSION_ATTR_MAPSTATE_MARKERLAT, request.getParameter("markerLat"));
		toolSession.setAttribute(SESSION_ATTR_MAPSTATE_MARKERLNG, request.getParameter("markerLng"));
	}
	
	/**
	 * Retrieve the map state last stored.
	 * @param model
	 */
	public void retrieveMapState(Map<String, Object> model) {
		ToolSession toolSession = SakaiUtils.getToolSession();
		model.put("centerLat", toolSession.getAttribute(SESSION_ATTR_MAPSTATE_CENTERLAT));
		model.put("centerLng", toolSession.getAttribute(SESSION_ATTR_MAPSTATE_CENTERLNG));
		model.put("zoom", toolSession.getAttribute(SESSION_ATTR_MAPSTATE_ZOOM));
		model.put("type", toolSession.getAttribute(SESSION_ATTR_MAPSTATE_TYPE));
		model.put("markerId", toolSession.getAttribute(SESSION_ATTR_ID));
		model.put("markerLat", toolSession.getAttribute(SESSION_ATTR_MAPSTATE_MARKERLAT));
		model.put("markerLng", toolSession.getAttribute(SESSION_ATTR_MAPSTATE_MARKERLNG));
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	

}
