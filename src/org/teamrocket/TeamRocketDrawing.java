package org.teamrocket;

import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.Figure;
import org.teamrocket.figures.StateFigure;

public class TeamRocketDrawing extends DefaultDrawing {
  public boolean remove(Figure figure) {
    if (figure instanceof StateFigure) {
      ApplicationModel.getStateEntityBucket().remove(((StateFigure) figure).getEntity());
    }
    return super.remove(figure);
  }
}