package org.teamrocket.entities;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jhotdraw.app.action.ActionUtil;
import org.teamrocket.figures.StateFigure;

public class ContextSubMenuItemAction extends AbstractAction {
  private static final long serialVersionUID = 1L;
  protected StateEntity nextEntity;
  protected StateFigure myFigure;

  public ContextSubMenuItemAction(String text, String desc, String submenu) {
    super(text);
    putValue(SHORT_DESCRIPTION, desc);
    this.putValue(ActionUtil.SUBMENU_KEY, submenu);
  }

  public ContextSubMenuItemAction(StateFigure figure, String submenu, StateEntity ent) {
    this(ent.getName(), ent.getName(), submenu);
    nextEntity = ent;
    myFigure = figure;
  }

  public StateEntity getObject() {
    return nextEntity;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (myFigure.getEntity().getChildren().contains(nextEntity)
        || !nextEntity.getChildren().isEmpty()
        || myFigure.getEntity().getParent() != null) {
      return;
    }

    myFigure.getEntity().addChild(nextEntity);

  }
}