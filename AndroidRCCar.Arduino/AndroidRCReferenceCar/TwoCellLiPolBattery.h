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

#ifndef __TwoCellLipolBattery_h__
#define __TwoCellLipolBattery_h__

#define __STDC_LIMIT_MACROS
#include <stdint.h>

/**
 * @brief The TwoCellLiPolBattery class provides functionality for 
 *        reading the battery power level of a two cell Lithium-ion polymer battery.
 *        It needs the a analog input pin where it can read the voltage of one cell
 *        and another analog pin where it can read the half voltage of both cells.
 */
class TwoCellLiPolBattery {
	public:
        /**
         * @brief Default constructor
         * @param enablePin The pin that will be set before reading the anlog input, unset after.
         *                  It will be used for enabling the optocopplers between the battery cells an the analog pins.
         * @param bothCellAnalogInPin The pin with the half voltage of both cells.
         * @param oneCellAnalogInPin The pin with the voltag of cell one.
         */
		TwoCellLiPolBattery(uint8_t enablePin, uint8_t bothCellAnalogInPin, uint8_t oneCellAnalogInPin):
			enablePin(enablePin),
			bothCellInPin(bothCellAnalogInPin),
			oneCellInPin(oneCellAnalogInPin),
			percent(0),
			batteryEmptyVal(true)
			{}
			
		/**
		 * Configures the ports.
		 */
		void setUp();
		
		/**
		 * Read a new value from the input pins.
		 */
		void update();
		
		/**
		 * Checks if the battery is empty.
		 * This means, when one of the two cells is discharged under 3.3V.
		 * @note The µController is working at this point, but the battery could be damaged if discharged under 3.0 V.
		 * 		 So 3.3V is a recommend point to warn the user.
		 */
		bool batteryNearEmpty();
		
		/**
		 * Returns a value from 0 to INT16_MAX for 0 to 100 percent battery power.
		 */
		int getValue();
	protected:
	private:
		uint8_t enablePin;
		uint8_t bothCellInPin;
		uint8_t oneCellInPin;
		int16_t percent;
		

		bool batteryEmptyVal;
		
		/**
		 * One battery cell shouldn't discharged under 3.3V
		 * The µController maps from 0 to 5 volts to a integer value from 0 to 1023
		 * So our minium value is: 3.3 V / 5 V * 1023
		 */
		static const int minPerCell = 675;
		
		/**
		 * One battery cell is full charged, wenn it's reached 4.22 volts.
		 * So our maximum value is: 4.22 V / 5 V * 1023
		 * @see minPerCell
		 */
		static const int maxPerCell = 863;
};

#endif
