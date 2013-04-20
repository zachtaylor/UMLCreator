package org.teamrocket.figures;

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
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.decoration.ArrowTip;
import org.jhotdraw.draw.layouter.LocatorLayouter;
import org.jhotdraw.draw.liner.CurvedLiner;
import org.jhotdraw.draw.locator.RelativeLocator;
import org.jhotdraw.util.ResourceBundleUtil;
import org.teamrocket.entities.TransitionEntity;

// May implement Observer?
public class TransitionFigure extends LabeledLineConnectionFigure {
  private String _label;
  private TransitionEntity _data;
  private TextFigure _labelFigure;
  
  public TransitionFigure() {
    // TODO:
    _data = new TransitionEntity();
    _label = "Trigger # Action";

    set(STROKE_COLOR, new Color(0x000099));
    set(STROKE_WIDTH, 1d);
    set(END_DECORATION, new ArrowTip());
    setLayouter(new LocatorLayouter());
    setAttributeEnabled(END_DECORATION, false);
    setAttributeEnabled(START_DECORATION, false);
    setAttributeEnabled(STROKE_DASHES, false);
    setAttributeEnabled(FONT_ITALIC, false);
    setAttributeEnabled(FONT_UNDERLINE, false);

    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.pert.Labels");
  }

  @Override
  public boolean canConnect(Connector start, Connector end) {
    final Figure startState = start.getOwner();
    final Figure endState = end.getOwner();
    
    if (!(startState instanceof StateFigure) ||
    		!(endState instanceof StateFigure))
    		return false;
    
    return !(startState instanceof EndStateFigure) && 
    		!(endState instanceof StartStateFigure) &&
    		(start != null) && (end != null);
  }

  @Override
  public boolean canConnect(Connector start) {
    return (start.getOwner() instanceof StateFigure || start.getOwner() instanceof StartStateFigure);
  }

  @Override
  protected void handleDisconnect(Connector start, Connector end) {
    if (start instanceof StateFigure)
      ((StateFigure) start.getOwner()).removeSuccessor(_data);
    else if (start instanceof StartStateFigure)
      ((StartStateFigure) start.getOwner()).removeSuccessor(_data);
    else if (start instanceof EndStateFigure)
      ((EndStateFigure) start.getOwner()).removeSuccessor(_data);
    if (end instanceof StateFigure)
      ((StateFigure) start.getOwner()).removePredecessor(_data);
    else if (end instanceof StartStateFigure)
      ((StartStateFigure) start.getOwner()).removePredecessor(_data);
    else if (end instanceof EndStateFigure)
      ((EndStateFigure) end.getOwner()).removePredecessor(_data);
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
      ((StateFigure) start.getOwner()).addPredecessor(_data);
    else if (end instanceof StartStateFigure)
      ((StartStateFigure) start.getOwner()).addPredecessor(_data);
    else if (end instanceof EndStateFigure)
      ((EndStateFigure) end.getOwner()).addPredecessor(_data);

    if (start.getOwner() == end.getOwner())
      setLiner(new CurvedLiner());
    
    setLabel("[label]");
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
   
  	willChange();
  	this.remove(_labelFigure);
  	_labelFigure = new TextFigure(_label);
  	LocatorLayouter.LAYOUT_LOCATOR.set(_labelFigure, new RelativeLocator(.25,.5,false));
  	_labelFigure.setEditable(false);
  	add(_labelFigure);
  	changed();
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

}
