package satyajitnets.remote;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;


import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.widget.RelativeLayout.LayoutParams;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;


public class ledControl extends AppCompatActivity {

   // Button btnOn, btnOff, btnDis;

    String address = null;
    AlertDialog dialog;
    Context con;

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    TextView v2,v3,v4,v5,text,n1,n2,n3,n4;
    LayoutInflater inflater;
    View layout;
    Toast toast;
    CardView cd3,cd4;
    String msg; //var to that stores msg
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Animation animV;
    Typeface fn2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);




        con=this;
        address="00:21:13:04:BE:A0"; //Bluetooth Device MAC Address

        //view of the ledControl
        setContentView(R.layout.main);


        overridePendingTransition(R.anim.anim_translate_in, R.anim.anim_translate_out);

        animV= AnimationUtils.loadAnimation(this, R.anim.anim_translate_in);


        Typeface fn3 = Typeface.createFromAsset(getAssets(), "fonts/shop.ttf");
        fn2 = Typeface.createFromAsset(getAssets(), "fonts/cnd.ttf");

       Intent intent = new Intent(this, MyFirebaseMessagingService.class);
      startService(intent);

        ActionBar ab = getSupportActionBar();
        TextView tv = new TextView(getApplicationContext());

        LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, // Width of TextView
                LayoutParams.WRAP_CONTENT);

        tv.setLayoutParams(lp);


        tv.setText("Remote Control Client"); // ActionBar title text

        tv.setTextColor(Color.WHITE);
        tv.setTypeface(fn3);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ab.setCustomView(tv);

        cd3=findViewById(R.id.crd3);
        cd4=findViewById(R.id.crd4);


        n1=findViewById(R.id.en11);
        n2=findViewById(R.id.t11);
        n3=findViewById(R.id.en8);
        n4=findViewById(R.id.t22);

        n1.setTypeface(fn3);
        n2.setTypeface(fn2);
        n3.setTypeface(fn3);
        n4.setTypeface(fn2);


        v2=findViewById(R.id.en1);
        v3=findViewById(R.id.en2);
        v4=findViewById(R.id.t1);
        v5=findViewById(R.id.t2);


v2.setTypeface(fn3);
v3.setTypeface(fn3);
        v4.setTypeface(fn2);
        v5.setTypeface(fn2);
        cd3.setAnimation(animV);
        cd4.setAnimation(animV);

        new ConnectBT().execute();




        //Toast Design
        inflater = getLayoutInflater();

        layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        text = (TextView) layout.findViewById(R.id.TMsg);


        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);


        text.setTypeface(fn3);



    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void turnOffLed()
    {
        if (btSocket!=null)
        {
            try
            {
                String s="Hello";
                btSocket.getOutputStream().write(s.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("Pubg".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    void msg(String message) {

        text.setText(message);


        toast.show();
    }





    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {


            dialog = new SpotsDialog.Builder()
                    .setContext(con)
                    .setTheme(R.style.upd)
                    .build();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();



        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Error Code 03");
                fgh();

            }
            else
            {
                msg("Connected...!!.");
                isBtConnected = true;
            }

        }
    }


    public void ad(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("SEND MESSAGE");
        alertDialog.setMessage("Enter the message that will be displayed on the Led.");
        alertDialog.setCancelable(false);
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lpp);
        input.setTypeface(fn2);
        input.setText(msg);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.msg);

        alertDialog.setPositiveButton("PUSH IT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        msg=String.valueOf(input.getText());
                        if(msg.length()>32) msg("Sorry such a long message cannot be displayed!!");
                        else
                        push();
                    }
                });



        alertDialog.show();



    }
    public void start(View v){
        ad();
    }
    public void fgh(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Error In Connection!!");
        alertDialog.setMessage("Sorry the bluetooth device may not be connected..");
        alertDialog.setCancelable(false);

        LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        alertDialog.setIcon(R.drawable.user);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });



        alertDialog.show();



    }
    public void start2(View v){
       Intent i=new Intent(this,abt.class);
       startActivity(i);

    }
    void push(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(msg.toString().getBytes());
                msg("Successfully Sent!!");
            }
            catch (IOException e)
            {
                msg("Cannot Send Msg..Error Code 01");
            }
        }
        else msg("Cannot Send Msg..Error Code 02");
    }
public void onD(View v){
    if (btSocket!=null)
    {
        try
        {
            btSocket.getOutputStream().write("ON".toString().getBytes()); //on the device
            msg("Supply On!!");
        }
        catch (IOException e)
        {
            msg("Cannot Send Msg..Error Code 123");
        }
    }
    else msg("Cannot Send Msg..Error Code 412");
}

    public void offD(View v){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("OFF".toString().getBytes()); //off the device
                msg("Supply is Cut!!!");
            }
            catch (IOException e)
            {
                msg("Cannot Send Msg..Error Code 123");
            }
        }
        else msg("Cannot Send Msg..Error Code 412");
    }
}
