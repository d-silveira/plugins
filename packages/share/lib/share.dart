// Copyright 2018 Duarte Silveira
// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';
import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart' show visibleForTesting;

/// Plugin for summoning a platform share sheet.
class Share {

	static final String TITLE  = "title";
	static final String TEXT   = "text";
	static final String PATH   = "path";
	static final String TYPE   = "type";
	static final String IS_MULTIPLE   = "is_multiple";

  /// [MethodChannel] used to communicate with the platform side.
  @visibleForTesting
  static const MethodChannel channel = const MethodChannel('plugins.flutter.io/shareanything');

  /// Summons the platform's share sheet to share text.
  ///
  /// Wraps the platform's native share dialog. Can share a text and/or a URL.
  /// It uses the ACTION_SEND Intent on Android and UIActivityViewController
  /// on iOS.
  ///
  /// The optional `sharePositionOrigin` parameter can be used to specify a global
  /// origin rect for the share sheet to popover from on iPads. It has no effect
  /// on non-iPads.
  ///
  /// May throw [PlatformException] or [FormatException]
  /// from [MethodChannel].

  static Future<void> share(String text, {Rect sharePositionOrigin}) {
    assert(text != null);
    assert(text.isNotEmpty);
    final Map<String, dynamic> params = <String, dynamic>{
      TEXT: text
    };

    if (sharePositionOrigin != null) {
      params['originX'] = sharePositionOrigin.left;
      params['originY'] = sharePositionOrigin.top;
      params['originWidth'] = sharePositionOrigin.width;
      params['originHeight'] = sharePositionOrigin.height;
    }

    return channel.invokeMethod('share', params);
  }

  static Future<void> shareWithTitle(String text, String title, {Rect sharePositionOrigin}) {
    assert(text != null);
    assert(text.isNotEmpty);
    assert(title != null);
    assert(title.isNotEmpty);
    final Map<String, dynamic> params = title != null && title.isNotEmpty ? <String, dynamic>{
      TEXT: text,
      TITLE: title
    }
    : <String, dynamic>{
      TEXT: text
    };

    if (sharePositionOrigin != null) {
      params['originX'] = sharePositionOrigin.left;
      params['originY'] = sharePositionOrigin.top;
      params['originWidth'] = sharePositionOrigin.width;
      params['originHeight'] = sharePositionOrigin.height;
    }

    return channel.invokeMethod('share', params);
  }

  static Future<void> shareAnything(String path, String mimeType, {Rect sharePositionOrigin}) {
    assert(path != null);
    assert(path.isNotEmpty);
    final Map<String, dynamic> params = <String, dynamic>{
      PATH: path,
      TYPE: mimeType,
    };

    if (sharePositionOrigin != null) {
      params['originX'] = sharePositionOrigin.left;
      params['originY'] = sharePositionOrigin.top;
      params['originWidth'] = sharePositionOrigin.width;
      params['originHeight'] = sharePositionOrigin.height;
    }

    return channel.invokeMethod('shareAnything', params);
  }

  static Future<void> shareAnythingWithTitle(String path, String title, String mimeType, {Rect sharePositionOrigin}) {
    assert(path != null);
    assert(path.isNotEmpty);
    assert(title != null);
    assert(title.isNotEmpty);
    final Map<String, dynamic> params = title != null && title.isNotEmpty ? <String, dynamic>{
      PATH: path,
      TYPE: mimeType,
      TITLE: title
    }
    : <String, dynamic>{
      PATH: path,
      TYPE: mimeType,
    };

    if (sharePositionOrigin != null) {
      params['originX'] = sharePositionOrigin.left;
      params['originY'] = sharePositionOrigin.top;
      params['originWidth'] = sharePositionOrigin.width;
      params['originHeight'] = sharePositionOrigin.height;
    }

    return channel.invokeMethod('shareAnything', params);
  }

}
