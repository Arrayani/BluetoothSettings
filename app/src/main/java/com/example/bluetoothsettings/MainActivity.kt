package com.example.bluetoothsettings

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    //bluetooth adapter
    lateinit var bAdapter :BluetoothAdapter
    private val REQUEST_CODE_ENABLE_BT:Int =1
    private val REQUEST_CODE_DISCOVERABLE_BT:Int =2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bluetoothStatusTv = findViewById<TextView>(R.id.bluetoothStatusTv)
        val bluetoothTv = findViewById<ImageView>(R.id.bluetoothTv)
        val turnOnBtn= findViewById<Button>(R.id.turnOnBtn)
        val turnOfBtn= findViewById<Button>(R.id.turnOfBtn)
        val discoverableBtn = findViewById<Button>(R.id.discoverableBtn)
        val pairedBtn = findViewById<Button>(R.id.pairedBtn)
        val pairedTv = findViewById<TextView>(R.id.pairedTv)

        //init bluetooth adapter
        bAdapter = BluetoothAdapter.getDefaultAdapter()
        //chek jika bluetooth avalaible or not
        if (bAdapter==null){
            bluetoothStatusTv.text = "Bluetooth is not available"
        }
        else {
            bluetoothStatusTv.text = "Bluetooth is available"
        }
        //set image according status
        if ( bAdapter.isEnabled){
            bluetoothTv.setImageResource(R.drawable.ic_bluetooth_on)
        }
        else {
            bluetoothTv.setImageResource(R.drawable.ic_bluetooth_off)
        }

        turnOnBtn.setOnClickListener {
            if (bAdapter.isEnabled){
            Toast.makeText(this,"already on",Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@setOnClickListener
                }
                startActivityForResult(intent,REQUEST_CODE_ENABLE_BT)

            }

        }
        turnOfBtn.setOnClickListener {
            if (!bAdapter.isEnabled){
                Toast.makeText(this,"already off",Toast.LENGTH_LONG).show()
            }
            else{
                bAdapter.disable()
                bluetoothTv.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this,"Bluetooth turn off",Toast.LENGTH_LONG).show()
            }
        }

        discoverableBtn.setOnClickListener {
        if (!bAdapter.isDiscovering){
            Toast.makeText(this,"Make device discoverable",Toast.LENGTH_LONG).show()
            val intent=Intent(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            startActivityForResult(intent,REQUEST_CODE_DISCOVERABLE_BT)
        }
        }

        pairedBtn.setOnClickListener {
            if (bAdapter.isEnabled){
                pairedTv.text = "Paired Devices"
                val devices  = bAdapter.bondedDevices
                for (device in devices ){
                    val deviceName = device.name
                    val deviceAddress = device
                    pairedTv.append("\nDevice: $deviceName,$device")
                }
            }
                        else{
                Toast.makeText(this,"Turn on bluetooth first",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val bluetoothTv = findViewById<ImageView>(R.id.bluetoothTv)
        when(requestCode){
            REQUEST_CODE_ENABLE_BT ->
                //if ( REQUEST_CODE_ENABLE_BT == Activity.RESULT_OK){
                if ( resultCode == Activity.RESULT_OK){
                    bluetoothTv.setImageResource(R.drawable.ic_bluetooth_on)
                    Toast.makeText(this,"Bluetooth is on",Toast.LENGTH_LONG).show()
            }else{
                    Toast.makeText(this,"Bluetooth failed",Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}