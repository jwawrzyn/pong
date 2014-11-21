package com.wawrzynczak.pong;

import java.util.Random;

/**
 * Created by jenny on 11/13/2014.
 */
public class VelocityGenerator
{
    private static final int MaxSpeed = 298;
    private static final int MinimumYSpeed = 123;
    private static final int MinimumXSpeed = 123;
    private Random speedRandomizer;

    public VelocityGenerator()
    {
        speedRandomizer = new Random(12313975);
    }

    public Velocity GenerateInitialVelocity()
    {
        return new Velocity(162.0, -152.0);
    }

    public Velocity GenerateNewReverseDown(Velocity velocity)
    {
        int xVelocity = GenerateNewXSpeed();
        int yVelocity = GenerateNewYSpeed();

        if (velocity.xVelocity > 0)
            xVelocity = -xVelocity;

        return new Velocity(xVelocity, yVelocity);
    }

    public Velocity GenerateNewReverseUp(Velocity velocity)
    {
        int xVelocity = GenerateNewXSpeed();
        int yVelocity = GenerateNewYSpeed();

        if (velocity.xVelocity > 0)
            xVelocity = -xVelocity;

        return new Velocity(xVelocity, -yVelocity);
    }



    private int GenerateNewYSpeed()
    {
        return speedRandomizer.nextInt(MaxSpeed) + MinimumYSpeed;
    }

    private int GenerateNewXSpeed()
    {
        return speedRandomizer.nextInt(MaxSpeed) + MinimumXSpeed;
    }

    public static Velocity SpeedUpVelocity( Velocity old)
    {
        double xVelocity = old.xVelocity;
        double yVelocity = old.yVelocity;
        int newX = (int)(xVelocity *1.10);
        int newY = (int)(yVelocity * 1.10);

        if (Math.abs(newX) > MaxSpeed)
            newX = MaxSpeed;

        if (Math.abs(newY) > MaxSpeed)
            newY = MaxSpeed;

        return new Velocity(newX, newY);
    }

    public static Velocity SlowDownVelocity(Velocity old)
    {
        double xVelocity = old.xVelocity;
        double yVelocity = old.yVelocity;
        int newX = (int)(xVelocity* .90);
        int newY = (int)(yVelocity * .90);

        if (Math.abs(newX) > MaxSpeed)
            newX = MaxSpeed;

        if (Math.abs(newY) > MaxSpeed)
            newY = MaxSpeed;

        return new Velocity(newX, newY);
    }

    public static Velocity changeVelocity(Velocity old, double percentage)
    {
        double xVelocity = old.xVelocity;
        double yVelocity = old.yVelocity;
        int newX = (int)(xVelocity* percentage);
        int newY = (int)(yVelocity * percentage);

        if (Math.abs(newX) > MaxSpeed)
            newX = MaxSpeed;

        if (Math.abs(newY) > MaxSpeed)
            newY = MaxSpeed;

        return new Velocity(newX, newY);
    }
}
