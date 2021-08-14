package com.led.led;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothDevice;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    Button btnPaired; // Button listener private variable
    ListView deviceList; // device list for bluetooth connection
    public final static String EXTRA_ADDRESS = "";
    private BluetoothAdapter myBluetooth = null; // Bluetooth connection variable
    private Set pairedDevices; // (Need to figure out what exactly this does)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPaired = (Button)findViewById(R.id.button);
        deviceList = (ListView)findViewById(R.id.ListView);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(myBluetooth == null) {
            // Checks for no BlueTooth connections available
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            //finish apk
            finish();
        } else if (!myBluetooth.isEnabled()){
            //Ask the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
    }
        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            // Make an intent to start next activity.
            Intent i = new Intent(MainActivity.this, ledControl.class);
            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };

    private void pairedDevicesList() {
        Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices(); //set define the datatype as an object
        ArrayList list = new ArrayList();

        if (pairedDevices.size() >= 1) {
            for(BluetoothDevice bt: pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);

    }


}