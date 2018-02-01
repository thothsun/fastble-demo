package com.sunshuai.fastble_demo.activity;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.orhanobut.logger.Logger;
import com.sunshuai.fastble_demo.R;
import com.sunshuai.fastble_demo.constant.IntentConstant;
import com.sunshuai.fastble_demo.observe.Observer;
import com.sunshuai.fastble_demo.observe.ObserverManager;

import butterknife.ButterKnife;


public class OperationActivity extends AppCompatActivity implements Observer {

    private BleDevice bleDevice;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGattService bluetoothGattService;
    private BluetoothGatt gatt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        ButterKnife.bind(this);
        initBLE();
    }


    private void initBLE(){
        bleDevice = getIntent().getParcelableExtra(IntentConstant.EXTRA_NAME_BLEDEVICE);
        if (bleDevice == null) {
            finish();
        }

        ObserverManager.getInstance().addObserver(this);

        gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
        bluetoothGattService = gatt.getServices().get(0);
        characteristic = bluetoothGattService.getCharacteristics().get(0);
    }

    private void send(byte[] bytes) {
        BleManager.getInstance().write(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                bytes,
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.i("onWriteSuccess");
                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.i("onWriteFailure");
                            }
                        });
                    }
                });
    }

    private void send(String hex) {
        BleManager.getInstance().write(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                HexUtil.hexStringToBytes(hex),
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.i("onWriteSuccess");
                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Logger.i("onWriteFailure");
                            }
                        });
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().clearCharacterCallback(bleDevice);
        ObserverManager.getInstance().deleteObserver(this);
    }

    @Override
    public void disConnected(BleDevice device) {
        if (device != null && bleDevice != null && device.getKey().equals(bleDevice.getKey())) {
            finish();
        }
    }

}
