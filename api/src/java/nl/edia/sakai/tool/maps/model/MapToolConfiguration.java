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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapToolConfiguration {
	
	/**
	 * The reference to the context the configuration belongs
	 * to. Used as primary key.
	 */
	private String context;
	
	/**
	 * The Google Maps key used. This is set by the MapToolCOnfigurationService,
	 * chosen from one of the keys from the googleMapsKeys list.
	 */
	private String googleMapsKey;
	
	/**
	 * The Google maps keys for this tool.
	 * A site that is accessible through more than one url will have multiple
	 * keys. Otherwise you'll normally have a single key.
	 */
	private List<GoogleMapsKey> googleMapsKeys = new ArrayList<GoogleMapsKey>();
	
	/**
	 * List of map configurations
	 */
	private List<MapConfiguration> maps = new ArrayList<MapConfiguration>();
	
	/**
	 * The map that loads by default. Is one out of the maps list.
	 */
	private MapConfiguration defaultMap;
	
	/**
	 * List of marker types
	 */
	private List<MarkerType> markerTypes = new ArrayList<MarkerType>();

	/**
	 * Creation date
	 */
	private Date creationDate;
	
	/**
	 * Modification date
	 */
	private Date modificationDate;
	
	public List<MarkerType> getMarkerTypes() {
		return markerTypes;
	}

	public void setMarkerTypes(List<MarkerType> markerTypes) {
		this.markerTypes = markerTypes;
	}

	public List<MapConfiguration> getMaps() {
		return maps;
	}

	public void setMaps(List<MapConfiguration> maps) {
		this.maps = maps;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getGoogleMapsKey() {
		return googleMapsKey;
	}

	public void setGoogleMapsKey(String googleMapKey) {
		this.googleMapsKey = googleMapKey;
	}

	/**
	 * @return the googleMapsKeys
	 */
	public List<GoogleMapsKey> getGoogleMapsKeys() {
		return googleMapsKeys;
	}

	/**
	 * @param googleMapsKeys the googleMapsKeys to set
	 */
	public void setGoogleMapsKeys(List<GoogleMapsKey> googleMapsKeys) {
		this.googleMapsKeys = googleMapsKeys;
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
	
	public boolean isConfigured() {
		return (googleMapsKey != null && googleMapsKey.trim().length() > 0) || (googleMapsKeys != null && !googleMapsKeys.isEmpty());
	}

	/**
	 * Returns the default map.
	 * The default map is the map that loads on first access to the map page.
	 * @return the default map
	 */
	public MapConfiguration getDefaultMap() {
		return defaultMap;
	}

	/**
	 * @param defaultMap the default map to set
	 */
	public void setDefaultMap(MapConfiguration defaultMap) {
		this.defaultMap = defaultMap;
	}
	
	
}
