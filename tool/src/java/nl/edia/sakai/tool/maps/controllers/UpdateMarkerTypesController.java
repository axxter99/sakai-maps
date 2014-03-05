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

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.edia.sakai.tool.maps.MarkerService;
import nl.edia.sakai.tool.maps.model.MarkerType;
import nl.edia.sakai.tool.maps.utils.EmptyListCreator;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.BaseCommandController;

public class UpdateMarkerTypesController extends BaseCommandController {

	private static final Pattern PATTERN_REQUEST_LIST_PARAM = Pattern.compile("markertypes\\[(\\d+)\\]\\.name");

	/**
	 * The injected marker service
	 */
	private MarkerService markerService;

	/**
	 * The error formView, defaults to "error"
	 */
	private String errorView = "error";

	/**
	 * The string representation of the formView, needs to be injected by spring
	 */
	private String formView;

	private String successView;

	/**
	 * Gets a list of empty instantiated elements
	 * 
	 * @param request
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	protected List<MarkerType> getEmptyMarkerTypeList(HttpServletRequest request) throws InstantiationException, IllegalAccessException {
		int myRequestListSize = getRequestListSize(request, PATTERN_REQUEST_LIST_PARAM);
		return new EmptyListCreator<MarkerType>().getEmptyList(myRequestListSize, MarkerType.class);
	}

	/**
	 * Gets the list size from the request, it ignores any missing elements in the
	 * list, it just gets the highest value.
	 * <ul>
	 * <li><code>xxx?index[0].id&index[1].id<code> will return 2</li>
	 * 	<li><code>xxx?index[0].id&index[10].id<code> will return 11</li>
	 * </ul>
	 *
	 */
	protected int getRequestListSize(HttpServletRequest request, Pattern pattern) {
		int myMaxIndex = 0;
		@SuppressWarnings("unchecked")
		Set<String> myKeySet = request.getParameterMap().keySet();
		Iterator<String> iter = myKeySet.iterator();
		boolean isEmpty = true;
		while (iter.hasNext()) {
			String myParamName = (String) iter.next();
			Matcher myMatcher = pattern.matcher(myParamName);
			if (myMatcher.matches()) {
				int myIndex = new Integer(myMatcher.group(1)).intValue();
				myMaxIndex = Math.max(myMaxIndex, myIndex);
				isEmpty = false;
			}
		}

		if (isEmpty) {
			return 0;
		}
		return myMaxIndex + 1;
	}

	/**
	 * @param request
	 * @return protected List<MarkerType>
	 *         prepareMarkerTypeList(HttpServletRequest request) { int
	 *         myRequestListSize = getRequestListSize(request,
	 *         PATTERN_REQUEST_LIST_PARAM);
	 * 
	 * List<MarkerType> myListMarkerTypes = markerService.listMarkerTypes(); int
	 * myDiff = myRequestListSize - myListMarkerTypes.size(); if (myDiff < 0) {
	 * for (int i = 0; i < -myDiff; i++) {
	 * myListMarkerTypes.remove(myListMarkerTypes.size() -1); } } else if (myDiff >
	 * 0) { for (int i = 0; i < myDiff; i++) { myListMarkerTypes.add(new
	 * MarkerType()); } }
	 * 
	 * return myListMarkerTypes; }
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (isFormSubmit(request)) {

			MarkerTypesDTO myMarkerTypesDTO = new MarkerTypesDTO(getEmptyMarkerTypeList(request));
			ServletRequestDataBinder binder = bindAndValidate(request, myMarkerTypesDTO);

			if (binder.getBindingResult().hasErrors()) {
				Map<String, Object> myModel = new HashMap<String, Object>();
				myModel.put("exception", new BindException(binder.getBindingResult()));
				return new ModelAndView(getErrorView(), myModel);
			}
			else {
				List<MarkerType> myMarkertypes = myMarkerTypesDTO.getFilledMarkertypes();
				markerService.updateMarkerTypes(myMarkertypes);
				Map<String, Object> myModel = new HashMap<String, Object>();
				return new ModelAndView(getSuccessView(), myModel);
			}
		}
		else {
			Map<String, Object> myModel = new HashMap<String, Object>();
			List<MarkerType> myMarkerTypes = markerService.listMarkerTypes();
			myModel.put("markertypes", myMarkerTypes);
			myModel.put("markerTypeImages", markerService.listMarkerTypeImages());
			return new ModelAndView(getFormView(), myModel);
		}
	}

	/**
	 * @param request
	 * @return
	 */
	private boolean isFormSubmit(HttpServletRequest request) {
		return "POST".equals(request.getMethod());
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(MarkerTypesDTO.class, "markertypes", new CustomCollectionEditor(List.class));
		binder.registerCustomEditor(Long.class, new LongEditor());
		super.initBinder(request, binder);
	}

	public String getErrorView() {
		return errorView;
	}

	public MarkerService getMarkerService() {
		return markerService;
	}

	public String getFormView() {
		return formView;
	}

	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

	public void setMarkerService(MarkerService markerService) {
		this.markerService = markerService;
	}

	public void setFormView(String view) {
		this.formView = view;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public class MarkerTypesDTO {
		private List<MarkerType> markertypes;

		/**
		 * @param markertypes
		 */
		public MarkerTypesDTO(List<MarkerType> markertypes) {
			super();
			this.markertypes = markertypes;
		}

		public List<MarkerType> getMarkertypes() {
			return markertypes;
		}

		public List<MarkerType> getFilledMarkertypes() {
			List<MarkerType> myList = new ArrayList<MarkerType>();
			Iterator<MarkerType> myIterator = markertypes.iterator();
			while (myIterator.hasNext()) {

				Object myNext = myIterator.next();
				if (myNext instanceof MarkerType) {
					MarkerType myMarkerType = (MarkerType) myNext;
					String myName = myMarkerType.getName();
					if (myName != null && myName.trim().length() > 0) {
						myList.add(myMarkerType);
					}
				}
			}
			return myList;
		}

		public void setMarkertypes(List<MarkerType> markertypes) {
			this.markertypes = markertypes;
		}

	}

	public class LongEditor extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (!"".equals(text)) {
				try {
					setValue(new Long(text));
				}
				catch (Exception e) {
					// ignore
				}
			}
		}
	}

}
