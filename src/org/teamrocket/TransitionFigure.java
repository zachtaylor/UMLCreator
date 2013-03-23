package org.teamrocket;

import static org.jhotdraw.draw.AttributeKeys.END_DECORATION;
import static org.jhotdraw.draw.AttributeKeys.FONT_ITALIC;
import static org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE;
import static org.jhotdraw.draw.AttributeKeys.START_DECORATION;
import static org.jhotdraw.draw.AttributeKeys.STROKE_COLOR;
import static org.jhotdraw.draw.AttributeKeys.STROKE_DASHES;
import static org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH;

import java.awt.Color;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.LabeledLineConnectionFigure;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.decoration.ArrowTip;
import org.jhotdraw.samples.pert.figures.DependencyFigure;
import org.jhotdraw.samples.pert.figures.TaskFigure;

// May implement Observer?
public class TransitionFigure extends LabeledLineConnectionFigure {
  private String _label;
  private TransitionEntity _data;
  
  public TransitionFigure()
  {
  // TODO:  
    set(STROKE_COLOR, new Color(0x000099));
    set(STROKE_WIDTH, 1d);
    set(END_DECORATION, new ArrowTip());

    setAttributeEnabled(END_DECORATION, false);
    setAttributeEnabled(START_DECORATION, false);
    setAttributeEnabled(STROKE_DASHES, false);
    setAttributeEnabled(FONT_ITALIC, false);
    setAttributeEnabled(FONT_UNDERLINE, false);
  }
  
  @Override
  public boolean canConnect(Connector start, Connector end) {
      if ((start.getOwner() instanceof StateFigure)
              && (end.getOwner() instanceof StateFigure)) {

         StateFigure sf = (StateFigure) start.getOwner();
          StateFigure ef = (StateFigure) end.getOwner();
            
          if (ef.getPredecessors().contains(sf)) {
            return false;
        }
          
      }  

      return false;
  }
  
  @Override
  public boolean canConnect(Connector start) {
      return (start.getOwner() instanceof StateFigure);
  }

  @Override
  protected void handleDisconnect(Connector start, Connector end) {
      StateFigure sf = (StateFigure) start.getOwner();
      StateFigure ef = (StateFigure) end.getOwner();

      sf.removeTransition(this);
      ef.removeTransition(this);
  }
  
  @Override
  protected void handleConnect(Connector start, Connector end) {
      StateFigure sf = (StateFigure) start.getOwner();
      StateFigure ef = (StateFigure) end.getOwner();

      sf.addTransition(this);
      ef.addTransition(this);
  }

  @Override
  public TransitionFigure clone() {
      TransitionFigure that = (TransitionFigure) super.clone();

      return that;
  }

  @Override
  public int getLayer() {
      return 1;
  }

  @Override
  public void removeNotify(Drawing d) {
      if (getStartFigure() != null) {
          ((StateFigure) getStartFigure()).removeTransition(this);      
          }
      if (getEndFigure() != null) {
          ((StateFigure) getEndFigure()).removeTransition(this);
      }
      super.removeNotify(d);
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
