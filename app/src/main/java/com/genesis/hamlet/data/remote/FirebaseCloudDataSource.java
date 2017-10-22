package com.genesis.hamlet.data.remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.interests.Interests;
import com.genesis.hamlet.data.models.user.RemoteUser;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.location.HamletConnectionService;
import com.genesis.hamlet.util.UserHelper;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaamel on 10/13/17.
 */

public class FirebaseCloudDataSource extends DataSource {
    public static FirebaseCloudDataSource firebaseCloudDataSource;
    public static FirebaseDatabase database;
    public static DatabaseReference usersRef;
    public static DatabaseReference messagesRef;
    BroadcastReceiver br;

    public FirebaseCloudDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        super(mainUiThread, threadExecutor);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("/users/");
        messagesRef = database.getReference("messages");
        //geoFire = new GeoFire(database.getReference("/geofire/"));
    }

    @Override
    public void disconnect(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), HamletConnectionService.class);
        context.startService(intent);
        context.stopService(intent);
    }

    public static synchronized FirebaseCloudDataSource getInstance(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        if (firebaseCloudDataSource == null) {
            firebaseCloudDataSource = new FirebaseCloudDataSource(mainUiThread, threadExecutor);
        }
        return firebaseCloudDataSource;
    }

    @Override
    public User getLoggedInUser() {
        return UserHelper.extractUser(FirebaseAuth.getInstance().getCurrentUser());
    }

    //protected static final Map<String, ValueEventListener> listenerMap = new HashMap<>();

    @Override
    public void goOnline(Context context, final OnUsersCallback onUsersCallback, long maxJoinTime) {
        //// TODO: 10/15/17 Set up the firebase, call back the initial list of users and call back as users join/leave
        //// TODO: 10/15/17 I also think we need to add the filter to the method

        initializeNewConnection(context);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                User user = Parcels.unwrap(intent.getParcelableExtra("user"));
                int what = intent.getIntExtra("what", 0);
                switch (what) {
                    case 1:
                        /*
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = RemoteUser.getUser((HashMap) dataSnapshot.getValue());
                                user.setUid(dataSnapshot.getKey());
                                onUsersCallback.onUserUpdate(user);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        usersRef.child(user.getUid()).addValueEventListener(eventListener);
                        listenerMap.put(user.getUid(), eventListener);
                        */
                        onUsersCallback.onNewUserJoined(user);
                        break;
                    case -1:
                        onUsersCallback.onUserLeft(user);
                        /*
                        usersRef.child(user.getUid()).removeEventListener(listenerMap.get(user.getUid()));
                        listenerMap.remove(user.getUid());
                        */
                        break;
                    default:
                        onUsersCallback.onUserUpdate(user);
                }
            }
        };


        IntentFilter filter = new IntentFilter(USER_ARRIVAL_DEPARTURE_ACTION);
        filter.addAction(USER_ARRIVAL_DEPARTURE_ACTION);
        context.registerReceiver(br, filter);

/*
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (s != null) {
                    User user = RemoteUser.getUser((HashMap) dataSnapshot.getValue());
                    onUsersCallback.onNewUserJoined(user);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (s != null) {
                    User user = RemoteUser.getUser((HashMap) dataSnapshot.getValue());
                    onUsersCallback.onUserUpdate(user);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                dataSnapshot.exists();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                String b = s==null?"":s.trim();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    HashMap map = (HashMap) dataSnapshot.getValue();
                    for (Object obj: map.keySet()) {
                        String key = (String) obj;
                        if (key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            continue;
                        }
                        User user = RemoteUser.getUser((HashMap) map.get(obj));
                        onUsersCallback.onNewUserJoined(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });*/
    }

    @Override
    public void updateUser() {
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        RemoteUser remoteUser = new RemoteUser(Interests.getInstance(), 0, 0, System.currentTimeMillis());
        Map<String, Object> postValues = remoteUser.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + key, postValues);

        //geoFire.setLocation(key, new GeoLocation(lat, lon));
        database.getReference().updateChildren(childUpdates);
    }

    private void initializeNewConnection(final Context context) {
        updateUser();

        Intent intent = new Intent(context.getApplicationContext(), HamletConnectionService.class);
        context.startService(intent);
    }

    //@Override
    public void storeUsers(List<User> users) {
        //// TODO: 10/15/17 I think we should never use this for firebase but ...
    }

    @Override
    public void getMMessages(Context context, OnMMessagesCallback onMMessagesCallback, long maxId) {
        //// TODO: 10/13/17
    }
}
