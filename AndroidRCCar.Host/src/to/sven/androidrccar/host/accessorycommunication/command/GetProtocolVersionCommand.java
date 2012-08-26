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

import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

/**
 * Implementation for sending {@link RequestCommand#GET_PROTOCOL_VERSION}.
 * Processes a {@link ResponseMessage#PROTOCOL_VERSION} as response.
 * 
 * @author sven
 *
 */
public class GetProtocolVersionCommand extends AbstractCommand {

	/**
	 * Default Constructor
	 * @param commandListener The {@link ICommandListener}
	 */
	public GetProtocolVersionCommand(ICommandListener commandListener) {
		super(commandListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RequestCommand getRequestCommandType() {
		return RequestCommand.GET_PROTOCOL_VERSION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ResponseMessage getExpectedResponseMessageType() {
		return ResponseMessage.PROTOCOL_VERSION;
	}

	/**
	 * Command has no payload.
	 */
	@Override
	protected void fillPayload(ByteBuffer payload) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sends a {@link GetFeaturesCommand} if protocol version match,
	 * else the {@link ICommandListener} will be informed about that problem. 
	 */
	@Override
	protected void processExpectedResponse(ByteBuffer payload) {
		short protocolVersion = payload.getShort();
		
		if(protocolVersion != AccessoryCommunication.PROTOCOL_VERSION) {
			commandListener.protocolVersionNotMatch(AccessoryCommunication.PROTOCOL_VERSION, protocolVersion);
		} else {
			commandListener.postCommand(new GetFeaturesCommand(commandListener));
		}
	}

}
