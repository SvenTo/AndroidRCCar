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

import to.sven.androidrccar.host.accessorycommunication.command.AbstractCommand;
import to.sven.androidrccar.host.accessorycommunication.command.GetFeaturesCommand;
import to.sven.androidrccar.host.accessorycommunication.command.GetProtocolVersionCommand;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

import com.google.android.testing.mocking.AndroidMock;

/**
 * This class should test the behavior of the {@link GetProtocolVersionCommand}.
 * This class also tests some(!) behavior of 
 * the abstract implementation of {@link AbstractCommand}.
 * 
 * @author sven
 * @see NoopCommandTest NoopCommandTest for tests of {@link AbstractCommand}.
 */
public class GetProtocolVersionCommandTest extends AbstractCommandTestCase {
	
	/**
	 * Common verification for test.
	 */
	protected void verifyTest() {
		byte[] expectedRequest = createArray(new byte[] { 0x02 }, (byte) 0x6D );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Common Run Test
	 */
	private void runMyTest() {
		replay();
		GetProtocolVersionCommand target = new GetProtocolVersionCommand(listenerMock);
		target.run();
	}
	
	/**
	 * Send a {@link RequestCommand#GET_PROTOCOL_VERSION}
	 * and receive the expected {@link ResponseMessage#PROTOCOL_VERSION}. 
	 */
	public void testRun() {
		// Configure Test
		// Note: You must change this, when the protocol version changed: 
		byte[] mockRespone = createArray(new byte[] { 0x04, 0x00, 0x01 }, (byte) 0x4E );
		prepareStreams(mockRespone);
		AndroidMock.expect(listenerMock.getInputStream()).andReturn(null);
		AndroidMock.expect(listenerMock.getOutputStream()).andReturn(null);
		listenerMock.postCommand(AndroidMock.isA(GetFeaturesCommand.class));
		
		// Run Test
		runMyTest();
		
		// Verify Test
		verifyTest();
	}
	
	/**
	 * Send a {@link RequestCommand#GET_PROTOCOL_VERSION}
	 * and receive the expected {@link ResponseMessage#PROTOCOL_VERSION}
	 * that doesn't match. 
	 */
	public void testRun_incorrectProtocolVersion() {
		// Configure Test
		byte[] mockRespone = createArray(new byte[] { 0x04, -0x01, -0x01 }, (byte) 0x38 );

		prepareStreams(mockRespone); 
		listenerMock.protocolVersionNotMatch(AccessoryCommunication.PROTOCOL_VERSION,
											 (short)-1);
		
		// Run Test
		runMyTest();
		
		// Verify Test
		verifyTest();
	}
}
