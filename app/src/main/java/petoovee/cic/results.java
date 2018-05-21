package petoovee.cic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.util.ArrayList;

public class results extends AppCompatActivity {
    ListView listView;
    ArrayList<String> convertedResults = new ArrayList<>();

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
        ListAdapter adapter = new AdAdapter(convertedResults, this);
        listView.setAdapter(adapter);
    }

    private void insertAds() {
        int adQuantity = (int) Math.sqrt((double) MainActivity.getResults().size());
        Toast.makeText(results.this, "Ads to show " + adQuantity, Toast.LENGTH_LONG).show();
        for (int i = MainActivity.getResults().size() / adQuantity; i < MainActivity.getResults().size(); i += MainActivity.getResults().size() / adQuantity) {
            convertedResults.add(i, "ad");
            Toast.makeText(results.this, "Showing ad at position " + i, Toast.LENGTH_LONG).show();
        }
    }


    public class AdAdapter extends BaseAdapter {

        ArrayList<String> convertedResults = new ArrayList<>();
        Context context;
        LayoutInflater layoutInflater;

        public AdAdapter(ArrayList<String> convertedResults, Context context) {
            this.convertedResults = convertedResults;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return convertedResults.size();
        }

        @Override
        public Object getItem(int position) {
            return convertedResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                if (convertedResults.get(0).equals("ad")) {
                    PublisherAdView convertAdView = new PublisherAdView(context);
                    convertAdView.setAdUnitId("ca-app-pub-7938385350213513/4460170795");
                    convertAdView.setAdSizes(AdSize.BANNER);
                    MobileAds.initialize(context, "ca-app-pub-7938385350213513~8813666583");
                    final PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                    convertAdView.loadAd(adRequest);
                    return convertAdView;
                } else {
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                    TextView text = (TextView) convertView.findViewById(android.R.id.text1);
                    text.setText(convertedResults.get(0));
                }
                convertedResults.remove(0);
            }
            return convertView;
        }
    }

}
