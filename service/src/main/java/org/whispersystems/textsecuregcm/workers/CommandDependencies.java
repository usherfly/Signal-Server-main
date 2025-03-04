/*
 * Copyright 2023 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.whispersystems.textsecuregcm.workers;

import static com.codahale.metrics.MetricRegistry.name;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.DeserializationFeature;
import io.dropwizard.core.setup.Environment;
import io.lettuce.core.resource.ClientResources;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import org.signal.libsignal.zkgroup.GenericServerSecretParams;
import org.signal.libsignal.zkgroup.InvalidInputException;
import org.whispersystems.textsecuregcm.WhisperServerConfiguration;
import org.whispersystems.textsecuregcm.attachments.TusAttachmentGenerator;
import org.whispersystems.textsecuregcm.auth.DisconnectionRequestManager;
import org.whispersystems.textsecuregcm.auth.ExternalServiceCredentialsGenerator;
import org.whispersystems.textsecuregcm.backup.BackupManager;
import org.whispersystems.textsecuregcm.backup.BackupsDb;
import org.whispersystems.textsecuregcm.backup.Cdn3BackupCredentialGenerator;
import org.whispersystems.textsecuregcm.backup.Cdn3RemoteStorageManager;
import org.whispersystems.textsecuregcm.configuration.dynamic.DynamicConfiguration;
import org.whispersystems.textsecuregcm.controllers.SecureStorageController;
import org.whispersystems.textsecuregcm.controllers.SecureValueRecovery2Controller;
import org.whispersystems.textsecuregcm.experiment.PushNotificationExperimentSamples;
import org.whispersystems.textsecuregcm.limits.RateLimiters;
import org.whispersystems.textsecuregcm.metrics.MicrometerAwsSdkMetricPublisher;
import org.whispersystems.textsecuregcm.push.APNSender;
import org.whispersystems.textsecuregcm.push.FcmSender;
import org.whispersystems.textsecuregcm.push.WebSocketConnectionEventManager;
import org.whispersystems.textsecuregcm.push.PushNotificationManager;
import org.whispersystems.textsecuregcm.push.PushNotificationScheduler;
import org.whispersystems.textsecuregcm.redis.FaultTolerantRedisClient;
import org.whispersystems.textsecuregcm.redis.FaultTolerantRedisClusterClient;
import org.whispersystems.textsecuregcm.securestorage.SecureStorageClient;
import org.whispersystems.textsecuregcm.securevaluerecovery.SecureValueRecovery2Client;
import org.whispersystems.textsecuregcm.storage.AccountLockManager;
import org.whispersystems.textsecuregcm.storage.Accounts;
import org.whispersystems.textsecuregcm.storage.AccountsManager;
import org.whispersystems.textsecuregcm.storage.ClientPublicKeys;
import org.whispersystems.textsecuregcm.storage.ClientPublicKeysManager;
import org.whispersystems.textsecuregcm.storage.DynamicConfigurationManager;
import org.whispersystems.textsecuregcm.storage.IssuedReceiptsManager;
import org.whispersystems.textsecuregcm.storage.KeysManager;
import org.whispersystems.textsecuregcm.storage.MessagesCache;
import org.whispersystems.textsecuregcm.storage.MessagesDynamoDb;
import org.whispersystems.textsecuregcm.storage.MessagesManager;
import org.whispersystems.textsecuregcm.storage.PhoneNumberIdentifiers;
import org.whispersystems.textsecuregcm.storage.Profiles;
import org.whispersystems.textsecuregcm.storage.ProfilesManager;
import org.whispersystems.textsecuregcm.storage.RegistrationRecoveryPasswords;
import org.whispersystems.textsecuregcm.storage.RegistrationRecoveryPasswordsManager;
import org.whispersystems.textsecuregcm.storage.ReportMessageDynamoDb;
import org.whispersystems.textsecuregcm.storage.ReportMessageManager;
import org.whispersystems.textsecuregcm.util.ManagedAwsCrt;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Construct utilities commonly used by worker commands
 */
