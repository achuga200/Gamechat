package com.example.gamechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private static final int SIGN_IN_REQUEST_CODE = 0;

    //initialize firebase analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Inside the onCreate() method of MainActivity, check if the user is already signed in by checking if the current FirebaseUser object is not null. If it is null, you must create and configure an Intent object that opens a sign-in activity. To do so, use the SignInIntentBuilder class. Once the intent is ready, you must launch the sign-in activity using the startActivityForResult() method.

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            //start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(),
                    SIGN_IN_REQUEST_CODE

            );
        } else {
            //user is already signed in . Therefore. display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome" + FirebaseAuth.getInstance()
            .getCurrentUser()
            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            //Load chat Room contents. displayChatMessages is a method
            displayChatMessages();
        }
    }

    private void displayChatMessages() {
        //Once the user has signed in, MainActivity will receive a result in the form of an Intent. To handle it, you must override the onActivityResult() method.
        //If the result's code is RESULT_OK, it means the user has signed in successfully. If so, you must call the displayChatMessages() method again. Otherwise, call finish() to close the app.


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE){
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "successfully signed in. welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                displayChatMessages();
            }else {
                Toast.makeText(this,"We couldn't sign you in. Please try again later.",
                Toast.LENGTH_SHORT)
                .show();

                //close the app
                finish();
            }
            }
        }

        //To instantiate the menu resource inside MainActivity, override the onCreateOptionsMenu() method and call the inflate() method of the MenuInflater object

    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    //Next, override the onOptionsItemSelected() method to handle click events on the menu item. Inside the method, you can call the signOut() method of the AuthUI class to sign the user out. Because the sign-out operation is executed asynchronously, we'll also add an OnCompleteListener to it.

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>(){

                        public void onComplete(@NonNull Task<Void> task){
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();
                            //close activity
                            finish();
                        }
                    });
        }
        return true;
    }
}

