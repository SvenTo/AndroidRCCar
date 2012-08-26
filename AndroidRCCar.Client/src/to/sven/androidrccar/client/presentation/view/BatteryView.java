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
package to.sven.androidrccar.client.presentation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Draws a battery icon
 * The remaining capacity can set with {@link #setChargingLevel(float)}.
 * @author sven
 *
 */
public class BatteryView extends View {
	
	/**
	 * {@link Paint} for the border. 
	 */
	private final Paint borderPaint = new Paint();
	
	/**
	 * {@link Paint} for the available capacity. 
	 */
	private final Paint contentPaint = new Paint();
	
	/**
	 * The charging level of the battery in percent (0f-100f).
	 */
	private float chargingLvl;
	
	/**
	 * The rectangle over the batteries  
	 */
	private RectF borderRect;
	
	/**
	 * Rectangle over the available capacity.
	 */
	private RectF powerRect;
	/**
	 * Rectangle of the battery icons plus pin.
	 */
	private RectF pinRect;
	/**
	 * Radius of the battery icons border. 
	 */
	private float borderRadius;
	
	/**
	 * @see View#View(Context, AttributeSet, int)
	 */
	public BatteryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	/**
	 * @see View#View(Context, AttributeSet)
	 */
	public BatteryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	/**
	 * @see View#View(Context)
	 */
	public BatteryView(Context context) {
		super(context);
		init();
	}
	
	/**
	 * Initializes the fixed values like colors.  
	 */
	private void init() {
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setAntiAlias(true);
		contentPaint.setARGB(100, 0, 255, 0);
		contentPaint.setAntiAlias(true);
		setChargingLevel(100);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawRoundRect(powerRect, borderRadius, borderRadius, contentPaint);
		canvas.drawRoundRect(borderRect, borderRadius, borderRadius, borderPaint);
		canvas.drawRoundRect(pinRect, borderRadius, borderRadius, borderPaint);
	}
	
	/**
	 * Sets the charging level. The view will be redrawn at some point in future.
	 * @param chargingLevel The charging level of the battery in percent (0f-100f).
	 */
	public void setChargingLevel(float chargingLevel) {
		chargingLvl = chargingLevel;
		
		float strokeWidth = borderPaint.getStrokeWidth();
		float width = getWidth()-strokeWidth;
		float margin = strokeWidth/2;
		borderRect = new RectF(margin, margin, width-margin, getHeight()-margin);
		pinRect = new RectF(width, getHeight()*0.30f, getWidth()-margin, getHeight()*0.70f);
		powerRect = new RectF(borderRect);
		powerRect.right = ((powerRect.right-strokeWidth)*chargingLevel/100)+strokeWidth;
		
		if(chargingLvl < 10) {
			contentPaint.setARGB(100, 255, 0, 0);
		} else {
			contentPaint.setARGB(100, 0, 255, 0);
		}
		
		invalidate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		float height = h;
		borderRadius = height/5;
		borderPaint.setStrokeWidth(height/10);
		setChargingLevel(chargingLvl);
	}
}
