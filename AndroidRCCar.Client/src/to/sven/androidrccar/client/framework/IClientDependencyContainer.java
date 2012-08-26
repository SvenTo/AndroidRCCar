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

import to.sven.androidrccar.client.logic.contract.IClientLogicListener;
import to.sven.androidrccar.client.logic.impl.ClientLogic;
import to.sven.androidrccar.client.service.contract.IClientConfigurationService;
import to.sven.androidrccar.client.service.contract.IVideoClientService;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.framework.IDependencyContainer;
import to.sven.androidrccar.common.model.ConnectionParameter;


/**
 * The {@link IClientDependencyContainer} contains all dependences of the {@link ClientLogic}
 * and it's dependences.
 * @author sven
 *
 */
public interface IClientDependencyContainer extends IDependencyContainer<IClientFactory, IClientLogicListener> {

	/**
	 * Returns the {@link IClientConfigurationService}
	 * @return {@link IClientConfigurationService}
	 */
	public IClientConfigurationService getConfiguration();

	/**
	 * Returns the {@link ConnectionParameter}
	 * @return {@link ConnectionParameter}
	 */
	public ConnectionParameter getConnectionParameter();

	/**
	 * Returns {@link FeatureMessage}, when received from Host.
	 * @return {@link FeatureMessage}
	 * @throws NullPointerException No {@link FeatureMessage} was set before.
	 */
	public FeatureMessage getFeatures();

	/**
	 * Sets the {@link FeatureMessage}
	 * @param message {@link FeatureMessage}
	 */
	public void setFeatures(FeatureMessage message);

	/**
	 * Returns the {@link IVideoClientService}
	 * @return {@link IVideoClientService}
	 */
	public IVideoClientService getVideoClientService();

}
