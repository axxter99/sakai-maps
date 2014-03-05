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
package nl.edia.sakai.tool.maps.views;

import java.text.DateFormat;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import nl.edia.sakai.tool.maps.controllers.Protocol;
import nl.edia.sakai.tool.maps.model.Marker;
import nl.edia.sakai.tool.maps.model.MarkerType;
import nl.edia.sakai.tool.util.SakaiUtils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;
/**
 * The Xslt view is used to render the model (map) into an
 * XML view. The absence of the xstl makes the xslt 
 * view return plain xml.
 * @author Roland
 */
@SuppressWarnings("deprecation")
public abstract class AbstractMarkerXsltView extends AbstractXsltView implements Protocol {

	/**
	 * Creats a xml representation of a marker.
	 * @param marker
	 * @return Element representing the marker.
	 */
	protected Element createMarkerElement(Marker marker) {
		
		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT, SakaiUtils.getLocale());

		Element element = new Element(MARKER_ELM);
		element.setAttribute(ID_ATTR, marker.getId().toString());
		element.setAttribute(LAT_ATTR, Float.toString(marker.getLat()));
		element.setAttribute(LNG_ATTR, Float.toString(marker.getLng()));
		element.setAttribute(TITLE_ATTR, marker.getTitle());
		element.setAttribute(URL_ATTR, (marker.getUrl() != null ? marker.getUrl() : ""));
		
		element.setAttribute(TYPE_ID_ATTR, getTypeId(marker));
		element.setAttribute(CREATIONDATE_ATTR, formatter.format(marker.getCreationDate()));
		element.setAttribute(MODIFICATIONDATER_ATTR, formatter.format(marker.getModificationDate()));
		
		element.setAttribute(CANDELETE_ATTR, Boolean.toString(marker.isCanDelete()));
		element.setAttribute(CANEDIT_ATTR, Boolean.toString(marker.isCanEdit()));
		
		element.setText((marker.getDescription() != null ? marker.getDescription() : ""));
		
		return element;
	}
	
	protected Element createMarkerTypeElement(MarkerType markerType) {
		Element element = new Element(MARKERTYPE_ELM);
		Long myId = markerType.getId();
		if (myId != null) {
			element.setAttribute(ID_ATTR, myId.toString());
		} else {
			element.setAttribute(ID_ATTR, "");
		}
		element.setAttribute(IMAGE_ATTR, getSave(markerType.getImage()));
		element.setAttribute(NAME_ATTR, getSave(markerType.getName()));
		element.setAttribute(CANDELETE_ATTR, Boolean.toString(markerType.isCanDelete()));
		
		return element;
	}

	/**
	 * @param value
	 * @return
	 */
	protected String getSave(String value) {
		return (value == null ? "" : value);
	}


	/**
	 * @param marker
	 * @return
	 */
	protected String getTypeId(Marker marker) {
		MarkerType myType = marker.getType();
		String typeId = (myType == null ? "" : (myType.getId() == null ? "" : myType.getId().toString()));
		return typeId;
	}

	/**
	 * @param element
	 * @return Source containing the document with as root the 
	 * given element.
	 * @throws JDOMException
	 */
	protected Source createDocSource(Element element) throws JDOMException {
		Document myDocument = new Document();
		myDocument.addContent(element);
		return new DOMSource(new DOMOutputter().output(myDocument));
	}

}
