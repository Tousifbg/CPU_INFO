package www.goldpay.exchange.cpu_info;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class StartScreen extends AppCompatActivity {

    FrameLayout ad_view_container;
    private AdView adView;

    RelativeLayout btn_open,btn_rate,btn_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

     /*   MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });*/

        initViews();

        loadAdaptiveAds();

        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartScreen.this,MainActivity.class));
                finish();
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();
                String myUrl = "https://play.google.com/store/apps/details?id=" + appPackageName;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, myUrl);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
            }
        });

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingDialog();
            }
        });

    }

    private void ratingDialog() {
        final AlertDialog alert_dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null);
        builder.setView(view);
        final RatingBar rating_bar = view.findViewById(R.id.rating_bar);
        TextView btn_submit = view.findViewById(R.id.btn_submit);
        TextView tv_no = view.findViewById(R.id.tv_no);
     /*   PushDownAnim.setPushDownAnimTo(btn_submit).setScale(MODE_SCALE, 0.89f)
                .setDurationPush(DEFAULT_PUSH_DURATION).setDurationRelease(DEFAULT_RELEASE_DURATION);*/

        alert_dialog = builder.create();
        alert_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTheme;
        alert_dialog.show();

        btn_submit.setOnClickListener(v -> {
            if (rating_bar.getRating() >= 3) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")));

                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")));
                }
                alert_dialog.dismiss();
            } else if (rating_bar.getRating() <= 0) {
                Toast.makeText(StartScreen.this, "" + getString(R.string.rating_error), Toast.LENGTH_SHORT).show();
            }

        });
        tv_no.setOnClickListener(v -> alert_dialog.dismiss());
    }

    private void loadAdaptiveAds() {
        // Initialize the Mobile Ads SDK.

        ad_view_container = findViewById(R.id.ad_view_container);
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        ad_view_container.addView(adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }


    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void initViews() {
        btn_open = findViewById(R.id.btn_open);
        btn_rate = findViewById(R.id.btn_rate);
        btn_share = findViewById(R.id.btn_share);
    }
}