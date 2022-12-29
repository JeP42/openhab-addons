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
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Comet Smart Meter Gateway COM-1 with IR reader to retrieve data from a power meters IR
 * interface according to IEC 62056-21
 *
 * @author pfaffmann - Initial contribution
 *
 */
public class CometCOM1IRConnector implements SmlMeterConnector {

    private static final int CONNECT_TIMEOUT_IN_MILLISECONDS = 5000;

    protected Logger logger = LoggerFactory.getLogger(CometCOM1IRConnector.class);

    private Socket socket = null;

    protected String host = "";

    protected int port = 1234;

    public CometCOM1IRConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean isDeviceReady() {
        try {
            connect();
            return true;
        } catch (IOException e) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error("IOEception while trying to connect to the gateway.", e);
            }
            return false;
        } finally {
            closeConnection();
        }
    }

    private void connect() throws UnknownHostException, IOException {
        if (this.socket == null) {
            this.socket = SocketFactory.getDefault().createSocket();
        }

        if (!this.socket.isConnected()) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Connecting to {}:{}", this.host, this.port);
            }
            this.socket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT_IN_MILLISECONDS);
        }
    }

    private void closeConnection() {
        try {
            this.logger.debug("Closing socket connection...");
            if (this.socket != null && socket.isConnected()) {
                this.socket.close();
            }
            this.socket = null;
        } catch (IOException e) {
            throw new RuntimeException("An unexpected exception occured while closing ressources.", e);
        }
    }

    @Override
    public String getRawSmlFile() throws IOException {
        try {
            connect();
            return readRawSmlFileFromSocket();
        } finally {
            closeConnection();
        }
    }

    private String readRawSmlFileFromSocket() throws IOException {
        if (this.socket == null) {
            throw new IllegalArgumentException("Socket connection not properly initialized");
        }
        InputStream ins = this.socket.getInputStream();

        boolean doRead = false;

        StringBuffer smlMessage = new StringBuffer();
        byte[] buffer = new byte[1];

        this.logger.debug("Searching for raw SML file...");
        while (ins.read(buffer) == 1) {
            if ("(".equals(new String(buffer))) {
                doRead = true;
                smlMessage = new StringBuffer();
            } else {
                if ((doRead) && (")".equals(new String(buffer)))) {
                    doRead = false;
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Read the raw SML file: {}", smlMessage.toString());
                    }
                    return smlMessage.toString();
                }
                if (doRead) {
                    smlMessage.append(new String(buffer));
                }
            }
        }
        return null;
    }
}
