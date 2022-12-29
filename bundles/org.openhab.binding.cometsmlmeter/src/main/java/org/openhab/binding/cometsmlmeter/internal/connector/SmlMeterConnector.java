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
package org.openhab.binding.cometsmlmeter.internal.connector;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Interface of an abstract device which is capable to connect with an energy meter which pushes its values via SML
 * protocol.
 *
 * @author pfaffmann - Initial contribution
 *
 */
@NonNullByDefault
public interface SmlMeterConnector {

    /**
     * Retrieve an SML File from the connected meter and return as String
     *
     * @return
     * @throws IOException
     */
    String getRawSmlFile() throws IOException;

    /**
     * Checks connection with the connector
     *
     * @return
     */
    boolean isDeviceReady();
}
