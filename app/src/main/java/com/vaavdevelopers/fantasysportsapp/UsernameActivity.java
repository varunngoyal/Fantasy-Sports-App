package com.vaavdevelopers.fantasysportsapp;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.vaavdevelopers.fantasysportsapp.models.UserProfile;

import java.util.Set;

public class UsernameActivity extends AppCompatActivity {

    private static final String PREFERENCES_APP = "AllPrefs";
    Button signUp;
    EditText username;
    TextView username_available;
    FirebaseFirestore db;
    ProgressBar progressBar;
    private static final String TAG = "tag";
    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        username = findViewById(R.id.username);
        username_available = findViewById(R.id.txt_uname_available);
        signUp = findViewById(R.id.signUp);
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null) {
            username.setText(account.getDisplayName().trim());
        }

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                username_available.setVisibility(View.INVISIBLE);

                if(username.length() < 4) {
                    username.setError("Min 4 characters required");
                } else {
                    //query firestore
                    isUsernameAvailable();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.length() > 4) {
                    if (!isNetworkAvailable(UsernameActivity.this)) {
                        Toast.makeText(UsernameActivity.this,
                                "There is a problem in network connectivity",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        db.collection("users")
                        .whereEqualTo("username", username.getText().toString().trim())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.INVISIBLE);

                                    if (task.getResult().isEmpty()) {
                                        username_available.setVisibility(View.VISIBLE);
                                        flag = true;
                                        Toast.makeText(UsernameActivity.this,
                                                "Username available",
                                                Toast.LENGTH_SHORT).show();

                                        Toast.makeText(UsernameActivity.this,
                                                "Username accepted, going into main activity",
                                                Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(UsernameActivity.this,
                                                MatchesActivity.class);

                                        //save profile info of user to the database
                                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(UsernameActivity.this);
                                        UserProfile user;
                                        if(account != null) {
                                            user = new UserProfile(username.getText().toString().trim(),
                                                    null, account.getEmail(), 0);
                                        } else {
                                            user = new UserProfile(username.getText().toString().trim(),
                                                    getIntent().getStringExtra("mobileNo"), null, 0);
                                        }
                                        saveUserToFirebase(user);

                                        //save for offline purposes
                                        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_APP, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", username.getText().toString().trim());
                                        editor.commit();
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        username.setError("Username not available!");
                                        flag = false;
                                        username_available.setVisibility(View.INVISIBLE);
                                        Toast.makeText(UsernameActivity.this,
                                                "Username not available",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                    Toast.makeText(UsernameActivity.this,
                                            "Error " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            }


        });

    }

    public void saveUserToFirebase(UserProfile user) {
        db.collection("users").document(user.getUsername())
        .set(user)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot added!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public boolean isUsernameAvailable() {
        flag = false;

        if(!isNetworkAvailable(UsernameActivity.this)) {
            Toast.makeText(this, "There is a problem in network connectivity",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        progressBar.setVisibility(View.VISIBLE);

        db.collection("users")
        .whereEqualTo("username", username.getText().toString().trim())
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);

                    if (task.getResult().isEmpty()) {
                        username_available.setVisibility(View.VISIBLE);
                        flag = true;
                        Toast.makeText(UsernameActivity.this, "Username available",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        username.setError("Username not available!");
                        flag = false;
                        username_available.setVisibility(View.INVISIBLE);
                        Toast.makeText(UsernameActivity.this, "Username not available",
                                Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                    Toast.makeText(UsernameActivity.this,
                            "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(this,"Flag returned: "+flag, Toast.LENGTH_SHORT).show();
        return flag;
    }

    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }
        return false;
    }
}


