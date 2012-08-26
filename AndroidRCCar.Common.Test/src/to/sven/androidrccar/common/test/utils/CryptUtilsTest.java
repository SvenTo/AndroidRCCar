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
package to.sven.androidrccar.common.test.utils;

import to.sven.androidrccar.common.utils.CryptUtils;
import android.test.AndroidTestCase;

/**
 * This class should test the behavior of the {@link CryptUtils}.
 * @author sven
 *
 */
public class CryptUtilsTest extends AndroidTestCase {
	
	/**
	 * Tests {@link CryptUtils#encryptPassword}:
	 * Encrypt three password with salts and check the result.
	 */
	public void testEncryptPassword() {
		String password = "AVerySecurePasswort";
		String salt = "8f210a0962f541e1bc5954f7f1279fa98fad0de3";
		String expected = "5e6291665974e84bd707c37bb7b4b0d6fc709803";
		testEncryptPasswordHelper(password, salt, expected);

		password = "FooBar";
		salt = "36b0ba052efeb5c91581e785fc41f4ee61193924";
		expected = "4ddd521ed26364db37e280c91f0aa02e7876fcc7";
		testEncryptPasswordHelper(password, salt, expected);

		password = "password";
		salt = "34973274ccef6ab4dfaaf86599792fa9c3fe4689";
		expected = "9fb82790600356201f6803437f884191bd12c817";
		testEncryptPasswordHelper(password, salt, expected);
	}
	
	/**
	 * Runs {@link CryptUtils#encryptPassword} and check if the result
	 * is the {@code expected} value.
	 * @param password The password to encrypt...
	 * @param salt ...with this salt.
	 * @param expected What is the result? (hopefully)
	 */
	private void testEncryptPasswordHelper(String password,
										   String salt,
										   String expected) {
		String actual = CryptUtils.encryptPassword(password, salt);
		assertEquals(expected, actual);
	}
	
	/**
	 * Tests {@link CryptUtils#generateSalt}:
	 * Generate two salt, check its format and check if its not equal.
	 * This method tests NOT if the generated salts are really random.
	 * (This would be to complex, so see the implementation to check it.)
	 */
	public void testGenerateSalt() {
		String actual1 = CryptUtils.generateSalt();
		checkSalt(actual1);
		String actual2 = CryptUtils.generateSalt();
		checkSalt(actual2);
		assertFalse(actual1.equals(actual2));
	}
	
	/**
	 * Check if the value is a hex encoded 160-bit-value {@code salt}.
	 * @param actual String to check.
	 */
	private void checkSalt(String actual) {
		assertTrue("No hex encoded 160-bit value: " + actual,
				   actual.matches("[a-f0-9]{40}"));
	}
}
