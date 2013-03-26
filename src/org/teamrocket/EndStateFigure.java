package org.teamrocket;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.EllipseFigure;

public class EndStateFigure extends EllipseFigure {
  public EllipseFigure circle;
  private String _label;
  private StateEntity _data;

  public EndStateFigure() {
    super();

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
}