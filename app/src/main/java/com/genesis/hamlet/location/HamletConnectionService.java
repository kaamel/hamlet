package com.genesis.hamlet.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.genesis.hamlet.data.models.interests.MyInterests;
import com.genesis.hamlet.data.models.user.RemoteUser;
import com.genesis.hamlet.data.models.user.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import static com.genesis.hamlet.data.DataSource.USER_ARRIVAL_DEPARTURE_ACTION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class HamletConnectionService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private FusedLocationProviderClient mFusedLocationClient;
    private GeoQuery geoQuery;

    public static FirebaseDatabase database;
    public static GeoFire geoFire;
    private static GeoLocation location;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 5000;  /* 60 secs */
    private long FASTEST_INTERVAL = 1000; /* 5 secs */

    FirebaseUser me;

    protected com.google.android.gms.common.api.GoogleApiClient mGoogleApiClient;

    public HamletConnectionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null && intent.getStringExtra("command") != null) {
            if ("refresh".equals(intent.getStringExtra("command"))) {
                referesh();
            }
            return START_STICKY;
        }
        database = FirebaseDatabase.getInstance();
        geoFire = new GeoFire(database.getReference("/geofire/"));

        if (mGoogleApiClient.isConnected() /*&& mRequestingLocationUpdates*/) {
            //  Toast.makeText(FusedLocationWithSettingsDialog.this, "location was already on so detecting location now", Toast.LENGTH_SHORT).show();
            startLocationUpdates();
        }
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("Firebase Service", "Unbinding the service");
        return super.onUnbind(intent);
    }

    public void onLocationChanged(Location currentLocation) {
        location = new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
        updateLocation();
    }

    @Override
    public void onCreate() {
        Log.d("Firebase Service", "Creating the service");
        me = FirebaseAuth.getInstance().getCurrentUser();
        //initializeLocationManager();
        GoogleApiClient.Builder build = new GoogleApiClient.Builder(this);
        build.addConnectionCallbacks(this);
        build.addOnConnectionFailedListener(this);
        build.addApi(LocationServices.API);
        build.build();
        mGoogleApiClient = build.build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient.connect();
        } else {
            //// TODO: 10/21/17 handle error
            //googleAPI.getErrorDialog(getApplicationContext(),resultCode, RQS_GooglePlayServices);
        }
        //noinspection MissingPermission

        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(getApplicationContext());
        //noinspection MissingPermission
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });

    }

    private void updateLocation() {
        FirebaseUser fu = FirebaseAuth.getInstance().getCurrentUser();
        if (fu != null) {
            geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), location);
            if (geoQuery == null)
                setUpLocationUpdates();
            else
                geoQuery.setCenter(location);
        }
    }

    private void setUpLocationUpdates() {
        FirebaseUser fu = FirebaseAuth.getInstance().getCurrentUser();
        if (fu != null) {

            geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(location.latitude, location.longitude));
            geoQuery = geoFire.queryAtLocation(location, 20);

            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                    if (!isMe(key))
                        filterAndBroadcastByInterests(key, 1);
                }

                @Override
                public void onKeyExited(String key) {
                    System.out.println(String.format("Key %s is no longer in the search area", key));
                    if (!isMe(key))
                        filterAndBroadcastByInterests(key, -1);
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    if (!isMe(key)) {
                        System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
                        filterAndBroadcastByInterests(key, 0);
                    } else {
                        System.out.println(String.format("I moved within the search area to [%f,%f]", location.latitude, location.longitude));
                    }
                }

                @Override
                public void onGeoQueryReady() {
                    System.out.println("All initial data has been loaded and events have been fired!");
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    System.err.println("There was an error with this query: " + error);
                }
            });
        }
    }

    private void referesh() {
        synchronized (listenerMap) {
            if (listenerMap.size() > 0) {
                for (String key : listenerMap.keySet()) {
                    filterAndBroadcastByInterests(key, 1);
                }
            }
        }
    }

    protected static final Map<String, ValueEventListener> listenerMap = new HashMap<>();

    private void filterAndBroadcastByInterests(final String key, final int what) {
        FirebaseDatabase.getInstance().getReference("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                synchronized (listenerMap) {
                    User user = RemoteUser.getUser((HashMap) dataSnapshot.getValue());
                    user.setUid(key);

                    if (what == 1) {
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = RemoteUser.getUser((HashMap) dataSnapshot.getValue());
                                user.setUid(dataSnapshot.getKey());
                                if (isInteresting(user))
                                    broadcast(user, 1);
                                else {
                                    broadcast(user, -1);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        if (user.getUid() != null && listenerMap.get(user.getUid()) != null) {
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addValueEventListener(eventListener);
                        }
                        listenerMap.put(user.getUid(), eventListener);
                        if (isInteresting(user)) {
                            broadcast(user, what);
                        }
                    } else if (what == -1) {
                        if (user.getUid() != null && listenerMap.get(user.getUid()) != null) {
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).removeEventListener(listenerMap.get(user.getUid()));
                        }
                        listenerMap.remove(user.getUid());
                        broadcast(user, what);
                    }
                    if (isInteresting(user)) {
                        broadcast(user, what);
                    } else {
                        broadcast(user, what);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }

    private void broadcast(User user, int what) {
        Intent intent = new Intent();
        intent.setAction(USER_ARRIVAL_DEPARTURE_ACTION);
        intent.putExtra("user", Parcels.wrap(user));
        intent.putExtra("what",what);
        sendBroadcast(intent);
    }

    private boolean isInteresting(User user) {
        boolean[] interests = user.getInterests();
        boolean[] myInterests = MyInterests.getInstance().getInterests();
        for (int i=0; i< interests.length; i++ )
            if (interests[i] && myInterests[i])
                return true;
        return false;
    }

    protected void startLocationUpdates() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        //noinspection MissingPermission
        getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }

                    @Override
                    public void onLocationAvailability(LocationAvailability locationAvailability) {
                        super.onLocationAvailability(locationAvailability);
                    }
                },
                Looper.myLooper());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Firebase Service", "Leaving the service");
    }

    @Override
    public void onConnected(Bundle bundle) {


        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (location == null) {
            //noinspection MissingPermission
            FusedLocationProviderClient locationClient = getFusedLocationProviderClient(getApplicationContext());
            //noinspection MissingPermission
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                onLocationChanged(location);
                            }
                            startLocationUpdates();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Hamlet Location Service", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        }
        else {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("Hamlet Location Service", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    public boolean isMe(String key) {
        if (me == null)
            return false;
        return me.getUid().equals(key);
    }
}
