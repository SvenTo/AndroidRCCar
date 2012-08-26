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
package to.sven.androidrccar.client.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import to.sven.androidrccar.client.service.contract.IClientConfigurationService;
import to.sven.androidrccar.common.model.ConnectionParameter;

/**
 *  Provides access to the configuration of the Client application.
 *  @author sven
 */
public class ClientConfigurationService implements IClientConfigurationService {
	
	/**
	 * Key for value in {@link #prefs} used by {@link #getBatteryPowerMessageInterval}.
	 */
	private final static String BATTERY_POWER_MESSAGE_INTERVAL_KEY = "BatteryPowerMessageInterval";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #getLocationMessageInterval}.
	 */
	private final static String LOCATION_MESSAGE_INTERVAL_KEY = "LocationMessageInterval";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #getCameraEnabled}.
	 */
	private final static String CAMERA_ENABLED_KEY = "CameraEnabled";	

	/**
	 * Key for value in {@link #prefs} used by {@link #getLastHostAdress}.
	 */
	private final static String LAST_HOST_ADRESS_KEY = "LastHostAdress";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #getLastPort()}.
	 */
	private final static String LAST_PORT_KEY = "LastPort";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #getSavedPassword()}.
	 */
	private final static String SAVED_PASSWORD_KEY = "SavedPassword";
		
	/**
	 * The Object where the configuration is stored.
	 */
	private final SharedPreferences prefs;
	
	/**
	 * Default Constructor
	 * @param context {@link Context} used to get the {@link SharedPreferences}.
	 */
	public ClientConfigurationService(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public int getBatteryPowerMessageInterval() {
		return prefs.getInt(BATTERY_POWER_MESSAGE_INTERVAL_KEY, 60000);
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public int getLocationMessageInterval() {
		return prefs.getInt(LOCATION_MESSAGE_INTERVAL_KEY, 1000);
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean getCameraEnabled() {
		return prefs.getBoolean(CAMERA_ENABLED_KEY, true);
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public String getLastHostAdress() {
		return prefs.getString(LAST_HOST_ADRESS_KEY, "");
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public int getLastPort() {
		return prefs.getInt(LAST_PORT_KEY, ConnectionParameter.DEFAULT_PORT);
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public ConnectionParameter getConnectionParameter() {
		return new ConnectionParameter(getLastHostAdress(), getLastPort(), getSavedPassword());
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public String getSavedPassword() {
		return prefs.getString(SAVED_PASSWORD_KEY, null);
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void resetSavedConnectionParameter() {
		prefs.edit()
			 .remove(LAST_HOST_ADRESS_KEY)
			 .remove(LAST_PORT_KEY)
			 .remove(SAVED_PASSWORD_KEY)
			 .commit();
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void saveConnectionParameter(ConnectionParameter connectionParameter) {
		prefs.edit()
			 .putString(LAST_HOST_ADRESS_KEY, connectionParameter.host)
			 .putInt(LAST_PORT_KEY, connectionParameter.port)
			 .putString(SAVED_PASSWORD_KEY, connectionParameter.password)
			 .commit();
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void reset() {
		prefs.edit()
			 .clear()
			 .commit();
	}
}
