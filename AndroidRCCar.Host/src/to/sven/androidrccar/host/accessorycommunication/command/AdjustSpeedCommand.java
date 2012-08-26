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

import to.sven.androidrccar.common.communication.model.AdjustSpeedMessage;
import to.sven.androidrccar.common.exception.RangeException;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

/**
 * Implementation for sending {@link RequestCommand#ADJUST_SPEED}.
 * Processes a {@link ResponseMessage#REQUEST_OK} as response.
 * 
 * @author sven
 *
 */
public class AdjustSpeedCommand extends AbstractCommandWithOkResponse {

	/**
	 * @see AdjustSpeedMessage#speed
	 */
	private final float speed;

	/**
	 * Default Constructor
	 * @param commandListener The {@link ICommandListener}
	 * @param speed {@link AdjustSpeedMessage#speed}
	 */
	public AdjustSpeedCommand(ICommandListener commandListener, float speed) {
		super(commandListener);
		this.speed = speed;
		
		validateInputs();
	}
	
	/**
	 * Validates passed arguments in the constructor.
	 */
	private void validateInputs() {
		boolean backward = commandListener.getCarFeatures().driveBackward;
		float min = backward?-1f:0;
		
		if(speed > 1 || speed < min) {
			throw new RangeException("speed", speed);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RequestCommand getRequestCommandType() {
		return RequestCommand.ADJUST_SPEED;
	}

	/**
	 * Fills {@link #speed} into {@code payload}.
	 */
	@Override
	protected void fillPayload(ByteBuffer payload) {
		short speedInt = (short) (speed*Short.MAX_VALUE);
		payload.putShort(speedInt);
	}

}
