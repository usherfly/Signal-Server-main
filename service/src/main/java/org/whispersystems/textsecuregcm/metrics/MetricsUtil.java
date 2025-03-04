/*
 * Copyright 2021 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.whispersystems.textsecuregcm.metrics;

import com.codahale.metrics.SharedMetricRegistries;
import com.google.common.annotations.VisibleForTesting;
import io.dropwizard.core.setup.Environment;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.statsd.StatsdMeterRegistry;
import org.whispersystems.textsecuregcm.WhisperServerConfiguration;
import org.whispersystems.textsecuregcm.WhisperServerVersion;
import org.whispersystems.textsecuregcm.configuration.dynamic.DynamicConfiguration;
import org.whispersystems.textsecuregcm.storage.DynamicConfigurationManager;
import org.whispersystems.textsecuregcm.util.Constants;

public class MetricsUtil {

  public static final String PREFIX = "chat";

  private static volatile boolean registeredMetrics = false;

  /**
   * Returns a dot-separated ('.') name for the given class and name parts
   */
  public static String name(Class<?> clazz, String... parts) {
    return name(clazz.getSimpleName(), parts);
  }

  private static String name(String name, String... parts) {
    final StringBuilder sb = new StringBuilder(PREFIX);
    sb.append(".").append(name);
    for (String part : parts) {
      sb.append(".").append(part);
    }
    return sb.toString();
  }

  public static void configureRegistries(final WhisperServerConfiguration config, final Environment environment,
      DynamicConfigurationManager<DynamicConfiguration> dynamicConfigurationManager) {

    if (registeredMetrics) {
      throw new IllegalStateException("Metric registries configured more than once");
    }

    registeredMetrics = true;

    SharedMetricRegistries.add(Constants.METRICS_NAME, environment.metrics());

    {
      final StatsdMeterRegistry dogstatsdMeterRegistry = new StatsdMeterRegistry(
          config.getDatadogConfiguration(), io.micrometer.core.instrument.Clock.SYSTEM);

      dogstatsdMeterRegistry.config().commonTags(
          Tags.of(
              "service", "chat",
              "version", WhisperServerVersion.getServerVersion(),
              "env", config.getDatadogConfiguration().getEnvironment()));

      configureMeterFilters(dogstatsdMeterRegistry.config(), dynamicConfigurationManager);
      Metrics.addRegistry(dogstatsdMeterRegistry);
    }

    environment.lifecycle().addEventListener(new ApplicationShutdownMonitor(Metrics.globalRegistry));
    environment.lifecycle().addEventListener(
        new MicrometerRegistryManager(Metrics.globalRegistry,
            config.getDatadogConfiguration().getShutdownWaitDuration()));
  }

  @VisibleForTesting
  static MeterRegistry.Config configureMeterFilters(MeterRegistry.Config config,
      final DynamicConfigurationManager<DynamicConfiguration> dynamicConfigurationManager) {
    final DistributionStatisticConfig defaultDistributionStatisticConfig = DistributionStatisticConfig.builder()
        .percentiles(.75, .95, .99, .999)
        .build();

    final String awsSdkMetricNamePrefix = MetricsUtil.name(MicrometerAwsSdkMetricPublisher.class);

    return config
        .meterFilter(new MeterFilter() {
          @Override
          public DistributionStatisticConfig configure(final Meter.Id id, final DistributionStatisticConfig config) {
            return defaultDistributionStatisticConfig.merge(config);
          }
        })
        // Remove high-cardinality `command` tags from Lettuce metrics and prepend "chat." to meter names
        .meterFilter(new MeterFilter() {
          @Override
          public Meter.Id map(final Meter.Id id) {
            if (id.getName().startsWith("lettuce")) {
              return id.withName(PREFIX + "." + id.getName())
                  .replaceTags(id.getTags().stream()
                      .filter(tag -> !"command".equals(tag.getKey()))
                      .filter(tag -> dynamicConfigurationManager.getConfiguration().getMetricsConfiguration().
                          enableLettuceRemoteTag() || !"remote".equals(tag.getKey()))
                      .toList());
            }

            return MeterFilter.super.map(id);
          }
        })
        .meterFilter(MeterFilter.denyNameStartsWith(MessageMetrics.DELIVERY_LATENCY_TIMER_NAME + ".percentile"))
        .meterFilter(MeterFilter.deny(id -> !dynamicConfigurationManager.getConfiguration().getMetricsConfiguration().enableAwsSdkMetrics()
            && id.getName().startsWith(awsSdkMetricNamePrefix)));
  }

  public static void registerSystemResourceMetrics(final Environment environment) {
    new ProcessorMetrics().bindTo(Metrics.globalRegistry);
    new FreeMemoryGauge().bindTo(Metrics.globalRegistry);
    new FileDescriptorMetrics().bindTo(Metrics.globalRegistry);
    new OperatingSystemMemoryGauge("Buffers").bindTo(Metrics.globalRegistry);
    new OperatingSystemMemoryGauge("Cached").bindTo(Metrics.globalRegistry);

    new JvmMemoryMetrics().bindTo(Metrics.globalRegistry);
    new JvmThreadMetrics().bindTo(Metrics.globalRegistry);

    GarbageCollectionGauges.registerMetrics();
  }

}
