package io.flutter.plugins.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.EventChannel;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class ShareReceiverActivity extends FlutterActivity {

    public static final String TAG = "eventchannelsample";
    public static final String STREAM = "com.yourcompany.eventchannelsample/stream";

    private Disposable subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
//				handleSendText(intent); // Handle text being sent
    			String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				Log.w(TAG, "receiving shared text: " + sharedText);
			} else if (type.startsWith("image/")) {
//				handleSendImage(intent); // Handle single image being sent
				Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
				Log.w(TAG, "receiving shared file: " + imageUri);
			}
		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			Log.w(TAG, "receiving shared files!");
			ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
			if (type.startsWith("image/")) {
//				handleSendMultipleImages(intent); // Handle multiple images being sent
			}
		} else {
			// Handle other intents, such as being started from the home screen
			new EventChannel(getFlutterView(), STREAM).setStreamHandler(new EventChannel.StreamHandler() {
				@Override
				public void onListen(Object args, EventChannel.EventSink events) {
					Log.w(TAG, "adding listener");
					subscription = Observable.interval(0, 1, TimeUnit.SECONDS).subscribe((Long timer) -> {
						Log.w(TAG, "emitting timer event " + timer);
						events.success(timer);
					}, (Throwable error) -> {
						Log.e(TAG, "error in emitting timer", error);
						events.error("STREAM", "Error in processing observable", error.getMessage());
					}, () -> Log.w(TAG, "closing the timer observable"));
				}

				@Override
				public void onCancel(Object args) {
					Log.w(TAG, "cancelling listener");
					if (subscription != null) {
						subscription.dispose();
						subscription = null;
					}
				}
			});

		}

    }
}
