// Copyright 2018 Duarte Silveira
// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';
import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart' show visibleForTesting;

class ShareType {
  final String _type;
  const ShareType._internal(this._type);
  @override
  String toString() {
    return _type;
  }

  static const ShareType TYPE_PLAIN_TEXT = const ShareType._internal("text/plain");
  static const ShareType TYPE_IMAGE = const ShareType._internal("image/*");
  static const ShareType TYPE_FILE = const ShareType._internal("*/*");

}

/// Plugin for summoning a platform share sheet.
class Share {

  static const String TITLE = "title";
  static const String TEXT = "text";
  static const String PATH = "path";
  static const String TYPE = "type";
  static const String IS_MULTIPLE = "is_multiple";

  final ShareType mimeType;
  final String title;
  final String text;
  final String path;
  final List<Share> shares;

  const Share.plainText({
    this.title,
    this.text
  }) : assert(text != null),
       this.mimeType = ShareType.TYPE_PLAIN_TEXT,
       this.path = '',
       this.shares = const[];

  const Share.file({
    this.mimeType = ShareType.TYPE_FILE,
    this.title,
    this.path,
    this.text = ''
  }) : assert(mimeType != null),
       assert(path != null),
       this.shares = const[];

  const Share.image({
    this.mimeType = ShareType.TYPE_IMAGE,
    this.title,
    this.path,
    this.text = ''
  }) : assert(mimeType != null),
       assert(path != null),
       this.shares = const[];

  const Share.multiple({
    this.mimeType = ShareType.TYPE_FILE,
    this.title,
    this.shares
  }) : assert(mimeType != null),
       assert(shares != null),
       this.text = '',
       this.path = '';

  /// [MethodChannel] used to communicate with the platform side.
  @visibleForTesting
  static const MethodChannel channel = const MethodChannel('plugins.flutter.io/share');

  bool get isMultiple => this.shares.isNotEmpty;

  Future<void> share({Rect sharePositionOrigin}) {
    final Map<String, dynamic> params = <String, dynamic>{
      TYPE: mimeType.toString(),
      IS_MULTIPLE: isMultiple
    };
    if (sharePositionOrigin != null) {
      params['originX'] = sharePositionOrigin.left;
      params['originY'] = sharePositionOrigin.top;
      params['originWidth'] = sharePositionOrigin.width;
      params['originHeight'] = sharePositionOrigin.height;
    }
    if (title != null && title.isNotEmpty) {
      params[TITLE] = title;
    }

    switch (mimeType) {
      case ShareType.TYPE_PLAIN_TEXT:
        if (isMultiple) {
          for(var i = 0; i < shares.length; i++) {
            params["$i"] = shares[i].text;
          }
        } else {
          params[TEXT] = text;
        }
        break;

      case ShareType.TYPE_IMAGE:
      case ShareType.TYPE_FILE:
        if (isMultiple) {
          for (var i = 0; i < shares.length; i++) {
            params["$i"] = shares[i].path;
          }
        } else {
          params[PATH] = path;
          if (text != null && text.isNotEmpty) {
            params[TEXT] = text;
          }
        }
        break;

    }

    return channel.invokeMethod('share', params);
  }

}
