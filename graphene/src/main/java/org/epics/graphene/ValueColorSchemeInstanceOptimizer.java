/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

/**
 *
 * @author sjdallst
 */
public class ValueColorSchemeInstanceOptimizer {
    public ValueColorSchemeInstance optimize(ValueColorSchemeInstance instance, Range range){
        return new ValueColorSchemeInstanceOptimized(instance, range);
    }
    
    public ValueColorSchemeInstance optimize(ValueColorSchemeInstance instance, Range oldRange, Range newRange){
        return new ValueColorSchemeInstanceOptimized(instance, oldRange, newRange);
    }
    private class ValueColorSchemeInstanceOptimized implements ValueColorSchemeInstance{
        private int arrayLength = 1000;
        private int[] colors = new int[arrayLength];
        private int nanColor;
        private Range range;
        private double max, min, total;
        
        ValueColorSchemeInstanceOptimized(ValueColorSchemeInstance instance, Range range){
            min = range.getMinimum().doubleValue();
            max = range.getMaximum().doubleValue();
            total = max-min;
            for(int i = 0; i < arrayLength; i++){
                //account for possible rounding errors on last entry.
                if(i == arrayLength - 1){
                    colors[i] = instance.colorFor(max);
                }
                else{
                    colors[i] = instance.colorFor(min + i*(total/((double)(arrayLength-1))));
                }
            }
            this.range = range;
        }
        
        ValueColorSchemeInstanceOptimized(ValueColorSchemeInstance instance, Range oldRange, Range newRange){
            double oldMin = oldRange.getMinimum().doubleValue();
            double oldMax = oldRange.getMaximum().doubleValue();
            double oldTotal = oldMax-oldMin;
            for(int i = 0; i < arrayLength; i++){
                //account for possible rounding errors on last entry.
                if(i == arrayLength - 1){
                    colors[i] = instance.colorFor(oldMax);
                }
                else{
                    colors[i] = instance.colorFor(oldMin + i*(oldTotal/((double)(arrayLength-1))));
                }
            }
            min = newRange.getMinimum().doubleValue();
            max = newRange.getMaximum().doubleValue();
            total = max-min;
            this.range = newRange;
        }
        
        @Override
        public int colorFor(double value){
            int index = (int)((value - min)/total * arrayLength);
            return colors[index];
        }
    }
}
