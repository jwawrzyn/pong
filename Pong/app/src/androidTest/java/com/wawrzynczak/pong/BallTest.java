package com.wawrzynczak.pong;

import android.test.InstrumentationTestCase;

/**
 * Created by jenny on 11/18/2014.
 */
public class BallTest extends InstrumentationTestCase {

    public void test_createBall() throws Exception {
        VelocityGenerator generator = new VelocityGenerator();
        Ball ball = new Ball(300,300,generator);
        ball.center();

        assertEquals(145.0, ball.getxPosition());
        assertEquals(145.0, ball.getxPosition());
    }

    public void test_moveBall() throws  Exception {
        VelocityGenerator generator = new VelocityGenerator();
        Ball ball = new Ball(300, 300, generator);
        ball.setInitialVelocity();
        ball.center();
        double newX = ball.getNewXPosition(.01);
        ball.setxPosition((int) newX);

        assertEquals(146.0, ball.getxPosition());
    }

    public void test_collideWall() throws Exception {
        VelocityGenerator generator = new VelocityGenerator();
        Ball ball = new Ball(300, 300, generator);
        ball.setInitialVelocity();
        ball.center();

        assertEquals(true, ball.IsOutOfXBounds(1));
    }

    public void test_collideTop() throws Exception {
        VelocityGenerator generator = new VelocityGenerator();
        Ball ball = new Ball(300, 300, generator);
        ball.setInitialVelocity();
        ball.center();
        Velocity velocity = new Velocity(0,-150);
        ball.setVelocity(velocity);

        assertEquals(true, ball.IsOutOfUpperBounds(1));
    }

    public void test_notCollideTop() throws Exception {
        VelocityGenerator generator = new VelocityGenerator();
        Ball ball = new Ball(300, 300, generator);
        ball.setInitialVelocity();
        ball.center();
        Velocity velocity = new Velocity(0,100);
        ball.setVelocity(velocity);

        assertEquals(false, ball.IsOutOfUpperBounds(1));
    }

    public void test_collideBottom() throws Exception {
        VelocityGenerator generator = new VelocityGenerator();
        Ball ball = new Ball(300, 300, generator);
        ball.setInitialVelocity();
        ball.center();
        Velocity velocity = new Velocity(0,150);
        ball.setVelocity(velocity);

        assertEquals(true, ball.IsOutOfLowerBounds(1));
    }

    public void test_notCollideBottom() throws Exception {
        VelocityGenerator generator = new VelocityGenerator();
        Ball ball = new Ball(300, 300, generator);
        ball.setInitialVelocity();
        ball.center();
        Velocity velocity = new Velocity(0,100);
        ball.setVelocity(velocity);

        assertEquals(false, ball.IsOutOfLowerBounds(1));
    }
}
