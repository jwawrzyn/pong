package com.wawrzynczak.pong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by jenny on 11/12/2014.
 */
public class GameState {

    //screen width and height
    int screenWidth = 300;
    int screenHeight = 420;

    Ball gameBall;
    Sprite topPaddle;
    AIPaddle bottomPaddle;
    VelocityGenerator velocityGenerator;
    Score score;

    private double delayTime  = 0;

    public GameState()
    {
    }

    public double getDelayTime() {
        double delay = delayTime;
        delayTime = 0;
        return delay;
    }

    public GameState(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        velocityGenerator = new VelocityGenerator();
        gameBall = new Ball(screenWidth, screenHeight, velocityGenerator);
        topPaddle = new Sprite(screenWidth, screenHeight);
        bottomPaddle = new AIPaddle(screenWidth, screenHeight, gameBall);
        score = new Score(9);
        InitializeGameState();
        Log.i("Pong - GameState", "Initialized GameState");
    }

    public void canvasChange(int width,int height) {

    }

   private void InitializeGameState()
   {
       gameBall.center();;
       topPaddle.centerHorizontal();

       bottomPaddle.centerHorizontal();
       bottomPaddle.setyPosition(screenHeight - bottomPaddle.getHeight());

       ResetVelocities();
   }

    private void ResetVelocities()
    {
        bottomPaddle.setVelocity(new Velocity(10, 0));
        topPaddle.setVelocity(new Velocity(0, 0));
        gameBall.setInitialVelocity();
    }

    public void CheckCollision(double frameTime) {
        if (bottomPaddle.CollidesWith(gameBall, frameTime)) {
            gameBall.setPreviousLocation(frameTime);
            gameBall.speedUpVelocity();;
            gameBall.reverseYVelocity();
            bottomPaddle.setPreviousLocation(frameTime);
            //SoundManager.playSound(1, 1);
        } else if (topPaddle.CollidesWith(gameBall, frameTime)) {
            gameBall.setPreviousLocation(frameTime);
            gameBall.speedUpVelocity();
            gameBall.reverseYVelocity();
            //topPaddle.setPreviousLocation(frameTime);
            //SoundManager.playSound(1, 1);
        }
    }

    public boolean CheckBallBounds(double frameTime)
    {
        boolean finished = false;
        if (gameBall.IsOutOfXBounds(frameTime))
        {
            gameBall.reverseXVelocity();
            //.playSound(1, 2);
        }

        if (gameBall.IsOutOfLowerBounds(frameTime))
        {
            score.Player2Scored();
            if (score.isGameFinished())
            {
                finished = true;
            }
            else
            {
                Scored(new Velocity(133, -93));
            }
        }

        if (gameBall.IsOutOfUpperBounds(frameTime))
        {
            score.Player1Scored();
            if (score.isGameFinished())
            {
                finished = true;
            }
            else
            {
                Scored(new Velocity(133, 93));
            }
        }

        return finished;
    }

    private void Scored(Velocity newBallVelocity)
    {
        gameBall.center();
        gameBall.setVelocity(newBallVelocity);
        //SoundManager.playSound(2, 1);
        delayTime = 2;
    }

    public void AdvanceBall(double frameTime)
    {
        gameBall.Move(frameTime);
        topPaddle.Move(frameTime);
        bottomPaddle.Move(frameTime, gameBall);
    }

    public void AdjustPaddles()
    {
        AdjustPaddlePosition(bottomPaddle);
    }

    private void AdjustPaddlePosition(Sprite paddle)
    {
        if (paddle.getxPosition() < gameBall.getxPosition())
            paddle.setVelocity(new Velocity(100, 0));
        if (paddle.getxPosition() >= gameBall.getxPosition())
            paddle.setVelocity(new Velocity(-130, 0));
    }

   public boolean touchEvent(MotionEvent event) {
        float xPosition1 = 0;
        float yPosition1 = 0;
        float xPosition2 = 0;
        float yPosition2 = 0;

        for (int pointerIndex = 0; pointerIndex < event.getPointerCount(); pointerIndex++)
        {
            if (pointerIndex == 0)
            {
                xPosition1 = event.getX(pointerIndex);
                yPosition1 = event.getY(pointerIndex);
            }

            if (pointerIndex == 1)
            {
                xPosition2 = event.getX(pointerIndex);
                yPosition2 = event.getX(pointerIndex);
            }
        }

        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                topPaddle.setxPosition((int)xPosition1);

                break;
        }
        return true;
    }

    //the draw method
    public void draw(Canvas canvas, Paint paint) {

    //Clear the screen
        canvas.drawRGB(20, 20, 20);

    //set the colour
        paint.setARGB(200, 0, 200, 0);

    //draw the ball
        gameBall.draw(canvas, paint);

    //draw the bats
        topPaddle.draw(canvas, paint);
        bottomPaddle.draw(canvas, paint);

    }
}