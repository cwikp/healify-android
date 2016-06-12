package com.healify.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.healify.R;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

public class MainActivity extends AppCompatActivity {

    private ProximityManagerContract proximityManager;
    private Set<Beacon> beacons = Collections.synchronizedSet(new HashSet<Beacon>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setContentView(R.layout.activity_main);
        KontaktSDK.initialize("ICoaWqUPuyRrbSeeTCOZHfbtjtwrzFqn");

        proximityManager = new ProximityManager(this);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.configuration()
                .scanPeriod(ScanPeriod.RANGING);
        proximityManager.setEddystoneListener(createEddystoneListener());
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
//            @Override
//            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
////                Log.i("Sample", "IBeacon discovered " + ibeacon.getUniqueId());
////                Log.i("Sample", "\t proximity:" + ibeacon.getProximityUUID());
////                Log.i("Sample", "\t major: " + ibeacon.getMajor());
////                Log.i("Sample", "\t minor: " + ibeacon.getMinor());
////                Log.i("Sample", "\t identifier: " + region.getIdentifier());
////                Log.i("Sample", "\t shuffled: " + region.isShuffled());
//                Beacon beacon = new Beacon(ibeacon.getUniqueId(), ibeacon.getRssi());
////                Log.i("Sample", getBeacons().toString());
//
//                if(beacons.contains(beacon)) {
//                    beacons.remove(beacon);
//                }
//
//                beacons.add(beacon);
//            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
//                Log.i("Sample", "updated: " + iBeacons.size());

                for(IBeaconDevice ibeacon : iBeacons) {
                    Beacon beacon = new Beacon(ibeacon.getUniqueId(), ibeacon.getRssi());
//                Log.i("Sample", getBeacons().toString());

                    if(beacons.contains(beacon)) {
                        beacons.remove(beacon);
                    }

                    beacons.add(beacon);
                }

//                Log.i("Sample", getBeacons().toString());

            }

//            @Override
//            public void onIBeaconLost(IBeaconDevice ibeacon, IBeaconRegion region) {
//                Log.i("Sample", "lost: " + ibeacon.getUniqueId());
//                Beacon beacon = new Beacon(ibeacon.getUniqueId(), ibeacon.getRssi());
//
//                if(beacons.contains(beacon)) {
//                    beacons.remove(beacon);
//                }
//            }
        };
    }

    private List<Beacon> getBeacons() {
        ArrayList<Beacon> beacons = new ArrayList<>(this.beacons);
        Collections.sort(beacons);
        return beacons;
    }

    private EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i("Sample", "Eddystone discovered: " + eddystone.getUniqueId());
            }
        };
    }

    @Data
    @RequiredArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(exclude = {"rssi"})
    public static class Beacon implements Comparable<Beacon> {

        private final String id;
        private final double rssi;

        @Override
        public int compareTo(Beacon other) {
            return Double.compare(other.rssi, this.rssi);
        }
    }
}
