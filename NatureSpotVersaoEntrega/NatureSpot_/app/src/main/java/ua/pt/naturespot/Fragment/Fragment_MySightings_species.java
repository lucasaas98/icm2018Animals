package ua.pt.naturespot.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.pt.naturespot.Activities.MapsActivity;
import ua.pt.naturespot.Model.OurData;
import ua.pt.naturespot.R;

import static ua.pt.naturespot.Activities.MainActivity.EXTRA_MESSAGE;

public class Fragment_MySightings_species extends Fragment {
    private String[] StringArray = {"Cágado", "iguana"};
    private String[] StringArray2 = {"Mauremys leprosa", "Iguana iguana"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mysightings_species, null);
        // Construct the data source
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        // Create the adapter to convert the array to views
        Fragment_MySightings_species.UsersAdapter adapter = new Fragment_MySightings_species.UsersAdapter(getActivity(), arrayOfUsers);
        for (String elem : StringArray){
            adapter.add(elem);
        }
        // Attach the adapter to a ListView
        ListView listView = (ListView) view.findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        return view;
    }
    public class UsersAdapter extends ArrayAdapter<String> {
        public UsersAdapter(Context context, ArrayList<String> users) {
            super(context, 0, users);
        }



        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            String user = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_species, parent, false);
            }

            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.animalName);
            TextView tvName2 = (TextView) convertView.findViewById(R.id.animalSpecies);
            ImageView imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            CardView card_view = (CardView) convertView.findViewById(R.id.cardView2);
            card_view.setTag(position);
            card_view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    Intent nice = new Intent(getActivity().getBaseContext(), MapsActivity.class);
                    String message = "40.6405, -8.6538"; // "lat, long! lat, long! lat, long!"
                    nice.putExtra(EXTRA_MESSAGE, message);
                    startActivity(nice);
                }
            });
            tvName.setText(user);
            tvName2.setText(StringArray2[position]);
            imageView2.setImageResource(OurData.picturePath[position]);
            return convertView;
        }
    }
  }
