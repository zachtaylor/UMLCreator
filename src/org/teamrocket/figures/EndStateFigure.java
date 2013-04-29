package org.teamrocket.figures;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.EllipseFigure;
import org.teamrocket.ApplicationModel;
import org.teamrocket.entities.StateEntity;

public class EndStateFigure extends StateFigure {
  public EndStateFigure() {
    super(new EllipseFigure());
  }

  public void draw(Graphics2D g) {
    set(AttributeKeys.FILL_COLOR, Color.BLACK);
    super.draw(g);
  }

  public void init() {
    _data = new StateEntity(this);
  }

  public EndStateFigure clone() {
    EndStateFigure noob = (EndStateFigure) super.superDuperClone();

    noob._data = new StateEntity(noob);
    ApplicationModel.addStateEntity(noob._data);
    return noob;
  }
}