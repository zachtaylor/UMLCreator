/*
 * @(#)PertFactory.java
 *
 * Copyright (c) 1996-2010 by the original authors of JHotDraw and all its
 * contributors. All rights reserved.
 *
 * You may not use, copy or modify this file, except in compliance with the 
 * license agreement you entered into with the copyright holders. For details
 * see accompanying license terms.
 */

package org.jhotdraw.samples.pert;

import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.GroupFigure;
import org.jhotdraw.draw.ListFigure;
import org.jhotdraw.draw.TextAreaFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.connector.ChopEllipseConnector;
import org.jhotdraw.draw.connector.ChopRectangleConnector;
import org.jhotdraw.draw.connector.LocatorConnector;
import org.jhotdraw.draw.decoration.ArrowTip;
import org.jhotdraw.draw.locator.RelativeLocator;
import org.jhotdraw.samples.pert.figures.DependencyFigure;
import org.jhotdraw.samples.pert.figures.SeparatorLineFigure;
import org.jhotdraw.samples.pert.figures.TaskFigure;
import org.jhotdraw.xml.DefaultDOMFactory;
import org.teamrocket.EndStateFigure;
import org.teamrocket.StartStateFigure;
import org.teamrocket.StateFigure;
import org.teamrocket.TransitionFigure;
/**
 * PertFactory.
 * 
 * @author Werner Randelshofer
 * @version $Id: PertFactory.java 717 2010-11-21 12:30:57Z rawcoder $
 */
public class PertFactory extends DefaultDOMFactory {
    private final static Object[][] classTagArray = {
        { DefaultDrawing.class, "PertDiagram" },
        { TaskFigure.class, "task" },
        { DependencyFigure.class, "dep" },
        { ListFigure.class, "list" },
        { TextFigure.class, "text" },
        { GroupFigure.class, "g" },
        { TextAreaFigure.class, "ta" },
        { SeparatorLineFigure.class, "separator" },
        
        { StateFigure.class, "state" },
        { StartStateFigure.class, "startState" },
        { EndStateFigure.class, "endState" },
        { TransitionFigure.class, "tran" },
        
        { ChopEllipseConnector.class, "chopEllipseConnector" },
        { ChopRectangleConnector.class, "rectConnector" },
        { LocatorConnector.class, "locConnector" },
        { RelativeLocator.class, "relativeLocator" },
        { ArrowTip.class, "arrowTip" }
    };
    
    /** Creates a new instance. */
    public PertFactory() {
        for (Object[] o : classTagArray) {
            addStorableClass((String) o[1], (Class) o[0]);
        }
    }
}
