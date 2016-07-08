package com.anthonyprom.popdrop.MainGame;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Anthony on 7/7/2016.
 */
public class AdvancedActivity extends AppCompatActivity {
    AdvancedView aView;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        aView = new AdvancedView(this);
        setContentView(aView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
