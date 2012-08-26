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
package to.sven.androidrccar.client.test.logic.handler;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

import to.sven.androidrccar.client.framework.IClientDependencyContainer;
import to.sven.androidrccar.client.logic.handler.AbstractClientMessageHandler;
import to.sven.androidrccar.client.logic.handler.IClientLogicHandlerFacade;
import to.sven.androidrccar.common.communication.model.GreetingMessage;
import to.sven.androidrccar.common.exception.InvalidMessageException;
import android.test.AndroidTestCase;

/**
 * This class should test the behavior of the {@link AbstractClientMessageHandler}.
 * @author sven
 *
 */
@UsesMocks({IClientLogicHandlerFacade.class, IClientDependencyContainer.class})
public class ClientMessageHandlerTest extends AndroidTestCase {

	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private IClientLogicHandlerFacade logicMock;
			
	/**
	 * Set up the {@link #logicMock}.
	 */
	@Override
	protected void setUp() {
		logicMock = AndroidMock.createStrictMock(IClientLogicHandlerFacade.class);
	}
			
	/**
	 * Tests {@link AbstractClientMessageHandler#getMessageType}.
	 */
	public void testGetMessageType() {
		AndroidMock.expect(logicMock.getDependency())
				   .andReturn(AndroidMock.createStrictMock(IClientDependencyContainer.class));
		AndroidMock.replay(logicMock);
		
		AbstractClientMessageHandler<GreetingMessage> testMessageHandler =
				new AbstractClientMessageHandler<GreetingMessage>(logicMock) {
					
					@Override
					public void handleMessage(GreetingMessage message)
							throws InvalidMessageException {
						// method stub
					}
				};
		
		assertEquals(GreetingMessage.class,
					 testMessageHandler.getMessageType());
		
		AndroidMock.verify(logicMock);
	}
	 
}
