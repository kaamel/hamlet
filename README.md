# Group Project - *Hamlet*

**Hamlet** is an android app that allows a user to connect with other nearby users that are looking for others who are interested in common topics of interest to interact with. The "nearby" is based on the GPS location of the two users, and the interests are dynamically defined by each user for that moment and location. Whether one is looking for a walking companion in the afternoon, a playmate for their 5 year old son in the park, or other geeky Android developers in a convention, they can be looked by a few touches on the screen.


## User Stories

The following **required** functionality:

* [ ] User is **authenticated by singing with a supported account**.
  * [ ] Google/Gmail
  * [ ] Facebook
* [ ] User can select one or more of the **predefined interest categories**
* [ ] User can provide addtional information about themselves and/or what they are looking for
  * [ ] This informatoin can be available to other users in the area and are considered **public**
* [ ] When the user selects cateories and provide details, a new **session** is started
  * [ ] The session will last for a preset amount of time, or when the user leaves the app, which ever happens later
  * [ ] User can explicitly end the session at any time.
* [ ] While the session is open, the user can see a list of other users who have selected one or more common categories and are in the same geographic location.
  * [ ] If another user appears on the list, that user's list will also change to include the first user
  * [ ] At the end of the session others will not long see the first user on their list.
  * [ ] At the end of the session, the user will not see any other user on their list
* [ ] User can change their categories of interest at any time
  * [ ] The list of matches on their phone will change accordingly
  * [ ] They will appear or disappear from the other users' lists accordingly
* [ ] User can dismiss any user from their list
  * [ ] The dimssied user will dispear from the user's list for the duration of the session
  * [ ] The user that takes the dismiss action will also disappear from the other user's list
* [ ] When a user touchs another user on the list a **detail view** 
* [ ] The detail view has several sub views
  * [ ] In a text view it shows the information that the other user has made available to **public** 
  * [ ] It has a **connect** button
  * [ ] It has a **return** button
* [ ] In the detail view the user can take actions 
  * [ ] When the user touches the **connect** button an alert dialog dialog appears with only a **end** button
  * [ ] The **end** button will terminate the user's attempt to connect
  * [ ] The attempt is also terminated after a certain amount of time if the connection is not established
  * [ ] The other user receives a **request to connect notification**
  * [ ] When the connection is established the alert dialog disapears and the user sees a **multimedia chat screen**
  * [ ] The **return** button on the details view will take the user back to the list view
* [ ] When a user receives a **request to connet notification** they ignore it or open it
* [ ] If the user opens the **request  connect notification** they will see a dialog with the following views
  * [ ] A text message that shows the originating user's detail information
  * [ ] Timestamp of the when the request was sent
  * [ ] A **connect button** if the connection request is still valid and is not terminated
* [ ] If a user receives a **request to connect notification** and accept it a connection is established
  * [ ] The originating user will see a **multimedia chat screen**
  * [ ] The receiving user will see a **multimedia chat screen**
* [ ] In the **multimedia chat screen** user can see several sub views
  * [ ] A **multimedia list view*
  * [ ] An edit text view to enter text messages
  * [ ] An image button that can be used to add an attached image
  * [ ] An image view showing an attached image to be sent, if any
  * [ ] An image button that can be used to add an attached audio
  * [ ] An text view showing a link to an attached audio to be sent, if any
  * [ ] An image button that can be used to add an attached video
  * [ ] An image view showing a thumbnail of an attached video to be sent, if any
  * [ ] An image button that can be used to attach the user's current location
  * [ ] A text view showing the lat/long of the user's current location to be sent, if any
  * [ ] A **send** button to send the multimedia chat
  * [ ] An **end** button to end the connection and return to the nearby users list view
* [ ] When the user sends a multimedia text the following will happen
  * [ ] The message appears on their **multimedia list view**
  * [ ] The messages appears on the other user's **multimedia list view**
  * [ ] The other user receives a **notification alert** 
* [ ] The **multimedia list view** shows the previous chat message exchanges in the **current session**
  * [ ] The user can see the incoming messages, left aligned, in **blue** (or some other designated color)
  * [ ] The user can see their own messages, right aligned, in **orange** (or some other designated color)
* [ ] In the **multimedia list view** each item has one or more views
  * [ ] A text view that shows the text part of the message
  * [ ] An image view that shows the attached image, **if applicable**
  * [ ] An image button that can play the attached audio, **if applicable**
  * [ ] A video controller view (play, pause, stop, etc) than control the playback of the attached view **if applicable**
  * [ ] A map view that shows the current attached location, **if applicable**
* [ ] When the session ends everything is removed from the backend server
  * [ ] All messages are removed and there will be no history for the chat exchanges
  * [ ] All stored multimedia files (audio, video, images, location, text) are removed from the backend storage
* [ ] Firebase Authentication is used for user registration/authentication [Firebase Authentication](https://firebase.google.com/docs/auth/android/start/)
* [ ] Firebase Cloud Storage is used to preserve multimedia messages for active sessions [Firebase Cloud Storage](https://firebase.google.com/docs/storage/android/start)
* [ ] Firebase Cloud Messaging is used for user to user notifications [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/android/client)
* [ ] Firebase Realtime Database is used for multimedia messaging [Firbase Realtime Database](https://firebase.google.com/products/realtime-database/)
* [ ] Firebase Colud Functions is used for establising sessions, and cleaning up at the end of a session [Firebase Cloud Functions](https://firebase.google.com/docs/functions/get-started)
* [ ] Google Maps is used for location mapping/display [Google Maps Android API](https://developers.google.com/maps/documentation/android-api/start)
 
 
 
  
The following **optional** features are implemented:

* [ ] User can authenticate with their
  * [ ] LinkedIn account
  * [ ] Yahoo account
  * [ ] Twitter account
  * [ ] Email/password
  * [ ] Phone number
* [ ] In addition to the preset category of interests, users can enter up to 5 comma separated interests 
* [ ] **Stretch:** The **multimedia chat screen** also has and supports additional media types
  * [ ] A **multimedia list view*
  * [ ] An image button that can be used to add an attached audio
  * [ ] An text view showing a link to an attached audio to be sent, if any
  * [ ] An image button that can be used to add an attached video
  * [ ] An image view showing a thumbnail of an attached video to be sent, if any
  * [ ] An image button that can be used to attach the user's current location
  * [ ] A text view showing the lat/long of the user's current location to be sent, if any
* [ ] **Stretch:**In the **multimedia list view** additional item types are supported
  * [ ] An image button that can play the attached audio, **if applicable**
  * [ ] A video controller view (play, pause, stop, etc) than control the playback of the attached view **if applicable**
  * [ ] A map view that shows the current attached location, **if applicable**
* [ ] **Extrem Stretch:** The users can establish long term relationshiops in the chat view
  * [ ] Become Friends though Facebook
  * [ ] Follow each other on Twitter
  * [ ] Connected through Google+
  * [ ] Connected through LinkedIn 


## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img ' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android


## License

    Copyright [2017] [InceptionFactory]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
