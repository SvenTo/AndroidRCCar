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
package to.sven.androidrccar.common.test.communication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import junit.framework.Assert;

import org.easymock.IAnswer;

import to.sven.androidrccar.common.communication.contract.IRemoteCommunication;
import to.sven.androidrccar.common.communication.contract.IRemoteCommunicationListener;
import to.sven.androidrccar.common.communication.impl.RemoteCommunication;
import to.sven.androidrccar.common.communication.test.model.AlternativeTestMessage;
import to.sven.androidrccar.common.communication.test.model.TestMessage;
import to.sven.androidrccar.common.test.logic.TestDependencyContainer;
import android.test.AndroidTestCase;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of the {@link RemoteCommunication} and it's @{link MessageListenerThread}.
 * @author sven
 */
@UsesMocks({ Socket.class, IRemoteCommunicationListener.class })
public class RemoteCommunicationTest extends AndroidTestCase {

	/**
	 * The first message to send/receive in test.
	 */
	private final TestMessage testMessage1 = new TestMessage("Test Inhalt 1");
	
	/**
	 * The second message to send/receive in test.
	 */
	private final AlternativeTestMessage testMessage2 = new AlternativeTestMessage(42);
	
	/**
	 * The JSON message for send/receive in test.
	 */
	private final String jsonTestMessage =
			"{\"@messageType\":\""+TestMessage.class.getCanonicalName()+"\",\"test\":\""+testMessage1.test+"\"}"+
			"{\"@messageType\":\""+AlternativeTestMessage.class.getCanonicalName()+"\",\"someInt\":"+testMessage2.someInt+"}";
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private IRemoteCommunicationListener listenerMock;
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private Socket socketMock;

	/**
	 * {@link TestDependencyContainer}
	 */
	private TestDependencyContainer testContainer;
	
	/**
	 * Before Test:
	 * Set up new {@link Socket} and {@link IRemoteCommunicationListener} mocks. 
	 * @see AndroidMock#createStrictMock
	 */
	@Override
	protected void setUp() {
		testContainer = new TestDependencyContainer();
		socketMock = testContainer.getSocket();
		listenerMock = AndroidMock.createStrictMock(IRemoteCommunicationListener.class);
	}
	
	/**
	 * Sets the mocks in replay phase and create a mock injected {@link RemoteCommunication}
	 * @see AndroidMock#replay
	 * @return mock injected {@link RemoteCommunication}
	 */
	private RemoteCommunication aCPartnerForTest() {
		AndroidMock.replay(socketMock, listenerMock);
		
		return new RemoteCommunication(testContainer, listenerMock);
	}
	
	/******************************************
	 * testSendMessage
	 ******************************************/
	
	/**
	 * Tests {@link RemoteCommunication#sendMessage}.
	 * Sends two messages to the mock communication partner.
	 * @throws Exception Test failed.
	 */
	public void testSendMessage() throws Exception {
		// Configure Test
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		AndroidMock.expect(socketMock.getOutputStream())
				   .andReturn(outputStream)
				   .times(2);
		
		// Run Test
	    IRemoteCommunication target = aCPartnerForTest();
	    target.sendMessage(testMessage1);
	    target.sendMessage(testMessage2);
	    
	    // Verify Test
	    Assert.assertEquals(jsonTestMessage,
	    				    outputStream.toString());
		AndroidMock.verify(socketMock, listenerMock);
	}	
	
	/******************************************
	 * testReciveMessage
	 ******************************************/
	
	/**
	 * Tests the {@link IRemoteCommunicationListener#messageReceived} callback and {@link RemoteCommunication#startMessageListener}:
	 * - Start MessageListenerThread
	 * - "Send" two message through pseudo stream
	 * - "Close" the stream after that
	 * - Mock of {@link IRemoteCommunicationListener} checks that the correct callback are called.
	 * @throws Exception Test failed.
	 */
	public void testReciveMessage() throws Exception {
		// Configure Test
		InputStream mockStream = new InputStream() {
			private final byte[] bytes = jsonTestMessage.getBytes();
			private int pos = 0;
			
			@Override
			public int read() throws IOException {
				if(pos < bytes.length) {
					return bytes[pos++];
				} else {
					return -1; // Stream is closed
				}
			}
		};
		AndroidMock.expect(socketMock.getInputStream()).andReturn(mockStream);
		
		listenerMock.messageReceived(testMessage1);
		listenerMock.messageReceived(testMessage2);
		listenerMock.connectionProblem(AndroidMock.isA(IOException.class));
		
		// Run Test
		IRemoteCommunication target = aCPartnerForTest();
		target.startMessageListener();
	    
    	// Wait a little bit until InputStream is read.
		waitALittleBit();
	    
	    // Verify Test
		AndroidMock.verify(socketMock, listenerMock);
	}
	
	/******************************************
	 * testClose
	 ******************************************/

