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
 * Exception class used for exceptions related to the SmlMeterConnector
 *
 * @author pfaffmann - Initial contribution
 *
 */
@NonNullByDefault
public class ConnectorException extends Exception {

    private static final long serialVersionUID = -615572953968979350L;

    public ConnectorException(String msg) {
        super(msg);
    }

    public ConnectorException(Throwable t) {
        super(t);
    }

    public ConnectorException(String msg, IOException t) {
        super(msg, t);
    }
}
