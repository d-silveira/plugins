package io.flutter.plugins.share;

import android.content.Intent;
import android.os.Bundle;

import io.flutter.app.FlutterActivity;

public class ShareReceiverActivity extends FlutterActivity {

	public static final String ORIGINAL_ACTION = "original_action";
//
//    public static final String TAG = "eventchannelsample";
//    public static final String STREAM = "com.yourcompany.eventchannelsample/stream";
//
//    private Disposable subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Get intent, action and MIME type
//		Intent intent = getIntent();
//		String action = intent.getAction();
//		String type = intent.getType();
//
//		if (Intent.ACTION_SEND.equals(action) && type != null) {
//			if ("text/plain".equals(type)) {
////				handleSendText(intent); // Handle text being sent
//    			String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//				Log.w(TAG, "receiving shared text: " + sharedText);
//			} else {
////				handleSendImage(intent); // Handle single image being sent
//				Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
//				Log.w(TAG, "receiving shared file: " + imageUri);
//			}
//
//			restoreMainActivity();
//		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//			Log.w(TAG, "receiving shared files!");
//			ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//			if (type.startsWith("image/")) {
////				handleSendMultipleImages(intent); // Handle multiple images being sent
//			}
//
//			restoreMainActivity();
//		} else {
//
//			Log.w(TAG, "initializing eventChannel");
//
//			// Handle other intents, such as being started from the home screen
//			new EventChannel(getFlutterView(), STREAM).setStreamHandler(new EventChannel.StreamHandler() {
//				@Override
//				public void onListen(Object args, EventChannel.EventSink events) {
//					Log.w(TAG, "adding listener");
//					subscription = Observable.interval(0, 1, TimeUnit.SECONDS).subscribe((Long timer) -> {
//						Log.w(TAG, "emitting timer event " + timer);
//						events.success(timer);
//					}, (Throwable error) -> {
//						Log.e(TAG, "error in emitting timer", error);
//						events.error("STREAM", "Error in processing observable", error.getMessage());
//					}, () -> Log.w(TAG, "closing the timer observable"));
//				}
//
//				@Override
//				public void onCancel(Object args) {
//					Log.w(TAG, "cancelling listener");
//					if (subscription != null) {
//						subscription.dispose();
//						subscription = null;
//					}
//				}
//			});
//
//		}

		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		if ((Intent.ACTION_SEND.equals(action) ||  Intent.ACTION_SEND_MULTIPLE.equals(action)) && type != null) {
			passShareToMainActivity(intent);
		} else {
			finish();
		}


//		finish();
////		restoreMainActivity();

    }

    public void passShareToMainActivity(Intent intent) {
    	Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
    	launchIntent.setType(intent.getType());
    	launchIntent.putExtras(intent);
    	launchIntent.putExtra(ORIGINAL_ACTION, intent.getAction());

		startActivity(launchIntent);
		finish();
	}

    public void restoreMainActivity() {
    	Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
//    	Intent launchIntent = getPackageManager().getLaunchIntentForPackage("pt.sportzone.everyzone");
//    	launchIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//    	launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//    	launchIntent.setAction(Intent.ACTION_MAIN);
//    	launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(launchIntent);
//		finishAffinity();
		finish();
	}
}
