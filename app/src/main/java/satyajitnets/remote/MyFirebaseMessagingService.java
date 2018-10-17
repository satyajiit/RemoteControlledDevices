package satyajitnets.remote;

import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    AlertDialog dialog;
    LayoutInflater inflater;
    View layout;
    Toast toast;
    TextView text;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
   String address="00:21:13:04:BE:A0"; //Bluetooth Device MAC Address
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "From: " + remoteMessage.getNotification().getBody());

       // Map<String, String> data = remoteMessage.getData();
       // String myCustomKey = data.get("my_custom_key");












        final String msg=remoteMessage.getNotification().getBody();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
               Toast.makeText(getApplicationContext(),
                       msg,
                        Toast.LENGTH_LONG).show();

                if(msg.equals("ON")) onD();
        else offD();


            }
        });
    }


    public void onD(){
        if (btSocket!=null)
        {
            try {
                btSocket.getOutputStream().write("ON".toString().getBytes()); //on the device
                Toast.makeText(getApplicationContext(),
                        "ON",
                        Toast.LENGTH_LONG).show();
            }
            catch (IOException e)
            {

            }
        }
        else Toast.makeText(getApplicationContext(),
                "ER",
                Toast.LENGTH_LONG).show();
    }

    public void offD(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("OFF".toString().getBytes()); //off the device
                Toast.makeText(getApplicationContext(),
                        "OFF",
                        Toast.LENGTH_LONG).show();
            }
            catch (IOException e)
            {
                //msg("Cannot Send Msg..Error Code 123");
            }
        }
        else Toast.makeText(getApplicationContext(),
                "ERROR",
                Toast.LENGTH_LONG).show();
    }


}