package com.rommansabbir.smsverificationusingfirebaseexample;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.rommansabbir.smsverificationusingfirebaseexample.callbacks.firebaseotpcallback.FirebaseOTPCallback;

    /*
    Don't forgot to implement FirebaseOTPCallbackInterface
     */
public class MainActivity extends AppCompatActivity implements FirebaseOTPCallback.FirebaseOTPCallbackInterface {
    private FirebaseAuth auth;
    private EditText otpTextBox;
    private EditText phoneNumberTextBox;
    private Button verifyButton;
    private Button sendCodeButton;
    private FirebaseOTPCallback firebaseOTPCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Instantiate FirebaseAuth
        auth = FirebaseAuth.getInstance();

        /*
        View item references
         */
        otpTextBox = findViewById(R.id.otpTextBox);
        verifyButton = findViewById(R.id.verifyButton);
        phoneNumberTextBox = findViewById(R.id.phoneNumberTextBox);
        sendCodeButton = findViewById(R.id.sendCodeButton);

        /*
        Handle send code button action
         */
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberTextBox.getText().toString();

                //Instantiate FirebaseOTPCallback
                firebaseOTPCallback = new FirebaseOTPCallback(MainActivity.this, auth, phoneNumber);
            }
        });

        /*
        Handle verify button action
         */
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Convert your code into string
                 */
                String code =otpTextBox.getText().toString();

                /*
                Call verifyOTP() to verify your code
                 */
                firebaseOTPCallback.verifyOTP(code);
            }
        });
    }


    @Override
    public void onVerificationSuccess(String msg) {
        /*
        If the verification code is valid, then
         */

        //TODO implement your login here
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVerificationFailed(String msg) {
        /*
        If the verification is failed, then
         */
        //TODO implement your login here
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCodeSent(String msg) {
        /*
        If on code sent successful, then
         */
        //TODO implement your logic here
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        Call destroyCallback() after verification done
         */
        firebaseOTPCallback.destroyCallback();
    }
}
