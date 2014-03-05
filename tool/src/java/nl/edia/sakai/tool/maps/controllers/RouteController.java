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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.edia.sakai.tool.maps.MapToolConfigurationService;
import nl.edia.sakai.tool.maps.Permissions;
import nl.edia.sakai.tool.maps.model.MapToolConfiguration;
import nl.edia.sakai.tool.util.SakaiUtils;

import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class RouteController implements Controller {
	
	private String successView;
	private String toolAdminView;
	private String notConfiguredView;
	private MapToolConfigurationService mapToolConfigurationService;
	private SessionManager sessionManager;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		MapToolConfiguration myMapToolConfiguration = mapToolConfigurationService.readMapToolConfiguration();
		if (!myMapToolConfiguration.isConfigured()) {
			if (SakaiUtils.hasPermission(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_ADMIN)) {
				return new ModelAndView(toolAdminView);
			} else {
				return new ModelAndView(notConfiguredView);
			}
		}
		ToolSession mySession = sessionManager.getCurrentToolSession();
		if (mySession != null) {
			String myLastPage = (String)mySession.getAttribute("nl.edia.sakai.tool.maps.LastPage");
			if (myLastPage != null) {
				// Clean up and prevent looping
				mySession.removeAttribute("nl.edia.sakai.tool.maps.LastPage");
				return new ModelAndView(new RedirectView(myLastPage));
			}
		}
		
    	return new ModelAndView(successView);
    }

	public void setNotConfiguredView(String notConfiguredView) {
    	this.notConfiguredView = notConfiguredView;
    }

	public void setToolAdminView(String toolAdminView) {
    	this.toolAdminView = toolAdminView;
    }

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public void setMapToolConfigurationService(MapToolConfigurationService mapToolConfigurationService) {
    	this.mapToolConfigurationService = mapToolConfigurationService;
    }

	/**
	 * @param sessionManager the sessionManager to set
	 */
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
