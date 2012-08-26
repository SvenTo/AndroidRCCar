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
package to.sven.androidrccar.client.logic.handler;

import to.sven.androidrccar.client.logic.contract.IClientLogicListener;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.communication.model.LocationMessage;
import android.location.Location;

/**
 * This handler handles a received {@link LocationMessage}.
 * @author sven
 *
 */
public class LocationMessageHandler extends AbstractClientMessageHandler<LocationMessage> {
	
	/**
	 * Creates the {@link LocationMessageHandler}.
	 * @param logic {@link IClientLogicHandlerFacade}
	 */
	public LocationMessageHandler(IClientLogicHandlerFacade logic) {
		super(logic);
	}

	/**
	 * Tells the {@link IClientLogicListener} 
	 * the distance, bearing and speed
	 * depending on what supported and send.
	 */
	@Override
	public void handleMessage(LocationMessage message) {
		Location remoteLocation = message.toAndroidLocation();
		FeatureMessage features = dependences.getFeatures();
		IClientLogicListener listener = dependences.getLogicListener();
		
		if(features.location) {
			Location myLocation = dependences.getLocationService()
		   			   						 .getCurrentLocation();
			
			if(myLocation != null) { // TODO: Test null check
				float distance = myLocation.distanceTo(remoteLocation);
				float bearingTo = myLocation.bearingTo(remoteLocation);
				listener.setDistance(distance, bearingTo);
			}
		}
		
		if(remoteLocation.hasBearing()) {
			listener.setBearing(remoteLocation.getBearing());
		}
		
		if(remoteLocation.hasSpeed()) {
			listener.setSpeed(remoteLocation.getSpeed());
		}
	}

}
