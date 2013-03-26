package org.teamrocket;

import static org.jhotdraw.draw.AttributeKeys.END_DECORATION;
import static org.jhotdraw.draw.AttributeKeys.FONT_ITALIC;
import static org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE;
import static org.jhotdraw.draw.AttributeKeys.START_DECORATION;
import static org.jhotdraw.draw.AttributeKeys.STROKE_COLOR;
import static org.jhotdraw.draw.AttributeKeys.STROKE_DASHES;
import static org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH;

import java.awt.Color;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LabeledLineConnectionFigure;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.decoration.ArrowTip;
import org.jhotdraw.util.ResourceBundleUtil;

// May implement Observer?
public class TransitionFigure extends LabeledLineConnectionFigure {
  private String _label;
  private TransitionEntity _data;

  public TransitionFigure() {
    // TODO:
    _data = new TransitionEntity();
    _label = "Trigger # Action";

    set(STROKE_COLOR, new Color(0x000099));
    set(STROKE_WIDTH, 1d);
    set(END_DECORATION, new ArrowTip());

    setAttributeEnabled(END_DECORATION, false);
    setAttributeEnabled(START_DECORATION, false);
    setAttributeEnabled(STROKE_DASHES, false);
    setAttributeEnabled(FONT_ITALIC, false);
    setAttributeEnabled(FONT_UNDERLINE, false);

    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.pert.Labels");
  }

  @Override
  public boolean canConnect(Connector start, Connector end) {
    if ((start.getOwner() instanceof StateFigure)
        && (end.getOwner() instanceof StateFigure)) {

      StateFigure sf = (StateFigure) start.getOwner();
      StateFigure ef = (StateFigure) end.getOwner();

      _data.setPrev(sf.getEntity());
      _data.setNext(ef.getEntity());
      return true;
    }
    else if ((start.getOwner() instanceof StartStateFigure)
        && (end.getOwner() instanceof StateFigure)) {

      StartStateFigure sf = (StartStateFigure) start.getOwner();
      StateFigure ef = (StateFigure) end.getOwner();

      _data = new TransitionEntity();
      _data.setPrev(sf.getEntity());
      _data.setNext(ef.getEntity());
      return true;
    }
    else if ((start.getOwner() instanceof StateFigure)
        && (end.getOwner() instanceof EndStateFigure)) {

      StateFigure sf = (StateFigure) start.getOwner();
      EndStateFigure ef = (EndStateFigure) end.getOwner();

      _data = new TransitionEntity();
      _data.setPrev(sf.getEntity());
      _data.setNext(ef.getEntity());
      return true;
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

  }

  @Override
  protected void handleConnect(Connector start, Connector end) {
    if (start instanceof StateFigure)
      ((StateFigure) start.getOwner()).addSuccessor(_data);
    else if (start instanceof StartStateFigure)
      ((StartStateFigure) start.getOwner()).addSuccessor(_data);
    else if (start instanceof EndStateFigure)
      ((EndStateFigure) start.getOwner()).addSuccessor(_data);
    if (end instanceof StateFigure)
      ((StateFigure) start.getOwner()).addSuccessor(_data);
    else if (end instanceof StartStateFigure)
      ((StartStateFigure) start.getOwner()).addSuccessor(_data);
    else if (end instanceof EndStateFigure)
      ((EndStateFigure) end.getOwner()).addSuccessor(_data);

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

  public void setLabel(String s) {
    _label = s;
  }

  public String getLabel() {
    return _label;
  }

  public void setData(TransitionEntity tran) {
    _data = tran;
  }

  public TransitionEntity getData() {
    return _data;
  }

  // TODO: Other GUI and JHotDraw methods
  //

}
