package com.genesis.hamlet.util.threading;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kaamel on 10/24/17.
 */

public class FirebaseHelper {

    public static Task deleteFirebaseNode(String ref, String node) {
        return FirebaseDatabase.getInstance()
                .getReference(ref).child(node).setValue(null);
    }
}
