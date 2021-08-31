package www.goldpay.exchange.cpu_info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.scwang.wave.MultiWaveHeader;

import www.goldpay.exchange.cpu_info.fragments.Battery;
import www.goldpay.exchange.cpu_info.fragments.Camera_Fragment;
import www.goldpay.exchange.cpu_info.fragments.Home;

public class MainActivity extends AppCompatActivity {

    private MultiWaveHeader waveHeader;

    private MeowBottomNavigation meowBottomNavigation;
    private FrameLayout frameLayout;

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waveHeader = (MultiWaveHeader) findViewById(R.id.waveHeader);
        meowBottomNavigation = findViewById(R.id.bottom_navi);
        frameLayout = findViewById(R.id.framelayout);
        text = findViewById(R.id.text);

        text.setText("D E V I C E   I N F O");

        waveHeader.setVelocity(2);
        waveHeader.setScaleY(1);
        waveHeader.setProgress(0.9f);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(50);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.settings_icon));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.camera_icon));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.battery_icon));

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                //check condition
                switch (item.getId()){
                    case 1:
                        //when id is 1
                        //open home fragment
                        fragment = new Home();
                        break;
                    case 2:
                        fragment = new Camera_Fragment();
                        break;
                    case 3:
                        fragment = new Battery();
                        break;
                }
                //load fragment
                loadFragment(fragment);
            }
        });

        meowBottomNavigation.show(1, true);

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                if (item.getId() == 1){
                    text.setText("D E V I C E   I N F O");
                }
                else if (item.getId() == 2){
                    text.setText("C A M E R A   I N F O");

                }
                else if (item.getId() == 3){
                    text.setText("B A T T E R Y   I N F O");

                }
            }
        });

        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(MainActivity.this, "You reselected " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        //replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout,fragment)
                .commit();
    }
}