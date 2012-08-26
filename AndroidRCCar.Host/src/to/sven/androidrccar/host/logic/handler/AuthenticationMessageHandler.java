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
package to.sven.androidrccar.host.logic.handler;

import to.sven.androidrccar.common.communication.model.AuthenticationFailedMessage;
import to.sven.androidrccar.common.communication.model.AuthenticationMessage;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.communication.model.GreetingMessage;
import to.sven.androidrccar.common.utils.CryptUtils;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.service.contract.IHostConfigurationService;

/**
 * This handler handles a received {@link AuthenticationMessage}.
 * This message is expected after sending the {@link GreetingMessage} (so directly after connecting).
 * @author sven
 *
 */
public class AuthenticationMessageHandler extends
		AbstractHostMessageHandler<AuthenticationMessage> {

	/**
	 * Creates the {@link AuthenticationMessageHandler}.
	 * @param logic The {@link IHostLogicHandlerFacade}
	 */
	public AuthenticationMessageHandler(IHostLogicHandlerFacade logic) {
		super(logic);
	}
	
	/**
	 *  Checks if the client has send the correct password.
	 *  
	 *  If: {@link #AuthenticationSuccessfull()}
	 *  If not: {@link #AuthenticationFailed()}
	 */
	@Override
	public void handleMessage(AuthenticationMessage message) {
		logic.clearMessageHandlers();
		
		String password = dependences.getConfiguration()
						   			 .getPassword();
		String expectedHash = CryptUtils.encryptPassword(password, logic.getSalt());
		
		if(message.passwordHash.equals(expectedHash)) {
			AuthenticationSuccessfull();
		} else {
			AuthenticationFailed();
		}
	}
	
	/**
	 * The client has send the correct password:
	 * The host goes from the initialize state into the ready state.
	 * This means all "normal" {@link AbstractHostMessageHandler} will be registered
	 * and a {@link FeatureMessage} will be send.
	 */
	private void AuthenticationSuccessfull() {
		FeatureMessage featureMessage = createFeatureMessage();
		registerHandlers(featureMessage);

		dependences.getLogicListener()
		 		   .connectedEstablished();
		logic.sendMessage(featureMessage);
	}
	
	/**
	 * Register handlers depending on the Features.
	 * @param featureMessage The Features.
	 */
	private void registerHandlers(FeatureMessage featureMessage) {
		logic.registerMessageHandler(TurnCarMessageHandler.class);
		logic.registerMessageHandler(AdjustSpeedMessageHandler.class);
		
		if(featureMessage.camera) {
			logic.registerMessageHandler(RequestCameraMessageHandler.class);
			if(featureMessage.supportRotateCamera()) {
				logic.registerMessageHandler(RotateCameraMessageHandler.class);
			}
		}
		
		if(featureMessage.supportAnyLocationDependingFeature() || featureMessage.batteryPower) {
			logic.registerMessageHandler(SetUpCyclicUpdateMessageHandler.class);
		}
	}

	/**
	 * The client HAS NOT send the correct password:
	 * The Client will be informed about that. (The communication is over.)
	 */
	private void AuthenticationFailed() {
		logic.sendMessage(new AuthenticationFailedMessage("Password did not match.")); // TODO: Diesen String auslagern?
		/**
		 * Wait until the client exits or the {@link NewConnectionWatchdog} kills the connection.
		 */
		// TODO: Wirds den Watchdog geben?
	}
	
	/**
	 * Creates the FeatureMessage from the {@link IHostConfigurationService}
	 * and the {@link CarFeatures}
	 * @return A {@link FeatureMessage} with the features of the Host
	 */
	private FeatureMessage createFeatureMessage() {
		CarFeatures carFeatures = dependences.getCarFeatures();
		IHostConfigurationService config = dependences.getConfiguration();
		
		return new FeatureMessage(config.shareCamera(),
								  (config.canRotateCamera()?carFeatures.cameraPanMin:0),
								  (config.canRotateCamera()?carFeatures.cameraPanMax:0),
								  (config.canRotateCamera()?carFeatures.cameraTiltMin:0),
								  (config.canRotateCamera()?carFeatures.cameraTiltMax:0),
								  carFeatures.adjustableSpeed,
								  carFeatures.driveBackward,
								  config.shareLocation(),
								  config.shareBearing(),
								  config.shareSpeed(),
								  carFeatures.batteryPower);
	}
}
