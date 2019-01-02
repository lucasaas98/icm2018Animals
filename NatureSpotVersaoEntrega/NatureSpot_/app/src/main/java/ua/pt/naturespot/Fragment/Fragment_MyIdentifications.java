package ua.pt.naturespot.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ua.pt.naturespot.Adapters.MyAdapter;
import ua.pt.naturespot.Model.SightingsData;
import ua.pt.naturespot.R;

public class Fragment_MyIdentifications extends Fragment {
    RecyclerView mRecyclerView;
    private static final String TAG = "YOYO";
    List<SightingsData> mSightingList;
    SightingsData mSightingData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("My Identifications");

        // BEGIN: Hiding Floating Action Button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
        // End:   Hiding Floating Action Button


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef= db.getReference();
        final String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();



        final View view = inflater.inflate(R.layout.fragment_myidentifications,container, false);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSightingList = new ArrayList<>();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
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
                        SightingsData sd = new SightingsData(name, description, location, imageUrl, data, id, fin.getKey(), species, species_fancy, verified, identifier);
                        if(sd.getIdentifier().equals(uuid))
                            mSightingList.add(sd);
                    }
                }

                mRecyclerView = view.findViewById(R.id.listSightings);
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
                mRecyclerView.setLayoutManager(mGridLayoutManager);
                Collections.reverse(mSightingList);
                MyAdapter myAdapter = new MyAdapter(getActivity(),mSightingList );
                mRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return view;
    }
}
