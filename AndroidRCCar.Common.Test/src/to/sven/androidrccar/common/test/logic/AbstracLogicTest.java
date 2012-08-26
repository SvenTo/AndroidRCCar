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
package to.sven.androidrccar.common.test.logic;

import java.io.IOException;
import java.net.Socket;

import to.sven.androidrccar.common.communication.contract.IRemoteCommunication;
import to.sven.androidrccar.common.communication.contract.IRemoteCommunicationListener;
import to.sven.androidrccar.common.communication.impl.RemoteCommunication;
import to.sven.androidrccar.common.communication.test.model.AlternativeTestMessage;
import to.sven.androidrccar.common.communication.test.model.TestMessage;
import to.sven.androidrccar.common.exception.ConnectionProblemException;
import to.sven.androidrccar.common.exception.InvalidMessageException;
import to.sven.androidrccar.common.framework.IFactory;
import to.sven.androidrccar.common.logic.contract.ILogicListener;
import to.sven.androidrccar.common.logic.impl.AbstractLogic;
import to.sven.androidrccar.common.logic.test.AlternativeTestMessageHandler;
import to.sven.androidrccar.common.logic.test.TestMessageHandler;
import android.test.AndroidTestCase;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of the {@link AbstractLogic}.
 * 
 * Notes: initialized() is implicit tested by any of this tests.
 * @author sven
 */
@UsesMocks({IFactory.class, ILogicListener.class, RemoteCommunication.class,
			Socket.class, IRemoteCommunicationListener.class})
public class AbstracLogicTest extends AndroidTestCase {

	/**
	 * The first message to send/receive in test.
	 */
	private final TestMessage testMessage1 = new TestMessage("Test Inhalt 1");
	
	/**
	 * The second message to send/receive in test.
	 */
	private final AlternativeTestMessage testMessage2 = new AlternativeTestMessage(42);
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private IFactory factoryMock;
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private ILogicListener listenerMock;

	/**
	 * {@link TestDependencyContainer}
	 */
	private TestDependencyContainer testContainer;

	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private IRemoteCommunication rcMock;
	
	/**
	 * Before Test:
	 * Set up new {@link ILogicListener} and {@link RemoteCommunication} mocks. 
	 * @see AndroidMock#createStrictMock
	 */
	@Override
	protected void setUp() {
		testContainer = new TestDependencyContainer();
		factoryMock = testContainer.getFactory();
		listenerMock = testContainer.getLogicListener();
		rcMock = AndroidMock.createStrictMock(IRemoteCommunication.class);
		
		AndroidMock.expect(factoryMock.createRemoteCommuncation(AndroidMock.same(testContainer),
																AndroidMock.isA(IRemoteCommunicationListener.class)))
				   .andReturn(rcMock);
		rcMock.startMessageListener();
	}
	
	/**
	 * Sets the mocks in replay phase and create a mock injected {@link LogicMock}
	 * @see AndroidMock#replay
	 * @return mock injected {@link AbstracLogicTest}
	 */
	private LogicMock aAbstracLogicForTest() {
		AndroidMock.replay(listenerMock, rcMock, factoryMock);
		
		return new LogicMock(testContainer);
	}
	
	/**
	 * Verifies all mock that created by {@link #setUp()}
	 */
	private void verifyFieldMocks() {
		AndroidMock.verify(listenerMock, rcMock, factoryMock);
	}
	
	/******************************************
	 * testSendMessage
	 ******************************************/
	
