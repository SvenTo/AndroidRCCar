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
package to.sven.androidrccar.host.service.contract;

import to.sven.androidrccar.common.communication.model.LocationMessage;

/**
 * Provides access to the configuration of the Host application.
 * @author sven
 *
 */
public interface IHostConfigurationService {
	
	/**
	 * Is sharing  the location with the client allowed? 
	 * @return True if allowed.
	 */
	public boolean shareLocation();
	
	/**
	 * Is sharing  the bearing with the client allowed? 
	 * @return True if allowed.
	 */
	public boolean shareBearing();
	
	/**
	 * Is sharing the speed with the client allowed? 
	 * @return True if allowed.
	 */
	public boolean shareSpeed();
	
	/**
	 * Is sharing the camera with the client allowed? 
	 * @return True if allowed.
	 */
	public boolean shareCamera();
	
	
	/**
	 * True, if any of the information in {@link LocationMessage} is shared.
	 * @return True if allowed.
	 */
	public boolean shareAnyLocationDependingFeature();
	
	/**
	 * Is rotation of the camera allowed? (Can only {@code true} if {@link #shareCamera()} is enabled.)
	 * @return True if allowed.
	 */
	public boolean canRotateCamera();
	
	/**
	 * Gets the port where listen for the client.
	 * @return port number.
	 */
	int getPort();
	
	/**
	 * Get the password for client authentication.
	 * @return Stored password or if no password is stored, a generated.
	 */
	public String getPassword();
}
