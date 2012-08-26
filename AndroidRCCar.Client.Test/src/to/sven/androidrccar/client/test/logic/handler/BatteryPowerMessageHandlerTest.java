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
import to.sven.androidrccar.client.logic.handler.BatteryPowerMessageHandler;
import to.sven.androidrccar.common.communication.model.BatteryPowerMessage;
import to.sven.androidrccar.common.exception.InvalidMessageException;

/**
 * This class should test the behavior of the {@link BatteryPowerMessageHandler}.
 * @author sven
 *
 */
public class BatteryPowerMessageHandlerTest
	extends AbstractTestCaseForClientMessageHandler<BatteryPowerMessageHandler> {
	
	/**
	 * Tests {@link BatteryPowerMessageHandler#handleMessage}.
	 * Test with 0% and 100% battery power.
	 * @throws InvalidMessageException The test fails.
	 */
	public void testHandleMessage_Success() throws InvalidMessageException {
		// Configure Test
		logicListenerMock.setBatteryPower(0);
		logicListenerMock.setBatteryPower(100);
		
		// Run Test
		BatteryPowerMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new BatteryPowerMessage(0));
		handler.handleMessage(new BatteryPowerMessage(100));
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link BatteryPowerMessageHandler#handleMessage}.
	 * Test with -1% and 101%  battery power.
	 * A {@link InvalidMessageException} should be thrown.
	 */
	public void testHandleMessage_InvalidMessageToLess() {
		// Configure Test
		// No calls expected, because handleMessage() Fails
		
		// Run Test
		BatteryPowerMessageHandler handler = aHandlerForTest();
		try {
			handler.handleMessage(new BatteryPowerMessage(-1));
			fail("No exception thrown.");
		} catch(InvalidMessageException e) {
			assertEquals("A message of type "+BatteryPowerMessage.class.toString() +
						 " contains a invalid value (-1.0) in member chargingLevel.",
					 	 e.getMessage());
		}
		
		try {
			handler.handleMessage(new BatteryPowerMessage(101));
			fail("No exception thrown.");
		} catch(InvalidMessageException e) {
			assertEquals("A message of type "+BatteryPowerMessage.class.toString() +
					     " contains a invalid value (101.0) in member chargingLevel.",
						 e.getMessage());
		}

		// Verify Test
		verifyFieldMocks();
	}
}
