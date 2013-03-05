package org.teamrocket;

import java.util.Observable;
import java.util.Observer;

public class StateControl implements Observer {

  public StateControl() {
    // TODO:
  }

  public StateFigure clone() {
    // TODO:
    return null;
  }

  public void addTransition(TransitionFigure t) {
    // TODO:
  }

  public void removeTransition(TransitionFigure t) {
    // TODO:
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    // TODO Auto-generated method stub
  }

  public String toString() {
    // TODO: Create and return a string representation of the State object
    return null;
  }

  private StateEntity _data;
}