	/**
	 * The Thread that is started by the MessageListenerThread.
	 * For testClose*() tests.
	 */
	private Thread messageListenerThread;
	
	/**
	 * What should the mock stream send?
	 * For testClose*() tests.
	 */
	private int mockStreamSend;
	
	/**
	 * Checks, if the {@link Socket} is closed on {@link RemoteCommunication#close}.
	 * It also checks if the MessageListenerThread will be closed.
	 * @throws Exception Test failed.
	 */
	public void testClose() throws Exception {
		// Configure Test
		AndroidMock.makeThreadSafe(socketMock, true);
		AndroidMock.makeThreadSafe(listenerMock, true);
		configureMockStreamForTestClose();
		
		AndroidMock.expect(socketMock.isClosed()).andReturn(false);
		socketMock.close();
		AndroidMock.expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				// Simulate a closing of the socket:
				mockStreamSend = -1;
				return null;
			}
		});
		
		// Run Test
		RemoteCommunication target = aCPartnerForTest();
		target.startMessageListener();
		waitALittleBit();
		target.close();
		
	    // Verify Test
		verfyListenerClosedForTestClose();
		AndroidMock.verify(socketMock, listenerMock);
	}
	
	/**
	 * Checks, if the {@link Socket} isn't closed, when it's closed.
	 * It also checks if the MessageListenerThread will be closed.
	 * @throws Exception Test failed.
	 */
	public void testClose_alreadyClosed() throws Exception {
		// Configure Test
		AndroidMock.makeThreadSafe(socketMock, true);
		AndroidMock.makeThreadSafe(listenerMock, true);
		configureMockStreamForTestClose();
		
		AndroidMock.expect(socketMock.isClosed()).andAnswer(new IAnswer<Boolean>() {
			@Override
			public Boolean answer() throws Throwable {
				// Now the stream recognize that the socket is closed:
				mockStreamSend = -1;
				return true;
			}
		});
		
		// Run Test
		RemoteCommunication target = aCPartnerForTest();
		target.startMessageListener();
		waitALittleBit();
		target.close();
		
	    // Verify Test
		verfyListenerClosedForTestClose();
		AndroidMock.verify(socketMock, listenerMock);
	}
	
	/**
	 * Checks if the IOException is rethrown on close.
	 * It also checks if the MessageListenerThread will be closed.
	 * @throws Exception Couldn't thrown in configure.
	 */
	public void testClose_ioException() throws Exception {
		// Configure Test
		AndroidMock.makeThreadSafe(socketMock, true);
		AndroidMock.makeThreadSafe(listenerMock, true);
		configureMockStreamForTestClose();
		
		AndroidMock.expect(socketMock.isClosed()).andReturn(false);
		socketMock.close();
		final IOException expectedEx = new IOException();
		AndroidMock.expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				// Simulate a closing of the socket:
				mockStreamSend = -1;
				throw expectedEx;
			}
		});
		
		// Run Test
		RemoteCommunication target = aCPartnerForTest();
		target.startMessageListener();
		waitALittleBit();
		try {
			target.close();
			fail("Exception not rethrown.");
		} catch(IOException actualEx) {
			assertSame(expectedEx, actualEx);
		}
		
	    // Verify Test
		verfyListenerClosedForTestClose();
		AndroidMock.verify(socketMock, listenerMock);
	}
	
	/**
	 * Initialize a pseudo stream, so we can check if the MessageListenerThread is closed.
	 * @see #messageListenerThread
	 * @throws IOException Couldn't thrown in configure.
	 */
	private void configureMockStreamForTestClose() throws IOException {
		mockStreamSend = ' ';
		InputStream mockStream = new InputStream() {
			
			/**
			 * Returns #mockStreamSend on every call and sets the #messageListenerThread
			 */
			@Override
			public int read() throws IOException {
				messageListenerThread = Thread.currentThread();
				try {
					synchronized(this) {
						wait(10);
					}
				} catch (InterruptedException e) {
					messageListenerThread.interrupt(); // If we interrupted the Thread, it should be interrupted for the test.
				}
				return mockStreamSend;
			}
		};
		AndroidMock.expect(socketMock.getInputStream()).andReturn(mockStream);
	}
	
	/**
	 * Is the {link #messageListenerThread} closed?
	 * @throws InterruptedException Test failed.
	 */
	private void verfyListenerClosedForTestClose() throws InterruptedException {
    	// Wait a little bit until InputStream is read.
    	waitALittleBit();
		
		assertNotNull("messageListenerThread not set.", messageListenerThread);
		assertFalse(messageListenerThread.isAlive());
		
		messageListenerThread = null;
	}
	
	/**
	 * Waits a little bit (for another thread).
	 * @throws InterruptedException Test failed.
	 */
	private synchronized void waitALittleBit() throws InterruptedException {
		this.wait(100);
	}
}
