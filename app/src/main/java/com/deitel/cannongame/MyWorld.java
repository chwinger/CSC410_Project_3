package com.deitel.cannongame;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.noctrl.craig.generic.CollisionDetection;
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
    ArrayList<GameObject> enemies = new ArrayList<>();

    private double timeLeft = 15; // time remaining in seconds

    final int NUM_ENEMIES = 6;
    Random rand = new Random();

    public MyWorld(StateListener listener, SoundManager sounds) {
        super(listener, sounds);
        //brain = view;
        ship = new CreateSprite(this);
        this.addObject(ship);
        addEnemies();
        numBullets = 0;
        totalShots = 0;
        kills = 0;
        remaining = NUM_ENEMIES;
        score = 0;
        winningState = "You Lose!";
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
        bulletList.add(bullet);
        totalShots++;
    }
    @Override
    public void update(float elapsedTimeMS){
        float interval = elapsedTimeMS / 1000.0F; // convert to seconds
        //decrease time
        timeLeft -= (double)interval; //decrease total time from time left
        totalElapsedTime = 15.0 - timeLeft;
        if(timeLeft <= 0.0){
            listener.onGameOver(true);
        }
        for(GameObject obj : objects){
            obj.update(interval);
        }
        List<GameObject> removed = new ArrayList<>();
        if(remaining == 0){
            winningState="You Won!";
            listener.onGameOver(true);
        }
        //check if any of the enemies were hit but any bullets
        for(GameObject bullet : bulletList){
            for(GameObject enemy : enemies){
                boolean hit = false;
                if(CollisionDetection.collision(bullet, enemy)){hit = true;}
                if(hit){
                    enemy.kill(); //put off screen.
                    removed.add(enemy);
                    bullet.kill();
                    kills += 1;
                    score += 2;
                    Log.i("DEBUG", "1 less guy, 1+ enemy, score + 3");
                }
            }
        }
        //bury the dead.
        for(GameObject e : removed){
            remaining -= 1;
            enemies.remove(e);
        }
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
        //choose random points and then add enemy ships there.
        for(int i = 0; i < NUM_ENEMIES; i++){
            Point3F pos = new Point3F(rand.nextInt(865) + 75, rand.nextInt(500) + 20, 0);
            GameObject enemy = new CreateEnemy(this, pos);
            this.addObject(enemy);
            enemies.add(enemy);
        }
    }
}
