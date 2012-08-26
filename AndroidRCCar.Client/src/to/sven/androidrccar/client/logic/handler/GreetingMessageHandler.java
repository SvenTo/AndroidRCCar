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

import to.sven.androidrccar.client.logic.impl.ClientLogic;
import to.sven.androidrccar.common.communication.model.AuthenticationMessage;
import to.sven.androidrccar.common.communication.model.GreetingMessage;
import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.exception.InvalidMessageException;
import to.sven.androidrccar.common.utils.CryptUtils;

/**
 * This handler handles a received {@link GreetingMessage}.
 * This message is first message the client expects.
 * @author sven
 *
 */
public class GreetingMessageHandler extends AbstractClientMessageHandler<GreetingMessage> {

	/**
	 * Creates the {@link FeatureMessageHandler}.
	 * @param logic The {@link ClientLogic}
	 */
	public GreetingMessageHandler(IClientLogicHandlerFacade logic) {
		super(logic);
	}

	/**
	 * Send password if {@link #validateProtocolVersion(GreetingMessage)} 
	 * is successful.
	 */
	@Override
	public void handleMessage(GreetingMessage message) throws InvalidMessageException {
		validateProtocolVersion(message);
		
		logic.clearMessageHandlers();
		
		logic.registerMessageHandler(FeatureMessageHandler.class);
		logic.registerMessageHandler(AuthenticationFailedMessageHandler.class);
		
		String password = dependences.getConnectionParameter()
						   			 .password;
		String passwordHash = CryptUtils.encryptPassword(password, message.authSalt);
		logic.sendMessage(new AuthenticationMessage(passwordHash));
	}
	
	/**
	 * Validates that the {@link Message#PROTOCOL_VERSION} match with remote version.
	 * @param message {@link GreetingMessage} send from host.
	 * @throws InvalidMessageException If protocol versions don't match.
	 */
	private void validateProtocolVersion(GreetingMessage message)
		throws InvalidMessageException {
		if(Message.PROTOCOL_VERSION != message.version) {
			String msg = String.format("Unsupported Protocol Version (Host Version: {0}; Client Version: {1}).", message.version, Message.PROTOCOL_VERSION);
			throw new InvalidMessageException(msg);
		}
	}

}
