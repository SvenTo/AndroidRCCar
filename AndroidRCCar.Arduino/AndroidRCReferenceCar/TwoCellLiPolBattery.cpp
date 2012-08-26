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

#include "TwoCellLiPolBattery.h"
#include<Arduino.h>

void TwoCellLiPolBattery::setUp() {
	pinMode(enablePin, OUTPUT);
	pinMode(bothCellInPin, INPUT);
	digitalWrite(bothCellInPin, LOW);
	pinMode(oneCellInPin, INPUT);
	digitalWrite(oneCellInPin, LOW);
}

void TwoCellLiPolBattery::update() {
	digitalWrite(enablePin, HIGH);
	// Wait until the optocopler response (according datasheet: =< 18µS)
	delayMicroseconds(20);
	int bothCellValue = analogRead(bothCellInPin)*2; 
	int cellOneValue = analogRead(oneCellInPin);
	int cellTwoValue = bothCellValue-cellOneValue;
	digitalWrite(enablePin, LOW);
	
	batteryEmptyVal = !(cellOneValue >= minPerCell && cellTwoValue >= minPerCell);
	int moreEmptyCell = (cellOneValue < cellTwoValue)?cellOneValue:cellTwoValue;
	moreEmptyCell = constrain(moreEmptyCell, minPerCell, maxPerCell);
	percent = map(moreEmptyCell, minPerCell, maxPerCell, 0, INT16_MAX);
}

bool TwoCellLiPolBattery::batteryNearEmpty() {
	return batteryEmptyVal;
}

int16_t TwoCellLiPolBattery::getValue() {
	return percent;
}
