package ua.pt.naturespot.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ua.pt.naturespot.Dialogs.BecomeVerifierDialogFragment;
import ua.pt.naturespot.R;

public class Fragment_Profile extends Fragment {

    private static final String TAG = "YOYO";
    private EditText etName;
    private EditText etEmail;
    private EditText etDistrict;
    private EditText etDescription;

    private String name;
    private String email;
    private String district;
    private String description;

    private CircleImageView civConfirm;
    private CircleImageView civCancel;
    private String idUser;
    private TextView becomeVerifier;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Edit Profile");

        // BEGIN: Hiding Floating Action Button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
        // End:   Hiding Floating Action Button


        View view = inflater.inflate(R.layout.fragment_profile,container, false);
        etName = view.findViewById(R.id.nameUser);
        etEmail = view.findViewById(R.id.userEmail);
        etDistrict = view.findViewById(R.id.userDistrict);
        etDescription = view.findViewById(R.id.userDescription);
        civCancel = view.findViewById(R.id.cancel);
        civConfirm = view.findViewById(R.id.confirm);
        becomeVerifier = view.findViewById(R.id.becomeVerifier);


        final BecomeVerifierDialogFragment bvd = new BecomeVerifierDialogFragment();
        becomeVerifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bvd.show(getFragmentManager(), "BecomeVerifierDialogFragment");

            }
        });


        // BEGIN : Get Info About Current User
        final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot here = dataSnapshot.child("users");
                for(DataSnapshot fin : here.getChildren()){
                    String email = (String) fin.child("userEmail").getValue();

                    if (email.equals(userEmail)) {
                        etName.setText((String) fin.child("userName").getValue());
                        name = etName.getText().toString();
                        etEmail.setText((String) fin.child("userEmail").getValue());
                        email = etEmail.getText().toString();
                        etDistrict.setText((String) fin.child("userDistrict").getValue());
                        district = etDistrict.getText().toString();
                        etDescription.setText((String) fin.child("userDescription").getValue());
                        description = etDescription.getText().toString();
                        idUser = fin.getKey();
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


        final FloatingActionButton elemFab = view.findViewById(R.id.fab);
        elemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etName.setEnabled(true);
                etName.setFocusable(true);
                etDistrict.setEnabled(true);
                etDescription.setEnabled(true);

                elemFab.setVisibility(View.INVISIBLE);
                civCancel.setVisibility(View.VISIBLE);
                civConfirm.setVisibility(View.VISIBLE);
                becomeVerifier.setVisibility(View.INVISIBLE);

            }
        });

        civConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference hopperRef = dbRef.child("users").child(idUser);
                Map<String, Object> hopperUpdates = new HashMap<>();
                hopperUpdates.put("userName", etName.getText().toString());
                hopperUpdates.put("userEmail", etEmail.getText().toString());
                hopperUpdates.put("userDistrict", etDistrict.getText().toString());
                hopperUpdates.put("userDescription", etDescription.getText().toString());
                hopperUpdates.put("userImage", "Not Defined");

                hopperRef.updateChildren(hopperUpdates);

                etName.setEnabled(false);
                etName.setFocusable(false);
                etDistrict.setEnabled(false);
                etDistrict.setFocusable(false);
                etDescription.setEnabled(false);
                etDescription.setFocusable(false);

                elemFab.setVisibility(View.VISIBLE);
                civCancel.setVisibility(View.INVISIBLE);
                civConfirm.setVisibility(View.INVISIBLE);
                becomeVerifier.setVisibility(View.VISIBLE);
            }
        });

        civCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText(name);
                etDistrict.setText(district);
                etDescription.setText(description);

                etName.setEnabled(false);
                etName.setFocusable(false);
                etDistrict.setEnabled(false);
                etDistrict.setFocusable(false);
                etDescription.setEnabled(false);
                etDescription.setFocusable(false);

                elemFab.setVisibility(View.VISIBLE);
                civCancel.setVisibility(View.INVISIBLE);
                civConfirm.setVisibility(View.INVISIBLE);
                becomeVerifier.setVisibility(View.VISIBLE);

            }
        });

        return view;
    }
}
