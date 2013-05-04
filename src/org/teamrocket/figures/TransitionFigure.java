package org.teamrocket.figures;

import static org.jhotdraw.draw.AttributeKeys.END_DECORATION;
import static org.jhotdraw.draw.AttributeKeys.FONT_ITALIC;
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
  }

  @Override
  public boolean canConnect(Connector start, Connector end) {
    final Figure startState = start.getOwner();
    final Figure endState = end.getOwner();

    if (!(startState instanceof StateFigure) || !(endState instanceof StateFigure))
      return false;
    
    StateFigure sf = (StateFigure) startState;
    StateFigure ef = (StateFigure) endState;

    return !(startState instanceof EndStateFigure) 
    		&& !(endState instanceof StartStateFigure) 
    		&& (start != null) && (end != null)
    		&& ((sf.getEntity().getParent() == ef.getEntity().getParent())
    				|| (ef.getEntity().getParent() == null)
    				|| (sf.getEntity() == ef.getEntity().getParent()));
  }

  @Override
  public boolean canConnect(Connector start) {
    return (start.getOwner() instanceof StateFigure);
  }

  @Override
  protected void handleDisconnect(Connector start, Connector end) {
  	if (!(start instanceof StateFigure) || !(end instanceof StateFigure))
  		return;
  	
  	StateFigure sf = (StateFigure) start;
  	StateFigure ef = (StateFigure) end;
  	sf.removeSuccessor(_data);
  	ef.removePredecessor(_data);
  }

  @Override
  protected void handleConnect(Connector start, Connector end) {
    Figure startFigure = start.getOwner();
    Figure endFigure = end.getOwner();
    
    if (startFigure instanceof StateFigure) {
      _data.setPrev(((StateFigure) startFigure).getEntity());
      ((StateFigure) startFigure).addSuccessor(_data);
    }
    if (endFigure instanceof StateFigure) {
      _data.setNext(((StateFigure) endFigure).getEntity());
      ((StateFigure) endFigure).addPredecessor(_data);
    }

    if (startFigure == endFigure)
      setLiner(new CurvedLiner());
    
    if (startFigure == endFigure) 
      setLabelSelf("[label]");
    else 
      setLabel("[label]");
  }
  
  @Override
  public TransitionFigure clone() {
    TransitionFigure that = (TransitionFigure) super.clone();
    that._data = new TransitionEntity();

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
    _labelFigure = new TextFigure(_label) {
      @Override
      public void setText(String text) {
        super.setText(text);
        setTransitionInfo(text);
      }
    };
    LocatorLayouter.LAYOUT_LOCATOR.set(_labelFigure, new RelativeLocator(.25, .5, false));
    _labelFigure.setEditable(true);
    add(_labelFigure);
    changed();

  }

  private void setTransitionInfo(String s) {
    String[] parts = s.split("#");

    getData().setInput(parts[0]);

    if (parts.length > 1) {
      getData().setAction(parts[1]);
    }
  }

  public void setLabelSelf(String s) {
    _label = s;

    willChange();
    this.remove(_labelFigure);
    _labelFigure = new TextFigure(_label) {
      @Override
      public void setText(String text) {
        super.setText(text);
        setTransitionInfo(text);
      }
    };
    LocatorLayouter.LAYOUT_LOCATOR.set(_labelFigure, new RelativeLocator(.25, -.5, false));
    _labelFigure.setEditable(true);
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