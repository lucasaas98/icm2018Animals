package ua.pt.naturespot.Fragment;

import android.content.Context;
import android.content.Intent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ua.pt.naturespot.Activities.MapsActivity;
import ua.pt.naturespot.Model.SightingsData;
import ua.pt.naturespot.R;

import static ua.pt.naturespot.Activities.MainActivity.EXTRA_MESSAGE;

public class Fragment_ModeratorOptions_Requests extends Fragment {

    private static final String TAG = "YOYO";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com");
    ArrayList<String> mUsersEmails;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Moderator Options");

        // BEGIN: Hiding Floating Action Button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
        // End:   Hiding Floating Action Button


        View view = inflater.inflate(R.layout.fragment_moderatoractions_request,container, false);
        listView = view.findViewById(R.id.listView1);
        createData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        createData();
    }

    public void createData(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef= db.getReference();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUsersEmails = new ArrayList<>();

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot here = dataSnapshot.child("requests");
                Log.w("zzz",  "HEYYYYYYYYYYYYYYYYYYYYYYYYY");
                for(DataSnapshot fin : here.getChildren()){
                    String email = (String) fin.getValue();
                    mUsersEmails.add(email);
                }

                Log.w("zzz",  mUsersEmails.toString()+"");

                insertToList();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void insertToList(){
        if (getContext()!=null) {
            Fragment_ModeratorOptions_Requests.UsersAdapter adapter = new Fragment_ModeratorOptions_Requests.UsersAdapter(getContext(), new ArrayList<String>());

            if(mUsersEmails!=null)
                for (String elem : mUsersEmails){
                    adapter.add(elem);
                }
            if(mUsersEmails!=null)
                listView.setAdapter(adapter);
        }


    }

    public class UsersAdapter extends ArrayAdapter<String> {
        private List<String> keys;
        public UsersAdapter(Context context, ArrayList<String> users) {
            super(context, 0, users);
            //this.keys = Arrays.asList( mapa.keySet().toArray(new String[mapa.keySet().size()]));
            //System.out.println(this.keys);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com");

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_requests, parent, false);
            }

            // Lookup view for data population
            CircleImageView civName = convertView.findViewById(R.id.perfilFoto);
            CircleImageView civAccept = convertView.findViewById(R.id.acceptRequest);
            CircleImageView civRemove = convertView.findViewById(R.id.deleteRequest);

            TextView userEmail = convertView.findViewById(R.id.userEmail);

            //final ImageView imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            final CardView card_view = (CardView) convertView.findViewById(R.id.cardView2);

            // Get the data item for this position
            final String user = getItem(position);
            card_view.setTag(position);

            /*
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
            */

            userEmail.setText(user);
            //nSpecies.setText(sds.size()+"");
            //tvName2.setText(key);

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

                            DataSnapshot here = dataSnapshot.child("users");
                            for(DataSnapshot fin : here.getChildren()){
                                String email = (String) fin.child("userEmail").getValue();

                                if (email.equals(user)) {

                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference hopperRef = dbRef.child("users").child(fin.getKey());
                                    Map<String, Object> hopperUpdates = new HashMap<>();
                                    hopperUpdates.put("userVerifier", "true");

                                    hopperRef.updateChildren(hopperUpdates);

                                    DataSnapshot requests = dataSnapshot.child("requests");
                                    for(DataSnapshot r : requests.getChildren()){
                                        String emailRequest = (String) r.getValue();
                                        if (emailRequest.equals(user))
                                            r.getRef().removeValue();
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
                    // END : Get Info About Current User
                }
            });

            civRemove.setTag(position);
            civRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int position = (Integer) v.getTag();

                    // BEGIN : Get Info About Current User

                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            DataSnapshot requests = dataSnapshot.child("requests");
                            for(DataSnapshot r : requests.getChildren()){
                                String emailRequest = (String) r.getValue();
                                if (emailRequest.equals(user))
                                    r.getRef().removeValue();
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
