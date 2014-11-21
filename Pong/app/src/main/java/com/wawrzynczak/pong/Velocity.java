package com.wawrzynczak.pong;

/**
 * Created by jenny on 11/13/2014.
 */
public class Velocity
{
    double xVelocity;
    double yVelocity;

    public Velocity(double xVelocity, double yVelocity)
    {
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
    }

    public void ReverseX()
    {
        this.xVelocity = -this.xVelocity;
    }

    public void ReverseY()
    {
        this.yVelocity = -this.yVelocity;
    }

    public double getNewXPosition(double xPosition, double frameTime)
    {
        return xPosition + frameTime * xVelocity;
    }

    public double getNewYPosition(double yPosition, double frameTime)
    {
        return yPosition + frameTime * yVelocity;
    }

    public double getPreviousXPosition(double xPosition, double frameTime)
    {
        return xPosition - frameTime * xVelocity;
    }

    public double getPreviousYPosition(double yPosition, double frameTime)
    {
        return yPosition - frameTime * yVelocity;
    }
}
