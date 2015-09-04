/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

import org.diirt.util.array.ListFloat;

/**
 * Scalar Float array with alarm, timestamp, display and control information.
 *
 * @author carcassi
 */
public abstract class VFloatArray extends VNumberArray {
    
	/**
     * {@inheritDoc }
     */
    @Override
    public abstract ListFloat getData();
    
    /**
     * Creates a new VFloatArray.
     * 
     * @param data the value
     * @param alarm the alarm
     * @param time the time
     * @param display the display
     * @return the new value
     */
    public static VFloatArray create(final ListFloat data, final Alarm alarm, final Time time, final Display display) {
        return new IVFloatArray(data, null, alarm, time, display);
    }
}
