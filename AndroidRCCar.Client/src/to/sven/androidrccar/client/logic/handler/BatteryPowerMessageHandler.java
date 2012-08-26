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
import to.sven.androidrccar.client.logic.impl.ClientLogic;
import to.sven.androidrccar.common.communication.model.BatteryPowerMessage;
import to.sven.androidrccar.common.exception.InvalidMessageException;

/**
 * This handler handles a received {@link BatteryPowerMessageHandler}.
 * @author sven
 *
 */
public class BatteryPowerMessageHandler extends
		AbstractClientMessageHandler<BatteryPowerMessage> {

	/**
	 * Creates the {@link BatteryPowerMessageHandler}.
	 * @param logic The {@link ClientLogic}
	 */
	public BatteryPowerMessageHandler(IClientLogicHandlerFacade logic) {
		super(logic);
	}

	/**
	 * Tell the {@link IClientLogicListener} to change the battery charging level.
	 */
	@Override
	public void handleMessage(BatteryPowerMessage message)
			throws InvalidMessageException {
		validateParameters(message);
		// TODO: Validate?
		dependences.getLogicListener()
		 		   .setBatteryPower(message.chargingLevel); // TODO: Auf dem Host den charging level merken und nur bei Bedarf übertragen.
	}
	
	/**
	 * Checks if the {@link BatteryPowerMessage#chargingLevel} is between 0 and 100.
	 * @param message The Message to check.
	 * @throws InvalidMessageException If the check failed.
	 */
	public void validateParameters(BatteryPowerMessage message)
			throws InvalidMessageException {
		if(message.chargingLevel < 0 ||
		   message.chargingLevel > 100) {
			throw new InvalidMessageException(message, "chargingLevel", message.chargingLevel);
		}
	}
}
