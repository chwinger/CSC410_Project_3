// CannonView.java
// Displays and controls the Cannon Game
package com.deitel.cannongame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;

import edu.noctrl.craig.generic.GameSprite;
import edu.noctrl.craig.generic.SoundManager;
import edu.noctrl.craig.generic.World;

public class JetGameView extends SurfaceView implements SurfaceHolder.Callback, World.StateListener {
    private static final String TAG = "GameView"; // for logging errors
    private static final String SPRITE_FILE = "sprites.png";
    public SensorManager mSensorManager;
    private GameThread gameThread; // controls the game loop
    public World world;
    private SoundManager soundManager;
    private Activity activity; // to display Game Over dialog in GUI thread
    private boolean dialogIsDisplayed = false;

    // variables for the game loop and tracking statistics
    private int gameOver = 0; // is the game over?

    private int screenWidth;
    private int screenHeight;

    //http://stackoverflow.com/questions/18973550/load-images-from-assets-folder
    private Bitmap getBitmapFromAsset(String strName)
    {
        AssetManager assetManager = activity.getAssets();
        try {
            InputStream istr = assetManager.open(strName);
            Bitmap bitmap = BitmapFactory.decodeStream(istr);
            istr.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void loadSprites(){
        if(GameSprite.SPRITE_SOURCE == null){
            GameSprite.SPRITE_SOURCE = getBitmapFromAsset(SPRITE_FILE);
        }
        World.resources = getResources();
    }
    public void releaseSprites(){
        if(GameSprite.SPRITE_SOURCE != null){
            GameSprite.SPRITE_SOURCE.recycle();
            GameSprite.SPRITE_SOURCE = null;
        }
    }
    // public constructor
    public JetGameView(Context context, AttributeSet attrs) {
        super(context, attrs); // call superclass constructor
        activity = (Activity) context; // store reference to MainActivity
        mSensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);

        // register SurfaceHolder.Callback listener
        getHolder().addCallback(this);
        soundManager = new SoundManager(context);
        loadSprites();
        //MediaPlayer player = MediaPlayer.create(context, R.raw.mortal_kombat);
        //player.start();
    } // end CannonView constructor

    // called by surfaceChanged when the size of the SurfaceView changes,
    // such as when it's first added to the View hierarchy
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w; // store CannonView's width
        screenHeight = h; // store CannonView's height
        //newGame(getHolder()); // set up and start a new game
    } // end method onSizeChanged

    // reset all the screen elements and start a new game
    public void newGame(SurfaceHolder holder) {
        if(gameOver == 1){
            world = new MyWorld2(this, soundManager);
            world.updateSize(screenWidth, screenHeight);
            this.setOnTouchListener(world);
            gameThread = new GameThread(holder, world); // create thread*/
            gameThread.start();
        }
        else if (gameOver == 0) // starting a new game after the last game ended
        {

            world = new MyWorld(this, soundManager);
            world.updateSize(screenWidth, screenHeight);
            this.setOnTouchListener(world);
            gameThread = new GameThread(holder, world); // create thread
            gameThread.start(); // start the game loop thread
        }
        else{
            world = new MyWorld3(this, soundManager);
            mSensorManager.registerListener(world, mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_FASTEST);
            world.updateSize(screenWidth, screenHeight);
            this.setOnTouchListener(world);
            gameThread = new GameThread(holder, world); // create thread
            gameThread.start(); // start the game loop thread

        }

    } // end method newGame

    // display an AlertDialog when the game ends
    private void showGameOverDialog(final int messageId) {
        // DialogFragment to display quiz stats and start new quiz
        final DialogFragment gameResult =
                new DialogFragment() {
                    // create an AlertDialog and return it
                    @Override
                    public Dialog onCreateDialog(Bundle bundle) {
                        // create dialog displaying String resource for messageId
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getActivity());
                        builder.setTitle(messageId);

                        // display number of shots fired and total time elapsed
                        builder.setMessage(getResources().getString(
                                R.string.results_format,
                                world.totalShots,
                                world.kills,
                                world.remaining,
                                world.score,
                                world.totalElapsedTime));
                        if(world.kills >= 10 && gameOver < 2) {
                            builder.setPositiveButton("Next Level",
                                    new DialogInterface.OnClickListener() {
                                        // called when "Reset Game" Button is pressed
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialogIsDisplayed = false;
                                            newGame(getHolder()); // set up and start a new game
                                        }
                                    } // end anonymous inner class
                            ); // end call to setPositiveButton
                        }
                        else{
                            builder.setPositiveButton(R.string.reset_game,
                                    new DialogInterface.OnClickListener() {
                                        // called when "Reset Game" Button is pressed
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialogIsDisplayed = false;
                                            gameOver = 0;
                                            newGame(getHolder()); // set up and start a new game
                                        }
                                    } // end anonymous inner class
                            ); // end call to setPositiveButton
                        }
                        return builder.create(); // return the AlertDialog
                    } // end method onCreateDialog
                }; // end DialogFragment anonymous inner class

        // in GUI thread, use FragmentManager to display the DialogFragment
        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        dialogIsDisplayed = true;
                        gameResult.setCancelable(false); // modal dialog
                        gameResult.show(activity.getFragmentManager(), "results");
                    }
                } // end Runnable
        ); // end call to runOnUiThread
    } // end method showGameOverDialog

    // stops the game; called by JetGameFragment's onPause method
    public void stopGame() {
        if (gameThread != null)
            gameThread.stopGame(); // tell thread to terminate
    }

    // releases resources; called by JetGameFragment's onDestroy method
    public void releaseResources() {
        soundManager.releaseResources();
    }

    // called when surface changes size
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {
    }

    // called when surface is first created
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSensorManager.registerListener(world, mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_FASTEST);
        if (!dialogIsDisplayed) {
            newGame(holder);
        }
    }

    // called when the surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // ensure that thread terminates properly
        boolean retry = true;
        mSensorManager.unregisterListener(world);
        gameThread.stopGame(); // terminate cannonThread

        while (retry) {
                newGame(getHolder()); // wait for cannonThread to finish
                retry = false;
        }
    } // end method surfaceDestroyed

    @Override
    public void onGameOver(boolean lost) {
        gameOver++; // the game is over
        mSensorManager.unregisterListener(world);
        gameThread.stopGame(); // terminate thread
        if(lost)
            showGameOverDialog(R.string.lose); // show the losing dialog
        else
            showGameOverDialog(R.string.win);
    }

    /**
     * should only be used for setting gameOver in the fragment.
     * @param gameState
     */
    public void setGameOver(int gameState){
        gameOver = gameState;
    }
}
