package com.genesis.hamlet.data.remote;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.parceler.Parcels;

import java.io.File;
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
    public static FirebaseStorage storage;

    public static DatabaseReference usersRef;
    public static DatabaseReference notificationsRef;
    public static DatabaseReference messagesRef;
    public static StorageReference storageRef;
    BroadcastReceiver br;

    private RemoteDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        super(mainUiThread, threadExecutor);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        usersRef = database.getReference("/users/");
        messagesRef = database.getReference("/messages/");
        notificationsRef = database.getReference("notifications");
        storageRef = storage.getReference();

        FirebaseMessaging.getInstance().subscribeToTopic("notifications");
    }

    private static boolean connected = false;

    @Override
    public void disconnect(Context context) {
        Log.d("RemoteDataSource", "disconnecting");
        if (!connected)
            return;
        connected = false;
        Intent intent = new Intent(context.getApplicationContext(), HamletConnectionService.class);

        context.getApplicationContext().stopService(intent);

        /*
        intent.putExtra("command","remove_stop");
        context.getApplicationContext().startService(intent);
        */
        usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
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

    private static final OnUsersCallback[] onUsersCallback = new OnUsersCallback[1];

    @Override
    public void goOnline(Context context, final OnUsersCallback usc, long maxJoinTime) {

        onUsersCallback[0] = usc;
        if (connected) {
            return;
        }
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
                        onUsersCallback[0].onNewUserJoined(user);
                        break;
                    case -1:
                        onUsersCallback[0].onUserLeft(user);
                        /*
                        usersRef.child(user.getSenderUid()).removeEventListener(listenerMap.get(user.getSenderUid()));
                        listenerMap.remove(user.getSenderUid());
                        */
                        break;
                    default:
                        onUsersCallback[0].onUserUpdate(user);
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
        updateUser();
        Intent intent = new Intent(context.getApplicationContext(), HamletConnectionService.class);
        intent.putExtra("command", "refresh");
        context.getApplicationContext().startService(intent);
    }

    @Override
    public String sendNotification(User user, String chatRoom, String action, String title, String message) {
        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiverUid = user.getUid();
        return sendNotification(senderUid, receiverUid, chatRoom, action, title, message);
    }

    //// TODO: 10/28/17 need to add the sender profileUrl as well
    @Override
    public String sendNotification(String senduerUid, String receiverUid, String chatRoom, String action, String title, String message) {
        if (chatRoom == null) {
            DatabaseReference ref = notificationsRef.push();
            chatRoom = ref.getKey();
        }
        //DatabaseReference ref = notificationsRef.child(chatRoom).child(senduerUid);


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notifications/" + receiverUid + "/" + senduerUid,
                new NotificationMessage(senduerUid, receiverUid, chatRoom, MyInterests.getInstance().getNickName(), action, title, message));
        database.getReference().updateChildren(childUpdates);
        return chatRoom;
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

    @Override
    public void storeFileRemote(String localPath, final OnFileStored onFileStored) {
        Uri file = Uri.fromFile(new File(localPath));
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl != null)
                    onFileStored.onFileStored(downloadUrl.toString());
                else
                    onFileStored.onFileStored(null);
            }
        });
    }

    private void initializeNewConnection(final Context context) {
        updateUser();
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Intent intent = new Intent(context.getApplicationContext(), HamletConnectionService.class);
        context.getApplicationContext().startService(intent);
    }
}
