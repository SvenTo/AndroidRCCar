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
package to.sven.androidrccar.host.framework;

import to.sven.androidrccar.common.framework.AbstractDependencyContainer;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryConnectorListener;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.communication.contract.ISocketConnectorListener;
import to.sven.androidrccar.host.logic.contract.IHostLogicListener;
import to.sven.androidrccar.host.logic.impl.HostLogic;
import to.sven.androidrccar.host.service.contract.ICameraStreamingService;
import to.sven.androidrccar.host.service.contract.IHostConfigurationService;
import android.content.Context;

/**
 * The {@link HostDependencyContainer} contains all dependences of the {@link HostLogic}
 * and it's dependences.
 * @author sven
 *
 */
public class HostDependencyContainer extends AbstractDependencyContainer<IHostFactory, IHostLogicListener>
	implements IHostDependencyContainer {

	/**
	 * Contains the {@link ICameraStreamingService} if it was set before.
	 */
	private ICameraStreamingService cameraStreamingProvider;
	
	/**
	 * Contains the {@link IHostConfigurationService}
	 */
	private final IHostConfigurationService hostConfigurationService;

	/**
	 * Contains the communication interface to the µController.
	 */
	private IAccessoryCommunication accessoryCommunication;

	/**
	 * Contains a {@link IAccessoryConnectorListener}.
	 */
	private final IAccessoryConnectorListener accessoryConnectorListener;

	/**
	 * Contains a {@link ISocketConnectorListener}.
	 */
	private ISocketConnectorListener socketConnectorListener;

	/**
	 * Default constructor.
	 * @param listener The one that want to know when something happen in the {@link HostLogic}.
	 * @param context {@link Context}
	 * @param accessoryConnectorListener {@link IAccessoryConnectorListener}
	 */
	public HostDependencyContainer(IHostLogicListener listener, Context context, IAccessoryConnectorListener accessoryConnectorListener) {
		super(new HostFactory(), listener, context);
		this.hostConfigurationService = getFactory().createHostConfigurationService(getContext());
		this.accessoryConnectorListener = accessoryConnectorListener;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IHostConfigurationService getConfiguration() {
		return hostConfigurationService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CarFeatures getCarFeatures() {
		return getAccessoryCommunication().getCarFeatures();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAccessoryCommunication getAccessoryCommunication() {
		return accessoryCommunication;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAccessoryCommunication(IAccessoryCommunication accessoryCommunication) {
		this.accessoryCommunication = accessoryCommunication;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICameraStreamingService getCameraStreamingService() {
		return cameraStreamingProvider;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCameraStreamingService(ICameraStreamingService service) {
		cameraStreamingProvider = service;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAccessoryConnectorListener getAccessoryConnectorListener() {
		return accessoryConnectorListener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISocketConnectorListener getSocketConnectorListener() {
		return socketConnectorListener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSocketConnectorListener(ISocketConnectorListener listener) {
		socketConnectorListener = listener;
	}
}
