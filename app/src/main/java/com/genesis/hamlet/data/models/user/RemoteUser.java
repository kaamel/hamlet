package com.genesis.hamlet.data.models.user;

import com.genesis.hamlet.data.models.interests.Interests;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaamel on 10/18/17.
 */

public class RemoteUser {
    Interests interests;
    double lat;
    double lon;
    long locationUpdateTime;

    public RemoteUser() {

    }

    public RemoteUser(Interests interests, double lat, double lon, long time) {
        this.interests = interests;
        this.lat = lat;
        this.lon = lon;
        this.locationUpdateTime = time;
    }

    public void  updateLocation(long lat, long lon, long time) {
        this.lat = lat;
        this.lon = lon;
        this.locationUpdateTime = time;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lat", lat);
        result.put("lon", lon);
        result.put("location_updated_time", locationUpdateTime);
        result.put("interests", interests);

        return result;
    }

    public static User getUser(HashMap map) {
        User user = new User();
        user.setLat((Long) map.get("lat"));
        user.setLon((Long) map.get("lon"));
        user.setLocationUpdateTime((Long) map.get("location_updated_time"));
        HashMap<String, Object> interests = (HashMap<String, Object>) map.get("interests");
        List<Boolean> in = (List<Boolean>) interests.get("interestCats");
        boolean[] array = new boolean[in.size()];
        for (int i=0; i< in.size(); i++) array[i] = in.get(i);
        user.setInterests(array);
        user.setDisplayName((String) interests.get("nickName"));
        user.setIntroTitle((String) interests.get("introTitle"));
        user.setIntroDetail((String) interests.get("introDetail"));

        return user;
    }
}
