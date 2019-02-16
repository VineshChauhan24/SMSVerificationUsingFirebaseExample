package com.rommansabbir.smsverificationusingfirebaseexample.callbacks.firebaseotpcallback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.concurrent.TimeUnit;

public class FirebaseOTPCallback extends AppCompatActivity {
    private Context context;
    private String phoneNumber;
    private FirebaseAuth firebaseAuth;
    private String verificationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseOTPCallbackInterface firebaseOTPCallbackInterface;
    private static final String TAG = "FirebaseOTPCallback";
    private String codeSentMessage = "Code Sent";
    private String successMessage = "OK";
    private String failedMessage = "ERROR";

    public FirebaseOTPCallback(Context context,FirebaseAuth firebaseAuth, String phoneNumber) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.firebaseAuth = firebaseAuth;

        /*
        Instantiate firebaseOTPCallbackInterface
         */
        firebaseOTPCallbackInterface = (FirebaseOTPCallbackInterface) context;



        handleFirebaseVerification();

        startPhoneNumberVerification(phoneNumber);
    }

    /*
    Start handling phone number verification
     */
    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    /*
    Handle firebase verification
     */
    private void handleFirebaseVerification(){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: ");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: ");
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                /*
                Assign the code to verificationCode and notify the callback interface
                 */
                verificationCode = s;
                firebaseOTPCallbackInterface.onCodeSent(codeSentMessage);

            }
        };
    }

    /*
    Verify the OTP code
     */
    public void verifyOTP(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
        checkOTPCredential(credential);
    }


    /*
    Check the OTP credential
     */
    private void checkOTPCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseOTPCallbackInterface.onVerificationSuccess(successMessage);

                        } else {
                            firebaseOTPCallbackInterface.onVerificationFailed(failedMessage);
                        }
                    }
                });
    }

    /*
    Destroy the callback after usages
     */
    public void destroyCallback(){
        firebaseOTPCallbackInterface = null;
        mCallbacks = null;
        firebaseAuth = null;
        context = null;
    }

    public interface FirebaseOTPCallbackInterface{
     void onVerificationSuccess(String msg);
     void onVerificationFailed(String msg);
     void onCodeSent(String msg);
    }
}
