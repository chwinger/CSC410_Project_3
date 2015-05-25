package com.deitel.cannongame;

import android.graphics.Rect;

import edu.noctrl.craig.generic.GameSprite;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.World;

/**
 * Created by Christian on 5/20/2015.
 */
public class CreateBullet extends GameSprite {
    public static final Rect source = new Rect(256,69,272,76);
    public static final Point3F scale = Point3F.identity();

    public CreateBullet(World theWorld){
        super(theWorld);
        this.speed = 200;
        this.position = new Point3F(75,270,0);
        this.substance = 1;
        this.collidesWith = 4;
    }

    @Override
    public Rect getSource(){
        return source;
    }

    @Override
    public Point3F getScale(){
        return scale;
    }

    @Override
    public void cull(){
        this.kill();
    }
}

