package org.teamrocket;

import static org.jhotdraw.draw.AttributeKeys.FILL_COLOR;
import static org.jhotdraw.draw.AttributeKeys.FONT_BOLD;
import static org.jhotdraw.draw.AttributeKeys.STROKE_COLOR;
import static org.jhotdraw.draw.AttributeKeys.STROKE_DASHES;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jhotdraw.draw.GraphicalCompositeFigure;
import org.jhotdraw.draw.ListFigure;
import org.jhotdraw.draw.RoundRectangleFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.connector.LocatorConnector;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.ConnectorHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.MoveHandle;
import org.jhotdraw.draw.layouter.VerticalLayouter;
import org.jhotdraw.draw.locator.RelativeLocator;
import org.jhotdraw.geom.Insets2D;
import org.jhotdraw.samples.pert.figures.DependencyFigure;
import org.jhotdraw.samples.pert.figures.SeparatorLineFigure;
import org.jhotdraw.samples.pert.figures.TaskFigure;
import org.jhotdraw.util.ResourceBundleUtil;

public class StateFigure extends GraphicalCompositeFigure{
  
  private String _label;
  private StateEntity _data;
  
  public StateFigure()
  {
    super(new RoundRectangleFigure());
    
    setLayouter(new VerticalLayouter());
    
    RoundRectangleFigure nameCompartmentPF = new RoundRectangleFigure();
    nameCompartmentPF.set(STROKE_COLOR, null);
    nameCompartmentPF.setAttributeEnabled(STROKE_COLOR, false);
    nameCompartmentPF.set(FILL_COLOR, null);
    nameCompartmentPF.setAttributeEnabled(FILL_COLOR, false);
    ListFigure nameCompartment = new ListFigure(nameCompartmentPF);
    ListFigure descriptionCompartment = new ListFigure();
    SeparatorLineFigure separator1 = new SeparatorLineFigure();
    
    add(nameCompartment);
    add(separator1);
    add(descriptionCompartment);
    
    Insets2D.Double insets = new Insets2D.Double(4, 8, 4, 8);
    nameCompartment.set(LAYOUT_INSETS, insets);
    descriptionCompartment.set(LAYOUT_INSETS, insets);
    
    TextFigure nameFigure;
    nameCompartment.add(nameFigure = new TextFigure());
    nameFigure.set(FONT_BOLD, true);
    nameFigure.setAttributeEnabled(FONT_BOLD, true);
    
    TextFigure descriptionFigure;
    descriptionCompartment.add(descriptionFigure = new TextFigure());
    descriptionFigure.set(FONT_BOLD, false);
    descriptionFigure.setText("<State Description>");
    descriptionFigure.setAttributeEnabled(FONT_BOLD, false);
    
    setAttributeEnabled(STROKE_DASHES, false);
    
    ResourceBundleUtil labels =
        ResourceBundleUtil.getBundle("org.jhotdraw.samples.pert.Labels");
    
    setName(labels.getString("teamrocket.state.defaultName"));
    
    // may want to change to false, depending on how we use the boolean value in StateEntity.java
    _data = new StateEntity(true);
    _data.setName(nameFigure.getText());
    _data.setDescription(descriptionFigure.getText());
  }
  
  @Override
  public Collection<Handle> createHandles(int detailLevel) {
      java.util.List<Handle> handles = new LinkedList<Handle>();
      switch (detailLevel) {
          case -1:
              handles.add(new BoundsOutlineHandle(getPresentationFigure(), false, true));
              break;
          case 0:
              handles.add(new MoveHandle(this, RelativeLocator.northWest()));
              handles.add(new MoveHandle(this, RelativeLocator.northEast()));
              handles.add(new MoveHandle(this, RelativeLocator.southWest()));
              handles.add(new MoveHandle(this, RelativeLocator.southEast()));
              ConnectorHandle ch;
              handles.add(ch = new ConnectorHandle(new LocatorConnector(this, RelativeLocator.center()), new TransitionFigure()));
              ch.setToolTipText("Drag the connector to a dependent state.");
              break;
      }
      return handles;
  }
  
  /*
  public void update(Observable obs, Object o)
  {
    // TODO: 
  }
  */
  
  public void setName(String newValue) {
    getNameFigure().setText(newValue);
  }

  public String getName() {
    return getNameFigure().getText();
  }
  
  public void setDescription(String newValue) {
  	getNameFigure().setText(newValue);
  }
  
  public String getDescription() {
  	return getNameFigure().getText();
  }
  
  public StateEntity getEntity() {
    return _data;
  }
  
  // TaskFigure has one of these - it might be for some unseen functionality of another class
  @Override
  public int getLayer() {
      return 0;
  }
  
  // not sure how this works, but TaskFigure seems to like it
  private TextFigure getNameFigure() {
    return (TextFigure) ((ListFigure) getChild(0)).getChild(0);
  }
  
  // modifiers for predecessor and successor lists
  public void addPredecessor(TransitionFigure f) {
    TransitionEntity t = f.getData();
    _data.addPredecessor(t);
    
  }

  public void removePredecessor(TransitionFigure f) {
    TransitionEntity t = f.getData();
    _data.removePredecessor(t);
    
  }
  
  public void addSuccessor(TransitionFigure f) {
    TransitionEntity t = f.getData();
    _data.addSuccessor(t);
    
  }
  
  public void removeSuccessor(TransitionFigure f) {
    TransitionEntity t = f.getData();
    _data.removeSuccessor(t);
    
  }
  
/*  public List<StateFigure> getSuccessors() {

    }
    return list;
  }
  
  public List<StateFigure> getPredecessors() {

    }
    return list;
  } */
}






















