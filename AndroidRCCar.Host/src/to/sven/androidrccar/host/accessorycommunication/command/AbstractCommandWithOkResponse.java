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
package to.sven.androidrccar.host.accessorycommunication.command;

import java.nio.ByteBuffer;

import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

/**
 * An abstract implementation for command with a {@link ResponseMessage#REQUEST_OK} as response.
 * @author sven
 *
 */
public abstract class AbstractCommandWithOkResponse extends AbstractCommand {

	/**
	 * Default Constructor
	 * @param commandListener The {@link ICommandListener}
	 */
	protected AbstractCommandWithOkResponse(ICommandListener commandListener) {
		super(commandListener);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ResponseMessage getExpectedResponseMessageType() {
		return ResponseMessage.REQUEST_OK;
	}

	/**
	 * All OK, do nothing.
	 */
	@Override
	protected void processExpectedResponse(ByteBuffer payload) {
		// All OK, do nothing.
	}

}
