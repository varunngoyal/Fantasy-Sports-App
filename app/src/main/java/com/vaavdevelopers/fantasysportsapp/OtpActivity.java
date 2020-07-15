package com.vaavdevelopers.fantasysportsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    private static final String PREFERENCES_APP = "AllPrefs";
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView textProgressBar;
    private EditText editOtp;
    private Button buttonVerifyOtp;
    String phoneNumber;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        textProgressBar = findViewById(R.id.text_progressBar);
        editOtp = findViewById(R.id.edit_OTP);
        buttonVerifyOtp = findViewById(R.id.verify_otp);
        db = FirebaseFirestore.getInstance();

        phoneNumber = getIntent().getStringExtra("mobileNo");
        sendVerificationCode(phoneNumber);

        buttonVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editOtp.getText().toString().trim();

                if(code.isEmpty() || code.length() < 6) {
                    editOtp.setError("6-digit OTP is required");
                    editOtp.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });
    }

    public void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithAuthCredential(credential);
    }

    private void signInWithAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            checkForExistingUser();


                        } else {
                            Toast.makeText(OtpActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            /*If unable to sign in, hide the progress bar and the text description*/
                            progressBar.setVisibility(View.INVISIBLE);
                            textProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void checkForExistingUser() {
        db.collection("users")
                .whereEqualTo("mobileNo", phoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            Toast.makeText(OtpActivity.this, "OTP verification successful!",
                                    Toast.LENGTH_SHORT).show();

                            if(task.getResult().isEmpty()) {
                                Intent intent = new Intent(OtpActivity.this, UsernameActivity.class);
                                intent.putExtra("mobileNo", phoneNumber);
                                startActivity(intent);
                                finish();

                            } else {
                                DocumentSnapshot snapshot = task.getResult().getDocuments().get(0);
                                String username = snapshot.getString("username");
                                Intent intent = new Intent(OtpActivity.this, MatchesActivity.class);
                                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_APP, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                editor.commit();
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            Toast.makeText(OtpActivity.this, "Error in network connection",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallback);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null) {
                progressBar.setVisibility(View.VISIBLE);
                textProgressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            textProgressBar.setVisibility(View.INVISIBLE);
        }
    };


}
