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

import to.sven.androidrccar.client.logic.contract.IClientLogicListener;
import to.sven.androidrccar.common.communication.model.AuthenticationFailedMessage;
import to.sven.androidrccar.common.communication.model.AuthenticationMessage;

/**
 * This handler handles a received {@link AuthenticationFailedMessage}.
 * This message is expected after sending the {@link AuthenticationMessage} 
 * when the authentication failed (Wrong password as example).
 * @author sven
 *
 */
public class AuthenticationFailedMessageHandler
	extends AbstractClientMessageHandler<AuthenticationFailedMessage> {
	
	/**
	 * Creates the {@link AuthenticationFailedMessageHandler}.
	 * @param logic {@link IClientLogicHandlerFacade}
	 */
	public AuthenticationFailedMessageHandler(IClientLogicHandlerFacade logic) {
		super(logic);
	}
	
	/**
	 * Informs the {@link IClientLogicListener} that the authtification failed and closes the connection.
	 */
	@Override
	public void handleMessage(AuthenticationFailedMessage message) {
		logic.clearMessageHandlers();
		
		dependences.getLogicListener()
	     		   .authenticationFalied(message.reason);
		
		logic.close();
	}
}
