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

/**
 * Compares values from a current and a previous meter snapshot to find out whether power is consumed or produced.
 *
 * @author pfaffmann - Initial contribution
 *
 */
public class PowerDirectionHelper {

    private MeterSnapshot previousSnapshot;

    public PowerDirection getDirectionFor(MeterSnapshot currentSnapshot) {
        PowerDirection direction = PowerDirection.UNKNOWN;

        if (previousSnapshot != null) {
            if (this.isIncreasedConsumption(currentSnapshot)) {
                direction = PowerDirection.IN;
            } else if (this.isIncreasedProduction(currentSnapshot)) {
                direction = PowerDirection.OUT;
            } else {
                direction = PowerDirection.NONE;
            }
        }
        this.previousSnapshot = currentSnapshot;
        return direction;
    }

    private boolean isIncreasedConsumption(MeterSnapshot currentSnapshot) {
        return previousSnapshot.getObis180().compareTo(currentSnapshot.getObis180()) < 0;
    }

    private boolean isIncreasedProduction(MeterSnapshot currentSnapshot) {
        return previousSnapshot.getObis280().compareTo(currentSnapshot.getObis280()) < 0;
    }
}
