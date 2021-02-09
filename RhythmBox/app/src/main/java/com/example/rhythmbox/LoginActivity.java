package com.example.rhythmbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        progressDialog = new ProgressDialog(this);
        signInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            Log.d("TAG", "onCreate: already signed in");
            Intent homePageIntent = new Intent(this,HomePage.class);
            startActivity(homePageIntent);
            //Log.d("TAG", "onActivityResult: logged in with"+GoogleSignIn.getSignedInAccountFromIntent(mGoogleSignInClient.getSignInIntent()).getResult().getEmail());
        }else
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Signing Into your Google Account!");
                progressDialog.show();
                signIn();
            }
        });
    }

    public void signIn(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,100);
          }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.dismiss();
        Log.d("TAG", "onActivityResult: resultcode "+resultCode);
        if(resultCode == 100){
            Intent homePageIntent = new Intent(this,HomePage.class);
            startActivity(homePageIntent);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //Log.d("TAG", "onActivityResult: logged in with"+task.getResult().getEmail());
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d("TAG", "handleSignInResult: signed in with ");
            progressDialog.dismiss();
            //updateUI(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Log.d("TAG", "handleSignInResult: ");
        }
    }
}