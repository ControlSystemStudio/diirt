/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.epics.util.array.IteratorDouble;
import org.epics.util.array.IteratorNumber;

/**
 *
 * @author carcassi
 */
public class Iterators {
    /**
     *Creates a pointer for the given data at the given starting offset.
     * Can not point past the given end offset.
     * @param data double[] 
     * @param startOffset current index of the new pointer.
     * @param endOffset last index that the pointer will be able to point to.
     * @return IteratorDouble, pointer to the data at the index of startOffset.
     */
    public static IteratorDouble arrayIterator(final double[] data, final int startOffset, final int endOffset) {
        return new IteratorDouble() {
            
            int index = startOffset;

            @Override
            public boolean hasNext() {
                return index != endOffset;
            }

            @Override
            public double nextDouble() {
                if (!hasNext())
                    throw new NoSuchElementException();
                double value = data[index];
                index++;
                if (index == data.length)
                    index = 0;
                return value;
            }
        };
    }
    
    /**
     *Creates a pointer for the given data at index 0.
     * @param data double[]
     * @return IteratorDouble, pointer to the data at index 0.
     */
    public static IteratorDouble arrayIterator(final double[] data) {
        return new IteratorDouble() {
            
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < data.length;
            }

            @Override
            public double nextDouble() {
                if (!hasNext())
                    throw new NoSuchElementException();
                double value = data[index];
                index++;
                return value;
            }
        };
    }
    
    /**
     *Takes a list of IteratorDoubles and combines them into one IteratorDouble.
     * @param iterators
     * @return IteratorDouble
     */
    public static IteratorDouble combine(final Collection<IteratorNumber> iterators) {
        return new IteratorDouble() {
            
            private IteratorNumber currentIterator = null;
            private Iterator<IteratorNumber> iterator = iterators.iterator();

            @Override
            public boolean hasNext() {
                if (currentIterator != null && currentIterator.hasNext()) {
                    return true;
                }
                
                if (iterator.hasNext()) {
                    currentIterator = iterator.next();
                    return hasNext();
                }
                
                return false;
            }

            @Override
            public double nextDouble() {
                return currentIterator.nextDouble();
            }
        };
    }
}
