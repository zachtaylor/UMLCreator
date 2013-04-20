package org.teamrocket.entities;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ContextMenuItemAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
  public ContextMenuItemAction(String text, String desc) {
    super(text);
    putValue(SHORT_DESCRIPTION, desc);
    //this.putValue(ActionUtil.SUBMENU_KEY, "SUBMENU");
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
  // TODO Auto-generated method stub

  }


}
