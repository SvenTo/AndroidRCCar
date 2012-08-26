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
package to.sven.androidrccar.client.service.contract;

import to.sven.androidrccar.common.communication.model.BatteryPowerMessage;
import to.sven.androidrccar.common.communication.model.LocationMessage;
import to.sven.androidrccar.common.model.ConnectionParameter;

/**
 * Provides access to the configuration of the Client application.
 * @author sven
 *
 */
public interface IClientConfigurationService {
	
	/**
	 * Gets the interval, how often the host should send {@link BatteryPowerMessage}s in milliseconds.
	 * @return Interval in milliseconds
	 */
	int getBatteryPowerMessageInterval();
	
	/**
	 * Gets the interval, how often the host should send {@link LocationMessage}s in milliseconds.
	 * @return Interval in milliseconds
	 */
	int getLocationMessageInterval();
	
	/**
	 * Defines, if showing the camera stream is enabled on the Client. 
	 * @return {@code true}, if enabled.
	 */
	boolean getCameraEnabled();
	
	/**
	 * Gets the IP/Hostname that was used for the last connection.
	 * If there is no, a empty string.
	 * @return IP/Hostname
	 */
	String getLastHostAdress();

	/**
	 * Gets the port that was used for the last connection.
	 * If there is no, the default port.
	 * @return Port
	 */
	int getLastPort();

	/**
	 * Gets the password, if saved, else {@code null}.
	 * @return password Password {@link String} or {@code null}
	 */
	String getSavedPassword();
	
	/**
	 * Resets to the default configuration.
	 */
	void reset();

	/**
	 * Creates connection parameter from settings.
	 * @return Created {@link ConnectionParameter}
	 */
	ConnectionParameter getConnectionParameter();

	/**
	 * Reset the connection parameter values to default (nothing).
	 */
	void resetSavedConnectionParameter();

	/**
	 * Save the connection parameters in the settings.
	 * @param connectionParameter connection parameters to save
	 */
	void saveConnectionParameter(ConnectionParameter connectionParameter);
}
