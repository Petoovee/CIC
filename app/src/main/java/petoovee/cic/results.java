package petoovee.cic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.util.ArrayList;

public class results extends AppCompatActivity {
    ListView listView;
    ArrayList<Object> convertedResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        listView = (ListView) findViewById(R.id.list);
        populateListView();
    }

    private void populateListView() {
        Log.d("Results: ", "populateListView: Displaying data in the ListView.");

        //create the list adapter and set the adapter to the HighScore ArrayList
        convertedResults = new ArrayList<>();
        for (int i = 0; i < MainActivity.getResults().size(); i++) {
            convertedResults.add(MainActivity.getResults().get(i));
        }

        insertAds();

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, convertedResults);
        listView.setAdapter(adapter);
    }

    private void insertAds(){
        int adQuantity = (int)Math.sqrt((double)MainActivity.getResults().size());
        Toast.makeText(results.this, "Ads to show " +adQuantity, Toast.LENGTH_LONG).show();
        for(int i = MainActivity.getResults().size()/adQuantity; i < MainActivity.getResults().size(); i+= MainActivity.getResults().size()/adQuantity){
            PublisherAdView ad = new PublisherAdView(this);
            ad.setAdUnitId("ca-app-pub-7938385350213513/4460170795");
            ad.setAdSizes(AdSize.BANNER);
            MobileAds.initialize(this, "ca-app-pub-7938385350213513~8813666583");
            final PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
            ad.loadAd(adRequest);
            convertedResults.add(i, ad);
            Toast.makeText(results.this, "Showing ad at position " +i, Toast.LENGTH_LONG).show();
        }
    }
}
