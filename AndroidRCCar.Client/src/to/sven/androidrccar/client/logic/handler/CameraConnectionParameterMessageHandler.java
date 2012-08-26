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

import to.sven.androidrccar.client.service.contract.IVideoClientService;
import to.sven.androidrccar.common.communication.model.CameraConnectionParameterMessage;

/**
 * This handler handles a received {@link CameraConnectionParameterMessage}.
 * @author sven
 *
 */
public class CameraConnectionParameterMessageHandler extends
		AbstractClientMessageHandler<CameraConnectionParameterMessage> {

	/**
	 * Creates the {@link CameraConnectionParameterMessageHandler}.
	 * @param logic {@link IClientLogicHandlerFacade}
	 */
	public CameraConnectionParameterMessageHandler(IClientLogicHandlerFacade logic) {
		super(logic);
	}

	/**
	 * Tells the {@link IVideoClientService} to start playing the video stream
	 * from the URL given in {@link CameraConnectionParameterMessage}.
	 * @param message A {@link CameraConnectionParameterMessage}
	 */
	@Override
	public void handleMessage(CameraConnectionParameterMessage message) {
		String host = dependences.getConnectionParameter()
					   			 .host;
		
		dependences.getVideoClientService()
		 		   .playStream(message.toURL(host));
		
		// TODO: Remove this handler, because it's not needed anymore?
	}
	
}