	/**
	 * Tests {@link LogicMock#sendMessage}.
	 * Sends two messages.
	 * @throws IOException The Test fails.
	 */
	public void testSendMessage() throws IOException {
		// Configure Test
		rcMock.sendMessage(testMessage1);
		rcMock.sendMessage(testMessage2);
		
		// Run Test
		LogicMock logic = aAbstracLogicForTest();
		logic.sendMessage(testMessage1);
		logic.sendMessage(testMessage2);
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link LogicMock#sendMessage}
	 * Simulates a IOException thrown by {@link RemoteCommunication#sendMessage}.
	 * @throws IOException The Test fails.
	 */
	public void testSendMessage_ioException() throws IOException {
		// Configure Test
		rcMock.sendMessage(testMessage1);
		AndroidMock.expectLastCall().andThrow(new IOException());
		rcMock.close();
		listenerMock.connectionLost(AndroidMock.isA(ConnectionProblemException.class));
		
		// Run Test
		LogicMock logic = aAbstracLogicForTest();
		logic.sendMessage(testMessage1);
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/******************************************
	 * more tests:
	 ******************************************/
	
	/**
	 * Tests {@link LogicMock#messageReceived}
	 * and {@link LogicMock#registerMessageHandler}
	 * @throws InvalidMessageException The Test fails.
	 */
	@UsesMocks({ TestMessageHandler.class, AlternativeTestMessageHandler.class })
	public void testMessageReceived() throws InvalidMessageException {
		// Configure Test
		TestMessageHandler m1HandlerMock = AndroidMock.createStrictMock(TestMessageHandler.class);
		AlternativeTestMessageHandler m2HandlerMock = AndroidMock.createStrictMock(AlternativeTestMessageHandler.class);
		AndroidMock.expect(factoryMock.createMessageHandler(AndroidMock.eq(TestMessageHandler.class),
															AndroidMock.isA(LogicMock.class)))
				   .andReturn(m1HandlerMock);
		AndroidMock.expect(m1HandlerMock.getMessageType())
				   .andReturn(TestMessage.class);
		AndroidMock.expect(factoryMock.createMessageHandler(AndroidMock.eq(AlternativeTestMessageHandler.class),
															AndroidMock.isA(LogicMock.class)))
				   .andReturn(m2HandlerMock);
		AndroidMock.expect(m2HandlerMock.getMessageType())
				   .andReturn(AlternativeTestMessage.class);
		m2HandlerMock.handleMessage(testMessage2);
		m1HandlerMock.handleMessage(testMessage1);
		
		// Run Test
		AndroidMock.replay(m1HandlerMock, m2HandlerMock);
		LogicMock logic = aAbstracLogicForTest();
		logic.registerMessageHandler(TestMessageHandler.class);
		logic.registerMessageHandler(AlternativeTestMessageHandler.class);
		logic.messageReceived(testMessage2);
		logic.messageReceived(testMessage1);
		
		// Verify Test
		verifyFieldMocks();
		AndroidMock.verify(m1HandlerMock, m2HandlerMock);
	}
	
	/**
	 * Tests {@link LogicMock#messageReceived}
	 * and {@link LogicMock#handleUnsupportedMessage}
	 * and {@link LogicMock#handleError}
	 * @throws IOException The Test fails.
	 */
	@UsesMocks({ TestMessageHandler.class })
	public void testHandleUnsupportedMessage() throws IOException {
		// Configure Test
		TestMessageHandler m1HandlerMock = AndroidMock.createStrictMock(TestMessageHandler.class);
		AndroidMock.expect(m1HandlerMock.getMessageType()).andReturn(TestMessage.class);
		AndroidMock.expect(factoryMock.createMessageHandler(AndroidMock.eq(TestMessageHandler.class),
				   											AndroidMock.isA(LogicMock.class)))
	   			   .andReturn(m1HandlerMock);
		m1HandlerMock.handleMessage(testMessage1);
		rcMock.close();
		listenerMock.connectionLost(AndroidMock.isA(ConnectionProblemException.class));
		
		// Run Test
		AndroidMock.replay(m1HandlerMock);
		LogicMock logic = aAbstracLogicForTest();
		logic.registerMessageHandler(TestMessageHandler.class);
		logic.messageReceived(testMessage1);
		logic.messageReceived(testMessage2);
		
		// Verify Test
		verifyFieldMocks();
		AndroidMock.verify(m1HandlerMock);
	}
	
	/**
	 * Tests {@link LogicMock#clearMessageHandlers}
	 * @throws IOException The Test fails.
	 */
	@UsesMocks({ TestMessageHandler.class })
	public void testClearMessageHandlers() throws IOException {
		// Configure Test
		TestMessageHandler m1HandlerMock = AndroidMock.createStrictMock(TestMessageHandler.class);
		AndroidMock.expect(factoryMock.createMessageHandler(AndroidMock.eq(TestMessageHandler.class),
						   									AndroidMock.isA(LogicMock.class)))
				   .andReturn(m1HandlerMock);
		AndroidMock.expect(m1HandlerMock.getMessageType()).andReturn(TestMessage.class);
		rcMock.close();
		listenerMock.connectionLost(AndroidMock.isA(ConnectionProblemException.class));
		
		// Run Test
		AndroidMock.replay(m1HandlerMock);
		LogicMock logic = aAbstracLogicForTest();
		logic.registerMessageHandler(TestMessageHandler.class);
		logic.clearMessageHandlers();
		logic.messageReceived(testMessage1);
		
		// Verify Test
		verifyFieldMocks();
		AndroidMock.verify(m1HandlerMock);
	}
	
	/**
	 * Tests {@link LogicMock#connectionProblem}
	 * and {@link LogicMock#handleError}
	 * @throws IOException The Test fails.
	 */
	public void testConnectionProblem() throws IOException {
		// Configure Test
		rcMock.close();
		listenerMock.connectionLost(AndroidMock.isA(ConnectionProblemException.class));
		
		// Run Test
		LogicMock logic = aAbstracLogicForTest();
		logic.connectionProblem(new IOException());
		
		// Verify Test
		verifyFieldMocks();
	}
}
