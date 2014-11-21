package com.wawrzynczak.pong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by jenny on 11/12/2014.
 */
public class Ball extends Sprite
{
    private VelocityGenerator velocityGenerator;

     //public Ball(DrawableResourceCollection drawableResourceCollection, int canvasWidth, int canvasHeight, VelocityGenerator velocityGenerator)
    public Ball(int canvasWidth, int canvasHeight, VelocityGenerator velocityGenerator)
    {
        //super(drawableResourceCollection, canvasWidth, canvasHeight, 12, true);
        super(canvasWidth, canvasHeight);
        //this.drawableResourceCollection = drawableResourceCollection;
        this.velocityGenerator = velocityGenerator;
    }

    public void reverseXVelocity()
    {
        this.velocity.ReverseX();
        this.reverseAnimation();
    }

    public void reverseYVelocity()
    {
        this.velocity.ReverseY();
        this.reverseAnimation();
    }

    public void setInitialVelocity()
    {
        this.velocity = velocityGenerator.GenerateInitialVelocity();
    }

    public void generateNewVelocityDown()
    {
        this.velocity = this.velocityGenerator.GenerateNewReverseDown(velocity);
    }

    public void generateNewVelocityUp()
    {
        this.velocity = this.velocityGenerator.GenerateNewReverseUp(velocity);
    }



    @Override
    public int getWidth()
    {
        return 10; //drawableResourceCollection.get(currentFrame).getIntrinsicWidth();
    }
    @Override
    public int getHeight()
    {
        return 10; // drawableResourceCollection.get(currentFrame).getIntrinsicHeight();
    }

    public void draw(Canvas canvas, Paint paint)
    {
        //drawableResourceCollection.get(currentFrame).setBounds((int) xPosition, (int) yPosition, (int) xPosition + getWidth(), (int) yPosition + getHeight());
        //drawableResourceCollection.get(currentFrame).draw(canvas);
        //currentFrame = GetNewFrame();
        //Rect rectangle = new Rect((int)xPosition, (int)yPosition, (int)(xPosition + getWidth()), (int)(yPosition + getHeight()));
        Rect rectangle = new Rect((int)xPosition,(int) yPosition, (int)(xPosition + getWidth()),  (int)(yPosition + getHeight()));

        canvas.drawRect(rectangle, paint);

    }

}
