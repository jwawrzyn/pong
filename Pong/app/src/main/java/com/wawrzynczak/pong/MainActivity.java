package com.wawrzynczak.pong;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {

    private GameView pongSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pongSurfaceView = (GameView)findViewById(R.id.gameview);
        pongSurfaceView.setTextView((TextView)findViewById(R.id.score));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, Constants.MENU_START_1P, 0, R.string.menu_start_1p);
        menu.add(0, Constants.MENU_START_2P, 0, R.string.menu_start_2p);
        menu.add(0, Constants.MENU_START_0P, 0, R.string.menu_start_0p);
        menu.add(0, Constants.MENU_PAUSE, 0, R.string.menu_pause);
        menu.add(0, Constants.MENU_RESUME, 0, R.string.menu_resume);
        menu.add(0, Constants.MENU_SOUND_ON, 0, R.string.menu_sound);
        menu.add(0, Constants.MENU_SHOWINFO, 0, R.string.menu_info);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        GameThread thread = pongSurfaceView.getGameThread();
        switch(id)
        {
            case Constants.MENU_START_1P :
                thread.doStart0p();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        GameThread gameThread = pongSurfaceView.getGameThread();
        gameThread.pause(); // pause game when Activity pauses
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        GameThread gameThread = pongSurfaceView.getGameThread();
        gameThread.unpause();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        //SoundManager.cleanup();
    }

}
