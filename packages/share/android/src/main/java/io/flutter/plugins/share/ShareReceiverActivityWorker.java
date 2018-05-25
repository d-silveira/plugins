package io.flutter.plugins.share;

import android.content.Intent;
import android.os.Bundle;

import io.flutter.app.FlutterActivity;

public class ShareReceiverActivityWorker extends FlutterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		if ((Intent.ACTION_SEND.equals(action) ||  Intent.ACTION_SEND_MULTIPLE.equals(action)) && type != null) {
			passShareToMainActivity(intent);
		} else {
			finish();
		}

    }

    public void passShareToMainActivity(Intent intent) {
    	Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
		launchIntent.setAction(intent.getAction());
    	launchIntent.setType(intent.getType());
    	launchIntent.putExtras(intent);

		startActivity(launchIntent);
		finish();
	}
}
