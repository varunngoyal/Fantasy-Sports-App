package com.vaavdevelopers.fantasysportsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_APP = "AllPrefs";
    private static final String TAG = "abc";
    int RC_SIGN_IN = 0;
    SignInButton signIn;
    GoogleSignInClient mGoogleSignInClient;
    ProgressBar progressBar;
    EditText editMobile;
    Button requestOtp;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = findViewById(R.id.sign_in_button);
        requestOtp = findViewById(R.id.request_otp);
        progressBar = findViewById(R.id.progressBar);
        editMobile = findViewById(R.id.edit_mobile);
        db = FirebaseFirestore.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }

            }
        });

        requestOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = editMobile.getText().toString().trim();

                if(mobileNo.isEmpty() || mobileNo.length() < 10) {
                    editMobile.setError("10 digit Mobile number is required!");
                    editMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, OtpActivity.class);
                intent.putExtra("mobileNo", "+91"+mobileNo);
                startActivity(intent);
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            progressBar.setVisibility(View.VISIBLE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            //check if account exists in firestore
            db.collection("users")
            .whereEqualTo("email", account.getEmail())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        if(!task.getResult().isEmpty()) {
                            Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
                            DocumentSnapshot query = task.getResult().getDocuments().get(0);
                            String username = (String)query.get("username");
                            Toast.makeText(MainActivity.this, "going in to matches activity with username : "+username,
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_APP, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username);
                            editor.commit();

                            startActivity(intent);
                            finish();
                        } else {
                            // Signed in successfully, show authenticated UI.
                            Intent intent = new Intent(MainActivity.this, UsernameActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Error in network connection",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });



        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Unable to sign in. Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_APP, Context.MODE_PRIVATE);

        String value = sharedPreferences.getString("username", "0");
        if(!value.equals("0")) {
            Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
            intent.putExtra("username", value);
            Toast.makeText(this, "Welcome back, "+value+"!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();

        }
    }
}
