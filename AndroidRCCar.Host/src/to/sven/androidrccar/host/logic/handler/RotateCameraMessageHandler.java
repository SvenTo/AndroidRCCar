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

import to.sven.androidrccar.common.communication.model.RotateCameraMessage;
import to.sven.androidrccar.common.exception.RangeException;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;

/**
 * This handler handles a received {@link RotateCameraMessageHandler}.
 * This message can be received after authentication.
 * @author sven
 *
 */
public class RotateCameraMessageHandler extends
		AbstractHostMessageHandler<RotateCameraMessage> {

	/**
	 * Creates the {@link RotateCameraMessageHandler}.
	 * @param logic The {@link IHostLogicHandlerFacade}
	 */
	public RotateCameraMessageHandler(IHostLogicHandlerFacade logic) {
		super(logic);
	}

	/**
	 * Tell {@link AccessoryCommunication} to pan &/or tilt the camera.
	 */
	@Override
	public void handleMessage(RotateCameraMessage message) {
		try {
			dependences.getAccessoryCommunication()
	   		   		   .rotateCamera(message.pan,
	   		   				   		 message.tilt);	
		} catch(RangeException ex) {
			logic.handleError(ex);
		}
	}

}
