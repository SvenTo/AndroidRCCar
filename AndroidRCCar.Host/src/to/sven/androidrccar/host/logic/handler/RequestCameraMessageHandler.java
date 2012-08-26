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

import to.sven.androidrccar.common.communication.model.CameraConnectionParameterMessage;
import to.sven.androidrccar.common.communication.model.RequestCameraMessage;
import to.sven.androidrccar.host.service.contract.ICameraStreamingService;

/**
 * This handler handles a received {@link RequestCameraMessageHandler}.
 * This message can be received after authentication (only if camera sharing is allowed.)
 * @author sven
 *
 */
public class RequestCameraMessageHandler extends
		AbstractHostMessageHandler<RequestCameraMessage> {

	/**
	 * Creates the {@link RequestCameraMessageHandler}.
	 * @param logic The {@link IHostLogicHandlerFacade}
	 */
	public RequestCameraMessageHandler(IHostLogicHandlerFacade logic) {
		super(logic);
	}

	/**
	 * Initializes or closes the camera streaming.
	 */
	@Override
	public void handleMessage(RequestCameraMessage message) {
		try {
			ICameraStreamingService service = dependences.getCameraStreamingService();
			
			validateProviderState(service, message.activateCamera);
			
			if(message.activateCamera) {
				service = dependences.getFactory()
									 .createCameraStreamingService();
				service.initialize(dependences.getContext());
				
				final ICameraStreamingService s = service;
				dependences.getHandler().postDelayed(new Runnable() {
					@Override
					public void run() {
						logic.sendMessage(new CameraConnectionParameterMessage(s.getPort(),
									   s.getScheme(),
									   s.getPath()));
					}
				}, 3000);

			} else if(service != null) {
				service.close();
				service = null;
			}
			
			dependences.setCameraStreamingService(service);
		} catch (IOException ex) {
			logic.handleError(ex);
		}
	}

	/**
	 * Validates that the camera shouldn't be enabled when it's enabled.
	 * @param service The {@link ICameraStreamingService}, if there is one, else null
	 * @param activateCamera Should the camera enabled?
	 * @throws IOException If the validation fails.
	 */
	private void validateProviderState(ICameraStreamingService service, boolean activateCamera) throws IOException {
		if(service != null && activateCamera) {
			throw new IOException("Can't activiate camera streaming service, when it's active.");
		}
	}

}
