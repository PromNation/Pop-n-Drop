package com.anthonyprom.popdrop.MainGame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * Created by Anthony on 6/14/2016.
 */
public class SpawnThread extends Thread{
    boolean run;
    Canvas canvas;
    SurfaceHolder sHolder;
    Context context;
    GameView panel;
    AdvancedView advPanel;
    int rate = 1500;
    boolean advMode = false;

    public SpawnThread(SurfaceHolder s, Context c, View g, boolean mode){
        sHolder = s;
        context = c;
        if(mode){
            advPanel = (AdvancedView)g;
        }
        else{
            panel = (GameView)g;
        }
        run = false;
        advMode = mode;
    }

    public void setRunning(boolean b){
        run = b;
    }

    @Override
    public void run(){
        super.run();
        while(run){
            canvas = sHolder.lockCanvas();
            if(canvas != null) {
                if(!advMode) {
                    int radius = 0;
                    while (radius < 100) {
                        radius = (int) (Math.random() * 400);
                    }
                    int speed = (int) (Math.random() * 10);
                    int val = (int) (Math.random() * 5) + 1;
                    int xCo = 0;
                    while (xCo < 20 || xCo > panel.getWidth() - 20) {
                        xCo = (int) (Math.random() * panel.getWidth());
                    }

                    Bubble newBubble = new Bubble(radius, xCo, val, speed, advMode, canvas);
                    newBubble.getRanBubbleColor();
                    System.out.println("Made new bubble!");
                    panel.addToList(newBubble);

                    sHolder.unlockCanvasAndPost(canvas);
                    try {
                        Thread.sleep(rate);
                        if (rate > 500) {
                            if (panel.getScore() % 5 == 0) {
                                rate -= 100;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else{

                }
            }
        }
    }

}
