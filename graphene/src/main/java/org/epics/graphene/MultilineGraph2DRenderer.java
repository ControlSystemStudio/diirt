/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import static org.epics.graphene.ColorScheme.*;
import org.epics.util.array.ListNumber;
import org.epics.util.array.SortedListView;

/**
 *
 * @author asbarber 
 * @author jkfeng 
 * @author sjdallst
 */
public class MultilineGraph2DRenderer extends Graph2DRenderer<MultilineGraph2DRendererUpdate>{
    
    /**
     *Uses constructor specified in super class (Graph2DRenderer)
     * @param imageWidth should be equal to the width of the bufferedImage.
     * @param imageHeight should be equal to the height of the bufferedImage.
     */
    public MultilineGraph2DRenderer(int imageWidth, int imageHeight){
        super(imageWidth, imageHeight);       
    }
    
    @Override
    public MultilineGraph2DRendererUpdate newUpdate() {
        return new MultilineGraph2DRendererUpdate();
    }
    
    private ValueColorScheme valueColorScheme = new ValueColorSchemeBlackAndWhite();
    private ValueColorSchemeInstance valueColorSchemeInstance;  
    private InterpolationScheme interpolation = InterpolationScheme.LINEAR;
    private ReductionScheme reduction = ReductionScheme.FIRST_MAX_MIN_LAST;
    private Range datasetRange;
    /**
     *Supported interpolation schemes. 
     * Possible values:
     * <ul>
     *  <li>NEAREST_NEIGHBOR: Draws a line in steps. Starts at an initial point, draws that point's value
     * until it is halfway to the next point, draws a straight line upwards to the next point's value, then draws
     * a straight line to the next point.</li>
     *  <li>LINEAR: Draws lines from one point to the next in a linear fashion.</li>
     *  <li>CUBIC: Fits a cubic curve to the points plotted.</li>
     * </ul>
     */
    public static java.util.List<InterpolationScheme> supportedInterpolationScheme = Arrays.asList(InterpolationScheme.NEAREST_NEIGHBOUR, InterpolationScheme.LINEAR, InterpolationScheme.CUBIC);
    /**
     *Supported reduction schemes. Possible values:
     * <ul>
     *  <li>FIRST_MAX_MIN_LAST: plots only the first max min and last points within every four pixels along the x-axis. 
     *  To be used when there are many more points than pixels.</li>
     *  <li>NONE: No reduction scheme, all points are plotted.</li>
     * </ul>
     */
    public static java.util.List<ReductionScheme> supportedReductionScheme = Arrays.asList(ReductionScheme.FIRST_MAX_MIN_LAST, ReductionScheme.NONE); 
    
    /**
     *Updates private data by getting new values from update.
     * Parameter not already taken care of by the super class: valueColorScheme
     * @param update
     */
    public void update(MultilineGraph2DRendererUpdate update) {
        super.update(update);
        
        if(update.getValueColorScheme() != null){
            valueColorScheme = update.getValueColorScheme();
            valueColorSchemeInstance = valueColorScheme.createInstance(datasetRange);
        }
        if(update.getDataReduction() != null){
            reduction = update.getDataReduction();
        }
        if(update.getInterpolation() != null){
            interpolation = update.getInterpolation();
        }
    }
    
    /**
     *Draws a graph with multiple lines, each pertaining to a different set of data.
     * @param g Graphics2D object used to perform drawing functions within draw.
     * @param data can not be null
     */
    public void draw(Graphics2D g, List<Point2DDataset> data) {
        this.g = g;
        
        //Calculate range, range will end up being from the lowest point to highest in all of the given data.
        for(Point2DDataset dataPiece: data){
          super.calculateRanges(dataPiece.getXStatistics(), dataPiece.getYStatistics());  
        }
        calculateLabels();
        calculateGraphArea();
        drawBackground();
        drawGraphArea();
        
        Range datasetRangeCheck = RangeUtil.range(0,data.size());
        
        //Set color scheme
        if(valueColorSchemeInstance == null || datasetRange == null || datasetRange != datasetRangeCheck){
            datasetRange = datasetRangeCheck;
            valueColorSchemeInstance = valueColorScheme.createInstance(datasetRange);
        }
        //Draw a line for each set of data in the data array.
        for(int datasetNumber = 0; datasetNumber < data.size(); datasetNumber++){
            SortedListView xValues = org.epics.util.array.ListNumbers.sortedView(data.get(datasetNumber).getXValues());
            ListNumber yValues = org.epics.util.array.ListNumbers.sortedView(data.get(datasetNumber).getYValues(), xValues.getIndexes());        
            setClip(g);
            g.setColor(new Color(valueColorSchemeInstance.colorFor((double)datasetNumber)));
            drawValueExplicitLine(xValues, yValues, interpolation, reduction);
        }
    }
    
}
