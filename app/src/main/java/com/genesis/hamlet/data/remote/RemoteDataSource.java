package com.genesis.hamlet.data.remote;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.interests.Interests;
import com.genesis.hamlet.data.models.interests.MyInterests;
import com.genesis.hamlet.data.models.mmessage.MMessage;
import com.genesis.hamlet.data.models.user.RemoteUser;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.location.HamletConnectionService;
import com.genesis.hamlet.notifiations.NotificationMessage;
import com.genesis.hamlet.util.UserHelper;
import com.genesis.hamlet.util.threading.FirebaseHelper;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The class for fetching data from Firebase
 */

/**
 * Created by kaamel on 10/13/17.
 */

public class RemoteDataSource extends DataSource {
    public static RemoteDataSource firebaseCloudDataSource;
    public static FirebaseDatabase database;
    public static DatabaseReference usersRef;
    public static DatabaseReference notificationsRef;
    public static DatabaseReference messagesRef;
    BroadcastReceiver br;
    private Intent serviceIntent;

    private RemoteDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        super(mainUiThread, threadExecutor);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("/users/");
        messagesRef = database.getReference("/messages/");
        notificationsRef = database.getReference("notifications");

        FirebaseMessaging.getInstance().subscribeToTopic("notifications");
    }

    private static boolean connected = false;

    @Override
    public void disconnect(Context context) {
        if (!connected)
            return;
        connected = false;
        //context.startService(intent);
        //serviceIntent.putExtra("caommand", "stop");
        context.getApplicationContext().startService(serviceIntent);
        context.getApplicationContext().unregisterReceiver(br);
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    public static synchronized RemoteDataSource getInstance(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        if (firebaseCloudDataSource == null) {
            firebaseCloudDataSource = new RemoteDataSource(mainUiThread, threadExecutor);
        }
        return firebaseCloudDataSource;
    }

    @Override
    public User getLoggedInUser() {
        return UserHelper.extractUser(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    public void findUserById(final String uId, final OnUserCallback userCallback) {

        //// TODO: 10/28/17 return here later
        usersRef.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null)
                    return;

                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                while (iter.hasNext()) {
                    DataSnapshot next = iter.next();
                    if (next.getKey().equals("interests")) {
                        User user = new User();
                        Interests interests = next.getValue(Interests.class);
                        if (interests != null) {
                            user.setUid(uId);
                            user.setDisplayName(interests.getNickName());
                            user.setIntroTitle(interests.getIntroTitle());
                            user.setIntroDetail(interests.getIntroDetail());
                            user.setPhotoUrl(interests.getProfileUrl());

                            userCallback.onSuccess(user);
                        }
                        userCallback.onSuccess(null);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                userCallback.onFailure(databaseError.toException());
            }
        });
        //return null;
    }

    //protected static final Map<String, ValueEventListener> listenerMap = new HashMap<>();

    @Override
    public void goOnline(Context context, final OnUsersCallback onUsersCallback, long maxJoinTime) {

        if (connected)
            return;
        initializeNewConnection(context.getApplicationContext());

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
                        usersRef.child(user.getSenderUid()).addValueEventListener(eventListener);
                        listenerMap.put(user.getSenderUid(), eventListener);
                        */
                        onUsersCallback.onNewUserJoined(user);
                        break;
                    case -1:
                        onUsersCallback.onUserLeft(user);
                        /*
                        usersRef.child(user.getSenderUid()).removeEventListener(listenerMap.get(user.getSenderUid()));
                        listenerMap.remove(user.getSenderUid());
                        */
                        break;
                    default:
                        onUsersCallback.onUserUpdate(user);
                }
            }
        };


        IntentFilter filter = new IntentFilter(USER_ARRIVAL_DEPARTURE_ACTION);
        filter.addAction(USER_ARRIVAL_DEPARTURE_ACTION);
        context.getApplicationContext().registerReceiver(br, filter);

    connected = true;
    }

    @Override
    public void refereshUsers(Context context) {
        //Intent intent = new Intent(context.getApplicationContext(), HamletConnectionService.class);
        serviceIntent.putExtra("command", "refresh");
        context.getApplicationContext().startService(serviceIntent);
    }

    @Override
    public void sendNotification(User user, String chatRoom, String action, String title, String message) {
        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiverUid = user.getUid();
        sendNotification(senderUid, receiverUid, chatRoom, action, title, message);
    }

    //// TODO: 10/28/17 need to add the sender profileUrl as well
    @Override
    public void sendNotification(String senduerUid, String receiverUid, String chatRoom, String action, String title, String message) {
        if (chatRoom == null) {
            DatabaseReference ref = notificationsRef.push();
            chatRoom = ref.getKey();
        }
        //DatabaseReference ref = notificationsRef.child(chatRoom).child(senduerUid);


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notifications/" + receiverUid + "/" + senduerUid,
                new NotificationMessage(senduerUid, receiverUid, chatRoom, MyInterests.getInstance().getNickName(), action, title, message));
        database.getReference().updateChildren(childUpdates);
    }

    @Override
    public void updateUser() {
        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        RemoteUser remoteUser = new RemoteUser(MyInterests.getInstance(), 0, 0, System.currentTimeMillis());
        Map<String, Object> postValues = remoteUser.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + key, postValues);

        //geoFire.setLocation(key, new GeoLocation(lat, lon));
        database.getReference().updateChildren(childUpdates);
    }

    @Override
    public void connectChatroom(final OnMMessagesCallback onMMessageCallback, final String chatroom, int page) {
        //FirebaseHelper.deleteFirebaseNode("/messages/", chatroom);
        Query messagesQuery = messagesRef.child(chatroom)
                .limitToLast(1);
        messagesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null)
                    return;

                List<MMessage> nmsgs;
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                MMessage msg;
                nmsgs = new ArrayList<>();
                while (iter.hasNext()) {
                    msg = iter.next().getValue(MMessage.class);
                    nmsgs.add(msg);
                    //onMMessageCallback.onSuccess(msg, chatroom, msg.getSenderUid());
                }

                onMMessageCallback.onSuccess(nmsgs.get(0), chatroom, nmsgs.get(0).getSenderUid());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onMMessageCallback.onFailure(databaseError.toException());
            }
        });
    }

    @Override
    public void closeChatroom(String chatroom) {
        FirebaseHelper.deleteFirebaseNode("/messages/", chatroom);
    }

    @Override
    public void sendMMessage(MMessage message, User user, String chatRoom) {
        message.setSenderUid(MyInterests.getInstance().getMyUid());
        message.setDisplayName(MyInterests.getInstance().getNickName());
        message.setProfileUrl(MyInterests.getInstance().getProfileUrl());
        DatabaseReference ref = messagesRef.child(chatRoom).push();
        message.setId(ref.getKey());
        message.setCreateTime(String.valueOf(System.currentTimeMillis()));
        messagesRef.child(chatRoom).push().setValue(message);
    }

    private void initializeNewConnection(final Context context) {
        updateUser();
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());
        serviceIntent = new Intent(context.getApplicationContext(), HamletConnectionService.class);
        //serviceIntent.putExtra("command", "start");
        context.getApplicationContext().startService(serviceIntent);
    }
}
