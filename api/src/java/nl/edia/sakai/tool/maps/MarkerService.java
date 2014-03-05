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
package nl.edia.sakai.tool.maps;


import java.util.List;

import nl.edia.sakai.tool.maps.model.Marker;
import nl.edia.sakai.tool.maps.model.MarkerType;
import nl.edia.sakai.tool.maps.model.MarkerTypeImage;

import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;

public interface MarkerService {
	
	/**
	 * Insert the given Marker into the database.
	 * @param marker the Marker to make persistent. 
	 */
	public Marker createMarker(Marker marker) throws PermissionException;
	
	/**
	 * Return the marker with the given id
	 * @param id the marker id.
	 * @return the marker with the given id.
	 */
	public Marker readMarker(Long id) throws PermissionException, IdUnusedException;
	
	/**
	 * Reads a marker type with the given id
	 * @param typeId
	 * @return
	 */
	public MarkerType readMarkerType(Long typeId);
	
	/**
	 * Lists the marker types for the active tool / placement
	 * @param typeId
	 * @return
	public List<MarkerType> listMarkerTypes();
	 */


	/**
	 * Return all Markers.
	 * @return all Markers by all users.
	 */
	public List<Marker> listMarkers() throws PermissionException;

	/**
	 * Return all Markers.
	 * @return all Markers by all users.
	 */
	public List<Marker> listMarkersByType(MarkerType type) throws PermissionException;

	/**
	 * Return all Markers.
	 * @return all Markers by all users.
	 */
	public List<MarkerType> listMarkerTypes();
	
	/**
	 * Returns all marker image types
	 * @return
	 */
	public List<MarkerTypeImage> listMarkerTypeImages();

	/**
	 * List all Markers created by the given person.
	 * @return a List of Markers by the given user.
	 */
	public List<Marker> listOwnMarkers() throws PermissionException;

	/**
	 * Update the given Marker.
	 * @param marker the Marker to update.
	 */
	public void updateMarker(Marker marker) throws PermissionException;

	/**
	 * Update the active set of marker types in this tool / placement.
	 * MarkerTypes without id are considered new, marker types
	 * with an id are updated. Marker types missing from
	 * this list are removed, if possible.
	 * @param marker the Marker to update.
	 */
	public void updateMarkerTypes(List<MarkerType> markerTypes) throws PermissionException;

	/**
	 * Delete the marker with the given id.
	 * @param id the marker id.
	 * @throws PermissionException if the current user is not allowed to delete the marker.
	 */
	public void deleteMarker(Long id) throws PermissionException;

}