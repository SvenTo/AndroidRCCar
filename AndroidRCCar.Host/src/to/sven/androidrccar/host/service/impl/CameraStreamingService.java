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

import de.kp.rtspcamera.RtspNativeCodecsCamera;
import android.content.Context;
import android.content.Intent;
import to.sven.androidrccar.host.service.contract.ICameraStreamingService;

/**
 * Activates streaming of the camera over network.
 * @author sven
 *
 */
public class CameraStreamingService implements ICameraStreamingService {
	/**
	 * The default port for streaming.
	 */
	private static final int PORT = 5548;
	
	/**
	 * The default path.
	 */
	private static final String PATH = "kupandroid.3gp";
	
	/**
	 * The default URI scheme.
	 */
	private static final String SCHEME = "rtsp";
	
	/**
	 * True, if {@link #initialize(Context)} was called, else false.
	 */
	private boolean activiated = false;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(Context context) {
		Intent intent = new Intent(context, RtspNativeCodecsCamera.class);
		intent.putExtra(RtspNativeCodecsCamera.SERVER_PORT_ID, PORT);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		activiated = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPort() {
		return PORT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return PATH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScheme() {
		return SCHEME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		/** TODO: Not implemented, the user must close the activity. */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean activated() {
		return activiated;
	}

}
