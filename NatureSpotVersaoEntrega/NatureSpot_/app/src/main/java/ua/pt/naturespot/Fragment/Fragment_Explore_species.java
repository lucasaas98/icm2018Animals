package ua.pt.naturespot.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ua.pt.naturespot.Activities.MapsActivity;
import ua.pt.naturespot.Model.OurData;
import ua.pt.naturespot.Model.SightingsData;
import ua.pt.naturespot.R;

import static ua.pt.naturespot.Activities.MainActivity.EXTRA_MESSAGE;

public class Fragment_Explore_species extends Fragment {
    ArrayList<SightingsData> mSightingList;
    private static final String TAG = "YOYO";
    private ListView listView;
    private HashMap<String, ArrayList<SightingsData>> map = new HashMap<>();
    private String sortOrder = "alpha";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_species, null);
        listView = (ListView) view.findViewById(R.id.listView1);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        createData();
    }

    private void setSortOrder(String sortOrder){
        this.sortOrder=sortOrder;
        createData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            createData();
        } else {
            //DO SOMETHING :)))
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.species_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sortByAlpha:
                if(this.sortOrder.equals("alpha"))
                    this.setSortOrder("reverse_alpha");
                else
                    this.setSortOrder("alpha");
                return true;

            case R.id.action_sortByNumber:
                if(this.sortOrder.equals("num"))
                    this.setSortOrder("reverse_num");
                else
                    this.setSortOrder("num");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void createData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef= db.getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mSightingList = new ArrayList<>();
                map = new HashMap<>();

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //DataSnapshot here = dataSnapshot.child("Sightings").child(uuid).child("sightings");
                DataSnapshot here = dataSnapshot.child("Sightings");
                for(DataSnapshot fin : here.getChildren()){
                    for(DataSnapshot nice : fin.child("sightings").getChildren()){
                        String name = (String) nice.child("name").getValue();
                        String data = (String) nice.child("date").getValue();
                        String description = (String) nice.child("description").getValue();
                        String location = (String) nice.child("location").getValue();
                        String imageUrl = (String) nice.child("photoURL").getValue();
                        String species = (String) nice.child("species").getValue();
                        String species_fancy = (String) nice.child("species_fancy").getValue();
                        String verified = (String) nice.child("verified").getValue();
                        String identifier = (String) nice.child("identifier").getValue();
                        String id = nice.getKey();
                        SightingsData sd = new SightingsData(name, description, location, imageUrl, data, id, here.getKey(), species, species_fancy,verified, identifier);
                        mSightingList.add(sd);
                        ArrayList<SightingsData> s;
                        if (!map.containsKey(species_fancy)) {
                            s = new ArrayList<>();
                            s.add(sd);
                        } else {
                            s = map.get(species_fancy);
                            s.add(sd);
                        }
                        map.put(species_fancy, s);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Construct the data source
        ArrayList<String> arrayOfUsers = new ArrayList<>();
        // Create the adapter to convert the array to views



        Map<String, ArrayList<SightingsData>> sortedMap=null;

        Log.d("sortOrder", sortOrder);

        //SORT BY KEY ALPHABETICALLY
        if(sortOrder.equals("alpha") || sortOrder.equals("normal"))
            sortedMap = new TreeMap<String, ArrayList<SightingsData>>(map);
        //SORT BY KEY ALPHABETICALLY

        if(sortOrder.equals("reverse_alpha"))
            sortedMap = new TreeMap<String, ArrayList<SightingsData>>(map).descendingMap();


        if(sortOrder.equals("num")) {
            //SORT BY NUMBER OF SIGHTINGS IN LIST
            // 1. Convert Map to List of Map
            List<Map.Entry<String, ArrayList<SightingsData>>> list =
                    new LinkedList<Map.Entry<String, ArrayList<SightingsData>>>(map.entrySet());

            // 2. Sort list with Collections.sort(), provide a custom Comparator
            //    Try switch the o1 o2 position for a different order
            Collections.sort(list, new Comparator<Map.Entry<String, ArrayList<SightingsData>>>() {
                public int compare(Map.Entry<String, ArrayList<SightingsData>> o1,
                                   Map.Entry<String, ArrayList<SightingsData>> o2) {
                    return (o1.getValue().size() > o2.getValue().size()) ? -1 : 1;
                }
            });

            // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
            sortedMap = new LinkedHashMap<String, ArrayList<SightingsData>>();
            for (Map.Entry<String, ArrayList<SightingsData>> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            //SORT BY NUMBER OF SIGHTINGS IN LIST
        }

        if(sortOrder.equals("reverse_num")) {
            //SORT BY NUMBER OF SIGHTINGS IN LIST
            // 1. Convert Map to List of Map
            List<Map.Entry<String, ArrayList<SightingsData>>> list =
                    new LinkedList<Map.Entry<String, ArrayList<SightingsData>>>(map.entrySet());

            // 2. Sort list with Collections.sort(), provide a custom Comparator
            //    Try switch the o1 o2 position for a different order
            Collections.sort(list, new Comparator<Map.Entry<String, ArrayList<SightingsData>>>() {
                public int compare(Map.Entry<String, ArrayList<SightingsData>> o1,
                                   Map.Entry<String, ArrayList<SightingsData>> o2) {
                    return (o2.getValue().size() > o1.getValue().size()) ? -1 : 1;
                }
            });

            // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
            sortedMap = new LinkedHashMap<String, ArrayList<SightingsData>>();
            for (Map.Entry<String, ArrayList<SightingsData>> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            //SORT BY NUMBER OF SIGHTINGS IN LIST
        }

        Fragment_Explore_species.UsersAdapter adapter = new Fragment_Explore_species.UsersAdapter(getContext(), arrayOfUsers, sortedMap);
        if(mSightingList!=null)
            for (String elem : sortedMap.keySet()){
                adapter.add(elem);
            }
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

    }



    public class UsersAdapter extends ArrayAdapter<String> {
        private Map<String, ArrayList<SightingsData>> mapa;
        private List<String> keys;
        public UsersAdapter(Context context, ArrayList<String> users, Map<String, ArrayList<SightingsData>> mapa) {
            super(context, 0, users);
            this.mapa = mapa;
            this.keys = Arrays.asList( mapa.keySet().toArray(new String[mapa.keySet().size()]));
            //System.out.println(this.keys);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = null;
            StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com");
            String key = this.keys.get(position);
            final ArrayList<SightingsData> sds = mapa.get(key);
            storageRef = storageRef.child(sds.get(0).getImage());

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_species, parent, false);
            }

            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.animalName);
            TextView nSpecies = (TextView) convertView.findViewById(R.id.numberOfSpecies);
            TextView tvName2 = (TextView) convertView.findViewById(R.id.animalSpecies);
            final ImageView imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            CardView card_view = (CardView) convertView.findViewById(R.id.cardView2);

            // Get the data item for this position
            String user = getItem(position);

            card_view.setTag(position);
            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    Intent nice = new Intent(getActivity().getBaseContext(), MapsActivity.class);
                    String message = "";
                    for(SightingsData sd : sds){
                        message+=" "+sd.getLocation()+"!";
                    }
                    Log.d("messageMap", message);
                    nice.putExtra(EXTRA_MESSAGE, message);
                    startActivity(nice);
                }
            });

            try {
                final File localFile = File.createTempFile("image", "jpg");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageView2.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FAIL: ", "FAIL");
                    }
                });
                localFile.delete();
            } catch (IOException e ) {}
            String species = sds.get(0).getSpecies();
            tvName.setText(species);
            nSpecies.setText(sds.size()+"");
            tvName2.setText(key);
            return convertView;
        }
    }
}