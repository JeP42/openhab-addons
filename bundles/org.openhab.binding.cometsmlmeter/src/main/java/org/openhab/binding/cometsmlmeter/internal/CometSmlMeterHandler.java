/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.cometsmlmeter.internal;

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.cometsmlmeter.internal.connector.ConnectorException;
import org.openhab.binding.cometsmlmeter.internal.connector.MeterSnapshot;
import org.openhab.binding.cometsmlmeter.internal.connector.PowerDirectionHelper;
import org.openhab.binding.cometsmlmeter.internal.connector.SmlMeterConnector;
import org.openhab.binding.cometsmlmeter.internal.connector.SmlMeterConnectorFactory;
import org.openhab.binding.cometsmlmeter.internal.connector.SmlReader;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link CometSmlMeterHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Jens Pfaffmann - Initial contribution
 */
@NonNullByDefault
public class CometSmlMeterHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(CometSmlMeterHandler.class);

    private @Nullable CometSmlMeterConfiguration config;

    @Nullable
    ScheduledFuture<?> refreshJob;

    PowerDirectionHelper powerDirectionHelper = new PowerDirectionHelper();

    public CometSmlMeterHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    @Override
    public void initialize() {
        // config = getConfigAs(CometSmlMeterConfiguration.class);
        //
        // // TODO: Initialize the handler.
        // // The framework requires you to return from this method quickly, i.e. any network access must be done in
        // // the background initialization below.
        // // Also, before leaving this method a thing status from one of ONLINE, OFFLINE or UNKNOWN must be set. This
        // // might already be the real thing status in case you can decide it directly.
        // // In case you can not decide the thing status directly (e.g. for long running connection handshake using WAN
        // // access or similar) you should set status UNKNOWN here and then decide the real status asynchronously in
        // the
        // // background.
        //
        // // set the thing status to UNKNOWN temporarily and let the background task decide for the real status.
        // // the framework is then able to reuse the resources from the thing handler initialization.
        // // we set this upfront to reliably check status updates in unit tests.
        // updateStatus(ThingStatus.UNKNOWN);
        //
        // // Example for background initialization:
        // scheduler.execute(() -> {
        // boolean thingReachable = true; // <background task with long running initialization here>
        // // when done do:
        // if (thingReachable) {
        // updateStatus(ThingStatus.ONLINE);
        // } else {
        // updateStatus(ThingStatus.OFFLINE);
        // }
        // });
        //
        // // These logging types should be primarily used by bindings
        // // logger.trace("Example trace message");
        // // logger.debug("Example debug message");
        // // logger.warn("Example warn message");
        // //
        // // Logging to INFO should be avoided normally.
        // // See https://www.openhab.org/docs/developer/guidelines.html#f-logging
        //
        // // Note: When initialization can NOT be done set the status with more details for further
        // // analysis. See also class ThingStatusDetail for all available status details.
        // // Add a description to give user information to understand why thing does not work as expected. E.g.
        // // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // // "Can not access device as username and/or password are invalid");

        logger.debug("Start initializing!");

        config = getConfigAs(CometSmlMeterConfiguration.class);
        updateStatus(ThingStatus.UNKNOWN);

        scheduler.execute(() -> {
            SmlMeterConnector connector;
            try {
                connector = SmlMeterConnectorFactory.getConnector(config.host, config.port, config.type);

                if (connector.isDeviceReady()) {
                    updateStatus(ThingStatus.ONLINE);
                    this.startRefreshJob();
                } else {
                    updateStatus(ThingStatus.OFFLINE);
                }
            } catch (ConnectorException e) {
                if (this.logger.isErrorEnabled()) {
                    this.logger.error("An exception occured during initialization of the SMLMeterConnector", e);
                }
                updateStatus(ThingStatus.OFFLINE);
            }
        });

        logger.debug("Finished initializing!");
    }

    private void startRefreshJob() {
        this.refreshJob = this.scheduler.scheduleAtFixedRate(() -> {
            update();
        }, config.refreshInterval, config.refreshInterval, TimeUnit.SECONDS);
    }

    private void update() {
        try {
            SmlMeterConnector connector = SmlMeterConnectorFactory.getConnector(config.host, config.port, config.type);

            // we get an SML file as String from the connector and pass it to the SML reader.
            // The SML reader parses the string and retrieves values which are then used to update state of the channels
            MeterSnapshot snapshot = new SmlReader().read(connector.getRawSmlFile());
            // when internal crc check fails null is returned... This happens from time to time, we can just ignore
            // it...
            if (snapshot != null) {
                updateStatus(ThingStatus.ONLINE);

                this.updateState(CometSmlMeterBindingConstants.CHANNEL_POWER_DIRECTION,
                        new StringType(powerDirectionHelper.getDirectionFor(snapshot).toString()));

                this.updateState(CometSmlMeterBindingConstants.CHANNEL_OBIS_1_8_0,
                        new DecimalType(snapshot.getObis180()));
                this.updateState(CometSmlMeterBindingConstants.CHANNEL_OBIS_1_8_1,
                        new DecimalType(snapshot.getObis181()));

                this.updateState(CometSmlMeterBindingConstants.CHANNEL_OBIS_2_8_0,
                        new DecimalType(snapshot.getObis280()));
                this.updateState(CometSmlMeterBindingConstants.CHANNEL_OBIS_2_8_1,
                        new DecimalType(snapshot.getObis281()));

                this.updateState(CometSmlMeterBindingConstants.CHANNEL_OBIS_15_7_0,
                        new DecimalType(snapshot.getObis1570()));

                this.updateState(CometSmlMeterBindingConstants.CHANNEL_VENDOR, new StringType(snapshot.getVendorId()));
                this.updateState(CometSmlMeterBindingConstants.CHANNEL_DEVICE_ID,
                        new StringType(snapshot.getDeviceId()));
            }

        } catch (ConnectorException e) {
            // this situation is unrecoverable, set thing OFFLINE and give up...
            this.logger.error("An unrecoverable error occurred during execution: {}", e.getMessage(), e);
            this.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR, e.getMessage());
            this.refreshJob.cancel(false);
        } catch (IOException e) {
            // this may be a temporary problem... log the message and continue...
            this.logger.warn("An error occured during execution, will try again. Message was: {}", e.getMessage());
        }
    }
}
