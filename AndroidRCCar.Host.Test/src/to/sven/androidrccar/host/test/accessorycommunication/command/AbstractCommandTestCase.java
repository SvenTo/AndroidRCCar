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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import to.sven.androidrccar.host.accessorycommunication.command.AbstractCommand;
import to.sven.androidrccar.host.accessorycommunication.command.ICommandListener;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

import android.test.AndroidTestCase;

/**
 * Abstract implementation for all command tests
 * (All classes that extends {@link AbstractCommand}). 
 * 
 * @author sven
 */
@UsesMocks({ICommandListener.class})
public abstract class AbstractCommandTestCase
	extends AndroidTestCase {
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	protected ICommandListener listenerMock;
	
	/**
	 * Mock input stream that stand for the connection to the µController.
	 */
	protected ByteArrayInputStream inputStream;
	
	/**
	 * Mock output stream that stand for the connection to the µController.
	 */
	protected ByteArrayOutputStream outputStream;
	
	/**
	 * Before Test:
	 * Set up new {@link ICommandListener} mock. 
	 * @see AndroidMock#createStrictMock
	 */
	@Override
	protected void setUp() {
		listenerMock = AndroidMock.createStrictMock(ICommandListener.class);
	}
	
	/**
	 * Set up the mock input and output streams
	 * that stand for the connection to the µController.
	 * 
	 * @param mockRespone The mock response that should "send from the µController".
	 */
	protected void prepareStreams(byte[] mockRespone) {
		/* The added byte should never read. */
		byte[] mockResponeWithExtraByte = concatByteArrays(mockRespone,
														   new byte[] {-0x80});
		inputStream = new ByteArrayInputStream(mockResponeWithExtraByte);
		outputStream = new ByteArrayOutputStream();
		
		AndroidMock.expect(listenerMock.getInputStream()).andReturn(inputStream);
		AndroidMock.expect(listenerMock.getOutputStream()).andReturn(outputStream);
	}
	
	/**
	 * Set all mock that created by {@link #setUp()} in replay state.
	 */
	protected void replay() {
		AndroidMock.replay(listenerMock);
	}
	
	/**
	 * Verifies all mock that created by {@link #setUp()}.
	 * Verifies that the response was read to the correct position.
	 * Verifies that the request was send as expected.
	 * @param expectedRequest The expected request bytes.
	 **/
	protected void verifyTest(byte[] expectedRequest) {
		byte[] actual = outputStream.toByteArray();
		assertArrayEquals(expectedRequest, actual);
		
		assertEquals("There should be exactly 1 unread byte.",
				 	 1, inputStream.available());
	
		AndroidMock.verify(listenerMock);
	}
	
	/**
	 * Check if every element in {@code actual} equals with
	 * the element at the same position in {@code expected}.
	 * @param expected What is expected
	 * @param actual What is actual there
	 */
	private void assertArrayEquals(byte[] expected, byte[] actual) {
		assertEquals("Array length not equal.", expected.length, actual.length);
		
		for(int i = 0; i < expected.length; i++) {
			assertEquals("Array is unequal at element "+i+".", expected[i], actual[i]);
		}
	}
	
	/**
	 * Concatenates n byte arrays.
	 * @param first First array for concatenation.
	 * @param arrays More arrays for concatenation.
	 * @return Concatenation of all arrays.
	 */
	protected byte[] concatByteArrays(byte[] first, byte[]... arrays) {
		int allLen = first.length;
		for(byte[] array : arrays) {
			allLen += array.length;
		}
		
		byte[] result = Arrays.copyOf(first, allLen);
		int pos = first.length;
		for(byte[] array : arrays) {
			System.arraycopy(array, 0, result, pos, array.length);
			pos += array.length;
		}
		return result;
	}
	
	/**
	 * Creates a byte array for testing request commands or response messages
	 * with a length of 16 bytes.
	 * @param start Will be copied a the begin of the array
	 * @param checksum Last element of the array (The checksum byte)
	 * @return Created array (all not filled byte will be 0x00)
	 */
	protected byte[] createArray(byte[] start, byte checksum) {
		final int len = 16;
		byte[] buffer = new byte[16];
		System.arraycopy(start, 0, buffer, 0, start.length);
		buffer[len-1] = checksum;
		return buffer;
	}
}
