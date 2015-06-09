// CannonGameFragment.java
// JetGameFragment creates and manages a CannonView
package com.deitel.cannongame;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class JetGameFragment extends Fragment {
    private JetGameView jetGameView; // custom view to display the game
    public SensorManager mSensorManager;

    // called when Fragment's view needs to be created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =
                inflater.inflate(R.layout.fragment_game, container, false);

        // get the view
        jetGameView = (JetGameView) view.findViewById(R.id.cannonView);
<<<<<<< HEAD
        mSensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
=======
        setHasOptionsMenu(true);
>>>>>>> 7bb1e6ceeee18ec12eea2b6fa9ebdb846df60ad1
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(jetGameView.world, mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_FASTEST);
    }
    // set up volume control once Activity is created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // allow volume keys to set game volume
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    // when MainActivity is paused, JetGameFragment terminates the game
    @Override
    public void onPause() {
        super.onPause();
        jetGameView.stopGame(); // terminates the game
    }

    // when MainActivity is paused, JetGameFragment releases resources
    @Override
    public void onDestroy() {
        super.onDestroy();
        jetGameView.releaseResources();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        //return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.toLevel1:
                //stop game and start that one?
                jetGameView.stopGame();
                jetGameView.setGameOver(0);
                try{Thread.sleep(100);}catch(Exception e){/*eat*/}
                jetGameView.newGame(jetGameView.getHolder());
                Toast.makeText(getActivity(), "Going to level 1...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.toLevel2:
                jetGameView.stopGame();
                jetGameView.setGameOver(1);
                try{Thread.sleep(100);}catch(Exception e){/*eat*/}
                jetGameView.newGame(jetGameView.getHolder());
                Toast.makeText(getActivity(), "Going to level 2...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.toLevel3:
                jetGameView.stopGame();
                jetGameView.setGameOver(2);
                try{Thread.sleep(100);}catch(Exception e){/*eat*/}
                jetGameView.newGame(jetGameView.getHolder());
                Toast.makeText(getActivity(), "Going to level 3...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.score:
                //showHelp(); //launch popup menu
                return true;
            case R.id.about:
                //showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
} // end class JetGameFragment

/*********************************************************************************
 * (C) Copyright 1992-2014 by Deitel & Associates, Inc. and * Pearson Education, *
 * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this   *
 * book have used their * best efforts in preparing the book. These efforts      *
 * include the * development, research, and testing of the theories and programs *
 * * to determine their effectiveness. The authors and publisher make * no       *
 * warranty of any kind, expressed or implied, with regard to these * programs   *
 * or to the documentation contained in these books. The authors * and publisher *
 * shall not be liable in any event for incidental or * consequential damages in *
 * connection with, or arising out of, the * furnishing, performance, or use of  *
 * these programs.                                                               *
 *********************************************************************************/
