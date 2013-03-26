package org.teamrocket;

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

public class EndStateFigure extends EllipseFigure {
  public EllipseFigure circle;
  private String _label;
  private StateEntity _data;

  public EndStateFigure() {
    super();

  }

  @Override
	public Collection<Handle> createHandles(int detailLevel) {
		java.util.List<Handle> handles = new LinkedList<Handle>();

		switch (detailLevel) {
		case -1:
			handles
					.add(new BoundsOutlineHandle(this, false, true));
			break;
		case 0:
			 handles.add(new MoveHandle(this, RelativeLocator.northWest()));
			 handles.add(new MoveHandle(this, RelativeLocator.northEast()));
			 handles.add(new MoveHandle(this, RelativeLocator.southWest()));
			 handles.add(new MoveHandle(this, RelativeLocator.southEast()));
			 
			ConnectorHandle ch;
			handles.add(ch = new ConnectorHandle(new LocatorConnector(this,
					RelativeLocator.center()), new TransitionFigure()));
			ch.setToolTipText("Drag the connector to a dependent state.");
			break;
		}
		return handles;
	}
  
  public void draw(Graphics2D g) {
    set(AttributeKeys.FILL_COLOR, Color.BLACK);
    super.draw(g);
  }

  @Override
  public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
    ellipse.x = Math.min(anchor.x, lead.x);
    ellipse.y = Math.min(anchor.y, lead.y);
    ellipse.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
    ellipse.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
  }

  public StateEntity getEntity() {
    return _data;
  }

  public void addSuccessor(TransitionEntity t) {
    _data.addSuccessor(t);
  }

  public void addPredecessor(TransitionEntity t) {
    _data.addPredecessor(t);
  }

  public void removeSuccessor(TransitionEntity t) {
    _data.removeSuccessor(t);
  }

  public void removePredecessor(TransitionEntity t) {
    _data.removePredecessor(t);
  }
}