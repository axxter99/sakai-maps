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
package nl.edia.sakai.tool.maps.impl;

import nl.edia.sakai.tool.maps.MapToolConfigurationService;
import nl.edia.sakai.tool.maps.Permissions;
import nl.edia.sakai.tool.maps.model.GoogleMapsKey;
import nl.edia.sakai.tool.maps.model.MapConfiguration;
import nl.edia.sakai.tool.maps.model.MapToolConfiguration;
import nl.edia.sakai.tool.util.SakaiUtils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.sakaiproject.authz.api.FunctionManager;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.exception.PermissionException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class MapToolConfigurationServiceImpl extends HibernateDaoSupport implements MapToolConfigurationService {

	List<MapConfiguration> defaultMaps = new ArrayList<MapConfiguration>();
	List<String> allMarkerIds = new ArrayList<String>();
	List<String> defaultMarkerIds = new ArrayList<String>();
	List<GoogleMapsKey> defaultKeys;
	
	private FunctionManager functionManager;
	
	public void init() {
		functionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_ADMIN);
		functionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKERTYPE_ADMIN);
	}
	
	/**
	 * @see MapToolConfigurationService#readMapToolConfiguration();
	 */
	public MapToolConfiguration readMapToolConfiguration() throws PermissionException {
		final String myContext = SakaiUtils.getCurrentPlacementId();

		MapToolConfiguration myConfig = (MapToolConfiguration) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				MapToolConfiguration myConfiguration = (MapToolConfiguration) session.get(MapToolConfiguration.class, myContext);
				// Create new, if none found
				if (myConfiguration == null) {
					myConfiguration = getDefaultMapToolConfiguration(myContext);
					session.save(myConfiguration);
					session.flush();
				}
				
				// We'll be adding default keys that we don't want saved.
				session.evict(myConfiguration);

				return myConfiguration;
			}
		});
		
		// insert defaults here, so that they aren't stored.
		if (getDefaultKeys() != null && myConfig.getGoogleMapsKeys().size() == 0) {
			myConfig.getGoogleMapsKeys().addAll(getDefaultKeys());
		}
		
		// find the proper maps key.
		GoogleMapsKey keyToUse = null;
		for(GoogleMapsKey key : myConfig.getGoogleMapsKeys()) {
			if (key.getUrl() == null || key.getUrl().trim().length() == 0) {
				// this is the default, which we'll use, but it may be overwritten 
				// later by a specific key.
				keyToUse = key;
			}
			else {
				if (key.getUrl().startsWith(ServerConfigurationService.getServerUrl())) {
					// this is our key.
					keyToUse = key;
					break;
				}
			}
		}
		if (keyToUse != null) {
			myConfig.setGoogleMapsKey(keyToUse.getKey());
		}
		else {
			// no default key and no key that matches!
		}
		
		return myConfig;

	}

	/**
	 * @see MapToolConfigurationService#readMapToolConfigurationForUpdate();
	 */
	public MapToolConfiguration readMapToolConfigurationForUpdate() throws PermissionException {
		final String myContext = SakaiUtils.getCurrentPlacementId();

		return (MapToolConfiguration) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				MapToolConfiguration myConfiguration = (MapToolConfiguration) session.get(MapToolConfiguration.class, myContext);
				// Create new, if none found
				if (myConfiguration == null) {
					myConfiguration = getDefaultMapToolConfiguration(myContext);
					session.save(myConfiguration);
					session.flush();
				}

				return myConfiguration;
			}
		});

	}

	public List<String> listAllImageIds() {
		return getAllMarkerIds();
	}

	/**
	 * @see MapToolConfigurationService#updateMapToolConfiguration(MapToolConfiguration)
	 */
	public void updateMapToolConfiguration(final MapToolConfiguration configuration) throws PermissionException {

		SakaiUtils.checkPermission(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_ADMIN);

		final String myContext = SakaiUtils.getCurrentPlacementId();
		configuration.setContext(myContext);
		configuration.setModificationDate(new Date());
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				session.update(configuration);
				return null;
			}
		});

		SakaiUtils.createModificationEvent(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_ADMIN, myContext);
	}

	/**
	 * Creates the default map, that is, the Amsterdam map set. Please overwrite
	 * this method for your own implemtation of the default values.
	 * 
	 * @param context
	 * @return
	 */
	protected MapToolConfiguration getDefaultMapToolConfiguration(final String context) {
		MapToolConfiguration myConfiguration;
		myConfiguration = new MapToolConfiguration();
		myConfiguration.setContext(context);
		myConfiguration.setCreationDate(new Date());
		myConfiguration.setModificationDate(new Date());
		myConfiguration.setMaps(getDefaultMaps());
		return myConfiguration;
	}

	public List<MapConfiguration> getDefaultMaps() {
		return defaultMaps;
	}

	public void setDefaultMaps(List<MapConfiguration> defaultMaps) {
		this.defaultMaps = defaultMaps;
	}

	public List<String> getAllMarkerIds() {
		return allMarkerIds;
	}

	public void setAllMarkerIds(List<String> allMarkerTypes) {
		this.allMarkerIds = allMarkerTypes;
	}

	public List<String> getDefaultMarkerIds() {
		return defaultMarkerIds;
	}

	public void setDefaultMarkerIds(List<String> defaultMarkerTypes) {
		this.defaultMarkerIds = defaultMarkerTypes;
	}

	/**
	 * @return the defaultKeys
	 */
	public List<GoogleMapsKey> getDefaultKeys() {
		return defaultKeys;
	}

	/**
	 * @param defaultKeys the defaultKeys to set
	 */
	public void setDefaultKeys(List<GoogleMapsKey> defaultKeys) {
		this.defaultKeys = defaultKeys;
	}
	
	/**
	 * This setter allows you to configure the default keys for all map tool instances 
	 * in the sakai.properties file. Should be a set of url-key tuples. Url and key are
	 * separated by a space, the tuples are separated by semicolons.
	 * 
	 * @param defaultKeys
	 */
	public void setKeys(String keys) {
		defaultKeys = new ArrayList<GoogleMapsKey>();
		StringTokenizer tuples = new StringTokenizer(keys, ";");
		while (tuples.hasMoreTokens()) {
			String tuple = tuples.nextToken().trim();
			GoogleMapsKey key = new GoogleMapsKey();
			if (tuple.indexOf(' ') == -1) {
				// this is a key without a url.
				key.setKey(tuple);
			}
			else {
				key.setUrl(tuple.substring(0, tuple.indexOf(' ')));
				key.setKey(tuple.substring(tuple.indexOf(' ')+1));
			}
			defaultKeys.add(key);
		}
	}

	/**
	 * @param functionManager the functionManager to set
	 */
	public void setFunctionManager(FunctionManager functionManager) {
		this.functionManager = functionManager;
	}

}
