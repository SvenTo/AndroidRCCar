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
import to.sven.androidrccar.host.accessorycommunication.command.AdjustSpeedCommand;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

import com.google.android.testing.mocking.AndroidMock;

/**
 * This class should test the behavior of the {@link AdjustSpeedCommand}.
 * This class also tests some(!) behavior of 
 * the abstract implementation of {@link AbstractCommand}.
 * 
 * @author sven
 * @see NoopCommandTest NoopCommandTest for tests of {@link AbstractCommand}.
 */
public class AdjustSpeedCommandTest extends AbstractCommandTestCase {
	
	/**
	 * Like {@link #configureAndRunTest(float, boolean)}
	 * with drive backward enabled.
	 * @param speed {@link #configureAndRunTest}
	 */
	private void configureAndRunTest(float speed) {
		configureAndRunTest(speed, true);
	}
	
	/**
	 * Common configure and run test
	 * @param speed Passed to {@link AdjustSpeedCommand#AdjustSpeedCommand}
	 * @param driveBackward {@link CarFeatures#driveBackward}
	 */
	private void configureAndRunTest(float speed, boolean driveBackward) {
		// Configure Test 
		byte[] mockRespone = createArray(new byte[] { 0x01 }, (byte) 0xB5);
		prepareStreams(mockRespone);
		AndroidMock.expect(listenerMock.getCarFeatures())
				   .andReturn(new CarFeatures(0, 0, 0, 0, true, driveBackward, false));
		
		// Run Test
		replay();
		AdjustSpeedCommand target = new AdjustSpeedCommand(listenerMock, speed);
		target.run();
	}
	
	/**
	 * Send a {@link RequestCommand#ADJUST_SPEED}
	 * with full forward speed
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_fullForwardSpeed() {
		configureAndRunTest(1.0f);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] { 0x05, 0x7F, -0x01, }, (byte) 0x54 );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Send a {@link RequestCommand#ADJUST_SPEED}
	 * with full backward speed
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_fullBackwardSpeed() {
		configureAndRunTest(-1.0f);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] {  0x05, -0x80, 0x01 }, (byte) 0x22 ); // TODO: MAX_VALUE Problem?
		verifyTest(expectedRequest);
	}
	
	/**
	 * Send a {@link RequestCommand#ADJUST_SPEED}
	 * with full backward speed
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_stop() {
		configureAndRunTest(0.0f);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] {  0x05, 0x00, 0x00 }, (byte) 0x6F );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Common test for passing a incorrect speed value. 
	 * @param speed {@link #configureAndRunTest}
	 * @param driveBackward {@link #configureAndRunTest}
	 */
	private void commonTestValidateInputs(float speed, boolean driveBackward) {
		try {
			configureAndRunTest(speed, driveBackward);
			fail("No RangeException was thrown.");
		} catch(RangeException ex) {
			// All OK.
		}
		
		AndroidMock.verify(listenerMock);
	}
	
	/**
	 * Speed is above the range.
	 */
	public void testValidateInputs_tooMuch() {
		commonTestValidateInputs(1.1f, false);
	}
	
	/**
	 * Speed is under the range.
	 */
	public void testValidateInputs_tooLess() {
		commonTestValidateInputs(-1.1f, true);
	}
	
	/**
	 * Speed is under the range without drive backward.
	 */
	public void testValidateInputs_tooLessNoDriveBackward() {
		commonTestValidateInputs(-0.1f, false);
	}
}
