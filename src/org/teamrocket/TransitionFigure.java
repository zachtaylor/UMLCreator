package org.teamrocket;

import org.jhotdraw.draw.LabeledLineConnectionFigure;

// May implement Observer?
public class TransitionFigure extends LabeledLineConnectionFigure {
  private String _label;
  private TransitionControl _ctrl;
  private TransitionEntity _data;
  
  public TransitionFigure()
  {
  // TODO:  
  }
  
  public void setLabel(String s)
  {
    _label = s;
  }
  
  public String getLabel()
  {
    return _label;
  }
  
  public void setData(TransitionEntity tran)
  {
    _data = tran;
  }
  
  public TransitionEntity getData()
  {
    return _data;
  }
  
  // TODO:  Other GUI and JHotDraw methods
  //
  
  
}
