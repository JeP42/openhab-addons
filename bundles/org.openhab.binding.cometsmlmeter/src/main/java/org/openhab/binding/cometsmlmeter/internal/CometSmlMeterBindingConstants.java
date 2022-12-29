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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link CometSmlMeterBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Jens Pfaffmann - Initial contribution
 */
@NonNullByDefault
public class CometSmlMeterBindingConstants {

    private static final String BINDING_ID = "cometsmlmeter";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_COMETSMLMETER = new ThingTypeUID(BINDING_ID, "cometsmlmeter");

    // List of all Channel ids
    public static final String CHANNEL_OBIS_1_8_0 = "180";

    public static final String CHANNEL_OBIS_1_8_1 = "181";

    public static final String CHANNEL_OBIS_2_8_0 = "280";

    public static final String CHANNEL_OBIS_2_8_1 = "281";

    public static final String CHANNEL_OBIS_15_7_0 = "1570";

    public static final String CHANNEL_VENDOR = "vendor";

    public static final String CHANNEL_DEVICE_ID = "deviceid";

    public static final String CHANNEL_POWER_DIRECTION = "powerdirection";
}
