/*
 * Copyright 2013-2020 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.whispersystems.textsecuregcm.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MismatchedDevices(@JsonProperty
                                @Schema(description = "Devices present on the account but absent in the request")
                                List<Byte> missingDevices,

                                @JsonProperty
                                @Schema(description = "Devices absent on the request but present in the account")
                                List<Byte> extraDevices) {
}
