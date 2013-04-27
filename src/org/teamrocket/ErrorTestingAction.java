package org.teamrocket;

import java.awt.event.ActionEvent;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractViewAction;
import org.jhotdraw.util.ResourceBundleUtil;

import edu.umd.cs.findbugs.annotations.Nullable;

public class ErrorTestingAction extends AbstractViewAction {
	public final static String ID = "testforerrors";
	
	public ErrorTestingAction(Application app, @Nullable View view) {
    super(app, view);
    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
    labels.configureAction(this, ID);
	}

	@Override
	public void actionPerformed(ActionEvent errorCheck) {
    final View view = (View) getActiveView();
    
	}
}
