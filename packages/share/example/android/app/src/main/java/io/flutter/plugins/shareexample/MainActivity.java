// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.shareexample;

import android.content.Intent;
import android.os.Bundle;

import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugins.share.ShareReceiverActivity;

public class MainActivity extends ShareReceiverActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    startActivity(new Intent(this, ShareReceiverActivity.class));
  }
}
