package com.deitel.cannongame;

import android.graphics.Rect;

import edu.noctrl.craig.generic.GameSprite;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.World;

/**
 * Created by Christian on 5/13/2015.
 */
public class CreateSprite extends GameSprite {
    public static final Rect source = new Rect(192,0,255,63);
    public static final Point3F scale = Point3F.identity();

    public CreateSprite(World theWorld){
        super(theWorld);
        this.position = new Point3F(75,270,0);
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
