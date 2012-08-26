/*******************************************************************************
 * Copyright (C) 2012 Sven Nobis
 * 
 * This file is part of AndroidRCCar (http://androidrccar.sven.to)
 * 
 * AndroidRCCar is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 ******************************************************************************/
package to.sven.androidrccar.common.service.impl;

import to.sven.androidrccar.common.service.contract.ILocationService;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

// TODO: Better implementation: implement a delta to locationChange, combine providers to better result, what is when GPS is not avaiable?

/**
 * Provides access to the current location.
 * @author sven
 *
 */
public class LocationService implements ILocationService, LocationListener {

	/**
	 * {@link LocationManager}
	 */
	private LocationManager locationManager;
	
	/**
	 * Current Location
	 */
	private Location location;
	
	/**
	 * Has the location changed since last request?
	 */
	private boolean locationChanged;
	
	/**
	 * Default Constructor
	 * @param locationManager {@link LocationManager}
	 */
	public LocationService(LocationManager locationManager) {
		this.locationManager = locationManager;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startListen() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopListen() {
		locationManager.removeUpdates(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasLocationChanged() {
		return locationChanged;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Location getCurrentLocation() {
		locationChanged = false;
		return location;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLocationChanged(Location newLocation) {
		this.location = newLocation;
		locationChanged = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}
