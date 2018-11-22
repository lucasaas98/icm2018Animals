package ua.pt.naturespot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    public EditText email;
    public EditText password;
    private Button mRegisterButton;
    private static final String TAG = "EmailPassword";

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        ImageView image = (ImageView) findViewById(R.id.logo);
        image.setImageResource(R.drawable.jhg);
        // Load a bitmap from the drawable folder
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.jhg);

        // Get height or width of screen at runtime
        int screenWidth = DeviceDimensionsHelper.getDisplayWidth(this);
        // Loads the resized Bitmap into an ImageView
        image.setImageBitmap(BitmapScaler.scaleToFitWidth(bMap, screenWidth));
        //image.setImageBitmap(BitmapScaler.scaleToFitHeight(bMap, screenWidth));

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText2);
        password = findViewById(R.id.editText);
        mRegisterButton = (Button) findViewById(R.id.button2);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())   {
                            Log.d(TAG, "createUserEmail:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            if(user!=null){
                                startActivity(new Intent(Register.this, Home.class));
                            }
                        }else {
                            Toast.makeText(Register.this, "Não foi possível criar uma nova conta", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
