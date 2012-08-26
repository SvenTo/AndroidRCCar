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

import org.easymock.Capture;

import to.sven.androidrccar.client.logic.handler.AuthenticationFailedMessageHandler;
import to.sven.androidrccar.client.logic.handler.FeatureMessageHandler;
import to.sven.androidrccar.client.logic.handler.GreetingMessageHandler;
import to.sven.androidrccar.common.model.ConnectionParameter;
import to.sven.androidrccar.common.communication.model.AuthenticationMessage;
import to.sven.androidrccar.common.communication.model.GreetingMessage;
import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.exception.InvalidMessageException;

import com.google.android.testing.mocking.AndroidMock;

/**
 * This class should test the behavior of the {@link GreetingMessageHandler}.
 * @author sven
 *
 */
public class GreetingMessageHandlerTest
	extends AbstractTestCaseForClientMessageHandler<GreetingMessageHandler> {
	
	/**
	 * @see GreetingMessage#authSalt
	 */
	private final static String SALT = "deadbeef42codeba5e42deadcode42deafbeef42";
	
	/**
	 * @see Message#PROTOCOL_VERSION
	 */
	private final int pVersion = Message.PROTOCOL_VERSION;
	
	/**
	 * Tests {@link GreetingMessageHandler#handleMessage}:
	 * Checks if the password is send and
	 * the correct handlers are registered.
	 * @throws InvalidMessageException The test fails.
	 */
	public void testHandleMessage_Success() throws InvalidMessageException {
		// Configure Test
		logicMock.clearMessageHandlers();
		logicMock.registerMessageHandler(AndroidMock.eq(FeatureMessageHandler.class));
		logicMock.registerMessageHandler(AndroidMock.eq(AuthenticationFailedMessageHandler.class));
		AndroidMock.expect(cdcMock.getConnectionParameter())
				   .andReturn(new ConnectionParameter(null, 0, "somePassword"));
		Capture<AuthenticationMessage> capture = new Capture<AuthenticationMessage>();
		logicMock.sendMessage(AndroidMock.capture(capture));
		
		// Run Test
		GreetingMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new GreetingMessage(pVersion, SALT));
		
		// Verify Test
		assertEquals("4ca1d689a13a83cc73de252d1ff67fbb1daa6e3c", capture.getValue().passwordHash);
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link GreetingMessageHandler#handleMessage}:
	 * A {@link InvalidMessageException} should be thrown,
	 * if {@link Message#PROTOCOL_VERSION} doesn't match.
	 */
	public void testHandleMessage_UnsupportedProtocolVersion() {
		// Configure Test
		// No mock calls expected, because handleMessage() Fails
		
		// Run Test
		GreetingMessageHandler handler = aHandlerForTest();
		checkUnsupportedProtocolVersion(handler, -1);
		checkUnsupportedProtocolVersion(handler, 1);
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Checks if a {@link InvalidMessageException} is thrown,
	 * when the message contains a different {@link Message#PROTOCOL_VERSION}.
	 * @param handler Test Object
	 * @param diffToCurrentVersion The difference to the current {@link Message#PROTOCOL_VERSION}
	 */
	private void checkUnsupportedProtocolVersion(GreetingMessageHandler handler, 
												 int diffToCurrentVersion) {
		try {
			handler.handleMessage(new GreetingMessage(pVersion + diffToCurrentVersion, SALT));
		} catch(InvalidMessageException e) {
			String expected = String.format("Unsupported Protocol Version (Host Version: {0}; Client Version: {1}).",
											pVersion +1, pVersion);
			assertEquals(expected, e.getMessage());
		}
	}
}

