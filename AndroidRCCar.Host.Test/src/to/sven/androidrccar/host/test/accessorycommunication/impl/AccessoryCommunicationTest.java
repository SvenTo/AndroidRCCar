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
package to.sven.androidrccar.host.test.accessorycommunication.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.easymock.Capture;
import org.easymock.IAnswer;

import to.sven.androidrccar.common.utils.CRC8;
import to.sven.androidrccar.host.accessorycommunication.command.AbstractCommand;
import to.sven.androidrccar.host.accessorycommunication.command.ICommandListener;
import to.sven.androidrccar.host.accessorycommunication.command.NoopCommand;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunicationListener;
import to.sven.androidrccar.host.accessorycommunication.exception.AccessoryConnectionProblemException;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;
import android.os.Handler;
import android.os.Message;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of {@link AccessoryCommunication}.
 * @author sven
 *
 */
@UsesMocks({ Handler.class, InputStream.class, OutputStream.class, IAccessoryCommunicationListener.class })
public class AccessoryCommunicationTest extends AndroidTestCase {

	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private Handler mainThreadHandlerMock;
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private InputStream inputStreamMock;

	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private OutputStream outputStreamMock;

	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private IAccessoryCommunicationListener listenerMock;
	
	/**
	 * Some {@link CarFeatures}.
	 */
	private final CarFeatures features = new CarFeatures(0, 180, 180, 0, false, false, false);
	
	/**
	 * Before Test:
	 * Set up new {@link Handler},
	 * {@link InputStream} and
	 * {@link OutputStream} mocks. 
	 * @see AndroidMock#createStrictMock
	 */
	@Override
	protected void setUp() {
		inputStreamMock = AndroidMock.createStrictMock(InputStream.class);
		outputStreamMock = AndroidMock.createStrictMock(OutputStream.class);
		mainThreadHandlerMock = AndroidMock.createStrictMock(Handler.class);
		listenerMock = AndroidMock.createStrictMock(IAccessoryCommunicationListener.class);
	}

	/**
	 * Sets the mocks in replay phase and create a mock injected {@link AccessoryCommunication}.
	 * @see AndroidMock#replay
	 * @return mock injected {@link AccessoryCommunication}
	 */
	private AccessoryCommunication createInstance() {
		replayFieldMocks();

		return new AccessoryCommunication(outputStreamMock, inputStreamMock, mainThreadHandlerMock);
	}

	/**
	 * Verifies all mock that created by {@link #setUp()}
	 * @see AndroidMock#verify
	 */
	private void verifyFieldMocks() {
		AndroidMock.verify(inputStreamMock, outputStreamMock, mainThreadHandlerMock, listenerMock);
	}

	/**
	 * Sets the mocks in replay phase by {@link #setUp()}.
	 * @see AndroidMock#replay
	 */
	private void replayFieldMocks() {
		AndroidMock.replay(inputStreamMock, outputStreamMock, mainThreadHandlerMock, listenerMock);
	}
	
	/**
	 * Creates strict {@link NoopCommand}-mock.
	 * @see AndroidMock#createStrictMock
	 * @return Strict {@link NoopCommand}-mock
	 */
	@UsesMocks({ NoopCommand.class, ICommandListener.class })
	private NoopCommand createCommandMock() {
		ICommandListener commandListenerMock = AndroidMock.createNiceMock(ICommandListener.class);
		AndroidMock.makeThreadSafe(commandListenerMock, true);
		AndroidMock.expect(commandListenerMock.getInputStream()).andReturn(inputStreamMock);
		AndroidMock.expect(commandListenerMock.getOutputStream()).andReturn(outputStreamMock);
		AndroidMock.replay(commandListenerMock);
		return AndroidMock.createStrictMock(NoopCommand.class, commandListenerMock);
	}
	
	/**
	 * Tests {@link AccessoryCommunication#startCommunication()},
	 * {@link AccessoryCommunication#setListener(IAccessoryCommunicationListener)}
	 * and {@link AccessoryCommunication#postCommand}.
	 * @throws Exception Something went wrong.
	 */
	public void testStartCommunication() throws Exception {
		// Configure Test
		configureWithStartCommunication();
		NoopCommand commandMock = createCommandMock();
		commandMock.run();
		
		// Run Test
		AndroidMock.replay(commandMock);
		AccessoryCommunication target = createWithStartCommunication();
		target.postCommand(commandMock);
		synchronized (this) {
			this.wait(100); // Wait a little bit for the other thread.
		}
		
		// Verify Test
		verifyFieldMocks();
		AndroidMock.verify(commandMock);
	}

