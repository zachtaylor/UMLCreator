package org.teamrocket;

import java.awt.event.ActionEvent;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractViewAction;
import org.jhotdraw.util.ResourceBundleUtil;

import edu.umd.cs.findbugs.annotations.Nullable;

public class SimulatorAction extends AbstractViewAction {
  public final static String ID = "runsimulator";

  /** Creates a new instance. */
  public SimulatorAction(Application app, @Nullable View view) {
    super(app, view);
    ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels").configureAction(this, ID);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    (new Thread(new Simulate())).start();
  }
}