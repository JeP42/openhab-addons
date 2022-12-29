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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Factory to create a concrete type of meter connector. The connector is capable of reading SML from an energy meter.
 * Currently, there is only one type of connector supported: COMET COM-1 gateway with IR reader.
 *
 * @author pfaffmann - Initial contribution
 *
 */
@NonNullByDefault
public class SmlMeterConnectorFactory {

    public static final String SML_METER_CONNECTOR_TYPE_COMET = "CometCom1";

    private static Map<String, SmlMeterConnector> connectors = new ConcurrentHashMap<>();

    public static SmlMeterConnector getConnector(String host, int port, String typeId) throws ConnectorException {
        String cacheKey = getCacheKey(host, port);
        SmlMeterConnector connector = connectors.get(cacheKey);
        if (connector == null) {
            connector = createConnectorFor(host, port, typeId);
            connectors.put(cacheKey, connector);
        }
        return connector;
    }

    private static SmlMeterConnector createConnectorFor(String host, int port, String typeId)
            throws ConnectorException {
        if (SML_METER_CONNECTOR_TYPE_COMET.equals(typeId)) {
            return new CometCOM1IRConnector(host, port);
        }
        throw new ConnectorException(String.format("The given connector type %s is not supported.", typeId));
    }

    private static String getCacheKey(String host, int port) {
        return host + ":" + port;
    }
}
