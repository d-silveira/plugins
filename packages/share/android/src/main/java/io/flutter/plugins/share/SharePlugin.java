// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.share;

import android.content.Intent;
import android.net.Uri;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * Plugin method host for presenting a share sheet via Intent
 *
 * @author Duarte Silveira
 * @version 1
 * @since 25/05/18
 */
public class SharePlugin implements MethodChannel.MethodCallHandler {

	private static final String CHANNEL = "plugins.flutter.io/shareanything";

	public static void registerWith(Registrar registrar) {
		MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
		SharePlugin instance = new SharePlugin(registrar);
		channel.setMethodCallHandler(instance);
	}

	private final Registrar mRegistrar;

	private SharePlugin(Registrar registrar) {
		this.mRegistrar = registrar;
	}

	@Override
	public void onMethodCall(MethodCall call, MethodChannel.Result result) {
		if (call.method.equals("share")) {
			if (!(call.arguments instanceof Map)) {
				throw new IllegalArgumentException("Map argument expected");
			}
			// Android does not support showing the share sheet at a particular point on screen.
			if (call.hasArgument("title")) {
				share((String) call.argument("text"), (String) call.argument("title"));
			} else {
				share((String) call.argument("text"));
			}
			result.success(null);
		} else if (call.method.equals("shareAnything")) {
			if (!(call.arguments instanceof Map)) {
				throw new IllegalArgumentException("Map argument expected");
			}
			// Android does not support showing the share sheet at a particular point on screen.
			if (call.hasArgument("title")) {
				shareAnything((String) call.argument("path"), (String) call.argument("mimeType"), (String) call.argument("title"));
			} else {
				shareAnything((String) call.argument("path"), (String) call.argument("mimeType"));
			}
			result.success(null);
		} else {
			result.notImplemented();
		}
	}

	private void share(String text) {
		share(text, "");
	}

	private void share(String text, String title) {
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("Non-empty text expected");
		}

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		if (title != null && !title.isEmpty()) {
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
		}
		shareIntent.setType("text/plain");
		Intent chooserIntent = Intent.createChooser(shareIntent, null /* dialog title optional */);
		if (mRegistrar.activity() != null) {
			mRegistrar.activity().startActivity(chooserIntent);
		} else {
			chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mRegistrar.context().startActivity(chooserIntent);
		}
	}

	private void shareAnything(String path, String mimeType) {
		shareAnything(path, mimeType, "");
	}

	private void shareAnything (String path, String mimeType, String title) {
		if (path == null || path.isEmpty()) {
			throw new IllegalArgumentException("Non-empty path expected");
		}
		if (mimeType == null || mimeType.isEmpty()) {
			throw new IllegalArgumentException("Non-empty mimeType expected");
		}

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		if (title != null && !title.isEmpty()) {
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
		}
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
		shareIntent.setType(mimeType);
		Intent chooserIntent = Intent.createChooser(shareIntent, null /* dialog title optional */);
		if (mRegistrar.activity() != null) {
			mRegistrar.activity().startActivity(chooserIntent);
		} else {
			chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mRegistrar.context().startActivity(chooserIntent);
		}
	}

	}
