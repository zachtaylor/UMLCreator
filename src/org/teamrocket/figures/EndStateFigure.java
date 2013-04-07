package org.teamrocket.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.draw.connector.LocatorConnector;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.ConnectorHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.MoveHandle;
import org.jhotdraw.draw.locator.RelativeLocator;
import org.teamrocket.entities.AbstractEntity;
import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;

public class EndStateFigure extends StateFigure {
  public EndStateFigure() {
    super(new EllipseFigure());
  }
  
  public void draw(Graphics2D g) {
    set(AttributeKeys.FILL_COLOR, Color.BLACK);
    super.draw(g);
  }
}