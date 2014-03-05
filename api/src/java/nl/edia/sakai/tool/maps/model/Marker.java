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
package nl.edia.sakai.tool.maps.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Class Marker.
 *
 * @author Maarten van Hoof
 */
public class Marker extends AbstractLongIdEntity {
	
	/**
	 * Latitude of the location
	 */
	private float lat;
	/**
	 * Longitude of the location
	 */
	private float lng;
	/**
	 * The title of the location, 
	 * @maarten: is this the name of the location?
	 */
	private String title;
	/**
	 * The description of the location
	 */
	private String description;
	/**
	 * The url, optional
	 */
	private String url;
	/**
	 * A list of resources linked to this Marker.
	 */
	private List<String> resources;
	/**
	 * The marker type.
	 */
	private MarkerType type;
	/**
	 * The owner id
	 */
	private String owner;
	
	/**
	 * The id for the context the marker is located in, this can be referred
	 * to as "unique space" identifier. In sakai this translates to the
	 * placement id, or map instance id.
	 */
	private String context;
	
	/**
	 * The creation date of the entity
	 */
	private Date creationDate;
	
	/**
	 * The modification date of the entity
	 */
	private Date modificationDate;
	
	/**
	 * Derived property that depicts the possiblilty to remove
	 * this marker by the current user
	 */
	private boolean canDelete;
	
	/**
	 * Derived property that depicts the possibility to edit
	 * this marker by the current user
	 */
	private boolean canEdit;



	public boolean isCanDelete() {
		return canDelete;
	}


	public void setCanDelete(boolean isDeletable) {
		this.canDelete = isDeletable;
	}


	public boolean isCanEdit() {
		return canEdit;
	}


	public void setCanEdit(boolean isEditable) {
		this.canEdit = isEditable;
	}


	/**
	 * Marker constructor.
	 */
	public Marker() {
	}

	
	/**
	 * Convinience constructor that sets only the fields that aren't set by the marker service.
	 * @param lat
	 * @param lng
	 * @param title
	 * @param description
	 * @param url
	 * @param type
	 */
	public Marker(float lat, float lng, String title, String description, String url, MarkerType type) {
		setLat(lat);
		setLng(lng);
		setTitle(title);
		setDescription(description);
		setUrl(url);
		setType(type);
	}

	/**
	 * Full constructor
	 * 
	 * @param id
	 * @param lat
	 * @param lng
	 * @param title
	 * @param description
	 * @param url
	 * @param type
	 * @param owner
	 * @param context
	 * @param creationDate
	 * @param modificationDate
	 */
	public Marker(Long id, float lat, float lng, String title, String description, String url, MarkerType type, String owner, String context, Timestamp creationDate, Timestamp modificationDate) {
		super();
		this.id = id;
		this.lat = lat;
		this.lng = lng;
		this.title = title;
		this.description = description;
		this.url = url;
		this.type = type;
		this.owner = owner;
		this.context = context;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
	}
	
	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	
	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	
	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
	/**
	 * @return
	 */
	public MarkerType getType() {
		return type;
	}

	
	/**
	 * @param type
	 */
	public void setType(MarkerType type) {
		this.type = type;
	}

	
	/**
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	
	
	/**
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	/**
	 * Get the latitude.
	 * @return
	 */
	public float getLat() {
		return lat;
	}
	
	
	/**
	 * Set the latitude.
	 * @param lat
	 */
	public void setLat(float lat) {
		this.lat = lat;
	}
	
	
	/**
	 * Get the longitude.
	 * @return
	 */
	public float getLng() {
		return lng;
	}
	
	
	/**
	 * Set the longitude.
	 * @param lng
	 */
	public void setLng(float lng) {
		this.lng = lng;
	}


	public String getContext() {
		return context;
	}


	public void setContext(String contextId) {
		this.context = contextId;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getModificationDate() {
		return modificationDate;
	}


	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	/**
	 * @return the resources
	 */
	public List<String> getResources() {
		return resources;
	}


	/**
	 * @param resources the resources to set
	 */
	public void setResources(List<String> resources) {
		this.resources = resources;
	}

}
