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
package to.sven.androidrccar.host.presentation;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * A {@link EditTextPreference} for editing {@link Integer}-Values.
 * @author sven
 *
 */
public class IntEditTextPreference extends EditTextPreference {
	
	/**
	 * @see EditTextPreference#EditTextPreference(Context)
	 */
    public IntEditTextPreference(Context context) {
        super(context);
    }
    
	/**
	 * @see EditTextPreference#EditTextPreference(Context, AttributeSet)
	 */
    public IntEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
	/**
	 * @see EditTextPreference#EditTextPreference(Context, AttributeSet, int)
	 */
    public IntEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * {@inheritDoc}
     * For {@link Integer} values only.
     */
    @Override
    protected String getPersistedString(String defaultReturnValue) {
        return String.valueOf(getPersistedInt(-1));
    }

    /**
     * {@inheritDoc}
     * For {@link Integer} values only.
     */
    @Override
    protected boolean persistString(String value) {
        return persistInt(Integer.valueOf(value));
    }
}
