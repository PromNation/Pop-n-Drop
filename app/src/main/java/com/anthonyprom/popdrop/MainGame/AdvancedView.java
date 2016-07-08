package com.anthonyprom.popdrop.MainGame;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.anthonyprom.popdrop.Menu.MenuActivity;
import com.anthonyprom.popdrop.R;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by Anthony on 7/7/16.
 */
public class AdvancedView extends View implements SurfaceHolder.Callback{

    Context con;
    Bitmap bgBitmap;
    SpawnThread spawnThread;
    public final static String EXTRA_MESSAGE = "com.anthonyprom.popdrop.MESSAGE";
    private final static String LEADERBOARD_ID = "CgkIuJG_jdMbEAIQBA";
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure = false;
    private boolean mSignInClicked = false;
    private boolean mAutoStartSignInFlow = true;//Auto start sign in
    private int SCREENWIDTH;
    private int SCREENHEIGHT;
    private MainThread mainThread;
    private ArrayList bubbleList = new ArrayList();
    private Paint back = new Paint();
    private int score = 0;
    private Paint scorePaint = new Paint();
    private Paint gameoverPaint = new Paint();
    private Paint goPaint = new Paint();
    private MediaPlayer mp;
    private boolean gameover = false;

    public AdvancedView(Context context) {
        super(context);
        init(context);
    }

    public AdvancedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdvancedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context){
        con = context;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap bmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        float scale = (float) bmap.getHeight() / (float) getHeight();
        int newWidth = Math.round(bmap.getWidth() / scale);
        int newHeight = Math.round(bmap.getHeight() / scale);
        bgBitmap = Bitmap.createScaledBitmap(bmap, newWidth, newHeight, true);
        spawnThread = new SpawnThread(holder, con, this, false);
        spawnThread.setRunning(true);
        spawnThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void doDraw(Canvas canvas){
        Rect r = new Rect();
        Rect q = new Rect();
        gameoverPaint.getTextBounds("GAME OVER", 0, 9, r);
        SCREENWIDTH = canvas.getWidth();
        SCREENHEIGHT = canvas.getHeight();
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        if(!gameover) {
            for (int i = 0; i < bubbleList.size(); i++) {
                Bubble currentBubble = (Bubble) bubbleList.get(i);
                int oldY = currentBubble.yCoord;
                if ((currentBubble.yCoord - currentBubble.diameter) > SCREENHEIGHT) {
                    spawnThread.interrupt();
                    spawnThread.setRunning(false);
                    gameover = true;
                } else {
                    currentBubble.moveBubble(oldY + 10);
                    currentBubble.drawBubble(canvas);
                }
            }
            String currentScre = "Score: " + score;
            scorePaint.getTextBounds(currentScre, 0, currentScre.length(), q);
            canvas.drawText(currentScre, ((SCREENWIDTH/2)+(SCREENWIDTH/4)) - (Math.abs(q.width()/2)), (SCREENHEIGHT-10), scorePaint);
        }
        else{
            canvas.drawText("GAME OVER", (SCREENWIDTH/2)-(Math.abs(r.width()/2)), (SCREENHEIGHT / 4) - ((gameoverPaint.descent() + gameoverPaint.ascent())/2), gameoverPaint);
            String finalScore = "Final Score: " + score;
            scorePaint.getTextBounds(finalScore, 0, finalScore.length(), q);
            canvas.drawText(finalScore, (SCREENWIDTH/2)-(Math.abs(q.width()/2)), (SCREENHEIGHT / 2) - ((scorePaint.descent() + scorePaint.ascent())/2), scorePaint);
        }

    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            int x = (int)event.getX();
            int y = (int)event.getY();
            for(Object b : bubbleList){
                int startX = ((Bubble)b).xCoord - ((Bubble)b).diameter;
                int finishX = ((Bubble)b).xCoord + ((Bubble)b).diameter;
                int startY = ((Bubble)b).yCoord - ((Bubble)b).diameter;
                int finishY = ((Bubble)b).yCoord + ((Bubble)b).diameter;
                if(!((Bubble) b).getBubbleShield()) {
                    if (x > startX && x < finishX) {
                        if (y > startY && y < finishY) {
                            if (((Bubble) b).getBubbleVal() == 0) {
                                ((Bubble) b).setBubbleVal(((Bubble) b).getBubbleVal() + 1);
                            } else {
                                ((Bubble) b).setBubbleVal(((Bubble) b).getBubbleVal() - 1);
                            }
                        }
                    }
                }
            }
            if(gameover){
                //Games.Leaderboards.submitScore(mGoogleApiClient, LEADERBOARD_ID, 1337);
                spawnThread.setRunning(false);
                mainThread.setRunning(false);
                boolean retry = true;
                while(retry){
                    try{
                        spawnThread.join();
                        mainThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        Log.v("Exception Occurred", e.getMessage());
                    }
                }
                Intent backToMenu = new Intent(getContext(), MenuActivity.class);
                getContext().startActivity(backToMenu);
            }
        }
        return false;
    }
}
