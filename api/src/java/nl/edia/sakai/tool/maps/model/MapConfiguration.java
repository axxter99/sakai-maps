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

public class MapConfiguration extends AbstractLongIdEntity {
	
	public static final String NORMAL_MAP = "G_NORMAL_MAP";
	public static final String SATELLITE_MAP = "G_SATELLITE_MAP";
	public static final String HYBRID_MAP = "G_HYBRID_MAP";
		
	
	/** Center lattitude */
	private float centerLat;
	
	/** Center longitude */
	private float centerLng;
	
	/** Zoom factor */
	private float zoom;
	
	/** The map type: NORMAL_MAP, SATELLITE_MAP,HYBRID_MAP. */ 
	private String type;
	
	/** Image */
	private String image;
	
	/** Name */
	private String name;

	/**
	 * 
	 */
	public MapConfiguration() {
		super();
	}

	/**
	 * @param centerLat
	 * @param centerLng
	 * @param zoom
	 * @param image
	 * @param name
	 */
	public MapConfiguration(float centerLat, float centerLng, float zoom, String image, String name) {
		super();
		this.centerLat = centerLat;
		this.centerLng = centerLng;
		this.zoom = zoom;
		this.image = image;
		this.name = name;
	}

	/**
	 * @param centerLat
	 * @param centerLng
	 * @param zoom
	 */
	public MapConfiguration(float centerLat, float centerLng, float zoom) {
		super();
		this.centerLat = centerLat;
		this.centerLng = centerLng;
		this.zoom = zoom;
	}

	/**
	 * @param centerLat
	 * @param centerLng
	 * @param zoom
	 * @param image
	 */
	public MapConfiguration(float centerLat, float centerLng, float zoom, String image) {
		super();
		this.centerLat = centerLat;
		this.centerLng = centerLng;
		this.zoom = zoom;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public float getCenterLat() {
		return centerLat;
	}

	public void setCenterLat(float centerLat) {
		this.centerLat = centerLat;
	}

	public float getCenterLng() {
		return centerLng;
	}

	public void setCenterLng(float centerLng) {
		this.centerLng = centerLng;
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	/** 
	 * Return the map type: NORMAL_MAP, SATELLITE_MAP,HYBRID_MAP. 
	 */ 
	public String getType() {
		return type;
	}

	/** 
	 * Set the map type: NORMAL_MAP, SATELLITE_MAP,HYBRID_MAP. 
	 */ 
	public void setType(String mapType) {
		this.type = mapType;
	}

}
