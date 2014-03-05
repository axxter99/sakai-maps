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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.edia.sakai.tool.maps.MarkerService;
import nl.edia.sakai.tool.maps.Permissions;
import nl.edia.sakai.tool.maps.model.MapToolConfiguration;
import nl.edia.sakai.tool.maps.model.Marker;
import nl.edia.sakai.tool.maps.model.MarkerType;
import nl.edia.sakai.tool.maps.model.MarkerTypeImage;
import nl.edia.sakai.tool.util.SakaiUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.collection.PersistentList;
import org.hibernate.criterion.Restrictions;
import org.sakaiproject.authz.api.FunctionManager;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MarkerServiceImpl extends HibernateDaoSupport implements MarkerService {

	List<MarkerTypeImage> markerTypeImages =  new ArrayList<MarkerTypeImage>();
	private FunctionManager functionManager;
	
	static Log log = LogFactory.getLog(MarkerServiceImpl.class);

	/**
	 * Does an initialization of the service, inserts the proper permissions.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		functionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_CREATE);
		functionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_EDIT);
		functionManager.registerFunction(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_DELETE);
	}

	/**
	 * @throws PermissionException
	 * @see nl.edia.sakai.tool.maps.MarkerService#createMarker(nl.edia.sakai.tool.maps.model.Marker)
	 */
	public Marker createMarker(final Marker marker) throws PermissionException {

		log.debug("create marker " + marker.getId() + ":" + marker.getTitle());

		SakaiUtils.checkPermission(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_CREATE);

		marker.setOwner(SakaiUtils.getCurrentUserId());
		marker.setContext(SakaiUtils.getCurrentPlacementId());
		Date myDate = new Date();
		marker.setCreationDate(myDate);
		marker.setModificationDate(myDate);
		return (Marker) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				session.save(marker);

				SakaiUtils.createModificationEvent(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_CREATE,
						"/marker/" + marker.getId() + "/" + marker.getTitle());

				return marker;
			}
		});

	}

	/**
	 * @see nl.edia.sakai.tool.maps.MarkerService#deleteMarker(nl.edia.sakai.tool.maps.model.Marker)
	 */
	public void deleteMarker(final Long id) throws PermissionException {
		try {
			final Marker marker = readMarker(id);
			log.debug("delete marker " + marker.getId() + ": " + marker.getTitle());
			final String myOwner = SakaiUtils.getCurrentUserId();
			if (!isOwner(myOwner, marker) && !isAdmin()) {
				SakaiUtils.checkPermission(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_DELETE);
			}
	
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) {
					session.delete(marker);
	
					SakaiUtils.createModificationEvent(
							Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_DELETE,
							"/marker/" + marker.getId() + "/" + marker.getTitle());
	
					return null;
				}
			});
		}
		catch (IdUnusedException alreadyGone) {
			return;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @throws PermissionException
	 * @see nl.edia.sakai.tool.maps.MarkerService#listMarkers()
	 */
	@SuppressWarnings("unchecked")
	public List<Marker> listMarkers() throws PermissionException {
		log.debug("list markers");
		final boolean canEdit = hasEditPermission();
		final boolean canDelete = hasDeletePermission();

		final String myContext = SakaiUtils.getCurrentPlacementId();
		final String myOwner = SakaiUtils.getCurrentUserId();

		return (List<Marker>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						Criteria myCriteria = session.createCriteria(Marker.class);
						myCriteria.add(Restrictions.eq("context", myContext));
						List<Marker> myList = myCriteria.list();
						Iterator myIterator = myList.iterator();
						while (myIterator.hasNext()) {
							Marker myMarker = (Marker) myIterator.next();
							setPermissions(canEdit, canDelete, myOwner, myMarker);
						}
						return myList;
					}
				});
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @throws PermissionException
	 * @see nl.edia.sakai.tool.maps.MarkerService#listMarkers()
	 */
	@SuppressWarnings("unchecked")
	public List<Marker> listMarkersByType(final MarkerType type) throws PermissionException {
		log.debug("list markers of type " + type.getName() + " ("+type.getId()+").");
		final boolean canEdit = hasEditPermission();
		final boolean canDelete = hasDeletePermission();

		final String myContext = SakaiUtils.getCurrentPlacementId();
		final String myOwner = SakaiUtils.getCurrentUserId();

		return (List<Marker>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						Criteria myCriteria = session.createCriteria(Marker.class);
						myCriteria.add(Restrictions.eq("context", myContext));
						myCriteria.add(Restrictions.eq("type", type));
						List<Marker> myList = myCriteria.list();
						Iterator myIterator = myList.iterator();
						while (myIterator.hasNext()) {
							Marker myMarker = (Marker) myIterator.next();
							setPermissions(canEdit, canDelete, myOwner, myMarker);
						}
						return myList;
					}
				});
	}

	/**
	 * @throws PermissionException
	 * @see nl.edia.sakai.tool.maps.MarkerService#listOwnMarkers()
	 */
	@SuppressWarnings("unchecked")
	public List<Marker> listOwnMarkers() throws PermissionException {

		final String myOwner = SakaiUtils.getCurrentUserId();
		final String myContext = SakaiUtils.getCurrentPlacementId();
		log.debug("list markers owned by " + myOwner);

		return (List<Marker>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						Criteria myCriteria = session.createCriteria(Marker.class);
						myCriteria.add(Restrictions.eq("owner", myOwner));
						myCriteria.add(Restrictions.eq("context", myContext));
						List<Marker> myList = myCriteria.list();
						Iterator myIterator = myList.iterator();
						while (myIterator.hasNext()) {
							Marker myMarker = (Marker) myIterator.next();
							myMarker.setCanDelete(true);
							myMarker.setCanEdit(true);
						}
						return myList;
					}
				});
	}

	@SuppressWarnings("unchecked")
	public List<MarkerType> listMarkerTypes() {
		final String myContext = SakaiUtils.getCurrentPlacementId();
		return (List<MarkerType>) getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {

						MapToolConfiguration myConfiguration = (MapToolConfiguration) session
								.get(MapToolConfiguration.class, myContext);
						return myConfiguration.getMarkerTypes();
					}
				});
	}

	/**
	 * @throws PermissionException
	 * @see nl.edia.sakai.tool.maps.MarkerService#readMarker(java.lang.Long)
	 */
	public Marker readMarker(final Long id) throws PermissionException, IdUnusedException {
		log.debug("read marker " + id);
		final boolean canEdit = hasEditPermission();
		final boolean canDelete = hasDeletePermission();
		final String myOwner = SakaiUtils.getCurrentUserId();
		Marker marker = (Marker) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Marker myMarker = (Marker) session.get(Marker.class, id);
				if (myMarker != null) {
					setPermissions(canEdit, canDelete, myOwner, myMarker);
				}
				return myMarker;
			}
		});
		
		if (marker == null) {
			log.warn("marker " + id + " does not exist.");
			throw new IdUnusedException(id.toString());
		}
		
		return marker;

	}

	public MarkerType readMarkerType(final Long id) {
		log.debug("read marker type " + id);
		return (MarkerType) getHibernateTemplate().execute(
			new HibernateCallback() {
				public Object doInHibernate(Session session) {
					return (MarkerType) session.get(MarkerType.class, id);
				}
			});

	}

	/**
	 * @see nl.edia.sakai.tool.maps.MarkerService#updateMarker(nl.edia.sakai.tool.maps.model.Marker)
	 */
	public void updateMarker(final Marker marker) throws PermissionException {
		log.debug("update marker " + marker.getId() + ":" + marker.getTitle());

		String myOwner = SakaiUtils.getCurrentUserId();
		if (!isOwner(myOwner, marker) && !isAdmin()) {
			SakaiUtils.checkPermission(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_EDIT);
		}

		marker.setModificationDate(new Date());

		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				session.update(marker);

				SakaiUtils.createModificationEvent(
						Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_EDIT,
						"/marker/" + marker.getId() + "/" + marker.getTitle());

				return null;
			}
		});

	}

	/**
	 * @return
	 */
	protected boolean isAdmin() {
		return SakaiUtils.hasPermission(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_ADMIN);
	}

	public void updateMarkerTypes(final List<MarkerType> markerTypes) throws PermissionException {
		SakaiUtils.checkPermission(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKERTYPE_ADMIN);

		final String myContext = SakaiUtils.getCurrentPlacementId();

		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				MapToolConfiguration myConfiguration = (MapToolConfiguration) session
						.get(MapToolConfiguration.class, myContext);
				PersistentList myMarkerTypes = (PersistentList)myConfiguration.getMarkerTypes();
				if (markerTypes == myMarkerTypes) {
				} else if (markerTypes instanceof PersistentList) {
					myConfiguration.setMarkerTypes(markerTypes);
				} else {
					mergeLists(myMarkerTypes, markerTypes);
					session.update(myConfiguration);
				}
				return null;
			}
		});

	}

	/**
	 * @param owner
	 * @param marker
	 * @return
	 */
	protected boolean isOwner(final String owner, final Marker marker) {
		return owner != null && marker.getOwner() != null && owner.equals(marker.getOwner());
	}

	/**
	 * @param canEdi
	 * @param canDelete
	 * @param myOwner
	 * @param myMarker
	 */
	private void setPermissions(final boolean canEdit, final boolean canDelete, final String myOwner, Marker myMarker) {
		if (isOwner(myOwner, myMarker)) {
			myMarker.setCanDelete(true);
			myMarker.setCanEdit(true);
		} else {
			myMarker.setCanDelete(canEdit);
			myMarker.setCanEdit(canDelete);
		}
	}

	/**
	 * Checks if the user has delete permission.
	 * 
	 * @return
	 */
	private boolean hasDeletePermission() {
		return SakaiUtils.hasPermission(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_DELETE);
	}

	/**
	 * Checks if the user has edit permission on site level.
	 * 
	 * @return
	 */
	private boolean hasEditPermission() {
		return SakaiUtils.hasPermission(Permissions.PERMISSION_EDIA_SAKAI_MAPS_MARKER_EDIT);
	}

	/**
	 * @param persistentMarkerTypes
	 * @param markerTypes
	 */
	@SuppressWarnings("unchecked")
	protected void mergeLists(PersistentList persistentMarkerTypes, final List<MarkerType> markerTypes) {
		ArrayList<MarkerType> myCache = new ArrayList<MarkerType>(persistentMarkerTypes);
		int mySize = persistentMarkerTypes.size();
		Iterator<MarkerType> myIterator = markerTypes.iterator();
		int myIndex = 0;
		while (myIterator.hasNext()) {
			MarkerType myMarkerType = myIterator.next();
			int myIndexOf = myCache.indexOf(myMarkerType);
			if (myIndexOf != -1) {
				MarkerType myDbMarkerType = myCache.get(myIndexOf);
				myDbMarkerType.copyProps(myMarkerType);
				myMarkerType = myDbMarkerType;
				
				markerTypes.set(myIndex, myMarkerType);
			} else if (myMarkerType.getId() != null){ 
				MarkerType myDbMarkerType = readMarkerType(myMarkerType.getId());
				if (myDbMarkerType != null) {
					myDbMarkerType.copyProps(myMarkerType);
					myMarkerType = myDbMarkerType;
				} else {
					myMarkerType.setId(null);
				}
			}
			if (myIndex < mySize) {
				if (persistentMarkerTypes.get(myIndex) != myMarkerType) {
					persistentMarkerTypes.set(myIndex, myMarkerType);
				}
			} else {
				persistentMarkerTypes.add(myMarkerType);
			}
			
			myIndex++;
		}
		
		for (int i = myIndex; i < mySize; i++) {
			persistentMarkerTypes.remove(persistentMarkerTypes.size() -1);
		}
	}

	public List<MarkerTypeImage> listMarkerTypeImages() {
	    return markerTypeImages;
    }

	public void setMarkerTypeImages(List<MarkerTypeImage> markerTypeImages) {
    	this.markerTypeImages = markerTypeImages;
    }

	/**
	 * @param functionManager the functionManager to set
	 */
	public void setFunctionManager(FunctionManager functionManager) {
		this.functionManager = functionManager;
	}

}
