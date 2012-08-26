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

import java.net.Socket;

import to.sven.androidrccar.common.communication.contract.IRemoteCommunication;
import to.sven.androidrccar.common.communication.contract.IRemoteCommunicationListener;
import to.sven.androidrccar.common.communication.impl.RemoteCommunication;
import to.sven.androidrccar.common.logic.handler.ILogicHandlerFacade;
import to.sven.androidrccar.common.logic.handler.IMessageHandler;
import to.sven.androidrccar.common.logic.impl.AbstractLogic;
import to.sven.androidrccar.common.service.contract.ILocationService;
import to.sven.androidrccar.common.service.impl.LocationService;
import android.content.Context;
import android.os.Handler;

/**
 * This factory provides creators for all classes that {@link AbstractLogic} needed to create.
 * It also contains a generic creator for {@link IMessageHandler}s.
 * @author sven
 *
 */
public interface IFactory {

	/**
	 * Creates an instance of the the given {@link Class}.
	 * The given {@link Class} needs to haven an constructor,
	 * where the given {@code logic} can passed.
	 * @param <THandler> An concrete {@link IMessageHandler} that should created.
	 * @param type The {@link Class} of the {@link IMessageHandler} 
	 * @param logic The logic that passed to the constructor of the {@link IMessageHandler}
	 * @return The created instance
	 * @throws IllegalArgumentException Thrown if the instantiation failed.
	 */
	public abstract <THandler extends IMessageHandler<?>> THandler createMessageHandler(
			Class<THandler> type, ILogicHandlerFacade<?> logic)
			throws IllegalArgumentException;

	/**
	 * Creates a new instance of {@link RemoteCommunication}.
	 * 
	 * @param dependencyContainer Needed to get the {@link Socket} and the {@link Handler}.
	 * @param cpListener {@link IRemoteCommunicationListener}
	 * @return Created instance as {@link IRemoteCommunication}.
	 */
	public abstract IRemoteCommunication createRemoteCommuncation(
			IDependencyContainer<?, ?> dependencyContainer,
			IRemoteCommunicationListener cpListener);

	
	/**
	 * Creates a new instance of {@link LocationService}.
	 * @param context {@link Context} of the application.
	 * @return Created instance as {@link ILocationService}
	 */
	public ILocationService createLocationService(Context context);
}
