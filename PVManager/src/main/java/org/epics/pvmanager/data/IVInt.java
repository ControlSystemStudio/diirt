/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.text.NumberFormat;
import org.epics.util.time.Timestamp;

/**
 * Immutable VInt implementation.
 *
 * @author carcassi
 */
class IVInt extends IVNumeric implements VInt {
    
    private final Integer value;

    IVInt(Integer value, Alarm alarm, Time time, Display display) {
        super(alarm, time, display);
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
