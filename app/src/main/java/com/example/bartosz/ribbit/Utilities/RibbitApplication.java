package com.example.bartosz.ribbit.Utilities;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by Bartosz on 20.10.2015.
 */
public class RibbitApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "79pjpERzVeKt6aLnjKmmucLiOg0g7sc3cjvlnac9", "YNYMCnxQzHhjB3ODjG1mDcLddR4KxvZwOl4DRCLh");

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public static void updateParseInstallation(ParseUser user){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }

}
