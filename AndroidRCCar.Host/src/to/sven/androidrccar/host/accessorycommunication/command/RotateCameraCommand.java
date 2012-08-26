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

import to.sven.androidrccar.common.communication.model.RotateCameraMessage;
import to.sven.androidrccar.common.exception.RangeException;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

/**
 * Implementation for sending {@link RequestCommand#ROTATE_CAM}.
 * Processes a {@link ResponseMessage#REQUEST_OK} as response.
 * 
 * @author sven
 *
 */
public class RotateCameraCommand extends AbstractCommandWithOkResponse {

	/**
	 * @see RotateCameraMessage#pan
	 */
	private float pan;
	
	/**
	 * @see RotateCameraMessage#tilt
	 */
	private float tilt;
	
	/**
	 * Default Constructor
	 * @param commandListener The {@link ICommandListener}
	 * @param pan {@link RotateCameraMessage#pan}
	 * @param tilt @link {@link RotateCameraMessage#tilt}
	 */
	public RotateCameraCommand(ICommandListener commandListener, float pan, float tilt) {
		super(commandListener);
		
		this.pan = pan;
		this.tilt = tilt;
		
		validateInputs();
	}
	
	/**
	 * Validates passed arguments in the constructor.
	 */
	private void validateInputs() {
		CarFeatures features = commandListener.getCarFeatures();
		if(pan > features.cameraPanMax || pan < -1*features.cameraPanMin) {
			throw new RangeException("pan", pan);
		}
		if(tilt > features.cameraTiltMax || tilt < -1*features.cameraTiltMin) {
			throw new RangeException("tilt", tilt);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RequestCommand getRequestCommandType() {
		return RequestCommand.ROTATE_CAM;
	}

	/**
	 * Fills {@link #pan} and {@link #tilt} into {@code payload}.
	 */
	@Override
	protected void fillPayload(ByteBuffer payload) {
		CarFeatures features = commandListener.getCarFeatures();
		
		short panShort;
		if(pan >= 0) {
			panShort = (short) (pan/features.cameraPanMax*Short.MAX_VALUE);
		} else {
			panShort = (short) (-1*pan/features.cameraPanMin*Short.MIN_VALUE);
		}
		
		short tiltShort;
		if(tilt >= 0) {
			tiltShort = (short) (tilt/features.cameraTiltMax*Short.MAX_VALUE);
		} else {
			tiltShort = (short) (-1*tilt/features.cameraTiltMin*Short.MIN_VALUE);
		}
		
		payload.putShort(panShort);
		payload.putShort(tiltShort);
	}

}
