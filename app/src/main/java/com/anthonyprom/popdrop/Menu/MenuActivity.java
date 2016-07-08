package com.anthonyprom.popdrop.Menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.anthonyprom.popdrop.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.games.Games;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.Player;


public class MenuActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button startButton;
    private static final String LEADERBOARD_ID = "CgkIuJG_jdMbEAIQBA";
    private static final String ADV_LEADERBOARD_ID = "";
    private static final String DID_IT_ID = "CgkIuJG_jdMbEAIQAQ";
    private static final String NOT_TOO_BAD_ID = "CgkIuJG_jdMbEAIQAg";
    private static final String EVEN_TRYING_ID = "CgkIuJG_jdMbEAIQAw";
    private static final String POWER_ID = "CgkIuJG_jdMbEAIQBQ";
    private static final String TINKERER_ID = "CgkIuJG_jdMbEAIQBg";
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure = false;//Currently resolving a connection failure?
    private boolean mSignInClicked = false;//Has user clicked sign in button?
    private boolean mAutoStartSignInFlow = true;//Auto start sign in
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
    private String TAG = "PND"; //Game name tag


    AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MenuView view = new MenuView(this);
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        mOutbox.loadLocal(this);

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart(): connecting");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop: disconnecting");
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
    }

    private boolean isSignedIn(){
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected(): connected to Google APIs");


        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String displayName;
        if(p == null) {
            Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL");
            displayName = "???";
        }
        else{
            displayName = p.getDisplayName();
        }

        if(!mOutbox.isEmpty()){
            pushAccomplishments();
            Toast.makeText(this, "Your progress will be uploaded", Toast.LENGTH_LONG).show();
        }

    }

    public void pushAccomplishments(){
        if(!isSignedIn()){
            mOutbox.saveLocal(this);
            return;
        }
        if(mOutbox.mDidIt){
            Games.Achievements.unlock(mGoogleApiClient, DID_IT_ID);
            mOutbox.mDidIt = false;
        }
        if(mOutbox.mNotTooBad){
            Games.Achievements.unlock(mGoogleApiClient, NOT_TOO_BAD_ID);
            mOutbox.mNotTooBad = false;
        }
        if(mOutbox.mTrying){
            Games.Achievements.unlock(mGoogleApiClient, EVEN_TRYING_ID);
            mOutbox.mTrying = false;
        }
        if(mOutbox.mPower){
            Games.Achievements.unlock(mGoogleApiClient, POWER_ID);
            mOutbox.mPower = false;
        }
        if(mOutbox.mTinkerer){
            Games.Achievements.unlock(mGoogleApiClient, TINKERER_ID);
            mOutbox.mTinkerer = false;
        }
        if(mOutbox.mNormalScore >= 0){
            Games.Leaderboards.submitScore(mGoogleApiClient, LEADERBOARD_ID, mOutbox.mNormalScore);
            mOutbox.mNormalScore = -1;
        }
        if(mOutbox.mAdvScore >= 0){
            Games.Leaderboards.submitScore(mGoogleApiClient, ADV_LEADERBOARD_ID, mOutbox.mAdvScore);
            mOutbox.mAdvScore = -1;
        }

        mOutbox.saveLocal(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): attempting to connect");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("PND", "onConnectionFailed(): attempting to resolve");
        if(mResolvingConnectionFailure){
            Log.d(TAG, "onConnectionFailed(): already resolving");
            return;
        }
    }

    class AccomplishmentsOutbox {
        boolean mDidIt = false;
        boolean mNotTooBad = false;
        boolean mTrying = false;
        boolean mPower = false;
        boolean mTinkerer = false;
        int mNormalScore = -1;
        int mAdvScore = -1;

        boolean isEmpty() {
            return !mDidIt && !mNotTooBad && !mTrying &&
                    !mPower && !mTinkerer && mNormalScore < 0
                    && mAdvScore < 0;
        }

        public void saveLocal(Context ctx) {
            /* TODO: This is left as an exercise. To make it more difficult to cheat,
             * this data should be stored in an encrypted file! And remember not to
             * expose your encryption key (obfuscate it by building it from bits and
             * pieces and/or XORing with another string, for instance). */
        }

        public void loadLocal(Context ctx) {
            /* TODO: This is left as an exercise. Write code here that loads data
             * from the file you wrote in saveLocal(). */
        }
    }

    public void updateLeaderboards(int finalScore){
        if(mOutbox.mNormalScore < finalScore){
            mOutbox.mNormalScore = finalScore;
        }
        if(mOutbox.mAdvScore < finalScore){
            mOutbox.mAdvScore = finalScore;
        }
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent intent){
        super.onActivityResult(requestcode, resultcode, intent);
        if(requestcode == RC_SIGN_IN){
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if(resultcode == RESULT_OK){
                mGoogleApiClient.connect();
            }
            else{
                showActivityResultError(this, requestcode, resultcode, -1);
            }
        }
    }

    public void showActivityResultError(Activity activity, int requestCode, int actResp, int errorDescription){
        if (activity == null) {
            Log.e("BaseGameUtils", "*** No Activity. Can't show failure dialog!");
            return;
        }
    }

    public void achievementToast(String ach){
        if(!isSignedIn()){
            Toast.makeText(this, "ACHIEVEMENT: " + ach, Toast.LENGTH_LONG).show();
        }
    }

    public void unlockAchievement(int achID, String fallbackString){
        if(isSignedIn()){
            Games.Achievements.unlock(mGoogleApiClient, getString(achID));
        }
        else{
            Toast.makeText(this, "ACHIEVEMENT: " + fallbackString, Toast.LENGTH_LONG).show();
        }
    }

    public void checkForAchievements(int finalScore){
        if(finalScore == 0){
            mOutbox.mTrying = true;
        }
        if(finalScore == 20){
            mOutbox.mNotTooBad = true;
        }
        if(finalScore == 12345){
            mOutbox.mDidIt = true;
        }
        if(finalScore == 55555){
            mOutbox.mTinkerer = true;
        }
        if(finalScore == 8691){
            mOutbox.mPower = true;
        }
    }

    public void onShowAchievementsRequested(){
        if(isSignedIn()){
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), RC_UNUSED);
        }
        else{
            Log.d(TAG, "Leaderboard not available");
        }
    }

    public void onShowLeaderboardsRequested(){
        if(isSignedIn()){
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), RC_UNUSED);
        }
    }

    public void onEnteredScore(int requestedScore){
        checkForAchievements(requestedScore);
        updateLeaderboards(requestedScore);
        pushAccomplishments();
    }

}
