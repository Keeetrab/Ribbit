package com.example.bartosz.ribbit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Bartosz on 20.10.2015.
 */
public class RibbitApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "79pjpERzVeKt6aLnjKmmucLiOg0g7sc3cjvlnac9", "YNYMCnxQzHhjB3ODjG1mDcLddR4KxvZwOl4DRCLh");

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

}
