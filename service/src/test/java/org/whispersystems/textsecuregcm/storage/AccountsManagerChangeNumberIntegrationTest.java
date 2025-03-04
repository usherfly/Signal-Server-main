/*
 * Copyright 2013 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.whispersystems.textsecuregcm.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.signal.libsignal.protocol.IdentityKey;
import org.signal.libsignal.protocol.ecc.Curve;
import org.signal.libsignal.protocol.ecc.ECKeyPair;
import org.whispersystems.textsecuregcm.auth.DisconnectionRequestManager;
import org.whispersystems.textsecuregcm.configuration.dynamic.DynamicConfiguration;
import org.whispersystems.textsecuregcm.controllers.MismatchedDevicesException;
import org.whispersystems.textsecuregcm.entities.AccountAttributes;
import org.whispersystems.textsecuregcm.entities.ECSignedPreKey;
import org.whispersystems.textsecuregcm.identity.IdentityType;
import org.whispersystems.textsecuregcm.redis.FaultTolerantRedisClient;
import org.whispersystems.textsecuregcm.redis.RedisClusterExtension;
import org.whispersystems.textsecuregcm.securestorage.SecureStorageClient;
import org.whispersystems.textsecuregcm.securevaluerecovery.SecureValueRecovery2Client;
import org.whispersystems.textsecuregcm.storage.DynamoDbExtensionSchema.Tables;
import org.whispersystems.textsecuregcm.tests.util.AccountsHelper;
import org.whispersystems.textsecuregcm.tests.util.KeysHelper;

class AccountsManagerChangeNumberIntegrationTest {

  @RegisterExtension
  static final DynamoDbExtension DYNAMO_DB_EXTENSION = new DynamoDbExtension(
      Tables.ACCOUNTS,
      Tables.CLIENT_PUBLIC_KEYS,
      Tables.DELETED_ACCOUNTS,
      Tables.DELETED_ACCOUNTS_LOCK,
      Tables.NUMBERS,
      Tables.PNI,
      Tables.PNI_ASSIGNMENTS,
      Tables.USERNAMES,
      Tables.EC_KEYS,
      Tables.PQ_KEYS,
      Tables.REPEATED_USE_EC_SIGNED_PRE_KEYS,
      Tables.REPEATED_USE_KEM_SIGNED_PRE_KEYS);

  @RegisterExtension
  static final RedisClusterExtension CACHE_CLUSTER_EXTENSION = RedisClusterExtension.builder().build();

  private KeysManager keysManager;
  private DisconnectionRequestManager disconnectionRequestManager;
  private ScheduledExecutorService executor;

  private AccountsManager accountsManager;

  @BeforeEach
  void setup() throws InterruptedException {

    {
      @SuppressWarnings("unchecked") final DynamicConfigurationManager<DynamicConfiguration> dynamicConfigurationManager =
          mock(DynamicConfigurationManager.class);

      DynamicConfiguration dynamicConfiguration = new DynamicConfiguration();
      when(dynamicConfigurationManager.getConfiguration()).thenReturn(dynamicConfiguration);

      keysManager = new KeysManager(
          DYNAMO_DB_EXTENSION.getDynamoDbAsyncClient(),
          Tables.EC_KEYS.tableName(),
          Tables.PQ_KEYS.tableName(),
          Tables.REPEATED_USE_EC_SIGNED_PRE_KEYS.tableName(),
          Tables.REPEATED_USE_KEM_SIGNED_PRE_KEYS.tableName()
      );

      final ClientPublicKeys clientPublicKeys = new ClientPublicKeys(DYNAMO_DB_EXTENSION.getDynamoDbAsyncClient(),
          DynamoDbExtensionSchema.Tables.CLIENT_PUBLIC_KEYS.tableName());

      final Accounts accounts = new Accounts(
          Clock.systemUTC(),
          DYNAMO_DB_EXTENSION.getDynamoDbClient(),
          DYNAMO_DB_EXTENSION.getDynamoDbAsyncClient(),
          Tables.ACCOUNTS.tableName(),
          Tables.NUMBERS.tableName(),
          Tables.PNI_ASSIGNMENTS.tableName(),
          Tables.USERNAMES.tableName(),
          Tables.DELETED_ACCOUNTS.tableName(),
          Tables.USED_LINK_DEVICE_TOKENS.tableName());

      executor = Executors.newSingleThreadScheduledExecutor();

      final AccountLockManager accountLockManager = new AccountLockManager(DYNAMO_DB_EXTENSION.getDynamoDbClient(),
          Tables.DELETED_ACCOUNTS_LOCK.tableName());

      final ClientPublicKeysManager clientPublicKeysManager =
          new ClientPublicKeysManager(clientPublicKeys, accountLockManager, executor);

      final SecureStorageClient secureStorageClient = mock(SecureStorageClient.class);
      when(secureStorageClient.deleteStoredData(any())).thenReturn(CompletableFuture.completedFuture(null));

      final SecureValueRecovery2Client svr2Client = mock(SecureValueRecovery2Client.class);
      when(svr2Client.deleteBackups(any())).thenReturn(CompletableFuture.completedFuture(null));

      disconnectionRequestManager = mock(DisconnectionRequestManager.class);

      final PhoneNumberIdentifiers phoneNumberIdentifiers =
          new PhoneNumberIdentifiers(DYNAMO_DB_EXTENSION.getDynamoDbAsyncClient(), Tables.PNI.tableName());

      final MessagesManager messagesManager = mock(MessagesManager.class);
      when(messagesManager.clear(any())).thenReturn(CompletableFuture.completedFuture(null));

      final ProfilesManager profilesManager = mock(ProfilesManager.class);
      when(profilesManager.deleteAll(any())).thenReturn(CompletableFuture.completedFuture(null));

      final RegistrationRecoveryPasswordsManager registrationRecoveryPasswordsManager =
          mock(RegistrationRecoveryPasswordsManager.class);

      when(registrationRecoveryPasswordsManager.remove(any()))
          .thenReturn(CompletableFuture.completedFuture(null));

      accountsManager = new AccountsManager(
          accounts,
          phoneNumberIdentifiers,
          CACHE_CLUSTER_EXTENSION.getRedisCluster(),
          mock(FaultTolerantRedisClient.class),
          accountLockManager,
          keysManager,
          messagesManager,
          profilesManager,
          secureStorageClient,
          svr2Client,
          disconnectionRequestManager,
          registrationRecoveryPasswordsManager,
          clientPublicKeysManager,
          executor,
          executor,
          mock(Clock.class),
          "link-device-secret".getBytes(StandardCharsets.UTF_8),
          dynamicConfigurationManager);
    }
  }

  @AfterEach
  void tearDown() throws InterruptedException {
    executor.shutdown();

    //noinspection ResultOfMethodCallIgnored
    executor.awaitTermination(1, TimeUnit.SECONDS);
  }

  @Test
  void testChangeNumber() throws InterruptedException, MismatchedDevicesException {
    final String originalNumber = "+18005551111";
    final String secondNumber = "+18005552222";
    final Account account = AccountsHelper.createAccount(accountsManager, originalNumber);

    final UUID originalUuid = account.getUuid();
    final UUID originalPni = account.getPhoneNumberIdentifier();

    accountsManager.changeNumber(account, secondNumber, null, null, null, null);

    assertTrue(accountsManager.getByE164(originalNumber).isEmpty());

    final Account updatedAccount = accountsManager.getByE164(secondNumber).orElseThrow();
    assertEquals(originalUuid, updatedAccount.getUuid());
    assertEquals(secondNumber, updatedAccount.getNumber());
    assertNotEquals(originalPni, updatedAccount.getPhoneNumberIdentifier());

    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(originalPni));
    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(updatedAccount.getPhoneNumberIdentifier()));
  }

  @Test
  void testChangeNumberWithPniExtensions() throws InterruptedException, MismatchedDevicesException {
    final String originalNumber = "+18005551111";
    final String secondNumber = "+18005552222";
    final int rotatedPniRegistrationId = 17;
    final ECKeyPair rotatedPniIdentityKeyPair = Curve.generateKeyPair();
    final ECSignedPreKey rotatedSignedPreKey = KeysHelper.signedECPreKey(1L, rotatedPniIdentityKeyPair);
    final AccountAttributes accountAttributes = new AccountAttributes(true, rotatedPniRegistrationId + 1, rotatedPniRegistrationId, "test".getBytes(StandardCharsets.UTF_8), null, true, Set.of());
    final Account account = AccountsHelper.createAccount(accountsManager, originalNumber, accountAttributes);

    keysManager.storeEcSignedPreKeys(account.getIdentifier(IdentityType.ACI),
        Device.PRIMARY_ID, KeysHelper.signedECPreKey(1, rotatedPniIdentityKeyPair)).join();

    final UUID originalUuid = account.getUuid();
    final UUID originalPni = account.getPhoneNumberIdentifier();

    final IdentityKey pniIdentityKey = new IdentityKey(rotatedPniIdentityKeyPair.getPublicKey());
    final Map<Byte, ECSignedPreKey> preKeys = Map.of(Device.PRIMARY_ID, rotatedSignedPreKey);
    final Map<Byte, Integer> registrationIds = Map.of(Device.PRIMARY_ID, rotatedPniRegistrationId);

    final Account updatedAccount = accountsManager.changeNumber(account, secondNumber, pniIdentityKey, preKeys, null, registrationIds);
    final UUID secondPni = updatedAccount.getPhoneNumberIdentifier();

    assertTrue(accountsManager.getByE164(originalNumber).isEmpty());

    assertTrue(accountsManager.getByE164(secondNumber).isPresent());
    assertEquals(originalUuid, accountsManager.getByE164(secondNumber).map(Account::getUuid).orElseThrow());
    assertNotEquals(originalPni, secondPni);
    assertEquals(secondPni, accountsManager.getByE164(secondNumber).map(Account::getPhoneNumberIdentifier).orElseThrow());

    assertEquals(secondNumber, accountsManager.getByAccountIdentifier(originalUuid).map(Account::getNumber).orElseThrow());

    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(originalPni));
    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(secondPni));

    assertEquals(pniIdentityKey, updatedAccount.getIdentityKey(IdentityType.PNI));

    assertEquals(OptionalInt.of(rotatedPniRegistrationId),
        updatedAccount.getPrimaryDevice().getPhoneNumberIdentityRegistrationId());

    assertEquals(Optional.of(rotatedSignedPreKey),
        keysManager.getEcSignedPreKey(updatedAccount.getIdentifier(IdentityType.PNI), Device.PRIMARY_ID).join());
  }

  @Test
  void testChangeNumberReturnToOriginal() throws InterruptedException, MismatchedDevicesException {
    final String originalNumber = "+18005551111";
    final String secondNumber = "+18005552222";

    Account account = AccountsHelper.createAccount(accountsManager, originalNumber);

    final UUID originalUuid = account.getUuid();
    final UUID originalPni = account.getPhoneNumberIdentifier();

    account = accountsManager.changeNumber(account, secondNumber, null, null, null, null);
    final UUID secondPni = account.getPhoneNumberIdentifier();
    accountsManager.changeNumber(account, originalNumber, null, null, null, null);

    assertTrue(accountsManager.getByE164(originalNumber).isPresent());
    assertEquals(originalUuid, accountsManager.getByE164(originalNumber).map(Account::getUuid).orElseThrow());
    assertEquals(originalPni, accountsManager.getByE164(originalNumber).map(Account::getPhoneNumberIdentifier).orElseThrow());

    assertTrue(accountsManager.getByE164(secondNumber).isEmpty());

    assertEquals(originalNumber, accountsManager.getByAccountIdentifier(originalUuid).map(Account::getNumber).orElseThrow());

    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(originalPni));
    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(secondPni));
  }

  @Test
  void testChangeNumberContested() throws InterruptedException, MismatchedDevicesException {
    final String originalNumber = "+18005551111";
    final String secondNumber = "+18005552222";

    final Account account = AccountsHelper.createAccount(accountsManager, originalNumber);

    final UUID originalUuid = account.getUuid();
    final UUID originalPni = account.getPhoneNumberIdentifier();

    final Account existingAccount = AccountsHelper.createAccount(accountsManager, secondNumber);

    final UUID existingAccountUuid = existingAccount.getUuid();

    accountsManager.changeNumber(account, secondNumber, null, null, null, null);
    final UUID secondPni = accountsManager.getByE164(secondNumber).get().getPhoneNumberIdentifier();

    assertTrue(accountsManager.getByE164(originalNumber).isEmpty());

    assertTrue(accountsManager.getByE164(secondNumber).isPresent());
    assertEquals(Optional.of(originalUuid), accountsManager.getByE164(secondNumber).map(Account::getUuid));

    assertEquals(secondNumber, accountsManager.getByAccountIdentifier(originalUuid).map(Account::getNumber).orElseThrow());

    verify(disconnectionRequestManager).requestDisconnection(existingAccountUuid);

    assertEquals(Optional.of(existingAccountUuid), accountsManager.findRecentlyDeletedAccountIdentifier(originalPni));
    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(secondPni));

    accountsManager.changeNumber(accountsManager.getByAccountIdentifier(originalUuid).orElseThrow(), originalNumber, null, null, null, null);

    final Account existingAccount2 = AccountsHelper.createAccount(accountsManager, secondNumber);

    assertEquals(existingAccountUuid, existingAccount2.getUuid());
  }

  @Test
  void testChangeNumberChaining() throws InterruptedException, MismatchedDevicesException {
    final String originalNumber = "+18005551111";
    final String secondNumber = "+18005552222";

    final Account account = AccountsHelper.createAccount(accountsManager, originalNumber);

    final UUID originalUuid = account.getUuid();
    final UUID originalPni = account.getPhoneNumberIdentifier();

    final Account existingAccount = AccountsHelper.createAccount(accountsManager, secondNumber);

    final UUID existingAccountUuid = existingAccount.getUuid();

    final Account changedNumberAccount = accountsManager.changeNumber(account, secondNumber, null, null, null, null);
    final UUID secondPni = changedNumberAccount.getPhoneNumberIdentifier();

    final Account reRegisteredAccount = AccountsHelper.createAccount(accountsManager, originalNumber);

    assertEquals(existingAccountUuid, reRegisteredAccount.getUuid());
    assertEquals(originalPni, reRegisteredAccount.getPhoneNumberIdentifier());

    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(originalPni));
    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(secondPni));

    final Account changedNumberReRegisteredAccount = accountsManager.changeNumber(reRegisteredAccount, secondNumber, null, null, null, null);

    assertEquals(Optional.of(originalUuid), accountsManager.findRecentlyDeletedAccountIdentifier(originalPni));
    assertEquals(Optional.empty(), accountsManager.findRecentlyDeletedAccountIdentifier(secondPni));
    assertEquals(secondPni, changedNumberReRegisteredAccount.getPhoneNumberIdentifier());
  }
}
