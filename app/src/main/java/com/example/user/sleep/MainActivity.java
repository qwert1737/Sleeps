package com.example.user.sleep;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private final int REQUEST_BLUETOOTH_ENABLE = 100;

    private TextView mConnectionStatus;
    private EditText mInputEditText;

    ConnectedTask mConnectedTask = null;
    static BluetoothAdapter mBluetoothAdapter;
    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    static boolean isConnectionError = false;
    private static final String TAG = "BluetoothClient";

    double initTime;

    ImageView imagecheck;
    TextView sleepcheck;

    TextView todaySleep;
    TextView tomorrowSleep;
    TextView meanSleep;
    TextView maxSleep;
    TextView minSleep;

    String sleeptime;
    String s_check;
    String getTime;
    int mean_sleeptime;
    int max_sleeptime;
    int min_sleeptime;

    int time;

    Date date = new Date(System.currentTimeMillis());

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        getTime = simpleDateFormat.format(date);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.activity_draw);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.open, R.string.close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);

        sleepshow();

        Log.d(TAG, "Initalizing Bluetooth adapter...");
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showErrorDialog("This device is not implement Bluetooth.");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
        } else {
            Log.d(TAG, "Initialisation successful.");
            showPairedDevicesListDialog();
        }


    }

    public void sleepshow(){
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor sleepCursor = db.rawQuery("select sleep_time from tb_sleep where day=? AND start_end=?",
                new String[]{getTime, "End"});

        sleepCursor.moveToNext();

        imagecheck = (ImageView) findViewById(R.id.sleep_image);
        sleepcheck = (TextView) findViewById(R.id.sleepcheck);
        todaySleep = (TextView) findViewById(R.id.todaysleep);
        tomorrowSleep = (TextView) findViewById(R.id.tomorrowsleep);
        meanSleep = (TextView) findViewById(R.id.meansleep);
        maxSleep = (TextView) findViewById(R.id.maxsleep);
        minSleep = (TextView) findViewById(R.id.minsleep);

        if (sleepCursor.getCount() == 0) {
            imagecheck.setImageResource(R.drawable.hello);
            s_check = "수면 기록 없음";
            sleeptime = "수면 기록 없음";
        } else {
            time = sleepCursor.getInt(0);
            sleeptime = (time / 100) + "시간 " + (time % 100) + "분";

            if (time > 630 && time < 830) {
                imagecheck.setImageResource(R.drawable.laughing);
                s_check = "적당한 수면";
            } else if (time <= 630) {
                imagecheck.setImageResource(R.drawable.sad);
                s_check = "수면 부족";
            } else if (time >= 830) {
                imagecheck.setImageResource(R.drawable.sad);
                s_check = "과한 수면";
            }
        }
        sleepcheck.setText(s_check);
        todaySleep.setText(sleeptime);

        time = Integer.parseInt(getTime) - 1;
        getTime = Integer.toString(time);

        sleepCursor = db.rawQuery("select sleep_time from tb_sleep where day=? AND start_end=?",
                new String[]{getTime, "End"});

        sleepCursor.moveToNext();

        if (sleepCursor.getCount() == 0) {
            sleeptime = "수면 기록 없음";
        } else {
            time = sleepCursor.getInt(0);
            sleeptime = (time / 100) + "시간 " + (time % 100) + "분";
        }
        tomorrowSleep.setText(sleeptime);

        sleepCursor = db.rawQuery("select sleep_time, day from tb_sleep where start_end=?",
                new String[]{"End"});

        sleepCursor.moveToNext();

        if (sleepCursor.getCount() == 0){

        }
        else{
            mean_sleeptime = sleepCursor.getInt(0);
            max_sleeptime = mean_sleeptime;
            min_sleeptime = mean_sleeptime;

            while (sleepCursor.moveToNext()) {
                time = sleepCursor.getInt(0);
                if (max_sleeptime < time) {
                    max_sleeptime = time;
                }

                if (min_sleeptime > time) {
                    min_sleeptime = time;
                }

                if ((mean_sleeptime % 100) + (time % 100) > 59) {
                    mean_sleeptime += time + 40;
                } else {
                    mean_sleeptime += time;
                }
            }

            mean_sleeptime = (((mean_sleeptime / 100)*60) + (mean_sleeptime % 100)) / sleepCursor.getCount();
        }

        mean_sleeptime = ((mean_sleeptime /60)*100) + (mean_sleeptime%60);
        sleeptime = (mean_sleeptime / 100) + "시간 " + (mean_sleeptime % 100) + "분";
        meanSleep.setText(String.valueOf(sleeptime));
        sleeptime = (max_sleeptime / 100) + "시간 " + (max_sleeptime % 100) + "분";
        maxSleep.setText(String.valueOf(sleeptime));
        sleeptime = (min_sleeptime / 100) + "시간 " + (min_sleeptime % 100) + "분";
        minSleep.setText(String.valueOf(sleeptime));

        db.close();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - initTime > 3000){
                Toast t = Toast.makeText(this,R.string.main_back_end,Toast.LENGTH_SHORT);
                t.show();
            }
            else{
                finish();
            }
            initTime=System.currentTimeMillis();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int position;

        Integer i = item.getItemId();
        if(i==R.id.sleeping){
            Intent intent = new Intent(this, weekGraph.class);
            startActivity(intent);
            return true;
        }
        if(i==R.id.CCTV){
            Toast.makeText(this,String.valueOf(time),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Readstudentactivity.class);
            startActivity(intent);
            return true;
        }
        if(i==R.id.setting){
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
            return true;
        }
        if(i==R.id.power){
            Toast.makeText(this,"power off",Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }


        return false;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mConnectedTask != null) {

            mConnectedTask.onCancelled(true);
        }
    }

    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        private BluetoothSocket mBluetoothSocket = null;
        private BluetoothDevice mBluetoothDevice = null;

        ConnectTask(BluetoothDevice bluetoothDevice) {
            mBluetoothDevice = bluetoothDevice;
            mConnectedDeviceName = bluetoothDevice.getName();

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            try {
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                Log.d(TAG, "create socket for " + mConnectedDeviceName);

            } catch (IOException e) {
                Log.e(TAG, "socket create failed " + e.getMessage());
            }

            mConnectionStatus.setText("connecting...");
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            mBluetoothAdapter.cancelDiscovery();
            try {
                mBluetoothSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mBluetoothSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " +
                            " socket during connection failure", e2);
                }

                return false;
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean isSucess) {

            if (isSucess) {
                connected(mBluetoothSocket);
            } else {

                isConnectionError = true;
                Log.d(TAG, "Unable to connect device");
                showErrorDialog("Unable to connect device");
            }
        }
    }


    public void connected(BluetoothSocket socket) {
        mConnectedTask = new ConnectedTask(socket);
        mConnectedTask.execute();
    }


    private class ConnectedTask extends AsyncTask<Void, String, Boolean> {

        private InputStream mInputStream = null;
        private OutputStream mOutputStream = null;
        private BluetoothSocket mBluetoothSocket = null;

        ConnectedTask(BluetoothSocket socket) {

            mBluetoothSocket = socket;
            try {
                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "socket not created", e);
            }

            Log.d(TAG, "connected to " + mConnectedDeviceName);
            mConnectionStatus.setText("connected to " + mConnectedDeviceName);
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            byte[] readBuffer = new byte[1024];
            int readBufferPosition = 0;

            while (true) {

                if (isCancelled()) return false;

                try {

                    int bytesAvailable = mInputStream.available();

                    if (bytesAvailable > 0) {

                        byte[] packetBytes = new byte[bytesAvailable];
                        mInputStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {

                            byte b = packetBytes[i];
                            if (b == '\n') {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                        encodedBytes.length);
                                String recvMessage = new String(encodedBytes, "UTF-8");

                                readBufferPosition = 0;

                                Log.d(TAG, "recv message: " + recvMessage);
                                Toast.makeText(MainActivity.this, "rec:" + recvMessage, Toast.LENGTH_SHORT).show();
                                publishProgress(recvMessage);
                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (IOException e) {

                    Log.e(TAG, "disconnected", e);
                    return false;
                }
            }

        }

        @Override
        protected void onProgressUpdate(String... recvMessage) {

            mConversationArrayAdapter.insert(mConnectedDeviceName + ": " + recvMessage[0], 0);
        }

        @Override
        protected void onPostExecute(Boolean isSucess) {
            super.onPostExecute(isSucess);

            if (!isSucess) {


                closeSocket();
                Log.d(TAG, "Device connection was lost");
                isConnectionError = true;
                showErrorDialog("Device connection was lost");
            }
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);

            closeSocket();
        }

        void closeSocket() {

            try {
                mBluetoothSocket.close();
                Log.d(TAG, "close socket()");

            } catch (IOException e2) {

                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }
        }
    }

    public void showPairedDevicesListDialog() {
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        final BluetoothDevice[] pairedDevices = devices.toArray(new BluetoothDevice[0]);

        if (pairedDevices.length == 0) {
            showQuitDialog("No devices have been paired.\n"
                    + "You must pair it with another device.");
            return;
        }

        String[] items;
        items = new String[pairedDevices.length];
        for (int i = 0; i < pairedDevices.length; i++) {
            items[i] = pairedDevices[i].getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select device");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                ConnectTask task = new ConnectTask(pairedDevices[which]);
                task.execute();
            }
        });
        builder.create().show();
    }


    public void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isConnectionError) {
                    isConnectionError = false;
                    finish();
                }
            }
        });
        builder.create().show();
    }


    public void showQuitDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_BLUETOOTH_ENABLE){
            if (resultCode == RESULT_OK){
                //BlueTooth is now Enabled
                showPairedDevicesListDialog();
            }
            if(resultCode == RESULT_CANCELED){
                showQuitDialog( "You need to enable bluetooth");
            }
        }
    }


}
