package ua.pt.naturespot.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ua.pt.naturespot.Adapters.MyAdapter;
import ua.pt.naturespot.R;
import ua.pt.naturespot.Model.SightingsData;

public class Fragment_MySightings_sightings extends Fragment {

    RecyclerView mRecyclerView;
    private static final String TAG = "YOYO";
    List<SightingsData> mSightingList;
    SightingsData mSightingData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef= db.getReference();



        final String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        View view = inflater.inflate(R.layout.fragment_mysightings_sightings, container, false);
        final View view2 = view;
        final ImageView ivImage = view.findViewById(R.id.ivImage);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSightingList = new ArrayList<>();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot here = dataSnapshot.child("Sightings").child(uuid).child("sightings");
                for(DataSnapshot fin : here.getChildren()){
                    String name = (String) fin.child("name").getValue();
                    String data = (String) fin.child("date").getValue();
                    String description = (String) fin.child("description").getValue();
                    String location = (String) fin.child("location").getValue();
                    String imageUrl = (String) fin.child("photoURL").getValue();
                    SightingsData sd = new SightingsData(name, description, location, imageUrl, data);
                    mSightingList.add(sd);

                }

                mRecyclerView = view2.findViewById(R.id.listSightings);
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
