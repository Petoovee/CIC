package petoovee.cic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button button, button2;
    EditText initSumText, monthlyDepositText, yearlyInterestText, monthsText;
    TextView adText, calcsLeft;
    static ArrayList<String> results = new ArrayList<>();
    private PublisherAdView ad;
    private RewardedVideoAd rewardedVideoAd;
    private InterstitialAd interstitialAd;
    int calculationsLeft = 0;
    Double initSum, monthlyDeposit, yearlyInterest, monthlyInterest;
    int months;

    int bannerReward = 1, bannerClickReward = 3, interstitialReward = 2, interstitialClickReward = 4, videoReward = 5, videoClickReward = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        initSumText = (EditText) findViewById(R.id.initSum);
        monthlyDepositText = (EditText) findViewById(R.id.monthlyDeposit);
        yearlyInterestText = (EditText) findViewById(R.id.yearlyInterest);
        monthsText = (EditText) findViewById(R.id.months);
        ad = (PublisherAdView) findViewById(R.id.ad);
        adText = (TextView) findViewById(R.id.adText);
        adText.setVisibility(View.GONE);
        calcsLeft = (TextView) findViewById(R.id.calcsLeft);

        fetchCalculationsLeft();
        updateCalculationsLeft();

        MobileAds.initialize(MainActivity.this, "ca-app-pub-7938385350213513~8813666583");

        final PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        ad.loadAd(adRequest);
        ad.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                calculationsLeft += bannerClickReward;
                updateCalculationsLeft();
            }

            @Override
            public void onAdLoaded() {
                calculationsLeft += bannerReward;
                updateCalculationsLeft();
            }
        });

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
            }

            @Override
            public void onRewardedVideoAdOpened() {
            }

            @Override
            public void onRewardedVideoStarted() {
            }

            @Override
            public void onRewardedVideoAdClosed() {
                rewardedVideoAd.loadAd("ca-app-pub-7938385350213513/9075332525",
                        new AdRequest.Builder().build());
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                calculationsLeft += videoClickReward;
                updateCalculationsLeft();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {
                calculationsLeft += videoReward;
                updateCalculationsLeft();
            }
        });
        rewardedVideoAd.loadAd("ca-app-pub-7938385350213513/9075332525",
                new AdRequest.Builder().build());

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7938385350213513/2515763222");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                calculationsLeft += interstitialReward;
                updateCalculationsLeft();
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLeftApplication() {
                calculationsLeft += interstitialClickReward;
                updateCalculationsLeft();
            }

        });
        interstitialAd.loadAd(new AdRequest.Builder().build());


        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          updateCalculationsLeft();
                                          if (calculationsLeft < 1) {
                                              if (rewardedVideoAd.isLoaded()) {
                                                  rewardedVideoAd.show();
                                              } else if (interstitialAd.isLoaded()) {
                                                  interstitialAd.show();
                                              }
                                          } else {
                                              calculate();
                                                  calculationsLeft--;
                                                  updateCalculationsLeft();

                                                  // Load results
                                                  Intent intent = new Intent(MainActivity.this, results.class);
                                                  startActivity(intent);
                                          }
                                      }
                                  }
        );

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCalculationsLeft();
                if (calculationsLeft < 5) {
                    if (rewardedVideoAd.isLoaded()) {
                        rewardedVideoAd.show();
                    } else if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    }
                } else {
                    calculate();
                    calculationsLeft -= 5;
                    updateCalculationsLeft();
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("CIC", TextUtils.join("\n\n", results));
                    clipboardManager.setPrimaryClip(clipData);
                }
            }
        });

    }

    public void calculate() {
        if (initSumText.length() != 0 && monthlyDepositText.length() != 0 && yearlyInterestText.length() != 0 && monthsText.length() != 0) {
            // Data gathering
            initSum = Double.parseDouble(initSumText.getText().toString());
            monthlyDeposit = Double.parseDouble(monthlyDepositText.getText().toString());
            yearlyInterest = Double.parseDouble(yearlyInterestText.getText().toString());
            monthlyInterest = yearlyInterest / 1200;
            months = Integer.parseInt(monthsText.getText().toString());

            // Calculation part
            Double sum = initSum;
            Double interest;
            Double totalInterest = 0.0;
            ArrayList<String> returnVal = new ArrayList<String>();
            for (int i = 1; i <= months; i++) {
                interest = sum * monthlyInterest;
                sum = sum + interest + monthlyDeposit;
                totalInterest += interest;
                returnVal.add("Month " + i + "\nSum: " + sum + "\nInterest: " + interest + "\nTotal interest: " + totalInterest);
            }
            setResults(returnVal);
        }
    }

    public synchronized void updateCalculationsLeft() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                calcsLeft.setText("Calculations remaining: " + calculationsLeft);
            }
        });
        saveCalculationsLeft();
    }

    public synchronized void fetchCalculationsLeft(){
        SharedPreferences sharedPreferences = getSharedPreferences("calculationsLeft", Context.MODE_PRIVATE);
        calculationsLeft = sharedPreferences.getInt("calculationsLeft", 0) + 0;
    }

    public synchronized void saveCalculationsLeft(){
        SharedPreferences sharedPreferences = getSharedPreferences("calculationsLeft", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("calculationsLeft", calculationsLeft);
        editor.apply();
    }

    public void setResults(ArrayList<String> Val) {
        results = Val;
    }

    public static ArrayList<String> getResults() {
        return results;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCalculationsLeft();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchCalculationsLeft();
    }
}
