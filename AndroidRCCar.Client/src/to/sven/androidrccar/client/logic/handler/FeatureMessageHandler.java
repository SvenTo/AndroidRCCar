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
import to.sven.androidrccar.common.communication.model.AuthenticationMessage;
import to.sven.androidrccar.common.communication.model.CyclicUpdateFeatureType;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.communication.model.RequestCameraMessage;
import to.sven.androidrccar.common.communication.model.SetUpCyclicUpdateMessage;

/**
 * This handler handles a received {@link FeatureMessage}.
 * This message is expected after sending the {@link AuthenticationMessage}
 * if the authentication is successful. 
 * @author sven
 *
 */
public class FeatureMessageHandler extends AbstractClientMessageHandler<FeatureMessage> {

	// TODO: Validate Feature Message?
	
	/**
	 * Creates the {@link FeatureMessageHandler}.
	 * @param logic {@link IClientLogicHandlerFacade}
	 */
	public FeatureMessageHandler(IClientLogicHandlerFacade logic) {
		super(logic);
	}
	
	/**
	 * Setting up the client depending on the Features.
	 */
	@Override
	public void handleMessage(FeatureMessage message) {
		logic.clearMessageHandlers();
		dependences.setFeatures(message);
		
		setUpCyclicUpdates(message);
		setUpCameraStream(message);
		setUpDriving(message);
	}
	
	/**
	 * Setting up the cyclic updates of the Battery Power and Location if supported.
	 * @param message The features of the host.
	 */
	private void setUpCyclicUpdates(FeatureMessage message) {
		if(message.batteryPower) {
			logic.registerMessageHandler(BatteryPowerMessageHandler.class);
			
			int batteryPowerInterval = dependences.getConfiguration()
										    	  .getBatteryPowerMessageInterval();
			logic.sendMessage(new SetUpCyclicUpdateMessage(CyclicUpdateFeatureType.batteryPower, batteryPowerInterval));
		}
		
		if(message.supportAnyLocationDependingFeature()) {
			logic.registerMessageHandler(LocationMessageHandler.class);
			logic.enableLocationService(); // TODO: Test thiz
			
			int locationInterval = dependences.getConfiguration()
											  .getLocationMessageInterval();
			logic.sendMessage(new SetUpCyclicUpdateMessage(CyclicUpdateFeatureType.location, locationInterval));
		}
	}
	
	/**
	 * If supported and wanted by the client, the camera stream is set up.
	 * And if supported the {@link IClientLogicListener}
	 * will advised to enable rotation of the camera.
	 * @param message The features of the host.
	 */
	private void setUpCameraStream(FeatureMessage message) {
		boolean cameraEnabledByClient = dependences.getConfiguration()
								 		 		   .getCameraEnabled();
		if(message.camera && cameraEnabledByClient) {
			logic.registerMessageHandler(CameraConnectionParameterMessageHandler.class);
			
			if(message.supportPanCamera()) {
				dependences.getLogicListener()
				 		   .enablePan(message.cameraPanMin, message.cameraPanMax);
			}
			if(message.supportTiltCamera()) {
				dependences.getLogicListener()
				 		   .enableTilt(message.cameraTiltMin, message.cameraTiltMax);
			}
			
			logic.sendMessage(new RequestCameraMessage(true));
		}
	}
	
	/**
	 * Informs the {@link IClientLogicListener} that the user can start driving.
	 * @param message The features of the host.
	 */
	private void setUpDriving(FeatureMessage message) {
		dependences.getLogicListener()
		 		   .enableDriving(message.adjustableSpeed, message.driveBackward);
	}
}
