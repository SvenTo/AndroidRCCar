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
package to.sven.androidrccar.host.logic.impl;

import java.io.IOException;

import to.sven.androidrccar.common.communication.model.BatteryPowerMessage;
import to.sven.androidrccar.common.communication.model.GreetingMessage;
import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.logic.impl.AbstractLogic;
import to.sven.androidrccar.common.utils.CryptUtils;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunicationListener;
import to.sven.androidrccar.host.accessorycommunication.exception.AccessoryConnectionProblemException;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;
import to.sven.androidrccar.host.framework.IHostDependencyContainer;
import to.sven.androidrccar.host.logic.contract.IHostLogicListener;
import to.sven.androidrccar.host.logic.handler.AuthenticationMessageHandler;
import to.sven.androidrccar.host.logic.handler.IHostLogicHandlerFacade;
import to.sven.androidrccar.host.service.contract.ICameraStreamingService;

/**
 * Logic for the Host:
 * It is the "missing" part between the {@link AccessoryCommunication}
 * (, the other services: location and camera on the host) and the client.
 * So it handles the communication between all parts of the host.
 * @author sven
 *
 */
public class HostLogic extends AbstractLogic<IHostDependencyContainer, IHostLogicListener>
	implements IHostLogicHandlerFacade, IAccessoryCommunicationListener {
	
	/**
	 * {@link GreetingMessage#authSalt}
	 */
	protected final String salt;
	
	/**
	 * {@link BatteryPowerCyclicTask}, if needed.
	 */
	private ICyclicTask batteryPowerCyclicTask;
	
	/**
	 * {@link LocationCyclicTask}, if needed.
	 */
	private ICyclicTask locationCyclicTask;
	
	/**
	 * Creates the {@link HostLogic}.
	 * @param container A {@link IHostDependencyContainer} that provides the dependences.
	 */
	public HostLogic(IHostDependencyContainer container) {
		super(container);
		salt = CryptUtils.generateSalt();
		if(container.getCarFeatures().batteryPower) {
			batteryPowerCyclicTask = new BatteryPowerCyclicTask(container.getAccessoryCommunication(), container.getHandler());
		}
		if(container.getConfiguration().shareAnyLocationDependingFeature()) {
			locationCyclicTask = new LocationCyclicTask(this);
		}
		
		getDependency().getAccessoryCommunication()
					   .setListener(this);
		
		registerMessageHandler(AuthenticationMessageHandler.class);
		
		initialized();
		
		sendMessage(new GreetingMessage(Message.PROTOCOL_VERSION, salt));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSalt() {
		return salt;
	}
	
	/**
	 * Close connection to client.
	 * Close {@link ICameraStreamingService} if open.
	 * {@link IAccessoryCommunication} won't closed here,
	 * because it could maybe reused.
	 */
	@Override
	public void close() {
		super.close();
		
		IAccessoryCommunication acc = getDependency().getAccessoryCommunication();
		acc.adjustSpeed(0);
		acc.turnCar(0);
		
		getDependency().getAccessoryCommunication().rotateCamera(0, 0);
		
		ICameraStreamingService service = getDependency().getCameraStreamingService();
		if(service != null) {
			service.close();
			getDependency().setCameraStreamingService(null);
		}
		if(batteryPowerCyclicTask != null) {
			batteryPowerCyclicTask.stop();
		}
		if(locationCyclicTask != null) {
			locationCyclicTask.stop();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICyclicTask getBatteryPowerCyclicTask() {
		return batteryPowerCyclicTask;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICyclicTask getLocationCyclicTask() {
		return locationCyclicTask;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void protocolVersionNotMatch(short hostVersion,
										short microControllerVersion) {
		throw new IllegalStateException("The µController-connection should allready initialized.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connectionInitialized() {
		throw new IllegalStateException("The µController-connection should allready initialized.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void batteryStateReceived(float chargingLevel) {
		sendMessage(new BatteryPowerMessage(chargingLevel));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void batteryNearEmpty() {
		handleError(new IOException("Battery of µController is near empty."));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void errorReceived(String message) {
		handleError(new IOException("Error received from µController: " + message));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connectionProblem(AccessoryConnectionProblemException ex) {
		handleError(ex);
	}
}
