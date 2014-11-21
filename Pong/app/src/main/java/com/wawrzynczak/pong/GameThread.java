package com.wawrzynczak.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * Created by jenny on 11/12/2014.
 */
public class GameThread extends Thread {

    /** Handle to the surface manager object we interact with */
    private SurfaceHolder surfaceHolder;
    private Paint _paint;
    private GameState _state;
    private Handler mHandler;
    private int canvasWidth;
    private int canvasHeight;
    private Score score;

    private boolean isRunning;
    private double frameTime;
    private double delayTime = 0;

    public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler)
    {
        this.surfaceHolder = surfaceHolder;
        _paint = new Paint();
        mHandler = handler;

        _state = new GameState(canvasWidth, canvasHeight);
        score = _state.score;
        Log.i("Pong - GameThread","Size: " + canvasWidth + " by " + canvasHeight);

    }

    private void AddToastToQueue(String messageToShow)
    {
        Message msg = mHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("toast", messageToShow);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    @Override
    public void run() {
        long startTime = SystemClock.uptimeMillis();
        while (isRunning)
        {
            Canvas canvas = null;
            try
            {
                long currentTime = SystemClock.uptimeMillis();
                frameTime = (currentTime - startTime) / 1000.0;
                startTime = currentTime;

                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder)
                {
                    if ( delayTime <= 0)
                    {
                        _state.CheckCollision(frameTime);
                        _state.CheckBallBounds(frameTime);
                        //_state.AdjustPaddles();
                        _state.AdvanceBall(frameTime);
                        delayTime = _state.getDelayTime();
                    }

                    if (delayTime > 0)
                        delayTime = delayTime - frameTime;
                    score = _state.score;
                    DrawScoreBoard();
                    //doDraw(canvas);
                    Paint paint = new Paint();
                    _state.draw(canvas,paint);
                }
            }
            finally
            {
                if (canvas != null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public GameState getGameState()
    {
        return _state;
    }

    public void setRunning(boolean isRunning)
    {
        this.isRunning = isRunning;
    }

    /*public void doStart0p()
    {
        synchronized (surfaceHolder)
        {
            previousState = STATE_RUNNING_0P;
            setState(STATE_RUNNING_0P);
            ResetGame();
        }
    }*/

    public void setSurfaceSize(int width, int height)
    {
        synchronized (surfaceHolder)
        {
            canvasWidth = width;
            canvasHeight = height;
            _state = new GameState(width, height);
            //backgroundImage = Bitmap.createScaledBitmap(backgroundImage, width, height, true);
            //ball.canvasChanges(width, height);
            //battBottom.canvasChanges(width, height);
            //battTop.canvasChanges(width, height);
            //ball.center();
            //SetInitialBattPosition();
        }
    }
    private void DrawScoreBoard()
    {
        Message msg = mHandler.obtainMessage();
        Bundle b = new Bundle();
        String scoreBoard = score.CreateScoreBoard();
        //if (shouldDiagnosticInformation)
        //    scoreBoard += " FPS: " + (int) (1 / frameTime);
        b.putString("text", scoreBoard);
        b.putInt("viz", View.VISIBLE);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

}
