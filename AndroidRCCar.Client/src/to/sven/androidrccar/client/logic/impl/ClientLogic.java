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
package to.sven.androidrccar.client.logic.impl;

import to.sven.androidrccar.client.framework.IClientDependencyContainer;
import to.sven.androidrccar.client.logic.contract.IClientLogic;
import to.sven.androidrccar.client.logic.contract.IClientLogicListener;
import to.sven.androidrccar.client.logic.handler.GreetingMessageHandler;
import to.sven.androidrccar.client.logic.handler.IClientLogicHandlerFacade;
import to.sven.androidrccar.client.service.contract.IVideoClientService;
import to.sven.androidrccar.common.communication.model.AdjustSpeedMessage;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.communication.model.RotateCameraMessage;
import to.sven.androidrccar.common.communication.model.TurnCarMessage;
import to.sven.androidrccar.common.logic.impl.AbstractLogic;
import android.util.Log;

/**
 * Logic for the Client:
 * The "missing" part between the user interface and the host.
 * @author sven
 *
 */
public class ClientLogic extends AbstractLogic<IClientDependencyContainer, IClientLogicListener> 
	implements IClientLogicHandlerFacade, IClientLogic {

	/**
	 * Default Constructor
	 * @param container The {@link IClientDependencyContainer}
	 */
	public ClientLogic(IClientDependencyContainer container) {
		super(container);
		
		registerMessageHandler(GreetingMessageHandler.class);
				
		initialized();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		super.close();
		
		IVideoClientService videoClient = getDependency().getVideoClientService();
		videoClient.stop();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void adjustSpeed(float speed) {
		boolean driveBackward = getDependency().getFeatures()
											   .driveBackward;
		float minSpeed = driveBackward?-1:0; 
		if(speed > 1.0f) {
			speed = 1;
		} else if(speed < minSpeed) {
			speed = minSpeed;
		}
		
		sendMessage(new AdjustSpeedMessage(speed));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnCar(float rotation) {
		if(rotation > 1.0f) {
			rotation = 1;
		} else if(rotation < -1.0f) {
			rotation = -1;
		}
		
		sendMessage(new TurnCarMessage(rotation));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotateCamera(float pan, float tilt) {
		FeatureMessage features = getDependency().getFeatures();
		if(pan < -features.cameraPanMin) {
			pan = -features.cameraPanMin; 
		} else if(pan > features.cameraPanMax) {
			pan = features.cameraPanMax;
		}
		if(tilt < -features.cameraTiltMin) {
			tilt = -features.cameraTiltMin;
		} else if(tilt > features.cameraTiltMax) {
			tilt = features.cameraTiltMax;
		}
		
		Log.i("rt", pan + "=p ; t= " + tilt);
		sendMessage(new RotateCameraMessage(pan, tilt));
	}
}