	/**
	 * Tests {@link AccessoryCommunication#startCommunication()}:
	 * Tests if a {@link IllegalStateException} is thrown, when no 
	 * {@link IAccessoryCommunicationListener} was set.
	 */
	public void testStartCommunication_noListener() {
		// Configure Test
				
		// Run Test
		AccessoryCommunication target = createInstance();
		try {
			target.startCommunication();
			fail("No IllegalStateException was thrown.");
		} catch (IllegalStateException e) {
			// All OK.
		}
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link AccessoryCommunication#setCarFeatures(CarFeatures)}
	 * and {@link AccessoryCommunication#getCarFeatures()}.
	 */
	public void testCarFeatures() {
		// Configure Test

		// Run Test
		AccessoryCommunication target = createInstance();
		target.setCarFeatures(features);
		assertSame(features, target.getCarFeatures());
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link AccessoryCommunication#setListener(IAccessoryCommunicationListener)}:
	 * Set null.
	 */
	public void testSetListener_null() {
		// Configure Test
		
		// Run Test
		AccessoryCommunication target = createInstance();
		try {
			target.setListener(null);
			fail("No IllegalStateException was thrown.");
		} catch (IllegalArgumentException e) {
			// All OK.
		}
		
		// Verify Test
		verifyFieldMocks();
	}

	/**
	 * This test method is a generic implementation for all methods in {@link AccessoryCommunication}
	 * that only post callback back into the main thread.
	 * @param testTargetRunner A implementation that calls the method that should be tested
	 * 						   on the passed test target.
	 */
	@UsesMocks({ IAccessoryCommunicationListener.class })
	private void commonPostIntoMainThreadTest(TestTargetRunner<IAccessoryCommunicationListener> testTargetRunner) {
		// Configure Test
		Capture<Message> messageCapture = new Capture<Message>();
		AndroidMock.expect(mainThreadHandlerMock.sendMessageAtTime(AndroidMock.capture(messageCapture),
																   AndroidMock.anyInt()))
			       .andReturn(true);
		testTargetRunner.run(listenerMock);
		
		// Run Test
		AccessoryCommunication target = createInstance();
		target.setListener(listenerMock);
		testTargetRunner.run(target);
		messageCapture.getValue().getCallback().run();
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Like a {@link Runnable} with a extra argument.
	 * @author sven
	 *
	 * @param <T> The type of the extra argument.
	 */
	private interface TestTargetRunner<T> {
		
		/**
		 * Executes it.
		 * @param target The extra argument.
		 */
		void run(T target);
	}
	
	/**
	 * Tests {@link AccessoryCommunication#protocolVersionNotMatch}.
	 */
	public void testProtocolVersionNotMatch() {
		commonPostIntoMainThreadTest(new TestTargetRunner<IAccessoryCommunicationListener>() {
			@Override
			public void run(IAccessoryCommunicationListener target) {
				target.protocolVersionNotMatch((short)2, (short)1);
			}
		});
	}
	
	/**
	 * Tests {@link AccessoryCommunication#connectionInitialized}.
	 */
	public void testConnectionInitialized() {
		commonPostIntoMainThreadTest(new TestTargetRunner<IAccessoryCommunicationListener>() {
			@Override
			public void run(IAccessoryCommunicationListener target) {
				target.connectionInitialized();
			}
		});
	}
	
	/**
	 * Tests {@link AccessoryCommunication#batteryStateReceived}.
	 */
	public void testBatteryStateReceived() {
		commonPostIntoMainThreadTest(new TestTargetRunner<IAccessoryCommunicationListener>() {
			@Override
			public void run(IAccessoryCommunicationListener target) {
				target.batteryStateReceived(50f);
			}
		});
	}
	
	/**
	 * Tests {@link AccessoryCommunication#batteryNearEmpty}.
	 */
	public void testBatteryNearEmpty() {
		commonPostIntoMainThreadTest(new TestTargetRunner<IAccessoryCommunicationListener>() {
			@Override
			public void run(IAccessoryCommunicationListener target) {
				target.batteryNearEmpty();
			}
		});
	}
	
	/**
	 * Tests {@link AccessoryCommunication#errorReceived}.
	 */
	public void testErrorReceived() {
		commonPostIntoMainThreadTest(new TestTargetRunner<IAccessoryCommunicationListener>() {
			@Override
			public void run(IAccessoryCommunicationListener target) {
				target.errorReceived("Some message.");
			}
		});
	}
	
	/**
	 * Tests {@link AccessoryCommunication#errorReceived}.
	 */
	public void testConnectionProblem() {
		commonPostIntoMainThreadTest(new TestTargetRunner<IAccessoryCommunicationListener>() {
			private AccessoryConnectionProblemException ex = new AccessoryConnectionProblemException("");
			
			@Override
			public void run(IAccessoryCommunicationListener target) {
				target.connectionProblem(ex);
			}
		});
	}

	/**
	 * Tests {@link AccessoryCommunication#getInputStream}.
	 */
	public void testGetInputStream() {
		// Configure Test
		
		// Run Test
		AccessoryCommunication target = createInstance();
		InputStream stream = target.getInputStream();
		
		// Verify Test
		assertSame(inputStreamMock, stream);
		verifyFieldMocks();
	}

	/**
	 * Tests {@link AccessoryCommunication#getOutputStream}.
	 */
	public void testGetOutputStream() {
		// Configure Test
		
		// Run Test
		AccessoryCommunication target = createInstance();
		OutputStream stream = target.getOutputStream();
		
		// Verify Test
		assertSame(outputStreamMock, stream);
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link AccessoryCommunication#close()}.
	 * @throws Exception Something went wrong
	 */
	public void testClose() throws Exception {
		// Configure Test
		configureWithStartCommunication();
		NoopCommand commandMock = createCommandMock();
		commandMock.run();
		AndroidMock.expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				synchronized (this) {
					Log.i("testClose", "Before wait.");
					this.wait(100); // Do some "work".
					Log.i("testClose", "After wait.");
				}
				return null;
			}
		});
		NoopCommand commandMock2 = createCommandMock();
		
		// Run Test
		AndroidMock.replay(commandMock, commandMock2);
		AccessoryCommunication target = createWithStartCommunication();
		target.postCommand(commandMock);
		target.postCommand(commandMock2);
		synchronized (this) {
			this.wait(50); // Wait a little bit, so the first command is executed.
		}
		target.close(); // The thread should closed, so the other command shouldn't run.
		synchronized (this) {
			this.wait(200); // Wait a little bit to verify it.
		}
		
		// Verify Test
		try {
			target.postCommand(commandMock);
			fail("Call of postCommand should illegal after stop.");
		} catch (IllegalStateException e) {
			// All OK.
		}
		verifyFieldMocks();
		AndroidMock.verify(commandMock, commandMock2);
	}
	
	/**
	 * Common implementation for all methods that should send a message to the µController.
	 * @param runner Call to the method that should be tested.
	 * @param expectedCommand The Command ID we expect.
	 * @param expectedPayload The Payload we expect.
	 * @throws Exception Something went wrong
	 */
	private void commonSendCommandTest(TestTargetRunner<AccessoryCommunication> runner,
									  RequestCommand expectedCommand,
									  byte[] expectedPayload) throws Exception {
		// Configure Test
		configureWithStartCommunication();
		
		expectCommunication(expectedCommand,
							expectedPayload,
							ResponseMessage.REQUEST_OK,
				   			null,
				   			0);
		
		// Run Test
		AccessoryCommunication target = createWithStartCommunication();
		runner.run(target);
		
		synchronized (this) {
			this.wait(50); // Wait a little bit, so the command is executed.
		}
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link AccessoryCommunication#adjustSpeed(float)}.
	 * @throws Exception Something went wrong
	 */
	public void testAdjustSpeed()  throws Exception {
		TestTargetRunner<AccessoryCommunication> runner = new TestTargetRunner<AccessoryCommunication>() {
			@Override
			public void run(AccessoryCommunication target) {
				target.adjustSpeed(1.0f);
			}
		};
		commonSendCommandTest(runner,
							  RequestCommand.ADJUST_SPEED,
							  new byte[] { 0x7F, -0x01 });
	}
	
	/**
	 * Tests {@link AccessoryCommunication#rotateCamera(float, float)}.
	 * @throws Exception Something went wrong
	 */
	public void testRotateCamera()  throws Exception {
		TestTargetRunner<AccessoryCommunication> runner = new TestTargetRunner<AccessoryCommunication>() {
			@Override
			public void run(AccessoryCommunication target) {
				target.rotateCamera(180, -180);
			}
		};
		commonSendCommandTest(runner,
							  RequestCommand.ROTATE_CAM,
							  new byte[] { 0x7F, -0x01, -0x80, 0x00 });
	}
	
	/**
	 * Tests {@link AccessoryCommunication#turnCar(float)}.
	 * @throws Exception Something went wrong
	 */
	public void testTurnCar()  throws Exception {
		TestTargetRunner<AccessoryCommunication> runner = new TestTargetRunner<AccessoryCommunication>() {
			@Override
			public void run(AccessoryCommunication target) {
				target.turnCar(1.0f);
			}
		};
		commonSendCommandTest(runner,
							  RequestCommand.TURN_CAR,
							  new byte[] { 0x7F, -0x01 });
	}
	
	/**
	 * Tests {@link AccessoryCommunication#requestBatteryState()}.
	 * @throws Exception Something went wrong
	 */
	public void testRequestBatteryState()  throws Exception {
		// Configure Test
		configureWithStartCommunication();
		expectCommunication(RequestCommand.GET_BATTERY_STATE,
							   null,
							   ResponseMessage.BATTERY_STATE,
							   new byte[] { 0x7F, -0x01 }, // 100%
							   1);
		
		// Run Test
		AccessoryCommunication target = createWithStartCommunication();
		target.requestBatteryState();
		
		synchronized (this) {
			this.wait(100); // Wait a little bit, so the command is executed.
		}
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Configures what is expected, when we call {@link AccessoryCommunication#startCommunication()}:
	 * - A {@link RequestCommand#GET_PROTOCOL_VERSION}
	 * - Then a {@link RequestCommand#GET_FEATURES}
	 * @throws Exception Something went wrong
	 */
	private void configureWithStartCommunication() throws Exception {
		expectCommunication(RequestCommand.GET_PROTOCOL_VERSION,
							   null,
							   ResponseMessage.PROTOCOL_VERSION,
							   new byte[] { 0x00, 0x01 },
							   0);
		expectCommunication(RequestCommand.GET_FEATURES,
							null,
							ResponseMessage.FEATURES,
							new byte[] { 0x07, // All Features on
							         	 0x7F, -0x01,   // cameraPanMin: 180 degree
							         	 0x7F, -0x01,   // cameraPanMax: 180 degree
							         	 0x7F, -0x01,   // cameraTiltMin: 180 degree
							         	 0x7F, -0x01 }, // cameraTiltMax: 180 degree
							1);
	}
	
	/**
	 * Creates a instance of {@link AccessoryCommunication} and
	 * sets the {@link #listenerMock} and starts communication.
	 * @return Created instance.
	 * @throws Exception Something went wrong
	 */
	private AccessoryCommunication createWithStartCommunication() throws Exception {
		AccessoryCommunication target = createInstance();
		target.setListener(listenerMock);
		target.startCommunication();	
		synchronized (this) {
			this.wait(100); // Wait a little bit, so the "connection" is initialized.
		}
		return target;
	}
	
	/**
	 * Configures that we expect that the test target will send a command to the "µController"
	 * and configures a mock response.
	 * @param command The {@link RequestCommand} that is expected
	 * @param requestPayload The payload of the command
	 * @param response The {@link ResponseMessage} that will send back
	 * @param responsePayload The payload of the response
	 * @param postsToMainThread The {@link AbstractCommand} implementation that send this command,
	 * 						    probably do some callbacks. If put the count of it here, else 0.
	 * @throws IOException Something went wrong
	 */
	private void expectCommunication(RequestCommand command, byte[] requestPayload,
										final ResponseMessage response, final byte[] responsePayload,
										int postsToMainThread) throws IOException {
		// What we expect to receive as µController:
		byte[] request = new byte[AccessoryCommunication.COMMAND_LENGTH];
		request[0] = command.messageId;
		if(requestPayload != null) {
			System.arraycopy(requestPayload, 0, request, 1, requestPayload.length);
		}
		request[request.length-1] = CRC8.calc(request, request.length-1);
		outputStreamMock.write(AndroidMock.aryEq(request));
		
		// What we response:
		final Capture<byte[]> responseCapture = new Capture<byte[]>();
		AndroidMock.expect(inputStreamMock.read(AndroidMock.capture(responseCapture)))
				   .andAnswer(new IAnswer<Integer>() {
						@Override
						public Integer answer() throws Throwable {
							byte[] actualResponse = responseCapture.getValue();
							actualResponse[0] = response.messageId;
							if(responsePayload != null) {
								System.arraycopy(responsePayload, 0, actualResponse, 1, responsePayload.length);
							}
							actualResponse[actualResponse.length-1] = CRC8.calc(actualResponse, actualResponse.length-1);
							return AccessoryCommunication.REPSONE_MESSAGE_LENGTH;
						}
				   	});
		// Commands possible make some callbacks:
		if(postsToMainThread > 0) {
			AndroidMock.expect(mainThreadHandlerMock.sendMessageAtTime((Message) AndroidMock.anyObject(),
					   												   AndroidMock.anyLong()))
					   .andReturn(true)
					   .times(postsToMainThread);
		}
	}
}
