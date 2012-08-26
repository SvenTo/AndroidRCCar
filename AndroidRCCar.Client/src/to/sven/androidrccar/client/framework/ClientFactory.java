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
package to.sven.androidrccar.client.framework;

import com.orangelabs.rcs.service.api.client.media.video.VideoSurfaceView;

import android.content.Context;
import to.sven.androidrccar.client.logic.handler.IClientLogicHandlerFacade;
import to.sven.androidrccar.client.logic.impl.ClientLogic;
import to.sven.androidrccar.client.service.contract.IClientConfigurationService;
import to.sven.androidrccar.client.service.contract.IVideoClientService;
import to.sven.androidrccar.client.service.impl.ClientConfigurationService;
import to.sven.androidrccar.client.service.impl.VideoClientService;
import to.sven.androidrccar.common.framework.AbstractFactory;
import to.sven.androidrccar.common.logic.handler.ILogicHandlerFacade;
import to.sven.androidrccar.common.logic.handler.IMessageHandler;

/**
 * This factory provides creators for all classes that {@link ClientLogic} needed to create.
 * @author sven
 *
 */
public class ClientFactory extends AbstractFactory implements IClientFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <THandler extends IMessageHandler<?>> THandler createMessageHandler(
			Class<THandler> type, ILogicHandlerFacade<?> logic)
		throws IllegalArgumentException {
		try {
			return type.getDeclaredConstructor(IClientLogicHandlerFacade.class).newInstance(logic); 
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create an instance of the given handler type.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IClientConfigurationService createClientConfigurationService(Context context) {
		return new ClientConfigurationService(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IVideoClientService createVideoClientServicer(VideoSurfaceView videoView) {
		return new VideoClientService(videoView);
	}
	
}
