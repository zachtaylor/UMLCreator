package org.teamrocket;

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

public class StartStateFigure extends EllipseFigure {
  public EllipseFigure circle;
  private String _label;
  private StateEntity _data;

  public StartStateFigure() {
    super();

		ResourceBundleUtil labels = ResourceBundleUtil
				.getBundle("org.jhotdraw.samples.pert.Labels");
		
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
  
  
  @Override
  public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
    ellipse.x = Math.min(anchor.x, lead.x);
    ellipse.y = Math.min(anchor.y, lead.y);
    ellipse.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
    ellipse.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
  }
}