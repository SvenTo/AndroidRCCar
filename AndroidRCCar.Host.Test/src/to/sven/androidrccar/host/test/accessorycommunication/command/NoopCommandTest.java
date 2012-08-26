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
package to.sven.androidrccar.host.test.accessorycommunication.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.easymock.Capture;

import to.sven.androidrccar.host.accessorycommunication.command.AbstractCommand;
import to.sven.androidrccar.host.accessorycommunication.command.AbstractCommandWithOkResponse;
import to.sven.androidrccar.host.accessorycommunication.command.NoopCommand;
import to.sven.androidrccar.host.accessorycommunication.exception.AccessoryConnectionProblemException;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of the {@link NoopCommandTest}.
 * This class also tests the behavior of 
 * the abstract implementation of {@link AbstractCommand}
 * and {@link AbstractCommandWithOkResponse}.
 * @author sven
 *
 */
public class NoopCommandTest extends AbstractCommandTestCase {

	/**
	 * Common verification for test.
	 */
	protected void verifyTest() {
		byte[] expectedRequest = createArray(new byte[] { 0x01 }, (byte)0xB5 );
		verifyTest(expectedRequest);
	}
	
	/** 
	 * Common Run Test
	 */
	private void runMyTest() {
		replay();
		NoopCommand target = new NoopCommand(listenerMock);
		target.run();
	}
	
	/**
	 * Send a {@link RequestCommand#NOOP}
	 * and receive the expected {@link ResponseMessage#REQUEST_OK}. 
	 */
	public void testRun() {
		// Configure Test
		byte[] mockRespone = createArray(new byte[] { 0x01 }, (byte) 0xB5 );
		prepareStreams(mockRespone);
		
		// Run Test
		runMyTest();
		
		// Verify Test
		verifyTest();
	}
	
	/**
	 * Send a {@link RequestCommand#NOOP}
	 * and receive a {@link ResponseMessage#ERROR}. 
	 */
	public void testRun_errorResponseUndefined() {
		// Configure Test
		byte[] mockResponse = createArray(new byte[] { 0x02, 0x00, 0x00 }, (byte) 0x6D);
		prepareStreams(mockResponse);
		Capture<String> errorMessageCapture = new Capture<String>();
		listenerMock.errorReceived(AndroidMock.capture(errorMessageCapture));
		
		// Run Test
		runMyTest();
		
		// Verify Test
		String actualErrorMsg = errorMessageCapture.getValue();
		assertEquals("Received an error from type UNDEFINED_ERROR.", actualErrorMsg);
		verifyTest();
	}
	
	
	/**
	 * Send a {@link RequestCommand#NOOP}
	 * and receive a {@link ResponseMessage#ERROR}. 
	 */
	public void testRun_errorResponseUnkown() {
		// Configure Test
		byte[] mockResponse = createArray(new byte[] { 0x02, 0x00, 0x04 }, (byte) 0x33);
		prepareStreams(mockResponse);
		Capture<String> errorMessageCapture = new Capture<String>();
		listenerMock.errorReceived(AndroidMock.capture(errorMessageCapture));
		
		// Run Test
		runMyTest();
		
		// Verify Test
		String actualErrorMsg = errorMessageCapture.getValue();
		assertEquals("Received an unknown error.", actualErrorMsg);
		verifyTest();
	}
	
	/**
	 * Send a {@link RequestCommand#NOOP}
	 * and receive a {@link ResponseMessage#BATTERY_NEAR_EMPTY}. 
	 */
	public void testRun_batteryEmptyResponse() {
		// Configure Test
		byte[] mockRespone = createArray(new byte[] { 0x03 }, (byte) 0xD8);
		prepareStreams(mockRespone);
		listenerMock.batteryNearEmpty();
		
		// Run Test
		runMyTest();
		
		// Verify Test
		verifyTest();
	}

