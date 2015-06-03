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
 * Created by Christian on 5/26/2015.
 */
public class MyWorld2 extends World {
    public CreateSprite ship;
    public CreateBullet bullet;
    Point3F bulletStart;
    ArrayList<GameObject> bulletList = new ArrayList<>();
    int numBullets;
    final int NUM_ENEMIES = 6;
    Random rand = new Random();


    public MyWorld2(StateListener listener, SoundManager sounds) {
        super(listener, sounds);
        ship = new CreateSprite(this);
        ship.substance = 2;
        ship.collidesWith = 6;
        this.addObject(ship);
        addEnemies();
        numBullets = 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        Point3F touch;
        final int action = event.getAction();
        Point3F shipPos = ship.position.clone();

        switch(action){
            case MotionEvent.ACTION_DOWN: {
                offScreenBulletCheck();
                if(bulletList.size() < 5) {
                    makeBullet();
                    touch = new Point3F(shipPos.X + 250, shipPos.Y, 0f);
                    touch.subtract(shipPos);
                    bullet.baseVelocity = touch.normalize();
                    this.addObject(bullet);
                    bullet.position = shipPos;
                    bullet.speed = 350;
                    bullet.updateVelocity();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                touch = new Point3F(event.getX(), event.getY(), 0f);
                touch.subtract(shipPos);
                ship.baseVelocity = touch.normalize();
                ship.updateVelocity();

                break;
            }
            case MotionEvent.ACTION_UP: {
                ship.baseVelocity = new Point3F(0,0,0f);
                ship.updateVelocity();
                break;
            }
        }
        return true;
    }

    public void makeBullet(){
        bullet = new CreateBullet(this);
        numBullets++;
        bulletList.add(bullet);
    }

    public void enemyBullet(Point3F pos){
        Point3F enemy = pos;
        bullet = new CreateBullet(this);
        this.addObject(bullet);
        bullet.collidesWith = 2;
        bullet.substance = 4;
        bullet.position = enemy;
        bullet.speed = -350;
        bullet.baseVelocity = new Point3F(1,0,0);
        bullet.updateVelocity();
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
            Point3F pos = new Point3F(rand.nextInt(739) + 241, rand.nextInt(329) + 71, 0);
            this.addObject(new CreateEnemyMove(this, pos, this));
        }
    }
}
