package com.wawrzynczak.pong;

/**
 * Created by jenny on 11/19/2014.
 */
public class AIPaddle extends Sprite {
    private Ball ball;
    private int canvasWidth;
    private int canvasHeight;
    private int canvasCenterX;

    public AIPaddle(int width, int height, Ball gameBall) {
        super(width,height);
        this.ball = gameBall;
        //this.velocity = new Velocity(gameBall.velocity.xVelocity,0);
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.canvasCenterX = canvasWidth / 2;
    }

    public void Move(double frameTime, Ball newBall)
    {
        this.ball = newBall;
        if (ball.velocity.yVelocity < 0)
            //this.centerHorizontal();
            this.stop();
        else
        {
            double velocityDiff = Math.abs(ball.velocity.xVelocity) - Math.abs(velocity.xVelocity);
            double ballXVelocity = ball.velocity.xVelocity;
            double xVelocity = velocity.xVelocity;

            if (xVelocity == 0.0 ){
                this.velocity = VelocityGenerator.GenerateRandomHorizontalVelocity();
            }

            if (Math.abs(velocityDiff) / Math.abs(velocity.xVelocity) > .20)
            {
                if (Math.abs(ballXVelocity) - Math.abs(xVelocity) > 0 )
                    super.speedUpVelocity();
                else
                    super.slowDownVelocity();

            }

            if (ball.xPosition > getCenterX()) // && this.xPosition < canvasCenterX)
                if (velocity.xVelocity < 0)
                    velocity.ReverseX();

            if (ball.xPosition < getCenterX()) // && xPosition > canvasCenterX)
                if (velocity.xVelocity > 0)
                    velocity.ReverseX();

            super.Move(frameTime);
        }
    }

    private int getCenterX()
    {
        return (int)xPosition + getWidth() / 2;
    }
}
