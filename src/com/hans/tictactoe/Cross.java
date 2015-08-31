package com.hans.tictactoe;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
 
public class Cross extends Cell {
 
	public Cross(int x, int y) {
        super(x, y);
    }
 
    public Cross(int x, int y, boolean normal) {
        super(x, y);
    	this.normal = normal;
    }
    
    public void draw(Canvas g, Resources res, int x, int y, int w, int h) {
    	int img = normal? R.drawable.ie:R.drawable.ie1;
        Bitmap im = BitmapFactory.decodeResource(res, img);
        g.drawBitmap(im, null, new Rect(x*w, y*h, (x*w)+w, (y*h)+h), new Paint());
    }
 
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cross) {
            return true;
        } else {
            return false;
        }
    }
 
    @Override
    public String toString() {
        return "X";
    }
}