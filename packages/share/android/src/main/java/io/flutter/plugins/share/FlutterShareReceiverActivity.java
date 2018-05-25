package io.flutter.plugins.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;

/**
 * @author Duarte Silveira
 * @version 1
 * @since 25/05/18
 */
public class FlutterShareReceiverActivity extends FlutterActivity {

	public static final String STREAM = "io.flutter.plugins.shareanything/stream";

	private EventChannel.EventSink eventSink = null;
	private boolean inited = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!inited) {
			init(getFlutterView(), this);
		}
	}

	public void init(BinaryMessenger flutterView, Context context) {
		Log.i(getClass().getSimpleName(), "initializing eventChannel");

		context.startActivity(new Intent(context, ShareReceiverActivityWorker.class));

		// Handle other intents, such as being started from the home screen
		new EventChannel(flutterView, STREAM).setStreamHandler(new EventChannel.StreamHandler() {
			@Override
			public void onListen(Object args, EventChannel.EventSink events) {
				Log.i(getClass().getSimpleName(), "adding listener");
				eventSink = events;
			}

			@Override
			public void onCancel(Object args) {
				Log.i(getClass().getSimpleName(), "cancelling listener");
				eventSink = null;
			}
		});

		inited = true;

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	public void handleIntent(Intent intent) {
		// Get intent, action and MIME type
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				Log.i(getClass().getSimpleName(), "receiving shared text: " + sharedText);
				if (eventSink != null) {
					eventSink.success(sharedText);
				}
			} else {
				Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
				Log.i(getClass().getSimpleName(), "receiving shared file: " + imageUri);
			}

		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			Log.i(getClass().getSimpleName(), "receiving shared files!");
			ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
			if (type.startsWith("image/")) {
			}

		}
	}
}
