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

import java.net.Socket;

import to.sven.androidrccar.client.logic.contract.IClientLogicListener;
import to.sven.androidrccar.client.logic.impl.ClientLogic;
import to.sven.androidrccar.client.service.contract.IClientConfigurationService;
import to.sven.androidrccar.client.service.contract.IVideoClientService;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.framework.AbstractDependencyContainer;
import to.sven.androidrccar.common.model.ConnectionParameter;
import android.content.Context;

import com.orangelabs.rcs.service.api.client.media.video.VideoSurfaceView;

/**
 * The {@link ClientDependencyContainer} contains all dependences of the {@link ClientLogic}
 * and it's dependences.
 * @author sven
 *
 */
public class ClientDependencyContainer extends AbstractDependencyContainer<IClientFactory, IClientLogicListener> implements IClientDependencyContainer {

	/**
	 * {@link FeatureMessage}, when received from Host.
	 */
	private FeatureMessage featureMessage;
	
	/**
	 * Contains the {@link IClientConfigurationService}
	 */
	private final IClientConfigurationService clientConfigurationService;
	
	/**
	 * The {@link ConnectionParameter}
	 */
	private final ConnectionParameter connectionParameter;
	
	/**
	 * The {@link IVideoClientService}
	 */
	private final IVideoClientService videoClientService;

	
	/**
	 * Default constructor.
	 * @param listener The one that want to know when something happen in the {@link ClientLogic}.
	 * @param socket Connected Socket to the Client
	 * @param context {@link Context}
	 * @param connectionParameter {@link ConnectionParameter}
	 * @param videoView The view where the video should be shown
	 */
	public ClientDependencyContainer(IClientLogicListener listener, Socket socket, Context context,
									 ConnectionParameter connectionParameter, VideoSurfaceView videoView) {
		super(new ClientFactory(), listener, context);
		setSocket(socket);
		this.clientConfigurationService = getFactory().createClientConfigurationService(context);
		this.connectionParameter = connectionParameter;
		this.videoClientService = getFactory().createVideoClientServicer(videoView);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IClientConfigurationService getConfiguration() {
		return clientConfigurationService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnectionParameter getConnectionParameter() {
		return connectionParameter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeatureMessage getFeatures() {
		if(featureMessage == null) {
			throw new NullPointerException("Features wasn't set yet!");
		}
		return featureMessage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFeatures(FeatureMessage message) {
		featureMessage = message;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IVideoClientService getVideoClientService() {
		return videoClientService;
	}
}
