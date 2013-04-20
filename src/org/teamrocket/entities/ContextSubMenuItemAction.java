package org.teamrocket.entities;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jhotdraw.app.action.ActionUtil;

public class ContextSubMenuItemAction extends AbstractAction {
  private static final long serialVersionUID = 1L;
  protected Object object;

  public ContextSubMenuItemAction(String text, String desc, String submenu) {
    super(text);
    putValue(SHORT_DESCRIPTION, desc);
    this.putValue(ActionUtil.SUBMENU_KEY, submenu);
  }

  public ContextSubMenuItemAction(String text, String desc,String submenu, Object obj) {
    this(text,desc,submenu);
    object = obj;
  }
  
  @Override
  public void actionPerformed(ActionEvent arg0) {
  // TODO Auto-generated method stub

  }

}
