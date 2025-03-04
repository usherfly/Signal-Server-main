/*
 * Copyright 2023 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.whispersystems.textsecuregcm.grpc;

import io.grpc.Status;
import org.whispersystems.textsecuregcm.storage.Device;

public class DeviceIdUtil {

  static byte validate(int deviceId) {
    if (deviceId < Device.PRIMARY_ID || deviceId > Byte.MAX_VALUE) {
      throw Status.INVALID_ARGUMENT.withDescription("Device ID is out of range").asRuntimeException();
    }

    return (byte) deviceId;
  }
}
