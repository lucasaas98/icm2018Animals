package ua.pt.naturespot.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.xml.sax.DTDHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ua.pt.naturespot.Model.SightingsData;
import ua.pt.naturespot.R;

public class Fragment_ModeratorOptions_Reports extends Fragment {

    private static final String TAG = "YOYO";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com");
    private ArrayList<SightingsData> mUsersEmails;
    private ArrayList<SightingsData> getData;
    private ListView listView;
    private boolean flag=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Moderator Options");

        // BEGIN: Hiding Floating Action Button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
        // End:   Hiding Floating Action Button


        View view = inflater.inflate(R.layout.fragment_moderatoractions_reports,container, false);
        listView = view.findViewById(R.id.listView1);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        createData();
    }

    public void createData(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef= db.getReference();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsersEmails = new ArrayList<>();

                DataSnapshot here = dataSnapshot.child("reports");
                for(DataSnapshot fin : here.getChildren()){
                    for (DataSnapshot nice : fin.getChildren()) {
                        final String timestamp = (String) nice.getValue();
                        for(SightingsData sd : getData) {
                            if(sd.getId_().equals(timestamp))
                                mUsersEmails.add(sd);
                        }
                    }
                }
                insertToList();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getData() {

        final DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData = new ArrayList<>();
                DataSnapshot here2 = dataSnapshot.child("Sightings");
                for (DataSnapshot m : here2.getChildren()) {
                    for (DataSnapshot f : m.child("sightings").getChildren()) {
                        String name = (String) f.child("name").getValue();
                        String data = (String) f.child("date").getValue();
                        String description = (String) f.child("description").getValue();
                        String location = (String) f.child("location").getValue();
                        String imageUrl = (String) f.child("photoURL").getValue();
                        String species = (String) f.child("species").getValue();
                        String species_fancy = (String) f.child("species_fancy").getValue();
                        String verified = (String) f.child("verified").getValue();
                        String identifier = (String) f.child("identifier").getValue();
                        String id = f.getKey();
                        SightingsData sd = new SightingsData(name, description, location, imageUrl, data, id, m.getKey(), species, species_fancy, verified, identifier);
                        for(SightingsData elem : getData)
                            if(elem.getId_().equals(sd.getId_()))
                                flag=false;
                        if (flag) getData.add(sd);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void insertToList(){
        if (getContext()!=null) {
            Fragment_ModeratorOptions_Reports.UsersAdapter adapter = new Fragment_ModeratorOptions_Reports.UsersAdapter(getContext(), new ArrayList<SightingsData>());

            List<SightingsData> nice = new ArrayList<>();
            for(SightingsData elem : mUsersEmails) {
                flag = true;
                for (SightingsData elem2 : nice) {
                    if (elem2.getId_().equals(elem.getId_())) {
                        flag = false;
                    }
                }
                if (flag) nice.add(elem);
            }

            if(nice!=null)
                for (SightingsData elem : nice) {
                    adapter.add(elem);
                }
            if(nice!=null)
                listView.setAdapter(adapter);
        }
    }

    public class UsersAdapter extends ArrayAdapter<SightingsData> {
        public UsersAdapter(Context context, ArrayList<SightingsData> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_reports, parent, false);
            }

            // Lookup view for data population
            //CircleImageView civName = convertView.findViewById(R.id.perfilFoto);
            CircleImageView civAccept = convertView.findViewById(R.id.acceptReport);
            CircleImageView civRemove = convertView.findViewById(R.id.deleteReport);

            TextView userEmail = convertView.findViewById(R.id.sightingName);

            final ImageView imageView2 = (ImageView) convertView.findViewById(R.id.perfilFoto);
            final CardView card_view = convertView.findViewById(R.id.cardView2);

            // Get the data item for this position
            final SightingsData sd = getItem(position);

            card_view.setTag(position);

            StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com").child(sd.getImage());

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


            userEmail.setText(sd.getName());
            //nSpecies.setText(sds.size()+"");
            //tvName2.setText(key);

            civRemove.setTag(position);
            civRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();

                    // BEGIN : Get Info About Current User
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            DataSnapshot here = dataSnapshot.child("reports");

                            for(DataSnapshot fin : here.getChildren()) {
                                for (DataSnapshot nice : fin.getChildren()) {
                                    if (nice.getValue().equals(sd.getId_())) {
                                        nice.getRef().removeValue();
                                    }
                                }
                            }
                            createData();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                    // END : Get Info About Current User
                }
            });


            civAccept.setTag(position);
            civAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();

                    // BEGIN : Get Info About Current User

                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            DataSnapshot here = dataSnapshot.child("reports");
                            for(DataSnapshot fin : here.getChildren()) {
                                for (DataSnapshot nice : fin.getChildren()) {
                                    if (nice.getValue().equals(sd.getId_())) {
                                        nice.getRef().removeValue();
                                        DataSnapshot l = dataSnapshot.child("Sightings").child(sd.getUuidUser()).child("sightings").child(sd.getId_());
                                        l.getRef().removeValue();
                                        /*
                                         (DataSnapshot m : l.getChildren()) {
                                            m.getRef().removeValue();
                                        }*/
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
            });

            return convertView;
        }
    }


}
