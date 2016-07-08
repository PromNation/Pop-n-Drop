package com.anthonyprom.popdrop.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.anthonyprom.popdrop.MainGame.MainActivity;
import com.anthonyprom.popdrop.R;
import com.anthonyprom.popdrop.Settings.SettingsActivity;

/**
 * Created by Anthony on 6/23/2016.
 */
public class MenuView extends SurfaceView implements SurfaceHolder.Callback{

    boolean DEVMODEENABLED = false;
    int DEVCOUNT = 0;
    Bitmap bgBitmap;
    MenuThread thread;
    Context con;
    SurfaceHolder h;
    int SCREENWIDTH;
    int SCREENHEIGHT;
    Paint startPaint;
    Paint textPaint;
    Paint outStartPaint;
    MenuActivity menu;

    public MenuView(Context context) {
        super(context);
        con = context;
        init();
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        con = context;
        init();
    }

    public void init(){
        menu = (MenuActivity)getContext();
        startPaint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        h = getHolder();
        h.addCallback(this);
    }

    public void doDraw(Canvas canvas){
        startPaint.setColor(Color.RED);
        startPaint.setAlpha(100);
        outStartPaint = new Paint();
        outStartPaint.setStrokeWidth(10);
        outStartPaint.setStyle(Paint.Style.STROKE);
        SCREENWIDTH = canvas.getWidth();
        SCREENHEIGHT = canvas.getHeight();
        Rect r = new Rect();
        String startText = "Start Game";
        String options = "Options";
        String leaderboard = "Leaderboard";
        textPaint.getTextBounds(startText, 0, startText.length(), r);
        outStartPaint.setColor(Color.RED);
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawCircle(SCREENWIDTH/2, SCREENHEIGHT/2, 150, startPaint);
        canvas.drawCircle(SCREENWIDTH/2, SCREENHEIGHT/2, 150, outStartPaint);
        canvas.drawText(startText, (SCREENWIDTH/2)-(Math.abs(r.width()/2)), (SCREENHEIGHT / 2) - ((textPaint.descent() + textPaint.ascent())/2), textPaint);
        startPaint.setColor(Color.BLUE);
        outStartPaint.setColor(Color.BLUE);
        startPaint.setAlpha(100);
        textPaint.getTextBounds(options, 0, options.length(), r);
        canvas.drawCircle(SCREENWIDTH/2, 2*(SCREENHEIGHT/3), 150, startPaint);
        canvas.drawCircle(SCREENWIDTH/2, 2*(SCREENHEIGHT/3), 150, outStartPaint);
        canvas.drawText(options, (SCREENWIDTH/2)-(Math.abs(r.width()/2)), 2*(SCREENHEIGHT / 3)  - ((textPaint.descent() + textPaint.ascent())/2), textPaint);

        startPaint.setColor(Color.GREEN);
        outStartPaint.setColor(Color.GREEN);
        startPaint.setAlpha(100);
        textPaint.getTextBounds(leaderboard, 0, leaderboard.length(), r);
        canvas.drawCircle(SCREENWIDTH/2, (SCREENHEIGHT/2) + (SCREENHEIGHT/3), 150, startPaint);
        canvas.drawCircle(SCREENWIDTH/2, (SCREENHEIGHT/2) + (SCREENHEIGHT/3), 150, outStartPaint);
        canvas.drawText(leaderboard, (SCREENWIDTH/2)-(Math.abs(r.width()/2)), ((SCREENHEIGHT/2) + (SCREENHEIGHT/3))  - ((textPaint.descent() + textPaint.ascent())/2), textPaint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap bmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        float scale = (float) bmap.getHeight() / (float) getHeight();
        int newWidth = Math.round(bmap.getWidth() / scale);
        int newHeight = Math.round(bmap.getHeight() / scale);
        bgBitmap = Bitmap.createScaledBitmap(bmap, newWidth, newHeight, true);
        thread = new MenuThread(h, con, this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if(x > ((SCREENWIDTH/2)-150) && (x < (SCREENWIDTH/2)+150)){
                if(y > ((SCREENHEIGHT/2)-150) && y < ((SCREENHEIGHT/2)+150)){
                    System.out.println("Game Accessed");
                    Intent gameIntent = new Intent(con, MainActivity.class);
                    thread.interrupt();
                    thread.setRunning(false);
                    con.startActivity(gameIntent);
                }
            }
            if(x > ((SCREENWIDTH/2)-150) && x < ((SCREENWIDTH/2)+150)){
                if(y > ((2*(SCREENHEIGHT/3))-150) && y < ((2*(SCREENHEIGHT/3))+150)){
                    Intent settingsIntent = new Intent(con, SettingsActivity.class);
                    int requestCode = 1;
                    Activity act = (Activity)getContext();
                    act.startActivityForResult(settingsIntent, requestCode);
                    System.out.println("Settings Accessed");
                }
            }
            if(x > ((SCREENWIDTH/2)-150) && x < ((SCREENWIDTH/2)+150)){
                if(y > (((SCREENHEIGHT/2)+SCREENHEIGHT/3)-150) && y < (((SCREENHEIGHT/2)+SCREENHEIGHT/3)+150)){
                    menu.onShowLeaderboardsRequested();
                }
            }
            if(DEVCOUNT == 0) {
                if (x < 100 && y < 100) {
                    DEVCOUNT = 1;
                    System.out.println("CLICK");
                }
            }
            else if(DEVCOUNT == 1){
                if(x > (SCREENWIDTH - 100) && y > (SCREENHEIGHT - 100)){
                    DEVCOUNT = 2;
                    System.out.println("CLICK");
                }
                else{
                    DEVCOUNT = 0;
                }
            }
            else if(DEVCOUNT == 2){
                if(x < 100 && y > (SCREENHEIGHT - 100)){
                    DEVCOUNT = 3;
                    System.out.println("CLICK");
                }
                else{
                    DEVCOUNT = 0;
                }
            }
            else if(DEVCOUNT == 3){
                if(x > (SCREENWIDTH - 100) && y < 100){
                    DEVCOUNT = 4;
                    DEVMODEENABLED = true;
                    System.out.println("Dev mode enabled");
                }
                else{
                    DEVCOUNT = 0;
                }
            }
        }
        return false;
    }

    public boolean isDEVMODEENABLED(){
        return DEVMODEENABLED;
    }

}
