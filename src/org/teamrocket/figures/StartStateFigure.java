package org.teamrocket.figures;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;

import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.draw.connector.LocatorConnector;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.ConnectorHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.MoveHandle;
import org.jhotdraw.draw.locator.RelativeLocator;
import org.jhotdraw.util.ResourceBundleUtil;
import org.teamrocket.entities.AbstractEntity;
import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;

public class StartStateFigure extends StateFigure {
  public StartStateFigure() {
    super(new EllipseFigure());
  }
/*
  @Override
  public Collection<Handle> createHandles(int detailLevel) {
    java.util.List<Handle> handles = new LinkedList<Handle>();

    switch (detailLevel) {
    case -1:
      handles.add(new BoundsOutlineHandle(this, false, true));
    break;
    case 0:
      handles.add(new MoveHandle(this, RelativeLocator.northWest()));
      handles.add(new MoveHandle(this, RelativeLocator.northEast()));
      handles.add(new MoveHandle(this, RelativeLocator.southWest()));
      handles.add(new MoveHandle(this, RelativeLocator.southEast()));

      ConnectorHandle ch;
      handles.add(ch = new ConnectorHandle(new LocatorConnector(this, RelativeLocator.center()), new TransitionFigure()));
      ch.setToolTipText("Drag the connector to a dependent state.");
    break;
    }
    return handles;
  }*/
}