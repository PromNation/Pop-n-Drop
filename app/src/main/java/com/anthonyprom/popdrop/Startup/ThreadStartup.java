package com.anthonyprom.popdrop.Startup;

import android.content.Context;
import android.content.Intent;

import com.anthonyprom.popdrop.Menu.MenuActivity;

/**
 * Created by Anthony on 6/16/2016.
 */
public class ThreadStartup extends Thread {
    boolean run = false;
    Context c;

    public ThreadStartup(Context context){
        c = context;
    }

    public void setRunning(boolean b) {
        run = b;
    }

    @Override
    public void run() {
        super.run();
        while (run) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(c, MenuActivity.class);
            c.startActivity(intent);
            setRunning(false);
        }
    }
}