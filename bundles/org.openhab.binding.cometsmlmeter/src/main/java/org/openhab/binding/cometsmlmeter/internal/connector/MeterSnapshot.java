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

import java.math.BigDecimal;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 *
 * Represents a snapshot of the meter values at a particular time
 *
 * @author pfaffmann - Initial contribution
 *
 */
@NonNullByDefault
public class MeterSnapshot {

    private long time = 0;

    private String vendorId = "";
    private String deviceId = "";

    private BigDecimal obis180 = BigDecimal.ZERO;
    private BigDecimal obis181 = BigDecimal.ZERO;
    private BigDecimal obis280 = BigDecimal.ZERO;
    private BigDecimal obis281 = BigDecimal.ZERO;
    private BigDecimal obis1570 = BigDecimal.ZERO;

    public MeterSnapshot() {
        super();
        this.time = System.currentTimeMillis();
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public BigDecimal getObis180() {
        return obis180;
    }

    public void setObis180(BigDecimal obis180) {
        this.obis180 = obis180;
    }

    public BigDecimal getObis181() {
        return obis181;
    }

    public void setObis181(BigDecimal obis181) {
        this.obis181 = obis181;
    }

    public BigDecimal getObis280() {
        return obis280;
    }

    public void setObis280(BigDecimal obis280) {
        this.obis280 = obis280;
    }

    public BigDecimal getObis281() {
        return obis281;
    }

    public void setObis281(BigDecimal obis281) {
        this.obis281 = obis281;
    }

    public BigDecimal getObis1570() {
        return obis1570;
    }

    public void setObis1570(BigDecimal obis1570) {
        this.obis1570 = obis1570;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
