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
package to.sven.androidrccar.common.framework;

import android.content.Context;
import android.location.LocationManager;
import to.sven.androidrccar.common.communication.contract.IRemoteCommunication;
import to.sven.androidrccar.common.communication.contract.IRemoteCommunicationListener;
import to.sven.androidrccar.common.communication.impl.RemoteCommunication;
import to.sven.androidrccar.common.logic.handler.ILogicHandlerFacade;
import to.sven.androidrccar.common.logic.handler.IMessageHandler;
import to.sven.androidrccar.common.logic.impl.AbstractLogic;
import to.sven.androidrccar.common.service.contract.ILocationService;
import to.sven.androidrccar.common.service.impl.LocationService;

// TODO: Do Test Factory?

/**
 * This factory provides creators for all classes that {@link AbstractLogic} needed to create.
 * It also contains a generic creator for {@link IMessageHandler}s.
 * @author sven
 *
 */
public abstract class AbstractFactory implements IFactory {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract <THandler extends IMessageHandler<?>> THandler createMessageHandler(Class<THandler> type, ILogicHandlerFacade<?> logic)
			throws IllegalArgumentException;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRemoteCommunication createRemoteCommuncation(IDependencyContainer<?, ?> dependencyContainer, IRemoteCommunicationListener cpListener) {
		return new RemoteCommunication(dependencyContainer, cpListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILocationService createLocationService(Context context) {
		return new LocationService((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
	}
}
