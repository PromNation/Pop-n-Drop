package com.anthonyprom.popdrop.Menu;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Anthony on 6/23/2016.
 */
public class MenuThread extends Thread{
    boolean run;
    Canvas canvas;
    SurfaceHolder sHolder;
    Context context;
    MenuView panel;

    public MenuThread(SurfaceHolder s, Context c, MenuView g){
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
