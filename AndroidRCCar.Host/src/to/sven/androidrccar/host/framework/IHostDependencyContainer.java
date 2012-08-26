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

import to.sven.androidrccar.common.framework.IDependencyContainer;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryConnectorListener;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.communication.contract.ISocketConnectorListener;
import to.sven.androidrccar.host.logic.contract.IHostLogicListener;
import to.sven.androidrccar.host.logic.impl.HostLogic;
import to.sven.androidrccar.host.service.contract.ICameraStreamingService;
import to.sven.androidrccar.host.service.contract.IHostConfigurationService;

/**
 * The {@link IHostDependencyContainer} contains all dependences of the {@link HostLogic}
 * and it's dependences.
 * @author sven
 *
 */
public interface IHostDependencyContainer extends IDependencyContainer<IHostFactory, IHostLogicListener> {

	/**
	 * Returns the {@link IHostConfigurationService}
	 * @return {@link IHostConfigurationService}
	 */
	IHostConfigurationService getConfiguration();

	/**
	 * Returns {@link CarFeatures}
	 * @return {@link CarFeatures}
	 */
	CarFeatures getCarFeatures();

	/**
	 * Returns an fully initialized {@link IAccessoryCommunication}
	 * @return {@link IAccessoryCommunication}
	 */
	IAccessoryCommunication getAccessoryCommunication();

	/**
	 * Sets the fully initialized {@link IAccessoryCommunication}
	 * @param accessoryCommunication {@link IAccessoryCommunication}
	 */
	void setAccessoryCommunication(IAccessoryCommunication accessoryCommunication);

	/**
	 * Returns {@link ICameraStreamingService} if one was set before, else null.
	 * @return {@link ICameraStreamingService} or null.
	 */
	ICameraStreamingService getCameraStreamingService();

	/**
	 * Sets the {@link ICameraStreamingService}.
	 * @param service {@link ICameraStreamingService}
	 */
	void setCameraStreamingService(ICameraStreamingService service);
	
	/**
	 * Gets the {@link IAccessoryConnectorListener}.
	 * @return {@link IAccessoryConnectorListener}
	 */
	IAccessoryConnectorListener getAccessoryConnectorListener();
	
	/**
	 * Gets the {@link ISocketConnectorListener}.
	 * @return {@link ISocketConnectorListener}
	 */
	ISocketConnectorListener getSocketConnectorListener();
	
	/**
	 * Sets the {@link ISocketConnectorListener}.
	 * @param listener {@link ISocketConnectorListener}
	 */
	void setSocketConnectorListener(ISocketConnectorListener listener);
}
