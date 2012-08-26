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
package to.sven.androidrccar.host.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import to.sven.androidrccar.common.model.ConnectionParameter;
import to.sven.androidrccar.common.utils.CryptUtils;
import to.sven.androidrccar.host.service.contract.IHostConfigurationService;

/**
 * Provides access to the configuration of the Host application.
 * @author sven
 *
 */
public class HostConfigurationService implements IHostConfigurationService {
	
	/**
	 * Key for value in {@link #prefs} used by {@link #shareLocation()}.
	 */
	private final static String SHARE_LOCATION_KEY = "ShareLocation";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #shareBearing()}.
	 */
	private final static String SHARE_BEARING_KEY = "ShareBearing";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #shareSpeed()}.
	 */
	private final static String SHARE_SPEED_KEY = "ShareSpeed";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #shareCamera()}.
	 */
	private final static String SHARE_CAMERA_KEY = "ShareCamera";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #canRotateCamera()}.
	 */
	private final static String CAN_ROTATE_CAMERA_KEY = "CanRotateCamera";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #getPort()}.
	 */
	private final static String PORT_KEY = "Port";
	
	/**
	 * Key for value in {@link #prefs} used by {@link #getPassword()}.
	 */
	private final static String PASSWORD_KEY = "Password";
	
	/**
	 * The Object where the configuration is stored.
	 */
	private final SharedPreferences prefs;
	
	/**
	 * Default Constructor
	 * @param context {@link Context} used to get the {@link SharedPreferences}.
	 */
	public HostConfigurationService(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shareLocation() {
		return prefs.getBoolean(SHARE_LOCATION_KEY, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shareBearing() {
		return prefs.getBoolean(SHARE_BEARING_KEY, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shareSpeed() {
		return prefs.getBoolean(SHARE_SPEED_KEY, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shareCamera() {
		return prefs.getBoolean(SHARE_CAMERA_KEY, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canRotateCamera() {
		return prefs.getBoolean(CAN_ROTATE_CAMERA_KEY, true) && shareCamera();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shareAnyLocationDependingFeature() {
		return (shareLocation() || shareSpeed() || shareBearing());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPort() {
		return prefs.getInt(PORT_KEY, ConnectionParameter.DEFAULT_PORT);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPassword() {
		String password = prefs.getString(PASSWORD_KEY, null);
		if(password == null) {
			// Generate and set a default password:
			password = CryptUtils.generateSalt().substring(0, 6);
			prefs.edit()
				 .putString(PASSWORD_KEY, password)
				 .apply();
		}
		return password;
	}

	/**
	 *  Reset configuration to the default. 
	 */
	public void reset() {
		prefs.edit()
			 .clear()
			 .commit();
	}
	
}
