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
package nl.edia.sakai.tool.maps.controllers;

import java.util.ArrayList;
import java.util.List;

import nl.edia.sakai.tool.maps.MarkerService;
import nl.edia.sakai.tool.maps.value.ResourceValue;

import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.ContentTypeImageService;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Class MarkerWindowController is the superclass for the marker view and marker edit controller.
 * @author Maarten van Hoof
 *
 */
public abstract class MarkerWindowController implements Controller {
	
	protected ContentHostingService contentHostingService;
	protected ContentTypeImageService contentTypeImageService;
	protected EntityManager entityManager;
	protected String view;
	protected MarkerService markerService;
	private String commandName;
	
	protected List<ResourceValue> getResourceValues(List<String> resourceIds) throws TypeException {
		List<ResourceValue> resources = new ArrayList<ResourceValue>();
		for (String resourceId : resourceIds) {
			try {
				// Create a value object to represent this ContentResource
				ResourceValue resource = new ResourceValue();
				
				Reference reference = entityManager.newReference(resourceId);
				ContentResource contentResource = contentHostingService.getResource(reference.getId());
				ResourceProperties props = contentResource.getProperties();
										
				resource.setId(resourceId);
				resource.setDisplayName(props.getProperty(props.getNamePropDisplayName()));
				resource.setUrl(contentResource.getUrl(true));
				String image = contentTypeImageService.getContentTypeImage(contentResource.getContentType());
				resource.setContentTypeImage(image);
				
				resources.add(resource);
			}
			catch (IdUnusedException iue) {
				// looks like this id doesn't exist anymore. Ignore.
			}
			catch (PermissionException pe) {
				// This user can't read this resource...
			}
		}
		return resources;
	}

	/**
	 * @return the contentHostingService
	 */
	public ContentHostingService getContentHostingService() {
		return contentHostingService;
	}

	/**
	 * @param contentHostingService the contentHostingService to set
	 */
	public void setContentHostingService(ContentHostingService contentHostingService) {
		this.contentHostingService = contentHostingService;
	}

	public MarkerService getMarkerService() {
		return markerService;
	}

	public void setMarkerService(MarkerService markerService) {
		this.markerService = markerService;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandeName) {
		this.commandName = commandeName;
	}

	/**
	 * @param contentTypeImageService the contentTypeImageService to set
	 */
	public void setContentTypeImageService(ContentTypeImageService contentTypeImageService) {
		this.contentTypeImageService = contentTypeImageService;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


}
