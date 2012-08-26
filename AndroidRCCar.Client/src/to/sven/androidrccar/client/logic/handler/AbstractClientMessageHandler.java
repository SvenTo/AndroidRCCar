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

import java.lang.reflect.ParameterizedType;

import to.sven.androidrccar.client.framework.IClientDependencyContainer;
import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.logic.handler.IMessageHandler;

/**
 * Abstract implementation for all Client-{@link IMessageHandler}.
 * @author sven
 *
 * @param <T> The concrete {@link Message} type, that is handled by the implementor.
 */
public abstract class AbstractClientMessageHandler<T extends Message> implements IMessageHandler<T> {
	
	/**
	 * {@link IClientLogicHandlerFacade}
	 */
	protected final IClientLogicHandlerFacade logic;
	
	/**
	 * {@link IClientDependencyContainer}
	 */
	protected final IClientDependencyContainer dependences;

	/**
	 * Creates the {@link AbstractClientMessageHandler}.
	 * @param logic The {@link IClientLogicHandlerFacade}
	 */
	public AbstractClientMessageHandler(IClientLogicHandlerFacade logic) {
		this.logic = logic;
		this.dependences = logic.getDependency();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getMessageType() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
