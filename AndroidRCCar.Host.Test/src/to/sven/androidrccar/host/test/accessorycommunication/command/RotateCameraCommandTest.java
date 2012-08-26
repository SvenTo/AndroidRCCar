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
import to.sven.androidrccar.host.accessorycommunication.command.RotateCameraCommand;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

import com.google.android.testing.mocking.AndroidMock;

/**
 * This class should test the behavior of the {@link RotateCameraCommand}.
 * This class also tests some(!) behavior of 
 * the abstract implementation of {@link AbstractCommand}.
 * 
 * @author sven
 * @see NoopCommandTest NoopCommandTest for tests of {@link AbstractCommand}.
 */
public class RotateCameraCommandTest extends AbstractCommandTestCase {

	/**
	 * Common configure and run test
	 * 
	 * @param pan Passed to {@link RotateCameraCommand#RotateCameraCommand}
	 * @param tilt Passed to {@link RotateCameraCommand#RotateCameraCommand}
	 * @param panMin {@link CarFeatures#cameraPanMin}
	 * @param panMax {@link CarFeatures#cameraPanMax}
	 * @param tiltMin {@link CarFeatures#cameraTiltMin}
	 * @param tiltMax {@link CarFeatures#cameraTiltMax}
	 */
	private void configureAndRunTest(float pan, float tilt, 
									 float panMin, float panMax,
									 float tiltMin, float tiltMax) {
		// Configure Test 
		CarFeatures features = new CarFeatures(panMin, panMax, tiltMin, tiltMax,
					  						   false, false, false);
		byte[] mockRespone = createArray(new byte[] { 0x01 }, (byte) 0xB5 );
		prepareStreams(mockRespone);
		AndroidMock.expect(listenerMock.getCarFeatures())
				   .andReturn(features).atLeastOnce();
		
		// Run Test
		replay();
		RotateCameraCommand target = new RotateCameraCommand(listenerMock, pan, tilt);
		target.run();
	}
	
	/**
	 * Send a {@link RequestCommand#ROTATE_CAM}
	 * with maximal pan 
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_panMax() {
		configureAndRunTest(50, 0, 40, 50, 0, 0);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] { 0x06, 0x7F, -0x01, 0x00, 0x00 }, (byte) 0x8C );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Send a {@link RequestCommand#ROTATE_CAM}
	 * with minimal pan 
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_panMin() {
		configureAndRunTest(-60, 0, 60, 50, 0, 0);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] { 0x06, -0x80, 0x00, 0x00, 0x00 }, (byte) 0x6E );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Send a {@link RequestCommand#ROTATE_CAM}
	 * with pan and tilt to look forward
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_noPan() {
		configureAndRunTest(0, 0, 40, 50, 0, 0);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] { 0x06, 0x00, 0x00, 0x00, 0x00 }, (byte) 0xB7 );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Send a {@link RequestCommand#ROTATE_CAM}
	 * with maximal tilt 
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_tiltMax() {
		configureAndRunTest(0, 180, 0, 0, 100, 180);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] { 0x06, 0x00, 0x00, 0x7F, -0x01 }, (byte) 0x79 );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Send a {@link RequestCommand#ROTATE_CAM}
	 * with minimal tilt 
	 * and receive a {@link ResponseMessage#REQUEST_OK}.
	 */
	public void testRun_tiltMin() {
		configureAndRunTest(0, -150, 0, 0, 150, 80);
		
		// Verify Test
		byte[] expectedRequest = createArray(new byte[] { 0x06, 0x00, 0x00, -0x80, 0x00 }, (byte) 0xFD );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Common test for passing a incorrect speed value. 
	 * @param pan Passed to {@link RotateCameraCommand#RotateCameraCommand}
	 * @param tilt Passed to {@link RotateCameraCommand#RotateCameraCommand}
	 */
	private void commonTestValidateInputs(float pan, float tilt) {
		try {
			configureAndRunTest(pan, tilt, 180, 180, 180, 180);
			fail("No RangeException was thrown.");
		} catch(RangeException ex) {
			// All OK.
		}
		
		AndroidMock.verify(listenerMock);
	}
	
	/**
	 * Pan is above the range.
	 */
	public void testValidateInputs_panTooMuch() {
		commonTestValidateInputs(180.1f, 150);
	}
	
	/**
	 * Pan is above the range.
	 */
	public void testValidateInputs_panTooLess() {
		commonTestValidateInputs(-180.1f, 150);
	}
	
	/**
	 * Tilt is above the range.
	 */
	public void testValidateInputs_tiltTooMuch() {
		commonTestValidateInputs(150, 180.1f);
	}
	
	/**
	 * Tilt is above the range.
	 */
	public void testValidateInputs_tiltTooLess() {
		commonTestValidateInputs(150, -180.1f);
	}
	
	
}
