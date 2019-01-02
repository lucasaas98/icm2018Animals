package ua.pt.naturespot.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

import ua.pt.naturespot.Bitmap.BitmapScaler;
import ua.pt.naturespot.Bitmap.DeviceDimensionsHelper;
import ua.pt.naturespot.Model.User;
import ua.pt.naturespot.R;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private Button mSignInButton;
    public EditText email;
    public EditText password;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        ImageView image = (ImageView) findViewById(R.id.logo);
        image.setImageResource(R.drawable.lagarto);
        // Load a bitmap from the drawable folder
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.lagarto);

        // Get height or width of screen at runtime
        int screenWidth = DeviceDimensionsHelper.getDisplayWidth(this);
        // Loads the resized Bitmap into an ImageView
        image.setImageBitmap(BitmapScaler.scaleToFitWidth(bMap, screenWidth));
        //image.setImageBitmap(BitmapScaler.scaleToFitHeight(bMap, screenWidth));

        mSignInButton = (Button) findViewById(R.id.button);

        mRegisterButton = (Button) findViewById(R.id.button2);

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText2);
        password = findViewById(R.id.editText);

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mFirebaseAuth.getCurrentUser()!= null){
                    User user = new User();
                    user.setUserEmail(mFirebaseAuth.getCurrentUser().getEmail());
                    //user.saveUser();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void startSignIn() {
        final String email2 = email.getText().toString();
        String pass2 = password.getText().toString();

        if(TextUtils.isEmpty(email2) || TextUtils.isEmpty(pass2)){
            Toast.makeText(LoginActivity.this, "Não pode deixar campos vazios!", Toast.LENGTH_LONG).show();
        }else{
            mFirebaseAuth.signInWithEmailAndPassword(email2, pass2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Problema de autenticação!", Toast.LENGTH_LONG).show();
                    }else{
                        Log.d("Aqui", "Entrei no else");
                        User user = new User();
                        user.setUserEmail(email2);
                        user.setUserName("Not Defined");
                        user.setUserDistrict("Not Defined");
                        user.setUserImage("Not Defined");
                        user.setUserDescription("Not Defined");
                        user.setUserVerifier("false");
                        LongOperation l = new LongOperation();
                        l.setUser(user);
                        l.execute("");

                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        private User user;
        public void setUser(User user) {this.user = user;}

        @Override
        protected String doInBackground(String... params) {

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot here = dataSnapshot;
                    boolean flag = false;
                    for(DataSnapshot fin : here.getChildren()){
                        String email = (String) fin.child("userEmail").getValue();
                        Log.d("Email Current User ", user.getUserEmail());
                        Log.d("Email ", email);
                        if (email.equals(user.getUserEmail())) {
                            flag = true;
                        }
                    }

                    if (!flag) {
                        // new user node would be /users/$userid/
                        String userId = mDatabase.push().getKey();
                        // Check for already existed userId
                        if (TextUtils.isEmpty(userId)) {
                            mDatabase.child(userId).setValue(user);
                        }else{
                            mDatabase.child(userId).setValue(user);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}


