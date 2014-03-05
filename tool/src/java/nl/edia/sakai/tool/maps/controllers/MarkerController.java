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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.edia.sakai.tool.maps.MapsProtocolException;
import nl.edia.sakai.tool.maps.MarkerService;
import nl.edia.sakai.tool.maps.model.MarkerType;

import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Base class for the controllers in this package.
 * 
 * 
 * @author Maarten van Hoof
 * 
 */
public abstract class MarkerController implements Controller, Protocol {

	protected MarkerService markerService;
	
	/**
	 * The string representation of the view, needs to be 
	 * injected by spring
	 */
	private String view;
	
	/**
	 * The error veiw, defaults to "error"
	 */
	private String errorView = "errorXml";

	public MarkerController() {
	}
	
	/**
	 * @see Controller#handleRequest(HttpServletRequest, HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			return new ModelAndView(getView(), processRequest(request));
		} 
		catch (Exception e) {
			Map<String, Object> myModel = new HashMap<String, Object>();
			myModel.put("exception",  e);
			return new ModelAndView(getErrorView(), myModel);
		}
	}

	/**
	 * Process the request and return an model (Map) that is the response.
	 * The XSLT view will be responsible for rendering the right output.
	 * @param request the request. 
	 * @return a Map containing the model.
	 */
	public abstract Map<String, Object> processRequest(HttpServletRequest request) throws MapsProtocolException, PermissionException, IdUnusedException;

	/**
	 * Get a required parameter from the request or throw a protocol exception.
	 * 
	 * @param request
	 *            the request
	 * @param parameter
	 *            the parameter name
	 * @return the value of the parameter.
	 * @throws MapsProtocolException
	 *             if the value is not specified or empty.
	 */
	protected String getRequiredParameter(HttpServletRequest request, String parameter) throws MapsProtocolException {
		if (request.getParameter(parameter) == null)
			throw new MapsProtocolException("Required attribute missing", "Attribute " + parameter + " is missing");
		if (request.getParameter(parameter).length() == 0)
			throw new MapsProtocolException("Invalid attribute value", "Attribute " + parameter + " can not be empty.");

		return request.getParameter(parameter);
	}

	/**
	 * Get a required int parameter from the request or throw a protocol
	 * exception.
	 * 
	 * @param request
	 *            the request.
	 * @param parameter
	 *            the parameter name.
	 * @return the value of the parameter.
	 * @throws MapsProtocolException
	 *             if the value is not specified, empty or not an integer.
	 */
	protected int getRequiredIntParameter(HttpServletRequest request, String attribute) throws MapsProtocolException {
		String value = getRequiredParameter(request, attribute);
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException nfe) {
			throw new MapsProtocolException("Invalid attribute value", "Attribute " + attribute + " must be an integer.");
		}
	}

	/**
	 * Get a required int parameter from the request or throw a protocol
	 * exception.
	 * 
	 * @param request
	 *            the request.
	 * @param parameter
	 *            the parameter name.
	 * @return the value of the parameter.
	 * @throws MapsProtocolException
	 *             if the value is not specified, empty or not an integer.
	 */
	protected Long getLongParameter(HttpServletRequest request,	String attribute) throws MapsProtocolException {
		String value = request.getParameter(attribute);
		try {
			return new Long(value);
		}
		catch (NumberFormatException nfe) {
			return null;
		}
	}

	/**
	 * Get a required float parameter from the request or throw a protocol
	 * exception.
	 * 
	 * @param request
	 *            the request.
	 * @param parameter
	 *            the parameter name.
	 * @return the value of the parameter.
	 * @throws MapsProtocolException
	 *             if the value is not specified, empty or not a float.
	 */
	protected float getRequiredFloatParameter(HttpServletRequest request, String attribute) throws MapsProtocolException {
		String value = getRequiredParameter(request, attribute);
		try {
			return Float.parseFloat(value);
		}
		catch (NumberFormatException nfe) {
			throw new MapsProtocolException("Invalid attribute value", "Attribute " + attribute + " must be a float.");
		}
	}

	/**
	 * Get a required float parameter from the request or throw a protocol
	 * exception.
	 * 
	 * @param request
	 *            the request.
	 * @param parameter
	 *            the parameter name.
	 * @return the value of the parameter.
	 * @throws MapsProtocolException
	 *             if the value is not specified, empty or not a float.
	 */
	protected Long getIdParameter(HttpServletRequest request) throws MapsProtocolException {
		String value = getRequiredParameter(request, ID_ATTR);
		try {
			return new Long(value);
		}
		catch (NumberFormatException nfe) {
			throw new MapsProtocolException("Invalid attribute value", "Attribute " + ID_ATTR + " must be a Long.");
		}
	}

	protected String getUrlParameter(HttpServletRequest request, String attribute) throws MapsProtocolException {
		String value = request.getParameter(attribute);
		if (value != null && value.length() != 0) {
			try {
				new URL(value);
			}
			catch (MalformedURLException mue) {
				throw new MapsProtocolException("Invalid attribute value", "Attribute " + attribute + " must be a url.");
			}
		}
		return value;
	}
	
	protected List<String> getResourcesParameter(HttpServletRequest request) {
		int rescount = 0;
		try {
			rescount = getRequiredIntParameter(request, RESOURCECOUNT_ATTR);
		}
		catch (Exception ignore){}
		
		List<String> resources = new ArrayList<String>();
		for (int i = 0; i < rescount; i++) {
			resources.add(request.getParameter(RESOURCE_ATTR+i));
		}
		
		return resources;
	}

	/**
	 * Convenience method, creates a map with only a single key/value 
	 * pair.
	 * @param key, the key 
	 * @param value, the value
	 * @return a map with a the given key/value pair
	 */
	protected Map<String, Object> createModel(String key, Object value) {
		HashMap<String, Object> myModel = new HashMap<String, Object>();
		myModel.put(key, value);	
		return myModel;
	}

	/**
	 * @return the view
	 */
	public String getView() {
		return view;
	}

	/**
	 * @param view
	 *            the view to set
	 */
	public void setView(String view) {
		this.view = view;
	}

	public MarkerService getMarkerService() {
		return markerService;
	}

	public void setMarkerService(MarkerService markerService) {
		this.markerService = markerService;
	}

	public String getErrorView() {
		return errorView;
	}

	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

	/**
	 * Reads the marker type from the database
	 * @param typeId
	 * @return
	 */
	protected MarkerType readMarkerType(Long typeId) {
		MarkerType myMarkerType = null;
		if (typeId != null) {
			myMarkerType = markerService.readMarkerType(typeId);
		} 
		return myMarkerType;
	}

}
