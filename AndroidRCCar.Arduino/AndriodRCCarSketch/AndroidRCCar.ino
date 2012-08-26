/*
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
 */
 
#define __STDC_LIMIT_MACROS
#include<Arduino.h>
#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>
#include <AFMotor.h>
#include <Servo.h> 
#include <AndroidRCCarCommunication.h>
#include <AndroidRCReferenceCar.h>

AndroidRCReferenceCar car;
bool stopped = true;

/**
 * @brief Set up the car.
 */
void setup() {
	Serial.begin(9600);
	car.setUp();
	Serial.println("setup done."); // TODO: Debug
}

/**
 * @brief Checks, if the car is connected to a phone and reads the next message.
 *        If the car is not connected (anymore), the car will be stopped.
 */
void loop() {
	if(car.isConnected()) {
		stopped = false;
		car.handleNextCommand();
	} else {
		if(!stopped) {
			car.resetAll();
			stopped = true;
			//  TODO: Check Battery State and Power off if too low?
		}
		delay(50);
	}
}
