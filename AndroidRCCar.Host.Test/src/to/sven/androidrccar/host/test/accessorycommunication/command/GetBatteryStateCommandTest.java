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
import to.sven.androidrccar.host.accessorycommunication.command.GetBatteryStateCommand;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

import com.google.android.testing.mocking.AndroidMock;

/**
 * This class should test the behavior of the {@link GetBatteryStateCommand}.
 * This class also tests some(!) behavior of 
 * the abstract implementation of {@link AbstractCommand}.
 * 
 * @author sven
 * @see NoopCommandTest NoopCommandTest for tests of {@link AbstractCommand}.
 */
public class GetBatteryStateCommandTest extends AbstractCommandTestCase {
	
	/**
	 * Common verification for test.
	 */
	protected void verifyTest() {
		byte[] expectedRequest = createArray(new byte[] { 0x07 }, (byte) 0x02 );
		verifyTest(expectedRequest);
	}
	
	/**
	 * Common Run Test
	 */
	private void runMyTest() {
		replay();
		GetBatteryStateCommand target = new GetBatteryStateCommand(listenerMock);
		target.run();
	}
	
	/**
	 * Send a {@link RequestCommand#GET_BATTERY_STATE}
	 * and receive a {@link ResponseMessage#BATTERY_STATE}
	 * with 0% charging level. 
	 */
	public void testRun_0Percent() {
		// Configure Test 
		byte[] mockRespone = createArray(new byte[] { 0x06, 0x00, 0x00 }, (byte) 0xB7 );
		prepareStreams(mockRespone);
		listenerMock.batteryStateReceived(0f);
		
		// Run Test
		runMyTest();
		
		// Verify Test
		verifyTest();
	}
	
	/**
	 * Send a {@link RequestCommand#GET_BATTERY_STATE}
	 * and receive a {@link ResponseMessage#BATTERY_STATE}
	 * with 100% charging level. 
	 */
	public void testRun_100Percent() {
		// Configure Test 
		byte[] mockRespone = createArray(new byte[] { 0x06, 0x7F, -0x01 }, (byte) 0x8C ); /** {@link Short#MAX_VALUE} */
		prepareStreams(mockRespone);
		listenerMock.batteryStateReceived(100f);
		
		// Run Test
		runMyTest();
		
		// Verify Test
		verifyTest();
	}
	
	/**
	 * Send a {@link RequestCommand#GET_BATTERY_STATE}
	 * and receive a {@link ResponseMessage#BATTERY_STATE}
	 * with 50% charging level. 
	 */
	public void testRun_50Percent() {
		// Configure Test 
		byte[] mockRespone = createArray(new byte[] { 0x06, 0x40, 0x00 }, (byte) 0x58 ); /** ~ {@link Short#MAX_VALUE}/2 */
		prepareStreams(mockRespone);
		listenerMock.batteryStateReceived(AndroidMock.eq(50f, 0.01f));
		
		// Run Test
		runMyTest();
		
		// Verify Test
		verifyTest();
	}
}
