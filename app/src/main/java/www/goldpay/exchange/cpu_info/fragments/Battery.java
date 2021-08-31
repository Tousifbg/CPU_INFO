package www.goldpay.exchange.cpu_info.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import www.goldpay.exchange.cpu_info.R;

public class Battery extends Fragment {

    private TextView health,b_level,power,status,technology,temp,voltage,capacity;

    IntentFilter intentfilter;
    int deviceHealth;
    String currentBatteryHealth="Battery Health ";
    int batteryLevel;

    double batteryCapacity = 0;

    boolean bCharging;

    int health_;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_battery, container, false);

        health = view.findViewById(R.id.health);
        b_level = view.findViewById(R.id.level);
        status = view.findViewById(R.id.status);
        technology = view.findViewById(R.id.technology);
        temp = view.findViewById(R.id.temp);
        voltage = view.findViewById(R.id.voltage);
        capacity = view.findViewById(R.id.capacity);

        //battery health
        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getContext().registerReceiver(broadcastreceiver,intentfilter);

        //battery capacity
        getBatteryCapacity(getContext());

        //battery current charge
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getContext().registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = (level * 100) / (float)scale;

        capacity.setText(String.valueOf(batteryCapacity + " mAh"));
        b_level.setText(String.valueOf(batteryPct + "%"));

        isCharging();

        return view;
    }

    public double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return batteryCapacity;
    }

    public boolean isCharging()
    {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getContext().getApplicationContext()
                .registerReceiver(null, ifilter);
        int batterystatuss = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        bCharging = batterystatuss == BatteryManager.BATTERY_STATUS_CHARGING ||
                batterystatuss == BatteryManager.BATTERY_STATUS_FULL;

        if (batterystatuss == BatteryManager.BATTERY_STATUS_CHARGING)
        {
            status.setText("Charging");
        }
        if (batterystatuss == BatteryManager.BATTERY_STATUS_DISCHARGING){
            status.setText("Discharging");
        }
        if (batterystatuss == BatteryManager.BATTERY_STATUS_FULL)
        {
            status.setText("Full");
        }
        return bCharging;
    }

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int health_ = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            float tempTwo = ((float) temperature) / 10;

            boolean isPresent = intent.getBooleanExtra("present", false);

            Bundle bundle = intent.getExtras();
            String str = bundle.toString();
            Log.i("Battery Info", str);

            if (isPresent) {
                int percent = (level * 100) / scale;

                technology.setText(""+bundle.getString("technology"));
                voltage.setText(""+bundle.getInt("voltage")+"mV");
                temp.setText(String.valueOf(tempTwo + " 'C"));
                health.setText(""+getHealthString(health_));


            } else {
                //battery_percentage.setText("Battery not present!!!");
            }
        }
    };

    private String getPlugTypeString(int plugged) {
        String plugType = "Unknown";

        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = "USB";
                break;
        }
        return plugType;
    }

    private String getHealthString(int health) {
        String healthString = "Unknown";
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good Condition";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Over Heat";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Failure";
                break;
        }
        return healthString;
    }
    private String getStatusString(int status) {
        String statusString = "Unknown";

        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = "Not Charging";
                break;
        }
        return statusString;
    }

}