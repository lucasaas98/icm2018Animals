package ua.pt.naturespot.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ua.pt.naturespot.Activities.MainActivity;
import ua.pt.naturespot.Dialogs.ReportSightingDialogFragment;
import ua.pt.naturespot.R;
import ua.pt.naturespot.Model.SightingsData;

public class Fragment_Show_Sighting extends Fragment {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private SightingsData sd;
    private FragmentActivity myContext;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ImageButton reportSighting;

    //public Fragment_Show_Sighting(){}

    public SightingsData getSd() {
        return sd;
    }

    public void setSd(SightingsData sd) {
        this.sd = sd;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showsighting, null);
        TextView name = (TextView) view.findViewById(R.id.editText3);
        TextView date = (TextView) view.findViewById(R.id.editText4);
        TextView location = (TextView) view.findViewById(R.id.editText5);
        TextView description = (TextView) view.findViewById(R.id.editText7);
        TextView specie = (TextView) view.findViewById(R.id.editText8);
        final TextView species_fancy = (TextView) view.findViewById(R.id.species_fancy);
        final ImageView imgSpecies = (ImageView) view.findViewById(R.id.img_species);
        species_fancy.setText(sd.getSpecies_fancy());
        Boolean flag = false;


        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // BEGIN: ======== Nav Header Main ==========
        final String userEmail = mFirebaseUser.getEmail();
        final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("users");
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot here = dataSnapshot;
                boolean flag = false;
                for(DataSnapshot fin : here.getChildren()){
                    String email = (String) fin.child("userEmail").getValue();
                    if (email.equals(userEmail)) {
                        String userName = (String) fin.child("userName").getValue();

                        String verified = (String) fin.child("userVerifier").getValue();

                        if (verified.equals("true")) {
                            species_fancy.setVisibility(View.VISIBLE);
                            imgSpecies.setVisibility(View.VISIBLE);

                            imgSpecies.setImageResource(R.drawable.ic_check_box_black_24dp);
                            final boolean flagCopy = flag;
                            imgSpecies.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    species_fancy.setFocusable(true);
                                    species_fancy.setEnabled(true);

                                    final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference hopperRef = dbRef.child("Sightings").child(sd.getUuidUser()).child("sightings").child(sd.getId_());
                                    Map<String, Object> hopperUpdates = new HashMap<>();
                                    hopperUpdates.put("identifier", FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
                                    hopperUpdates.put("species_fancy", species_fancy.getText().toString());

                                    hopperRef.updateChildren(hopperUpdates);


                                    imgSpecies.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                                    species_fancy.setFocusable(false);
                                    species_fancy.setEnabled(false);

                                }
                            });



                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // END:   ========= Nav Header Main ==========







        final de.hdodenhof.circleimageview.CircleImageView imageView2 = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.imageView);

        StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com");
        storageRef = storageRef.child(sd.getImage());
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

        name.setText(sd.getName());
        description.setText(sd.getDescription());
        date.setText(sd.getDate());
        location.setText(sd.getLocation());
        specie.setText(sd.getSpecies());

        final String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String uuidSd = sd.getUuidUser();
        FloatingActionButton elemFab = view.findViewById(R.id.fab);

        if (uuid.equals(uuidSd)) {
            elemFab.setImageResource(R.drawable.ic_delete_black_24dp);
        } else {
            elemFab.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        }
        final FloatingActionButton fab2 = elemFab;
        elemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final Query applesQuery;
                if (uuid.equals(uuidSd)) {
                    applesQuery = ref.child("Sightings").child(uuid).child("sightings").child(sd.getId_());
                } else {
                    applesQuery = ref.child("Sightings").child(uuidSd).child("sightings").child(sd.getId_());
                }

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (uuid.equals(uuidSd)) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                            Intent itn = new Intent(getContext(), MainActivity.class);
                            startActivity(itn);
                        }else{

                            HashMap<String, String> oof = (HashMap<String, String>) dataSnapshot.child("likes").getValue();
                            Date date = new Date();
                            String timestamp = Long.toString(date.getTime());
                            if(oof == null)
                                oof = new HashMap<>();
                            oof.put(timestamp, FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Map<String,Object> report = new HashMap<>();
                            report.put("likes", oof);
                            Log.d("nice", oof.toString());
                            DatabaseReference ref2 = ref.child("Sightings").child(sd.getUuidUser()).child("sightings").child(sd.getId_());
                            ref2.updateChildren(report);

                            Toast.makeText(getContext(), "Sighting liked", Toast.LENGTH_SHORT).show();

                        }

                        // INSERT HERE CODE TO LIKE A SIGHTING


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("On Eliminate Sighting", "onCancelled", databaseError.toException());
                    }
                });
            }
        });

        reportSighting = view.findViewById(R.id.reportSighting);
        final ReportSightingDialogFragment rsdf = new ReportSightingDialogFragment();
        rsdf.setSd(sd);
        reportSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rsdf.show(getFragmentManager(), "BecomeVerifierDialogFragment");
                reportSighting.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }


}