record CommandDependencies(
    AccountsManager accountsManager,
    ProfilesManager profilesManager,
    ReportMessageManager reportMessageManager,
    MessagesCache messagesCache,
    MessagesManager messagesManager,
    KeysManager keysManager,
    RegistrationRecoveryPasswordsManager registrationRecoveryPasswordsManager,
    APNSender apnSender,
    FcmSender fcmSender,
    PushNotificationManager pushNotificationManager,
    PushNotificationExperimentSamples pushNotificationExperimentSamples,
    FaultTolerantRedisClusterClient cacheCluster,
    FaultTolerantRedisClusterClient pushSchedulerCluster,
    ClientResources.Builder redisClusterClientResourcesBuilder,
    BackupManager backupManager,
    IssuedReceiptsManager issuedReceiptsManager,
    DynamicConfigurationManager<DynamicConfiguration> dynamicConfigurationManager,
    DynamoDbAsyncClient dynamoDbAsyncClient,
    PhoneNumberIdentifiers phoneNumberIdentifiers) {

  static CommandDependencies build(
      final String name,
      final Environment environment,
      final WhisperServerConfiguration configuration)
      throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeyException {
    Clock clock = Clock.systemUTC();

    environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    final AwsCredentialsProvider awsCredentialsProvider = configuration.getAwsCredentialsConfiguration().build();

    ScheduledExecutorService dynamicConfigurationExecutor = environment.lifecycle()
        .scheduledExecutorService(name(name, "dynamicConfiguration-%d")).threads(1).build();

    DynamicConfigurationManager<DynamicConfiguration> dynamicConfigurationManager =
        new DynamicConfigurationManager<>(
            configuration.getDynamicConfig().build(awsCredentialsProvider, dynamicConfigurationExecutor), DynamicConfiguration.class);
    dynamicConfigurationManager.start();

    final ClientResources.Builder redisClientResourcesBuilder = ClientResources.builder();

    FaultTolerantRedisClusterClient cacheCluster = configuration.getCacheClusterConfiguration()
        .build("main_cache", redisClientResourcesBuilder);
    FaultTolerantRedisClusterClient pushSchedulerCluster = configuration.getPushSchedulerCluster()
        .build("push_scheduler", redisClientResourcesBuilder);
    FaultTolerantRedisClient pubsubClient =
        configuration.getRedisPubSubConfiguration().build("pubsub", redisClientResourcesBuilder.build());

    Scheduler messageDeliveryScheduler = Schedulers.fromExecutorService(
        environment.lifecycle().executorService("messageDelivery").minThreads(4).maxThreads(4).build());
    ExecutorService messageDeletionExecutor = environment.lifecycle()
        .executorService(name(name, "messageDeletion-%d")).minThreads(4).maxThreads(4).build();
    ExecutorService secureValueRecoveryServiceExecutor = environment.lifecycle()
        .executorService(name(name, "secureValueRecoveryService-%d")).maxThreads(8).minThreads(8).build();
    ExecutorService storageServiceExecutor = environment.lifecycle()
        .executorService(name(name, "storageService-%d")).maxThreads(8).minThreads(8).build();
    ExecutorService accountLockExecutor = environment.lifecycle()
        .executorService(name(name, "accountLock-%d")).minThreads(8).maxThreads(8).build();
    ExecutorService remoteStorageHttpExecutor = environment.lifecycle()
        .executorService(name(name, "remoteStorage-%d"))
        .minThreads(0).maxThreads(Integer.MAX_VALUE).workQueue(new SynchronousQueue<>())
        .keepAliveTime(io.dropwizard.util.Duration.seconds(60L)).build();
    ExecutorService apnSenderExecutor = environment.lifecycle().executorService(name(name, "apnSender-%d"))
        .maxThreads(1).minThreads(1).build();
    ExecutorService fcmSenderExecutor = environment.lifecycle().executorService(name(name, "fcmSender-%d"))
        .maxThreads(16).minThreads(16).build();
    ExecutorService clientEventExecutor = environment.lifecycle()
        .virtualExecutorService(name(name, "clientEvent-%d"));
    ExecutorService asyncOperationQueueingExecutor = environment.lifecycle()
        .executorService(name(name, "asyncOperationQueueing-%d")).minThreads(1).maxThreads(1).build();
    ExecutorService disconnectionRequestListenerExecutor = environment.lifecycle()
        .virtualExecutorService(name(name, "disconnectionRequest-%d"));

    ScheduledExecutorService secureValueRecoveryServiceRetryExecutor = environment.lifecycle()
        .scheduledExecutorService(name(name, "secureValueRecoveryServiceRetry-%d")).threads(1).build();
    ScheduledExecutorService remoteStorageRetryExecutor = environment.lifecycle()
        .scheduledExecutorService(name(name, "remoteStorageRetry-%d")).threads(1).build();
    ScheduledExecutorService storageServiceRetryExecutor = environment.lifecycle()
        .scheduledExecutorService(name(name, "storageServiceRetry-%d")).threads(1).build();
    ScheduledExecutorService messagePollExecutor = environment.lifecycle()
        .scheduledExecutorService(name(name, "messagePollExecutor-%d")).threads(1).build();

    ExternalServiceCredentialsGenerator storageCredentialsGenerator = SecureStorageController.credentialsGenerator(
        configuration.getSecureStorageServiceConfiguration());
    ExternalServiceCredentialsGenerator secureValueRecovery2CredentialsGenerator = SecureValueRecovery2Controller.credentialsGenerator(
        configuration.getSvr2Configuration());

    final ExecutorService awsSdkMetricsExecutor = environment.lifecycle()
        .virtualExecutorService(MetricRegistry.name(CommandDependencies.class, "awsSdkMetrics-%d"));

    DynamoDbAsyncClient dynamoDbAsyncClient = configuration.getDynamoDbClientConfiguration()
        .buildAsyncClient(awsCredentialsProvider, new MicrometerAwsSdkMetricPublisher(awsSdkMetricsExecutor, "dynamoDbAsyncCommand"));

    DynamoDbClient dynamoDbClient = configuration.getDynamoDbClientConfiguration()
        .buildSyncClient(awsCredentialsProvider, new MicrometerAwsSdkMetricPublisher(awsSdkMetricsExecutor, "dynamoDbSyncCommand"));

    RegistrationRecoveryPasswords registrationRecoveryPasswords = new RegistrationRecoveryPasswords(
        configuration.getDynamoDbTables().getRegistrationRecovery().getTableName(),
        configuration.getDynamoDbTables().getRegistrationRecovery().getExpiration(),
        dynamoDbAsyncClient,
        clock);

    ClientPublicKeys clientPublicKeys =
        new ClientPublicKeys(dynamoDbAsyncClient, configuration.getDynamoDbTables().getClientPublicKeys().getTableName());

    Accounts accounts = new Accounts(
        clock,
        dynamoDbClient,
        dynamoDbAsyncClient,
        configuration.getDynamoDbTables().getAccounts().getTableName(),
        configuration.getDynamoDbTables().getAccounts().getPhoneNumberTableName(),
        configuration.getDynamoDbTables().getAccounts().getPhoneNumberIdentifierTableName(),
        configuration.getDynamoDbTables().getAccounts().getUsernamesTableName(),
        configuration.getDynamoDbTables().getDeletedAccounts().getTableName(),
        configuration.getDynamoDbTables().getAccounts().getUsedLinkDeviceTokensTableName());
    PhoneNumberIdentifiers phoneNumberIdentifiers = new PhoneNumberIdentifiers(dynamoDbAsyncClient,
        configuration.getDynamoDbTables().getPhoneNumberIdentifiers().getTableName());
    Profiles profiles = new Profiles(dynamoDbClient, dynamoDbAsyncClient,
        configuration.getDynamoDbTables().getProfiles().getTableName());
    KeysManager keys = new KeysManager(
            dynamoDbAsyncClient,
        configuration.getDynamoDbTables().getEcKeys().getTableName(),
        configuration.getDynamoDbTables().getKemKeys().getTableName(),
        configuration.getDynamoDbTables().getEcSignedPreKeys().getTableName(),
        configuration.getDynamoDbTables().getKemLastResortKeys().getTableName()
    );
    MessagesDynamoDb messagesDynamoDb = new MessagesDynamoDb(dynamoDbClient, dynamoDbAsyncClient,
        configuration.getDynamoDbTables().getMessages().getTableName(),
        configuration.getDynamoDbTables().getMessages().getExpiration(),
        messageDeletionExecutor);
    FaultTolerantRedisClusterClient messagesCluster = configuration.getMessageCacheConfiguration()
        .getRedisClusterConfiguration().build("messages", redisClientResourcesBuilder);
    FaultTolerantRedisClusterClient rateLimitersCluster = configuration.getRateLimitersCluster().build("rate_limiters",
        redisClientResourcesBuilder);
    SecureValueRecovery2Client secureValueRecovery2Client = new SecureValueRecovery2Client(
        secureValueRecovery2CredentialsGenerator, secureValueRecoveryServiceExecutor,
        secureValueRecoveryServiceRetryExecutor,
        configuration.getSvr2Configuration());
    SecureStorageClient secureStorageClient = new SecureStorageClient(storageCredentialsGenerator,
        storageServiceExecutor, storageServiceRetryExecutor, configuration.getSecureStorageServiceConfiguration());
    DisconnectionRequestManager disconnectionRequestManager = new DisconnectionRequestManager(pubsubClient, disconnectionRequestListenerExecutor);
    MessagesCache messagesCache = new MessagesCache(messagesCluster,
        messageDeliveryScheduler, messageDeletionExecutor, Clock.systemUTC());
    ProfilesManager profilesManager = new ProfilesManager(profiles, cacheCluster);
    ReportMessageDynamoDb reportMessageDynamoDb = new ReportMessageDynamoDb(dynamoDbClient, dynamoDbAsyncClient,
        configuration.getDynamoDbTables().getReportMessage().getTableName(),
        configuration.getReportMessageConfiguration().getReportTtl());
    ReportMessageManager reportMessageManager = new ReportMessageManager(reportMessageDynamoDb, rateLimitersCluster,
        configuration.getReportMessageConfiguration().getCounterTtl());
    MessagesManager messagesManager = new MessagesManager(messagesDynamoDb, messagesCache,
        reportMessageManager, messageDeletionExecutor, Clock.systemUTC());
    AccountLockManager accountLockManager = new AccountLockManager(dynamoDbClient,
        configuration.getDynamoDbTables().getDeletedAccountsLock().getTableName());
    ClientPublicKeysManager clientPublicKeysManager =
        new ClientPublicKeysManager(clientPublicKeys, accountLockManager, accountLockExecutor);
    RegistrationRecoveryPasswordsManager registrationRecoveryPasswordsManager =
        new RegistrationRecoveryPasswordsManager(registrationRecoveryPasswords);
    AccountsManager accountsManager = new AccountsManager(accounts, phoneNumberIdentifiers, cacheCluster,
        pubsubClient, accountLockManager, keys, messagesManager, profilesManager,
        secureStorageClient, secureValueRecovery2Client, disconnectionRequestManager,
        registrationRecoveryPasswordsManager, clientPublicKeysManager, accountLockExecutor, messagePollExecutor,
        clock, configuration.getLinkDeviceSecretConfiguration().secret().value(), dynamicConfigurationManager);
    RateLimiters rateLimiters = RateLimiters.createAndValidate(configuration.getLimitsConfiguration(),
        dynamicConfigurationManager, rateLimitersCluster);
    final BackupsDb backupsDb =
        new BackupsDb(dynamoDbAsyncClient, configuration.getDynamoDbTables().getBackups().getTableName(), clock);
    final GenericServerSecretParams backupsGenericZkSecretParams;
    try {
      backupsGenericZkSecretParams =
          new GenericServerSecretParams(configuration.getBackupsZkConfig().serverSecret().value());
    } catch (InvalidInputException e) {
      throw new IllegalArgumentException(e);
    }
    final BackupManager backupManager = new BackupManager(
        backupsDb,
        backupsGenericZkSecretParams,
        rateLimiters,
        new TusAttachmentGenerator(configuration.getTus()),
        new Cdn3BackupCredentialGenerator(configuration.getTus()),
        new Cdn3RemoteStorageManager(
            remoteStorageHttpExecutor,
            remoteStorageRetryExecutor,
            configuration.getCdn3StorageManagerConfiguration()),
        clock);

    final IssuedReceiptsManager issuedReceiptsManager = new IssuedReceiptsManager(
        configuration.getDynamoDbTables().getIssuedReceipts().getTableName(),
        configuration.getDynamoDbTables().getIssuedReceipts().getExpiration(),
        dynamoDbAsyncClient,
        configuration.getDynamoDbTables().getIssuedReceipts().getGenerator(),
        configuration.getDynamoDbTables().getIssuedReceipts().getmaxIssuedReceiptsPerPaymentId());

    APNSender apnSender = new APNSender(apnSenderExecutor, configuration.getApnConfiguration());
    FcmSender fcmSender = new FcmSender(fcmSenderExecutor, configuration.getFcmConfiguration().credentials().value());
    PushNotificationScheduler pushNotificationScheduler = new PushNotificationScheduler(pushSchedulerCluster,
        apnSender, fcmSender, accountsManager, 0, 0);
    PushNotificationManager pushNotificationManager =
        new PushNotificationManager(accountsManager, apnSender, fcmSender, pushNotificationScheduler);
    PushNotificationExperimentSamples pushNotificationExperimentSamples =
        new PushNotificationExperimentSamples(dynamoDbAsyncClient,
            configuration.getDynamoDbTables().getPushNotificationExperimentSamples().getTableName(),
            Clock.systemUTC());

    WebSocketConnectionEventManager webSocketConnectionEventManager =
        new WebSocketConnectionEventManager(accountsManager, pushNotificationManager, messagesCluster, clientEventExecutor, asyncOperationQueueingExecutor);

    environment.lifecycle().manage(apnSender);
    environment.lifecycle().manage(disconnectionRequestManager);
    environment.lifecycle().manage(webSocketConnectionEventManager);
    environment.lifecycle().manage(new ManagedAwsCrt());

    return new CommandDependencies(
        accountsManager,
        profilesManager,
        reportMessageManager,
        messagesCache,
        messagesManager,
        keys,
        registrationRecoveryPasswordsManager,
        apnSender,
        fcmSender,
        pushNotificationManager,
        pushNotificationExperimentSamples,
        cacheCluster,
        pushSchedulerCluster,
        redisClientResourcesBuilder,
        backupManager,
        issuedReceiptsManager,
        dynamicConfigurationManager,
        dynamoDbAsyncClient,
        phoneNumberIdentifiers
    );
  }

}
