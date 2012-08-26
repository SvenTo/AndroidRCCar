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

import to.sven.androidrccar.client.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Draws a rectangle with the bearing of the car.
 * @author sven
 *
 */
public class BearingOfCarView extends View {

	/**
	 * {@link Paint} for drawing of the border
	 */
	private final Paint borderPaint = new Paint();
	/**
	 * {@link Paint} for filling the shapes.
	 */
	private final Paint fillPaint = new Paint();
	/**
	 * {@link Paint} for the text.
	 */
	private final Paint textPaint = new Paint();
	/**
	 * The rectangle
	 */
	private RectF rect;
	/**
	 * The border size
	 */
	private float borderSize;
	/**
	 * The center of the rectangle.
	 */
	private float center;
	
	/**
	 * Bearing in degree from north to east. 
	 */
	private float bearing = 0;
	/**
	 * Left-Position of the north string.
	 */
	private float northPos;
	/**
	 * Left-Position of the south string.
	 */
	private float southPos;
	/**
	 * Left-Position of the east string.
	 */
	private float eastPos;
	/**
	 * Left-Position of the west string.
	 */
	private float westPos;
	/**
	 * Top-Position of all texts.
	 */
	private float textTop;
	
	/**
	 * @see View#View(Context, AttributeSet, int)
	 */
	public BearingOfCarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * @see View#View(Context, AttributeSet)
	 */
	public BearingOfCarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * @see View#View(Context)
	 */
	public BearingOfCarView(Context context) {
		super(context);
		init();
	}
	
	/**
	 * Initializes the fixed values like colors.  
	 */
	private void init() {
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeJoin(Paint.Join.ROUND);
		borderPaint.setStrokeCap(Paint.Cap.ROUND);
		borderPaint.setAntiAlias(true);
		borderPaint.setShadowLayer(0.2f, 0.0f, 0.0f, 0xFF000000);
		fillPaint.setARGB(50, 255, 255, 255);
		fillPaint.setAntiAlias(true);
		
		textPaint.setAntiAlias(true);
		textPaint.setARGB(255, 255, 255, 255);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_size));
		
		textPaint.setShadowLayer(0.02f, 0.0f, 0.0f, 0xFF000000);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawRoundRect(rect, borderSize, borderSize, borderPaint);
		canvas.drawRoundRect(rect, borderSize, borderSize, fillPaint);
		canvas.drawLine(center, 0, center, getHeight(), borderPaint);
		canvas.drawText("N", northPos, textTop, textPaint);
		canvas.drawText("W", westPos, textTop, textPaint);
		canvas.drawText("S", southPos, textTop, textPaint);
		canvas.drawText("E", eastPos, textTop, textPaint);
	}
	
	/**
	 * Sets the bearing.
	 * @param bearing Bearing in degree from north to east. 
	 */
	public void setBearing(float bearing) {
		this.bearing = bearing;
		
		northPos = mapPos(bearing);
		southPos = mapPos(bearing+180);
		eastPos = mapPos(bearing-90);
		westPos = mapPos(bearing+90);
		
		invalidate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		float height = h;
		float width = w;
		float strokeWidth = height/20;
		borderPaint.setStrokeWidth(strokeWidth);
		float margin = strokeWidth/2;
		rect = new RectF(margin, margin, w-margin, getHeight()-margin);
		center = width/2;
		
		setBearing(bearing);
		
		textTop = h/2+textPaint.getTextSize()/2;
		invalidate();
	}
	
	/**
	 * Maps the dregreePos from -360 to 360 into -180 to 180.
	 * @param degreePos Value to map
	 * @return Mapped value
	 */
	private float mapPos(float degreePos) {
		if(degreePos > 180) {
			degreePos -= 360;
		}
		if(degreePos < -180) {
			degreePos += 360;
		}
		
		return center-(degreePos/180*center);
	}
}
