package com.reactnative.phoneverfication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class PhoneVerificationModule extends ReactContextBaseJavaModule {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String TAG = "PhoneVerification";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    public PhoneVerificationModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "PhoneVerification";
    }

    @ReactMethod
    public void sendVerificationCode(String countryCode, String phoneNumber){
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted: "+credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, "onVerificationFailed: "+e );
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent: "+verificationId+ ""+token);

                Toast.makeText(getReactApplicationContext(), " Code Send successfully", Toast.LENGTH_SHORT).show();


                mVerificationId = verificationId;
                mResendToken = token;

            }
        };

        startPhoneNumberVerification(countryCode + phoneNumber + "");



    }


    @ReactMethod
    public void verify(String code) {
        verifyPhoneNumberWithCode(mVerificationId,code);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(getCurrentActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        final FirebaseUser user = task.getResult().getUser();

                        Toast.makeText(getReactApplicationContext(), user.getPhoneNumber() + " verified successfully", Toast.LENGTH_LONG).show();


                    } else {
                        // Sign in failed, display a message and update the UI

                        Toast.makeText(getReactApplicationContext(), "Verification Failed", Toast.LENGTH_LONG).show();

                    }
                }
            });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getCurrentActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


}
