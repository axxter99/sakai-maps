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

import nl.edia.sakai.tool.maps.MapToolConfigurationService;
import nl.edia.sakai.tool.maps.model.GoogleMapsKey;
import nl.edia.sakai.tool.maps.model.MapToolConfiguration;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditMapToolConfigurationController extends SimpleFormController {
	
	MapToolConfigurationService mapToolConfigurationService;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse resp, Object command, BindException errors) throws Exception {
		MapToolConfiguration myConfiguration = (MapToolConfiguration)command;
		String defaultMap = request.getParameter("defaultMapSelector");
		if (defaultMap != null) {
			int index = Integer.parseInt(defaultMap);
			if (index == -1) {
				myConfiguration.setDefaultMap(null);
			}
			else if (index < myConfiguration.getMaps().size()){
				myConfiguration.setDefaultMap(myConfiguration.getMaps().get(index));				
			}
		}
		mapToolConfigurationService.updateMapToolConfiguration(myConfiguration);
		return new ModelAndView(getSuccessView());
	}
	
	/**
	 * Should return true if the form submit was not an actual submit but in stead changes the form
	 * structurally. This would be the case if a google maps key was added or removed.
	 */
	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		if (request.getParameter("addGoogleMapsKey") != null) {
			return true;
		}
		else {
			for (int i = 0; i < 20; i++) { // 20 is the practical limit here.
				if (request.getParameter("deleteGoogleMapsKey."+i) != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception{
		MapToolConfiguration myConfiguration = (MapToolConfiguration)command;
		if (request.getParameter("addGoogleMapsKey") != null) {
			myConfiguration.getGoogleMapsKeys().add(new GoogleMapsKey());
		}
		else {
			for (int i = 0; i < myConfiguration.getGoogleMapsKeys().size(); i++) {
				if (request.getParameter("deleteGoogleMapsKey."+i) != null) {
					myConfiguration.getGoogleMapsKeys().remove(i);
					break;
				}
			}
		}
		mapToolConfigurationService.updateMapToolConfiguration(myConfiguration);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return mapToolConfigurationService.readMapToolConfigurationForUpdate();
	}

	public MapToolConfigurationService getMapToolConfigurationService() {
		return mapToolConfigurationService;
	}

	public void setMapToolConfigurationService(MapToolConfigurationService mapToolConfigurationService) {
		this.mapToolConfigurationService = mapToolConfigurationService;
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
	    super.initBinder(request, binder);
//    	binder.setRequiredFields(requiredFields.toArray(new String[requiredFields.size()]));
	}
}
