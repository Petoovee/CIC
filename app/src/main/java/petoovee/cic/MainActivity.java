package petoovee.cic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText initSumText, monthlyDepositText, yearlyInterestText, monthsText;
    static ArrayList<String> results = new ArrayList<>();
    private PublisherAdView ad;
    Double initSum, monthlyDeposit, yearlyInterest, monthlyInterest;
    int months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        initSumText = (EditText) findViewById(R.id.initSum);
        monthlyDepositText = (EditText) findViewById(R.id.monthlyDeposit);
        yearlyInterestText = (EditText) findViewById(R.id.yearlyInterest);
        monthsText = (EditText) findViewById(R.id.months);
        ad = (PublisherAdView) findViewById(R.id.ad);

        MobileAds.initialize(this, "ca-app-pub-7938385350213513~8813666583");
        final PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        ad.loadAd(adRequest);

        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
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

                                              // Load results
                                              Intent intent = new Intent(MainActivity.this, results.class);
                                              startActivity(intent);
                                          }
                                      }
                                  }
        );
    }

    public void setResults(ArrayList<String> Val) {
        results = Val;
    }

    public static ArrayList<String> getResults() {
        return results;
    }
}
