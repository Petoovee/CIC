package petoovee.cucalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class results extends AppCompatActivity {
    ListView listView;

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
        ArrayList<String> convertedResults = new ArrayList<>();
        for (int i = 0; i < MainActivity.getResults().size(); i++) {
            convertedResults.add(MainActivity.getResults().get(i));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, convertedResults);
        listView.setAdapter(adapter);
    }
}
