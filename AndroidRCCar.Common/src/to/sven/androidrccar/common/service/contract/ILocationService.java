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
package to.sven.androidrccar.common.service.contract;

import android.location.Location;

/**
 * Provides access to the current location.
 * @author sven
 *
 */
public interface ILocationService {
	
	/**
	 * Requests location updates.
	 */
	void startListen();	
	
	/**
	 * Stops listen for location updates.
	 */
	void stopListen();
	
	/**
	 * Has the location changed since last call of {@link #getCurrentLocation()}
	 * @return True, if location has changed
	 */
	boolean hasLocationChanged();
	
	/**
	 * Returns the current location.
	 * Resets {@link #hasLocationChanged()} to false.
	 * It can be null, if no location is available yet. 
	 * @return Current {@link Location}.
	 */
	Location getCurrentLocation();
}
