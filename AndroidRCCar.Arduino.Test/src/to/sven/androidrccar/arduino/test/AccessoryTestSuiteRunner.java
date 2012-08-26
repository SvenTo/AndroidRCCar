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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The {@link AccessoryTestSuiteRunner} runs the {@link TestSuite}.
 * @author sven
 *
 */
public class AccessoryTestSuiteRunner implements Runnable {
	
	private TestSuite testSuite;
	private Exception exception;
	private volatile FileInputStream inputStream;
	private volatile FileOutputStream outputStream;
	private volatile AccessoryTestSuiteRunnerListener listener;
	private volatile Handler handler;
	
	/**
	 * Creates a new {@link AccessoryTestSuiteRunner}
	 * @param testSuite A Stream where the {@link TestSuite} can be read from.
	 * @throws Exception Could not read the {@link TestSuite}
	 */
	public AccessoryTestSuiteRunner(InputStream testSuite) throws Exception {
		ObjectMapper jsonMapper = new ObjectMapper();
		JsonFactory factory = jsonMapper.getJsonFactory();
		
		JsonParser jsonParser;
		jsonParser = factory.createJsonParser(testSuite);
		this.testSuite = jsonParser.readValueAs(TestSuite.class);
	}
    
	/**
	 * Returns the number of tests.
	 * @return number of tests
	 */
    public int getTestCount() {
    	return testSuite.tests.length;
    }
    
    /**
     * Returns the title of the test suite.
     * @return title of the test suite
     */
    public String getTestTitle() {
    	return testSuite.title;
    }
    
    /**
     * Runs the {@link TestSuite}.
     * @param inputStream
     * @param outputStream
     * @param listener
     */
    public synchronized void runTests(FileInputStream inputStream,
    								  FileOutputStream outputStream, 
    								  AccessoryTestSuiteRunnerListener listener) {
    	if(handler == null) {
			this.inputStream =  inputStream;
			this.outputStream =  outputStream;
    		handler = new Handler();
    		this.listener = listener;
    		new Thread(this).start();
    	}
    }
    
    /**
     * Generates the Report. If a exception occurred, the exception will be added.
     * @return The Report
     */
    public String getReport() {
    	if(exception != null) {
    		return "Exception occurred:\n" + exception.toString() +
    				"\n\nReport:\n" + testSuite.toString();
    	}
    	return testSuite.toString();
    }

    /**
     * Should only started with #runTests
     * Runs the test inside the test runner thread.
     */
	public void run() {
		try {
    		testSuite.reset();
    		exception = null;
    		
    		int i = 0;
    		for (Test test : testSuite.tests) {
				postStatusUpdate(test, ++i);
				outputStream.write(test.sendData);
    			
    			waitForAccessory(testSuite.waitBeforeReadResponse);
    			
    			byte[] buffer = new byte[16];
				int len = inputStream.read(buffer);
				if(len == -1) {
					throw new IOException("End of File");
				}
				test.responseData = new byte[len];
			    System.arraycopy(buffer, 0, test.responseData, 0, len);

    			waitForAccessory(testSuite.waitBetweenTests);
    			test.isRunned = true;
			}
		} catch(Exception e) {
			exception = e;
		} finally {
			synchronized(this) {
				outputStream = null;
				inputStream = null;
				handler.post(new Runnable() {
					public void run() {
						listener.done();
						listener = null;
					}
				});
				handler = null;
			}
		}
	}
	
	/**
	 * Posts a message, if a new test is running.
	 * @param test The new test
	 * @param i The number of the test.
	 */
	private void postStatusUpdate(final Test test, final int i) {
		handler.post(new Runnable() {
			public void run() {
				listener.statusUpdate("Run "+i+"/"+testSuite.tests.length+": "+test.title);
			}
		});
	}
	
	/**
	 * Waits some milliseconds
	 * @param millisec The milliseconds to wait.
	 * @throws InterruptedException Someone interrupted us
	 */
	private void waitForAccessory(long millisec) throws InterruptedException {
		Object monitor = new Object();
		synchronized(monitor) {
			monitor.wait(millisec);
		}
	}
}
