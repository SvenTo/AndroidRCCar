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
package to.sven.androidrccar.host.accessorycommunication.command;

import java.nio.ByteBuffer;

import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

/**
 * Implementation for sending {@link RequestCommand#GET_FEATURES}.
 * Processes a {@link ResponseMessage#FEATURES} as response.
 * 
 * @author sven
 *
 */
public class GetFeaturesCommand extends AbstractCommand {

	/**
	 * Default Constructor
	 * @param commandListener The {@link ICommandListener}
	 */
	public GetFeaturesCommand(ICommandListener commandListener) {
		super(commandListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RequestCommand getRequestCommandType() {
		return RequestCommand.GET_FEATURES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ResponseMessage getExpectedResponseMessageType() {
		return ResponseMessage.FEATURES;
	}

	/**
	 * Command has no payload.
	 */
	@Override
	protected void fillPayload(ByteBuffer payload) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Converts the Payload into a {@link CarFeatures}
	 * and tells the listener that the connection is initialized.
	 */
	@Override
	protected void processExpectedResponse(ByteBuffer payload) {
		byte flags = payload.get();
		boolean adjustableSpeed = checkBit(flags, (byte)1);
		boolean driveBackward =   checkBit(flags, (byte)2);
		boolean batteryPower = 	  checkBit(flags, (byte)4);
		float cameraPanMin = rangeConvert(payload.getShort(), 180);
		float cameraPanMax = rangeConvert(payload.getShort(), 180);
		float cameraTiltMin = rangeConvert(payload.getShort(), 180);
		float cameraTiltMax = rangeConvert(payload.getShort(), 180);
		
		CarFeatures features = new CarFeatures(cameraPanMin, cameraPanMax,
											   cameraTiltMin, cameraTiltMax,
											   adjustableSpeed, driveBackward, batteryPower);
		commandListener.setCarFeatures(features);
		commandListener.connectionInitialized();
	}
	
	// TODO: Least significant bit? -> Comment also in ResponseCommand
	/**
	 * Checks if the {@code bit} is set in in {@code flags}.
	 * @param flags Where the bit should checked.
	 * @param bit 2^n where n is bit 0-7
	 * @return True, if the bit is set.
	 */
	private boolean checkBit(byte flags, byte bit) {
		return (flags & bit) > 0;
	}
}
