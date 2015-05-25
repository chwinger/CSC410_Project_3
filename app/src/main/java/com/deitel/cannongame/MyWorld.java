package com.deitel.cannongame;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import edu.noctrl.craig.generic.GameObject;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.SoundManager;
import edu.noctrl.craig.generic.World;

/**
 * Created by Christian on 5/13/2015.
 */
public class MyWorld extends World {
    public CreateSprite ship;
    public CreateBullet bullet;
    public CreateEnemy enemy;
    ArrayList<GameObject> bulletList = new ArrayList<>();
    int numBullets;
    final int NUM_ENEMIES = 6;
    Random rand = new Random();

    public MyWorld(StateListener listener, SoundManager sounds) {
        super(listener, sounds);
        ship = new CreateSprite(this);
        this.addObject(ship);
        addEnemies();
        numBullets = 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        Point3F touch;
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
            offScreenBulletCheck();
            if(bulletList.size() < 5) {
                makeBullet();
                touch = new Point3F(event.getX(), event.getY(), 0f);
                Point3F shipPos = ship.position.clone();
                touch.subtract(shipPos);
                bullet.baseVelocity = touch.normalize();
                this.addObject(bullet);
                bullet.updateVelocity();
            }
        }
        return true;
    }

    public void makeBullet(){
        bullet = new CreateBullet(this);
        numBullets++;
        bulletList.add(bullet);
    }

    public void offScreenBulletCheck(){
        ArrayList<GameObject> offScreen = new ArrayList<>();
        for(GameObject b: bulletList){
            if(b.offScreen){
                offScreen.add(b);
                numBullets--;
            }
        }
        for(GameObject b: offScreen){
            bulletList.remove(b);
        }
    }

    public void addEnemies(){

        for(int i = 0; i < NUM_ENEMIES; i++){
            Point3F pos = new Point3F(rand.nextInt(865) + 75, rand.nextInt(500) + 20, 0);
            this.addObject(new CreateEnemy(this, pos));
        }
    }
}
