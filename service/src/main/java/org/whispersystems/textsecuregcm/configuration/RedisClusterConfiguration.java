/*
 * Copyright 2013-2020 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.whispersystems.textsecuregcm.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.annotations.VisibleForTesting;
import io.lettuce.core.resource.ClientResources;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.whispersystems.textsecuregcm.redis.FaultTolerantRedisClusterClient;

@JsonTypeName("default")
public class RedisClusterConfiguration implements FaultTolerantRedisClusterFactory {

  @JsonProperty
  @NotEmpty
  private String configurationUri;

  @JsonProperty
  @NotNull
  private Duration timeout = Duration.ofSeconds(1);

  @JsonProperty
  @NotNull
  @Valid
  private CircuitBreakerConfiguration circuitBreaker = new CircuitBreakerConfiguration();

  @JsonProperty
  @NotNull
  @Valid
  private RetryConfiguration retry = new RetryConfiguration();

  @VisibleForTesting
  void setConfigurationUri(final String configurationUri) {
    this.configurationUri = configurationUri;
  }

  public String getConfigurationUri() {
    return configurationUri;
  }

  public Duration getTimeout() {
    return timeout;
  }

  public CircuitBreakerConfiguration getCircuitBreakerConfiguration() {
    return circuitBreaker;
  }

  public RetryConfiguration getRetryConfiguration() {
    return retry;
  }

  @Override
  public FaultTolerantRedisClusterClient build(final String name, final ClientResources.Builder clientResourcesBuilder) {
    return new FaultTolerantRedisClusterClient(name, this, clientResourcesBuilder);
  }
}
