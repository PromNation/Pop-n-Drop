package com.anthonyprom.popdrop.MainGame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread{
    boolean run;
    Canvas canvas;
    SurfaceHolder sHolder;
    Context context;
    GameView panel;

    public MainThread(SurfaceHolder s, Context c, GameView g){
        sHolder = s;
        context = c;
        panel = g;
        run = false;
    }

    public void setRunning(boolean b){
        run = b;
    }

    @Override
    public void run(){
        super.run();
        while(run){
            canvas = sHolder.lockCanvas();
            if(canvas != null){
                panel.doDraw(canvas);
                sHolder.unlockCanvasAndPost(canvas);
            }
        }
    }


}
