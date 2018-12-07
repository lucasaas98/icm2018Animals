package ua.pt.naturespot.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import ua.pt.naturespot.Bitmap.BitmapScaler;
import ua.pt.naturespot.Bitmap.DeviceDimensionsHelper;
import ua.pt.naturespot.Model.User;
import ua.pt.naturespot.R;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private EditText email;
    private EditText password;
    private Button mRegisterButton;
    private static final String TAG = "EmailPassword";

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        ImageView image = (ImageView) findViewById(R.id.logo);
        image.setImageResource(R.drawable.lagarto);
        // Load a bitmap from the drawable folder
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.lagarto);

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

                mFirebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())   {
                            Log.d(TAG, "createUserEmail:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            if(user!=null){
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }
                        }else {
                            Toast.makeText(getBaseContext(), "Não foi possível criar uma nova conta", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
