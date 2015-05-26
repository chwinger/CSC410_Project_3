package com.deitel.cannongame;

import android.graphics.Rect;

import java.util.Random;

import edu.noctrl.craig.generic.GameSprite;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.World;

/**
 * Created by Christian on 5/26/2015.
 */
public class CreateEnemyMove extends GameSprite {
    public static final Rect source = new Rect(192, 0, 255, 63);
    public static final Point3F scale = Point3F.identity();
    Random rand;
    int randDir;
    boolean hitBound;

    public CreateEnemyMove(World theWorld, Point3F pos) {
        super(theWorld);
        rand = new Random((long)pos.X);
        this.position = pos;
        this.collidesWith = 1;
        this.speed = 220;
        this.baseVelocity = (new Point3F(rand.nextInt(150) - 75, rand.nextInt(100) - 50, 0f)).normalize();
        this.updateVelocity();
        this.substance = 4;
        hitBound = true;
    }

    @Override
    public Rect getSource() {
        return source;
    }

    @Override
    public Point3F getScale() {
        return scale;
    }

    @Override
    public void cull() {
        this.kill();
    }

    @Override
    public void update(float interval){
        randDir = rand.nextInt(100);
        if(((position.X > 980)
                || (position.X < 240)
                || (position.Y > 400)
                || (position.Y < 70))
                && !hitBound) {
            baseVelocity = (new Point3F(baseVelocity.X * -1, baseVelocity.Y * -1, baseVelocity.Z)).normalize();
            this.updateVelocity();
            position.add(velocity.clone().mult(interval));
            hitBound = true;
        }
        else if(randDir <= 10 && !hitBound){
            baseVelocity = (new Point3F(baseVelocity.X + (rand.nextInt(200) - 100), baseVelocity.Y + (rand.nextInt(100) - 50), baseVelocity.Z)).normalize();
            this.updateVelocity();
            position.add(velocity.clone().mult(interval));
        }
        else{
            position.add(velocity.clone().mult(interval));
            hitBound  = false;
        }
    }
}