	/**
	 * Send a {@link RequestCommand#NOOP}
	 * and receive a unknown response type. 
	 */
	public void testRun_unknownResponse() {
		// Configure Test
		byte[] mockRespone = createArray(new byte[] { 0x7F }, (byte) 0x91);
		prepareStreams(mockRespone);
		Capture<AccessoryConnectionProblemException> exCapture = new Capture<AccessoryConnectionProblemException>();
		listenerMock.connectionProblem(AndroidMock.capture(exCapture));
		
		// Run Test
		runMyTest();
		
		// Verify Test
		Throwable cause = exCapture.getValue().getCause();
		assertTrue(cause instanceof IOException);
		assertEquals("A response message with the type id 127 is unkown.", cause.getMessage());
		
		verifyTest();
	}

	/**
	 * Send a {@link RequestCommand#NOOP}
	 * and receive a very unexpected response type. 
	 */
	public void testRun_unexpectedResponse() {
		// Configure Test
		byte[] mockRespone = createArray(new byte[] { 0x04 }, (byte) 0xDA);
		prepareStreams(mockRespone);
		Capture<AccessoryConnectionProblemException> exCapture = new Capture<AccessoryConnectionProblemException>();
		listenerMock.connectionProblem(AndroidMock.capture(exCapture));
		
		// Run Test
		runMyTest();
		
		// Verify Test
		Throwable cause = exCapture.getValue().getCause();
		assertTrue(cause instanceof IOException);
		assertEquals("The response type PROTOCOL_VERSION" +
				  	 " is not valid answer for request command type " +
				  	 "NOOP.", cause.getMessage());
		
		verifyTest();
	}

	/**
	 * Send a {@link RequestCommand#NOOP}
	 * but the output stream is closed.
	 * @throws IOException Something went wrong.
	 */
	@UsesMocks({ InputStream.class, OutputStream.class })
	public void testRun_closedOutputStream() throws IOException {
		// Configure Test
		InputStream inputStreamMock = AndroidMock.createStrictMock(InputStream.class);
		OutputStream outputStreamMock = AndroidMock.createStrictMock(OutputStream.class);
		AndroidMock.expect(listenerMock.getInputStream()).andReturn(inputStreamMock);
		AndroidMock.expect(listenerMock.getOutputStream()).andReturn(outputStreamMock);
		
		outputStreamMock.write((byte[])AndroidMock.anyObject());
		IOException ioEx = new IOException();
		AndroidMock.expectLastCall().andThrow(ioEx);
		
		Capture<AccessoryConnectionProblemException> exCapture = new Capture<AccessoryConnectionProblemException>();
		listenerMock.connectionProblem(AndroidMock.capture(exCapture));
		
		// Run Test
		AndroidMock.replay(outputStreamMock, inputStreamMock);
		runMyTest();
		
		// Verify Test
		Throwable cause = exCapture.getValue().getCause();
		assertSame(ioEx, cause);
		
		AndroidMock.verify(listenerMock, outputStreamMock, inputStreamMock);
	}

	/**
	 * Send a {@link RequestCommand#NOOP}
	 * and the input stream is closed.
	 * @throws IOException Something went wrong.
	 */
	@UsesMocks({ InputStream.class, OutputStream.class })
	public void testRun_closedInputStream() throws IOException {
		// Configure Test
		InputStream inputStreamMock = AndroidMock.createStrictMock(InputStream.class);
		OutputStream outputStreamMock = AndroidMock.createStrictMock(OutputStream.class);
		AndroidMock.expect(listenerMock.getInputStream()).andReturn(inputStreamMock);
		AndroidMock.expect(listenerMock.getOutputStream()).andReturn(outputStreamMock);
		
		outputStreamMock.write((byte[])AndroidMock.anyObject());
		
		AndroidMock.expect(inputStreamMock.read((byte[])AndroidMock.anyObject()))
				   .andReturn(-1);
		
		Capture<AccessoryConnectionProblemException> exCapture = new Capture<AccessoryConnectionProblemException>();
		listenerMock.connectionProblem(AndroidMock.capture(exCapture));
		
		// Run Test
		AndroidMock.replay(outputStreamMock, inputStreamMock);
		runMyTest();
		
		// Verify Test
		Throwable cause = exCapture.getValue().getCause();
		assertTrue(cause instanceof IOException);
		assertEquals("Stream is closed.", cause.getMessage());
		
		AndroidMock.verify(listenerMock, outputStreamMock, inputStreamMock);
	}
}
