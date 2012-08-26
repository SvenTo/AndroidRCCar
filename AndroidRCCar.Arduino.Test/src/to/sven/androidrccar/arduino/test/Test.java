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
package to.sven.androidrccar.arduino.test;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * One test case.
 * @author sven
 *
 */
public class Test {
	
	/**
	 * Creates a new test case.
	 * @param title Title of the TestCase
	 * @param sendData Data to send as hex encoded string.
	 * @param exceptedResponseData Excepted response data as as hex encoded string or null, if #printResponseOnly is set
	 * @param printResponseOnly No comparison of the response data with some excepted ones 
	 */
	@JsonCreator
	public Test(@JsonProperty("title") String title,
				@JsonProperty("sendData") String sendData,
				@JsonProperty("exceptedResponseData") String exceptedResponseData,
				@JsonProperty("printResponseOnly") boolean printResponseOnly) {
		this.title = title;
		this.printResponseOnly = printResponseOnly;
		this.sendData = hexToBytes(sendData);
		if(!printResponseOnly) {
			this.exceptedResponseData = hexToBytes(exceptedResponseData);
		} else {
			this.exceptedResponseData = null;
		}
	}
	
	/**
	 * Title of the TestCase
	 */
	public final String title;
	
	/**
	 * Data to send
	 */
	public final byte[] sendData;
	
	/**
	 * Excepted response data
	 */
	private final byte[] exceptedResponseData;
	
	/**
	 * No comparison of the response data with some excepted ones
	 */
	public final boolean printResponseOnly;
	
	/**
	 * The real response data
	 */
	public byte[] responseData;
	
	/**
	 * Was the Test executed?
	 */
	public boolean isRunned;
	
	/**
	 * Is the Test successfully executed?
	 * @return True, if it was successfully
	 */
	public boolean wasSuccessfull() {
		return (printResponseOnly || assertEqual());
	}
	
	/**
	 * Check if every element in {@code actual} equals with
	 * the element at the same position in {@code expected}.
	 * If both null, it returns true.
	 * If one is null, it returns false.
	 */
	private boolean assertEqual() {
		if(exceptedResponseData == null || responseData == null) {
			return (exceptedResponseData == null && responseData == null);
		}
		
		if(exceptedResponseData.length != responseData.length) {
			return false;
		}
		
		for(int i = 0; i < exceptedResponseData.length; i++) {
			if(exceptedResponseData[i] != responseData[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Append the test result to the {@link StringBuilder}.
	 * @param builder The {@link StringBuilder}
	 */
	private void appendResult(StringBuilder builder) {
		if(!isRunned) {
			builder.append("Not executed");
		} else {
			if(printResponseOnly) {
				appendHex(builder, responseData);
			} else if(wasSuccessfull()) {
				builder.append("OK");
			} else {
				builder.append("Failed - Excepted: ");
				appendHex(builder, exceptedResponseData);
				builder.append("; Actual: ");
				appendHex(builder, responseData);
			}
		}
	}
	
	/**
	 * Append the given bytes as hex encoded string to the {@link StringBuilder}.
	 * @param builder The {@link StringBuilder}
	 * @param bytes The bytes
	 */
	private static void appendHex(StringBuilder builder, byte[] bytes){
		if(bytes == null) {
			builder.append("null");
		} else {
	        char[] chars = "0123456789ABCDEF".toCharArray();
	 
	        for(byte b : bytes) {
	        	int unsigned = b & 0xff;
	        	builder.append(chars[unsigned >> 4]);
	        	builder.append(chars[unsigned & 0x0f]);
        		builder.append(' ');
	        }
		}
    }
	
	/**
	 * Converts a string with hex encoded bytes to a byte array.
	 * Spaces will be ignored.
	 * @param hex The string
	 * @return a hex encoded byte array
	 */
	private byte[] hexToBytes(String hex) {
		hex = hex.replace(" ", "");
		byte[] result = new byte[hex.length()/2];
		byte[] bytes = new BigInteger(hex, 16).toByteArray();
		System.arraycopy(bytes, 0, result, result.length-bytes.length, bytes.length);
		return result;
	}
	
	/**
	 * Append the result to the passed {@link StringBuilder}.
	 * @param builder The passed {@link StringBuilder}.
	 * @return  The passed {@link StringBuilder}.
	 */
	public StringBuilder toStringBuilder(StringBuilder builder) {
		builder.append(title)
		       .append(": ");
		appendResult(builder);
		return builder.append("\n");
	}
	
	/**
	 * The result as String
	 * @return The result
	 */
	public String toString() {
		return toStringBuilder(new StringBuilder()).toString();
	}
	
	/**
	 * Reset test case, so it wasn't executed.
	 */
	public void reset() {
		isRunned = false;
		responseData = null;
	}
}
