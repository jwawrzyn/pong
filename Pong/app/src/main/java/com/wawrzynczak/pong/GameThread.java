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
    private GameState state;
    private Handler mHandler;
    private int canvasWidth;
    private int canvasHeight;
    private Score score;

    private boolean isRunning;
    private double frameTime;
    private double delayTime = 0;
    private int currentState;
    private int previousState;



    public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler)
    {
        this.surfaceHolder = surfaceHolder;

        mHandler = handler;

        state = new GameState(canvasWidth, canvasHeight);
        score = state.getScore();
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

    public void setState(int state)
    {
        this.currentState = state;
    }

    @Override
    public void run() {
        long startTime = SystemClock.uptimeMillis();
        boolean finished = false;
        while (isRunning)
        {
            Canvas canvas = null;
            try
            {
                if (currentState == Constants.STATE_RUNNING_1P) {
                    long currentTime = SystemClock.uptimeMillis();
                    frameTime = (currentTime - startTime) / 1000.0;
                    startTime = currentTime;

                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        if (delayTime <= 0) {
                            state.CheckCollision(frameTime);
                            state.CheckBallBounds(frameTime);
                            //state.AdjustPaddles();
                            state.AdvanceBall(frameTime);
                            delayTime = state.getDelayTime();
                        }

                        if (delayTime > 0)
                            delayTime = delayTime - frameTime;
                        score = state.getScore();
                        DrawScoreBoard();
                        //doDraw(canvas);
                        Paint paint = new Paint();
                        if (state.getFinished())
                        {
                            FinishGame();
                        }
                        state.draw(canvas, paint);
                    }
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
        return state;
    }

    public void setRunning(boolean isRunning)
    {
        this.isRunning = isRunning;
    }

    public void doStart1P() {
        synchronized (surfaceHolder) {
            previousState = Constants.STATE_RUNNING_1P;
            setState(Constants.STATE_RUNNING_1P);
            //ResetGame();
        }
    }
    public void doStart0p()
    {
        synchronized (surfaceHolder)
        {
            previousState = Constants.STATE_RUNNING_1P;
            setState(Constants.STATE_RUNNING_1P);
            //ResetGame();
        }
    }

    public void setSurfaceSize(int width, int height)
    {
        synchronized (surfaceHolder)
        {
            canvasWidth = width;
            canvasHeight = height;
            state = new GameState(width, height);
            doStart0p();
            //backgroundImage = Bitmap.createScaledBitmap(backgroundImage, width, height, true);
            //ball.canvasChanges(width, height);
            //battBottom.canvasChanges(width, height);
            //battTop.canvasChanges(width, height);
            //ball.center();
            //SetInitialBattPosition();
        }
    }
    private void DrawScoreBoard() {
        DrawScoreBoard(false);
    }
    private void DrawScoreBoard(boolean finished)
    {
        Message msg = mHandler.obtainMessage();
        Bundle b = new Bundle();
        String scoreBoard;
        if (finished)
            scoreBoard = score.CreateWinnerBoard();
        else
            scoreBoard = score.CreateScoreBoard();
        //if (shouldDiagnosticInformation)
        //    scoreBoard += " FPS: " + (int) (1 / frameTime);
        b.putString("text", scoreBoard);
        b.putInt("viz", View.VISIBLE);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    private void FinishGame()
    {
        setState(Constants.STATE_PAUSE);
        DrawScoreBoard(true);
        state.ResetGame();
    }

    private void ResetGame()
    {
        state.ResetGame();
    }

    public void pause()
    {
        synchronized (surfaceHolder)
        {
            setState(Constants.STATE_PAUSE);
        }
    }

    public void unpause()
    {
        synchronized (surfaceHolder)
        {
            if (currentState == Constants.STATE_PAUSE)
            {
                setState(previousState);
            }
        }
    }

}
