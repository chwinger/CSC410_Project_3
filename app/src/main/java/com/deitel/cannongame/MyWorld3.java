package com.deitel.cannongame;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.util.Random;

import edu.noctrl.craig.generic.GameObject;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.SoundManager;
import edu.noctrl.craig.generic.World;

/**
 * Created by Christian on 6/5/2015.
 */
public class MyWorld3 extends World{
    public CreateSprite ship;
    Random rand = new Random();
    public int enemiesDodged;

    public MyWorld3(World.StateListener listener, SoundManager sounds) {
        super(listener, sounds);
        ship = new CreateSprite(this);
        ship.substance = 2;
        ship.collidesWith = 6;
        ship.speed = 500;
        this.addObject(ship);
        numBullets = 0;
        enemiesDodged = 0;
    }

    public void addEnemies(){
        Point3F pos = new Point3F(rand.nextInt(865) + 75, rand.nextInt(500) + 20, 0);
        GameObject enemy = new CreateEnemyShoot(this, pos, this);
        this.addObject(enemy);
        enemy.speed = 250;
        enemy.baseVelocity = new Point3F(-1, 0, 0);
        enemy.updateVelocity();
    }

    public void update(float elapsedTimeMS){
        float interval = elapsedTimeMS / 1000.0F; // convert to seconds
        int spawnEnemy = rand.nextInt(100);
        for(GameObject obj : objects){
            obj.update(interval);
        }
        if(spawnEnemy < 2){
            addEnemies();
        }
        if(ship.isDead()){
            //Add shipsDodged to public score here
            listener.onGameOver(true);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Point3F rotationDirection;
        rotationDirection = new Point3F(-event.values[1], -event.values[0], 0);
        ship.baseVelocity = rotationDirection;
        ship.updateVelocity();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
