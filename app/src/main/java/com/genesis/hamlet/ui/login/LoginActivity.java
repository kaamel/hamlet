package com.genesis.hamlet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.di.Injection;
import com.genesis.hamlet.ui.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements DataSource.GetUserCallback {

    private static final int RC_SIGN_IN = 9001;
    GoogleSignIn googleSignIn;

    TextView tvSignInStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvSignInStatus = (TextView) findViewById(R.id.tvSigninStatus);

        SignInButton google = (SignInButton) findViewById(R.id.btSignInWithGoogle);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoogleSignIn();
            }
        });
        final DataRepository dataRepository = Injection.provideDataRepository();
    }

    @Override
    public void onSuccess(User user) {
        continueToUsersList();
    }

    @Override
    public void onFailure(Throwable throwable) {
        tvSignInStatus.setText("Sign in failed: " + throwable.getLocalizedMessage());
    }

    @Override
    public void onNetworkFailure() {
        tvSignInStatus.setText("Connection error: You need to be connected to network");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            continueToUsersList();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                googleSignIn.firebaseAuthWithGoogle(account, this);
            } else {
                // Google Sign In failed
                googleSignIn.removeCallback();
            }
        }
    }

    public void onGoogleSignIn() {
        if (googleSignIn == null)
            googleSignIn = new GoogleSignIn(this);
        googleSignIn.signIn(this, RC_SIGN_IN, this);
    }

    private void continueToUsersList() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
