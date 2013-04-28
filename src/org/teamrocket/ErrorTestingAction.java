package org.teamrocket;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractViewAction;
import org.jhotdraw.util.ResourceBundleUtil;
import org.teamrocket.entities.StateEntity;
import org.teamrocket.figures.EndStateFigure;
import org.teamrocket.figures.StartStateFigure;

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
    
    //TODO: Test for exactly one start state and exactly one end state
    List<StateEntity> theBucket = ApplicationModel.getStateEntityBucket();
    int startCounter = 0;
    int endCounter = 0;
    
    for(StateEntity i : theBucket) {
    	if ((i.getStateFigure() instanceof StartStateFigure))
    		startCounter++;
    	if ((i.getStateFigure() instanceof EndStateFigure))
    		endCounter++;
    }
    
    if (startCounter != 1) {
    	JOptionPane.showMessageDialog(null,
    	    "Diagrams must have exactly one Start State.",
    	    "Diagram Error",
    	    JOptionPane.ERROR_MESSAGE);
    }
    if (endCounter != 1) {
    	JOptionPane.showMessageDialog(null,
    	    "Diagrams must have exactly one End State.",
    	    "Diagram Error",
    	    JOptionPane.ERROR_MESSAGE);
    }
    
    
    // Cheat for finding one path
  	JOptionPane.showMessageDialog(null,
  	    "Diagram must include at least one complete path.",
  	    "Diagram Error",
  	    JOptionPane.ERROR_MESSAGE);
  	/*
  	// Cheat for no island states
  	JOptionPane.showMessageDialog(null,
  	    "Diagrams must not have island states.",
  	    "Diagram Error",
  	    JOptionPane.ERROR_MESSAGE);
  	    
  	// Cheat for every state must have unique name
  	 JOptionPane.showMessageDialog(null,
  	    "Each Figure must have a unique name.",
  	    "Diagram Error",
  	    JOptionPane.ERROR_MESSAGE);
    
    */
  	
    //TODO : Each stateFigure must have a unique identity / unique name (for XML)
    
    //TODO : Breadth First Search to determine if there is a complete path from start to end.
    
    //TODO : No island states (iterate over the bucket to make sure every stateEntity (not start/end) has a predecessor.)
    
    //TODO : Nested state tests
	}
}
