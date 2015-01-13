package xor.technology.sender;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

public class Application extends android.app.Application {

    public Application() {
    }

    public void onCreate() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Initialize the Parse SDK
        Parse.initialize(this, "SECRET_KEY", "SECRET_KEY");

        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, SenderMain.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
