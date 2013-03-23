package org.teamrocket;

import org.jhotdraw.draw.GraphicalCompositeFigure;
import org.jhotdraw.draw.RoundRectangleFigure;

public class StateFigure extends GraphicalCompositeFigure{
  
  private String _label;
  private StateControl _ctrl;
  private StateEntity _data;
  
  public StateFigure()
  {
    super(new RoundRectangleFigure());
    // TODO look at task figure
  }
  
  /*
  public void update(Observable obs, Object o)
  {
    // TODO: 
  }
  */
  
  public void addTransition(TransitionFigure tran)
  {
    // TODO: add a transition
    
  }
  
  public void removeTransition(TransitionFigure tran)
  {
    // TODO: remove a transtion
    
  }
  
  // Other GUI and JHotDraw methods
}






















