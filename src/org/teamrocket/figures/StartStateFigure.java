package org.teamrocket.figures;

import org.jhotdraw.draw.EllipseFigure;
import org.teamrocket.ApplicationModel;
import org.teamrocket.entities.StartStateEntity;
import org.teamrocket.entities.StateEntity;

public class StartStateFigure extends StateFigure {
  public StartStateFigure() {
    super(new EllipseFigure());
  }

  public void init() {
    _stateEntity = new StateEntity(this);
  }

  public StartStateFigure clone() {
    StartStateFigure noob = (StartStateFigure) super.superDuperClone();

    noob._stateEntity = new StartStateEntity(noob);
    ApplicationModel.addStartStateEntity(noob._stateEntity);
    return noob;
  }

  public String getName() {
    return _stateEntity.getName();
  }

  public void setName(String newValue) {
    _stateEntity.setLabel(newValue);
  }

  /*
   * @Override public Collection<Handle> createHandles(int detailLevel) { java.util.List<Handle> handles = new LinkedList<Handle>();
   * 
   * switch (detailLevel) { case -1: handles.add(new BoundsOutlineHandle(this, false, true)); break; case 0: handles.add(new MoveHandle(this,
   * RelativeLocator.northWest())); handles.add(new MoveHandle(this, RelativeLocator.northEast())); handles.add(new MoveHandle(this,
   * RelativeLocator.southWest())); handles.add(new MoveHandle(this, RelativeLocator.southEast()));
   * 
   * ConnectorHandle ch; handles.add(ch = new ConnectorHandle(new LocatorConnector(this, RelativeLocator.center()), new TransitionFigure()));
   * ch.setToolTipText("Drag the connector to a dependent state."); break; } return handles; }
   */
}