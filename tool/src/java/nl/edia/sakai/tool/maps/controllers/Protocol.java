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

public interface Protocol {
	
	public static final String MARKER_ELM = "marker";
	public static final String RESOURCE_ELM = "resource";
	public static final String MARKERTYPE_ELM = "markertype";
	public static final String MARKERS_ELM = "markers";
	public static final String MARKERTYPES_ELM = "markertypes";
	public static final String ERROR_ELM = "error";
	
	public static final String ID_ATTR = "id";
	public static final String USER_ATTR = "user";
	public static final String LAT_ATTR = "lat";
	public static final String LNG_ATTR = "lng";
	public static final String TITLE_ATTR = "title";
	public static final String DESCRIPTION_ATTR = "description";
	public static final String RESOURCECOUNT_ATTR = "resource.count";
	public static final String RESOURCE_ATTR = "resource.";
	public static final String URL_ATTR = "url";
	public static final String CREATIONDATE_ATTR = "creationdate";
	public static final String MODIFICATIONDATER_ATTR = "modificationdate";
	

	public static final String TYPE_ID_ATTR = "typeid";
	public static final String MESSAGE_ATTR = "message";
	public static final String CANDELETE_ATTR = "candelete";
	public static final String CANEDIT_ATTR = "canedit";
	public static final String CANCREATE_ATTR = "cancreate";

	public static final String IMAGE_ATTR = "image";
	public static final String NAME_ATTR = "name";

}
