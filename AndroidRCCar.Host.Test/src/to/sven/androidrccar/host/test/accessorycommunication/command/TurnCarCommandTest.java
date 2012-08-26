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

import to.sven.androidrccar.common.exception.RangeException;
import to.sven.androidrccar.host.accessorycommunication.command.AbstractCommand;
import to.sven.androidrccar.host.accessorycommunication.command.TurnCarCommand;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

import com.google.android.testing.mocking.AndroidMock;


/**
 * This class should test the behavior of the {@link TurnCarCommand}.
 * This class also tests some(!) behavior of 
 * the abstract implementation of {@link AbstractCommand}.
 * 
 * @author sven
 * @see NoopCommandTest NoopCommandTest for tests of {@link AbstractCommand}.
 */
public class TurnCarCommandTest extends AbstractCommandTestCase {
	
	/**
	 * Common configure and run test
	 * @param rotation Passed to {@link TurnCarCommand#TurnCarCommand}
	 */
	private void configureAndRunTest(float rotation) {
		// Configure Test 
		byte[] mockRespone = createArray(new byte[] { 0x01 }, (byte) 0xB5 );
		prepareStreams(mockRespone);

		// Run Test
		replay();
		TurnCarCommand target = new TurnCarCommand(listenerMock, rotation);
		target.run();
	}
	
	/**
	 * Send a {@link RequestCommand#TURN_CAR}
	 * with no rotation
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_Forward() {
		configureAndRunTest(0.0f);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] { 0x04, 0x00, 0x00 }, (byte) 0xDA );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Send a {@link RequestCommand#TURN_CAR}
	 * with full left rotation
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_Left() {
		configureAndRunTest(-1.0f);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] { 0x04, -0x80, 0x01 }, (byte) 0x97 ); // TODO: MAX_VALUE Problem?
		verifyTest(expectedRequest);
	}
	
	/**
	 * Send a {@link RequestCommand#TURN_CAR}
	 * with full right rotation
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_Right() {
		configureAndRunTest(1.0f);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] { 0x04, 0x7F, -0x01 }, (byte) 0xE1 );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Common test for passing a incorrect rotation value. 
	 * @param rotation {@link #configureAndRunTest}
	 */
	private void commonTestValidateInputs(float rotation) {
		try {
			configureAndRunTest(rotation);
			fail("No RangeException was thrown.");
		} catch(RangeException ex) {
			// All OK.
		}
		
		AndroidMock.verify(listenerMock);
	}
	
	/**
	 * Rotation is above the range.
	 */
	public void testValidateInputs_tooMuch() {
		commonTestValidateInputs(1.1f);
	}
	
	/**
	 * Rotation is under the range.
	 */
	public void testValidateInputs_tooLess() {
		commonTestValidateInputs(-1.1f);
	}
}
