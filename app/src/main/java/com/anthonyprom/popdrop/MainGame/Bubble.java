package com.anthonyprom.popdrop.MainGame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Anthony on 6/11/2016.
 */
public class Bubble {
    int diameter = 0;
    int value = 0;
    int spd = 0;
    int timeAlive;
    int xCoord = 0;
    int yCoord = 0;
    Paint textPaint = new Paint();
    int txtSize = 75;
    Paint bubblePaint = new Paint();
    Paint outsidePaint = new Paint();

    public Bubble(int w, int x, int num, int speed, Canvas c){
        diameter = w;
        xCoord = x;
        value = num;
        spd = speed;
        timeAlive = 0;
        drawBubble(c);
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(txtSize);
    }

    public void setBubbleSize(int size){
        this.diameter = size;
    }

    public int getBubbleSize(){
        return this.diameter;
    }

    public void setBubbleVal(int vlue){
        this.value = vlue;
    }

    public int getBubbleVal(){
        return this.value;
    }

    public void setBubbleSpeed(int speed){
        this.spd = speed;
    }

    public int getBubbleSpeed(){
        return this.spd;
    }

    public void moveBubble(int newY){
        yCoord = newY + spd;
    }

    public void getRanBubbleColor(){
        int colorInt = (int)(Math.random()*5);
        if(colorInt == 0){
            bubblePaint.setColor(Color.CYAN);
            outsidePaint.setColor(Color.CYAN);
        }
        else if(colorInt == 1){
            bubblePaint.setColor(Color.GREEN);
            outsidePaint.setColor(Color.GREEN);
        }
        else if(colorInt == 2){
            bubblePaint.setColor(Color.YELLOW);
            outsidePaint.setColor(Color.YELLOW);
        }
        else if(colorInt == 3){
            bubblePaint.setColor(Color.MAGENTA);
            outsidePaint.setColor(Color.MAGENTA);
        }
        else if(colorInt == 4){
            bubblePaint.setColor(Color.RED);
            outsidePaint.setColor(Color.RED);
        }
        else{
            bubblePaint.setColor(Color.WHITE);
            outsidePaint.setColor(Color.WHITE);
        }
    }

    public void drawBubble(Canvas canvas){
        //bubblePaint.setColor(Color.CYAN);
        bubblePaint.setAlpha(100);
        canvas.drawCircle(xCoord, yCoord, diameter, bubblePaint);
        outsidePaint.setStyle(Paint.Style.STROKE);
        outsidePaint.setStrokeWidth(10);
        canvas.drawCircle(xCoord, yCoord, diameter, outsidePaint);
        String text = "" + value;
        Rect r = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), r);
        canvas.drawText(text, xCoord-(Math.abs(r.width()/2)), yCoord - ((textPaint.descent() + textPaint.ascent())/2), textPaint);
    }

}
