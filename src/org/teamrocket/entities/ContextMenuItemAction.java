package org.teamrocket.entities;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.teamrocket.figures.StateFigure;

public class ContextMenuItemAction extends AbstractAction {
  private static final long serialVersionUID = 1L;
  protected StateEntity nextEntity;
  protected StateFigure myFigure;

  public ContextMenuItemAction(StateFigure figure) {
    super("Disassociate from parent");
    putValue(SHORT_DESCRIPTION, "remove parent");
    myFigure = figure;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    myFigure.getEntity().removeParent();
  }
}