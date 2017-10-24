package com.genesis.hamlet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.genesis.hamlet.R;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.ui.MainActivity;
import com.genesis.hamlet.util.threading.FirebaseHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements DataSource.OnUserCallback {

    private static final int RC_SIGN_IN = 9001;
    GoogleSignIn googleSignIn;
    private LoginButton btnFacebookLogIn;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private String TAG = LoginActivity.class.getSimpleName();

    TextView tvSignInStatus;
    ProgressBar pbLogin;

    String action = null;
    User otherUser = null;
    String chatRoom = null;
    String myUid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        if (getIntent().getExtras() != null) {
            //The LoginActivity is not the entry point for the app
            String senderUid = getIntent().getStringExtra("senderUid");
            if (senderUid != null) {
                //this is a notification

                String receiverUid = getIntent().getStringExtra("receiverUid");
                String chatRoom = getIntent().getStringExtra("chatRoom");
                String title = getIntent().getStringExtra("title");
                String action = getIntent().getStringExtra("action");
                String name = getIntent().getStringExtra("name");
                String message = getIntent().getStringExtra("message");
                User user = new User();
                user.setUid(senderUid);
                user.setDisplayName(name);
                user.setIntroTitle(title);
                user.setIntroDetail(message);
                this.action = action;
                this.otherUser = user;
                this.chatRoom = chatRoom;
                this.myUid = receiverUid;

                FirebaseHelper.deleteFirebaseNode("/notifications/" +receiverUid, senderUid);
            }
        }


        mAuth = FirebaseAuth.getInstance();

        //// TODO: 10/16/17 add a revoke button if action is "revoke"
        String action = getIntent().getStringExtra("action");



        tvSignInStatus = (TextView) findViewById(R.id.tvSigninStatus);
        pbLogin = (ProgressBar) findViewById(R.id.pbLogin);
        pbLogin.setVisibility(View.GONE);

        SignInButton google = (SignInButton) findViewById(R.id.btSignInWithGoogle);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoogleSignIn();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        btnFacebookLogIn = (LoginButton) findViewById(R.id.btnFaceBookLogin);
        btnFacebookLogIn.setReadPermissions("email", "public_profile");
        btnFacebookLogIn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onSuccess(User user) {
        pbLogin.setVisibility(View.GONE);
        continueToUsersList();
    }

    @Override
    public void onFailure(Throwable throwable) {
        pbLogin.setVisibility(View.GONE);
        tvSignInStatus.setText("Sign in failed: " + throwable.getLocalizedMessage());
    }

    @Override
    public void onNetworkFailure() {
        pbLogin.setVisibility(View.GONE);
        tvSignInStatus.setText("Connection error: You need to be connected to network");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                continueToUsersList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        }else{
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onGoogleSignIn() {
        pbLogin.setVisibility(View.VISIBLE);
        if (googleSignIn == null)
            googleSignIn = new GoogleSignIn(this);
        googleSignIn.signIn(this, RC_SIGN_IN, this);
    }

    private void continueToUsersList() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        if (action != null) {
            MainActivity.setRemoteAction(action, myUid, otherUser, chatRoom);
        }
        startActivity(intent);
        finish();
    }

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            continueToUsersList();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, user.getDisplayName() + " is now signed in!");
        } else {
            Log.d(TAG, "Sign in failed. Check your account/passwrod and try again");
        }
    }


    // [END auth_with_facebook]


}
