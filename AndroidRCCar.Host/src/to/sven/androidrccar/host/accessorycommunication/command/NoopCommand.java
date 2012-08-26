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

import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

/**
 * Implementation for sending {@link RequestCommand#NOOP}.
 * Processes a {@link ResponseMessage#REQUEST_OK} as response.
 * 
 * @author sven
 *
 */
public class NoopCommand extends AbstractCommandWithOkResponse {

	/**
	 * Default Constructor
	 * @param commandListener The {@link ICommandListener}
	 */
	public NoopCommand(ICommandListener commandListener) {
		super(commandListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RequestCommand getRequestCommandType() {
		return RequestCommand.NOOP;
	}

	/**
	 * Command has no payload.
	 */
	@Override
	protected void fillPayload(ByteBuffer payload) {
		throw new UnsupportedOperationException();
	}
}
