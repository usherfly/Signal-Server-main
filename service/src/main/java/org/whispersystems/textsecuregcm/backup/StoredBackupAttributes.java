/*
 * Copyright 2024 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.whispersystems.textsecuregcm.backup;

import java.time.Instant;

/**
 * Attributes stored in the backups table for a single backup id
 *
 * @param lastRefresh      The last time the record was updated with a messages or media tier credential
 * @param lastMediaRefresh The last time the record was updated with a media tier credential
 * @param bytesUsed        The number of media bytes used by the backup
 * @param numObjects       The number of media objects used byt the backup
 */
public record StoredBackupAttributes(
    Instant lastRefresh, Instant lastMediaRefresh,
    long bytesUsed, long numObjects) {}
