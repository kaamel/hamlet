package com.genesis.hamlet.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.DataSource;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by kaamel on 10/16/17.
 */

public class GoogleSignIn implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleSignin";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleApiClient mGoogleApiClient;
//

    GoogleSignIn(Context context) {

        if (!(context instanceof FragmentActivity)) {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentActivity)");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((
                        FragmentActivity) context /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
    }

    void firebaseAuthWithGoogle(GoogleSignInAccount acct, Activity activity) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
//        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            mGoogleApiClient.clearDefaultAccountAndReconnect();
                            updateUI(null, task.getException().getLocalizedMessage());
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    DataSource.GetUserCallback signInCallback;
    void signIn(Activity activity, int rcSignIn, DataSource.GetUserCallback getUserCallback) {
        signInCallback = getUserCallback;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, rcSignIn);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null, status.getStatusMessage());
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null, status.getStatusMessage());
                    }
                });
    }

    private void updateUI(FirebaseUser user, String message) {
//        hideProgressDialog();
        if (user != null) {
            signInCallback.onSuccess(com.genesis.hamlet.util.UserHelper.extractUser(user));
            Log.d(TAG, user.getDisplayName() + " is now signed in!");
            removeCallback();

        } else {
            signInCallback.onFailure(new Throwable(message));
            Log.d(TAG, "Sign in failed. Check your account/passwrod and try again");
            removeCallback();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        signInCallback.onNetworkFailure();
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        removeCallback();
        //Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void removeCallback() {
        signInCallback = null;
    }
}
