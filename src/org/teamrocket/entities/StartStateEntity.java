package org.teamrocket.entities;

import org.teamrocket.figures.StartStateFigure;

public class StartStateEntity extends StateEntity {
  public StartStateEntity(StartStateFigure parent) {
    super(parent);
  }

  public String getName() {
    return "startstate";
  }
}