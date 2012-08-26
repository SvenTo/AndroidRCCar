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
package to.sven.androidrccar.common.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import android.util.Log;

// TODO: Test this:

/**
 * This class contain utilities for password encryption.
 * @author sven
 *
 */
public class CryptUtils {
	/**
	 * Used as Log Tag.
	 * @see Log
	 */
	private static final String LOG_TAG = "CryptUtils";
	
	/**
	 * This class can not be instantiate, it contain only static methods.
	 */
	private CryptUtils() {}
	
	/**
	 * Generates a SHA-1 hash of password and salt and returns it as hex-encoded string.
	 * @param password the password to encrypt
	 * @param salt random string that should be used to salt the password
	 * @return hex-encoded string of hash
	 */
	public static String encryptPassword(String password, String salt) {
		final String algorithm = "SHA-1";
		try {
			MessageDigest digester = MessageDigest.getInstance(algorithm);
			byte[] saltedPassword = (password + salt).getBytes();
			byte[] hash = digester.digest(saltedPassword);
			
			return String.format("%040x",new BigInteger(1, hash));
		} catch (NoSuchAlgorithmException e) {
			Log.e(LOG_TAG, "Algorithm " + algorithm, e);
			return null;
		}
	}
	
	/**
	 * Generates a random 160-bit-value and returns it as hex-encoded string.
	 * @return 160 bit hex encoded random
	 */
	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt =	random.generateSeed(20);
		return String.format("%040x", new BigInteger(1, salt));
	}
}
