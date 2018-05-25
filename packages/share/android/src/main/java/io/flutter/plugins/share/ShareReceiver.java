package io.flutter.plugins.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static io.flutter.plugins.share.ShareReceiverActivity.ORIGINAL_ACTION;

/**
 * @author Duarte Silveira
 * @version 1
 * @since 25/05/18
 */
public class ShareReceiver {

	public static final String TAG    = "eventchannelsample";
	public static final String STREAM = "com.yourcompany.eventchannelsample/stream";

	private Disposable subscription;

	public ShareReceiver(BinaryMessenger flutterView, Context context) {
		Log.w(TAG, "initializing eventChannel");

		context.startActivity(new Intent(context, ShareReceiverActivity.class));

		// Handle other intents, such as being started from the home screen
		new EventChannel(flutterView, STREAM).setStreamHandler(new EventChannel.StreamHandler() {
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

	public void handleIntent(Intent intent) {
		// Get intent, action and MIME type
		String action = intent.hasExtra(ORIGINAL_ACTION) ? intent.getStringExtra(ORIGINAL_ACTION) : "";
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				//				handleSendText(intent); // Handle text being sent
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				Log.w(TAG, "receiving shared text: " + sharedText);
			} else {
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

		}
	}
}
