package com.wawrzynczak.pong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener {

    private GameView pongSurfaceView;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor rotation;

    private float currentX;
    private float currentY;
    private float currentDeltaX;

    private float currentRoll;
    private float currentDelta = 0;
    private static final int SENSOR_DELAY = 500 * 1000; // 500ms
    private static final int FROM_RADS_TO_DEGS = -57;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pongSurfaceView = (GameView)findViewById(R.id.gameview);
        pongSurfaceView.setTextView((TextView)findViewById(R.id.score));
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (rotation != null) {
            sensorManager.registerListener(this, rotation, SENSOR_DELAY);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String name = (String) item.getTitle();
        GameThread thread = pongSurfaceView.getGameThread();
        switch(item.getItemId())
        {
            case R.id.menu_start_1p :
                thread.doStart0p();
                return true;

            case  R.id.menu_pause :
                this.onPause();
                return true;

            case R.id.menu_resume :
                this.onResume();
                 return true;

            case R.id.action_settings :
                showUserSettings();
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
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        GameThread gameThread = pongSurfaceView.getGameThread();
        gameThread.unpause();
        if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        if (rotation != null) {
            sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void onDestroy()
    {
        super.onDestroy();
        //SoundManager.cleanup();
    }
    private void showUserSettings() {
        Intent i = new Intent(this, PreferencesActivity.class);
        startActivityForResult(i, Constants.RESULT_PREFERENCES);

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        /*
        float deltaX = 0;
        float deltaY = 0;

        float newX = event.values[0];
        float newY = event.values[1];
        int direction = 0;

        sensorManager.remapCoordinateSystem();
        //if (event.sensor == accelerometer) {
            deltaX = currentX - newX;
            deltaY = currentY - newY;


            currentX = newX;
            currentY = newY;

            if (deltaX < 2) {
                direction = 1;
            } else if (deltaX > -2) {
                direction = -1;
            }
            // Do something with this sensor value.
            if (direction != 0) {
                pongSurfaceView.movePlayerPaddle(newX, newY, direction);
                TextView text = (TextView) findViewById(R.id.accelerometer);
                text.setText("DeltaX: " + newX + "DeltaY: " + newY);
            }
        //}*/
        float delta = 0;
        if (event.sensor == rotation) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                delta = update(truncatedRotationVector);
            } else {
                delta = update(event.values);
            }
            float percentage = Math.abs( currentDelta / delta);
            if (percentage == 0)
                percentage = 1;
            if (delta > 2)
            {
                pongSurfaceView.movePlayerPaddle(1, percentage);
            }
            else if(delta < -2)
            {
                pongSurfaceView.movePlayerPaddle(-1, percentage);
            }
        }
    }

    private float update(float[] vectors) {
        float delta = 0;

        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        float pitch = orientation[1] * FROM_RADS_TO_DEGS;
        float roll = orientation[2] * FROM_RADS_TO_DEGS;
        //((TextView)findViewById(R.id.pitch)).setText("Pitch: "+pitch);
        //((TextView)findViewById(R.id.roll)).setText("Roll: "+roll);
        TextView text = (TextView) findViewById(R.id.accelerometer);
        text.setText("Pitch: "+ pitch + "Roll: "+roll);

        delta = currentRoll - roll;
        currentRoll = roll;
        return delta;
    }


}
