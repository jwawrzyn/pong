package com.wawrzynczak.pong;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jenny on 11/12/2014.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private SurfaceHolder holder;
    private Context context;
    private TextView statusText;

    public GameView(Context context, AttributeSet attrs) {
        super(context,attrs);

        //So we can listen for events...
        this.context = context;
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);

        //and instantiate the thread
        gameThread = CreateNewGameThread();


    }

    public void setTextView(TextView textView)
    {
        statusText = textView;
    }

    private GameThread CreateNewGameThread()
    {
        Log.i("Pong - GameView", "New game thread");

        return new GameThread(holder, context, new Handler()
        {
            @Override
            public void handleMessage(Message m) {
                Bundle bundle = m.getData();
                if (bundle.containsKey("viz")) {
                    int visibility = (int)m.getData().getInt("viz");
                    switch(visibility) {
                        case View.VISIBLE:
                            statusText.setVisibility(View.VISIBLE);
                            break;

                        default:statusText.setVisibility(View.INVISIBLE);
                            break;
                    }
                    statusText.setText(m.getData().getString("text"));
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameThread.getGameState().touchEvent(event);
    }

    public void movePlayerPaddle( int direction, float percentage)
    {
        gameThread.movePlayerPaddle(direction, percentage);
    }
    //implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceChanged(SurfaceHolder older, int format, int width, int height) {
        //mandatory, sallow it for now
        gameThread.setSurfaceSize(width, height);
    }

    //implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        try
        {
            gameThread.start();
        }
        catch (Exception error)
        {
            gameThread = CreateNewGameThread();
            gameThread.start();
            gameThread.setRunning(true);
        }
    }

    //implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.setRunning(false);
        boolean retry = true;
        while (retry)
        {
            try
            {
                gameThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public GameThread getGameThread()
    {
        return gameThread;
    }
}
