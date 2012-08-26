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

import java.io.IOException;

import to.sven.androidrccar.common.communication.model.CyclicUpdateFeatureType;
import to.sven.androidrccar.common.communication.model.SetUpCyclicUpdateMessage;
import to.sven.androidrccar.host.logic.impl.AbstractCyclicTask;
import to.sven.androidrccar.host.logic.impl.ICyclicTask;

/**
 * This handler handles a received {@link SetUpCyclicUpdateMessageHandler}.
 * This message can be received after authentication.
 * @author sven
 *
 */
public class SetUpCyclicUpdateMessageHandler extends AbstractHostMessageHandler<SetUpCyclicUpdateMessage> {

	/**
	 * Creates the {@link SetUpCyclicUpdateMessageHandler}.
	 * @param logic The {@link IHostLogicHandlerFacade}
	 */
	public SetUpCyclicUpdateMessageHandler(IHostLogicHandlerFacade logic) {
		super(logic);
	}

	/**
	 * Enables or Disables the specific {@link AbstractCyclicTask}
	 * with validation before.
	 */
	@Override
	public void handleMessage(SetUpCyclicUpdateMessage message) {
		try {
			validateFeatureEnabled(message.featureType);
			setUpTask(message);
		} catch (IOException e) {
			logic.handleError(e);
		}
	}
	
	/**
	 * Enables or Disables the specific {@link AbstractCyclicTask}. 
	 * @param message The received message.
	 */
	private void setUpTask(SetUpCyclicUpdateMessage message) {
		ICyclicTask task;
		switch(message.featureType) {
			case batteryPower:
				task = logic.getBatteryPowerCyclicTask();
				break;
			case location:
				task = logic.getLocationCyclicTask();
				break;
			default:
				throw new IllegalArgumentException("Unsupported feature type: "+message.featureType.name());
		}
		
		if(message.enableCyclicUpdate()) {
			task.setInterval(message.interval);
			if(!task.isRunning()) {
				task.start();
			}
		} else {
			task.stop();
		}
	}
	
	/**
	 * Checks if the requested {@link CyclicUpdateFeatureType} is allowed/supported.
	 * @param featureType Requested {@link CyclicUpdateFeatureType}
	 * @throws IOException If it's not allowed/supported.
	 */
	private void validateFeatureEnabled(CyclicUpdateFeatureType featureType) throws IOException {
		if(featureType == CyclicUpdateFeatureType.batteryPower && !dependences.getCarFeatures().batteryPower) {
			throw new IOException("Battery power isn't supported, but was requested.");
		} else if(featureType == CyclicUpdateFeatureType.location && !dependences.getConfiguration().shareAnyLocationDependingFeature()) {
			throw new IOException("Location sharing isn't allowed, but was requested.");
		}
	}
}
