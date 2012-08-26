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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A test suite with list of test cases
 * @author sven
 *
 */
public class TestSuite {

	/**
	 * Creates a new test suite.
	 * @param title The title of the suite
	 * @param tests A list of tests
	 * @param waitBeforeReadResponse How many milliseconds should be waited before read the response
	 * @param waitBetweenTests How many milliseconds should be waited between tests
	 */
	@JsonCreator
	public TestSuite(@JsonProperty("title") String title,
					 @JsonProperty("tests") Test[] tests,
					 @JsonProperty("waitBeforeReadResponse") int waitBeforeReadResponse,
					 @JsonProperty("waitBetweenTests") int waitBetweenTests) {
		this.title = title;
		this.tests = tests;
		this.waitBeforeReadResponse = waitBeforeReadResponse;
		this.waitBetweenTests = waitBetweenTests;
	}
	
	/**
	 * Title of the Test Suite
	 */
	public final String title;
	
	/**
	 * A list of Tests
	 */
	public final Test[] tests;
	
	/**
	 * How many milliseconds should be waited between after sending command
	 * before read response data?
	 */
	public final int waitBeforeReadResponse;
	
	/**
	 * How many milliseconds should be waited between each test?
	 */
	public final int waitBetweenTests;
	
	/**
	 * Resets all tests
	 * @see Test#reset()
	 */
	public void reset() {
		for(Test test : tests) {
			test.reset();
		}
	}
	
	/**
	 * Build a report from the {@link TestSuite}.
	 * @return The report
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int total = tests.length;
		int runned = 0;
		int failed = 0;
		for(Test test : tests) {
			test.toStringBuilder(builder);
			runned += test.isRunned?1:0;
			failed += test.wasSuccessfull()?0:1;
		}
		
		builder.insert(0, "Test " + title + "\nTotal: "+total+ " Runned: "+runned+ " Failed: "+failed + "\n");
		
		return builder.toString();
	}
}
