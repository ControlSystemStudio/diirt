/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.formula;

import static org.epics.vtype.ValueFactory.alarmNone;
import static org.epics.vtype.ValueFactory.newTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.epics.util.time.Timestamp;
import org.epics.vtype.Alarm;
import org.epics.vtype.Time;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.ValueFactory;
import org.epics.vtype.ValueUtil;

/**
 * @author shroffk
 *
 */
class ArrayOfStringFormulaFunction implements FormulaFunction {

    @Override
    public boolean isPure() {
        return true;
    }

    @Override
    public boolean isVarArgs() {
        return true;
    }

    @Override
    public String getName() {
        return "arrayOf";
    }

    @Override
    public String getDescription() {
        return "Constructs array from a series of string";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>> asList(VString.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("strArgs");
    }

    @Override
    public Class<?> getReturnType() {
        return VStringArray.class;
    }

    @Override
    public Object calculate(List<Object> args) {
        // Get highest alarm; null should appear as disconnected
        Alarm alarm = ValueUtil.highestSeverityOf(args, true);
        
        // Get latest time or now
        Time time = ValueUtil.latestTimeOf(args);
        if (time == null) {
            time = ValueFactory.timeNow();
        }
        
        List<String> data = new ArrayList<>();
        for (Object arg : args) {
            VString str = (VString) arg;
            if (str == null || str.getValue() == null)
                data.add(null);
            else
                data.add(str.getValue());
        }

        return ValueFactory.newVStringArray(data, alarm,
                time);
    }

}
