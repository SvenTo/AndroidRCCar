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
package to.sven.androidrccar.common.logic.handler;

import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.exception.InvalidMessageException;
import to.sven.androidrccar.common.logic.impl.AbstractLogic;

/**
 * A {@code IMessageHandler} handles received messages to a concrete {@link Message} type.  
 * 
 * Note: The implementors of this interface must have an constructor
 * that accept an implementor of {@link AbstractLogic} as only parameter.
 * @author sven
 *
 * @param <T> The concrete {@link Message} type, that is handled by the implementor.
 */
public interface IMessageHandler<T extends Message> {
	
	/**
	 * Get the {@link Class} of the concrete {@link Message} type.
	 * @return {@link Class}
	 */
	Class<T> getMessageType();
	
	/**
	 * Handle a received message.
	 * @param message The received message.
	 * @throws InvalidMessageException If the message have invalid data.
	 */
	void handleMessage(T message) throws InvalidMessageException;
}
