package org.teamrocket;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractViewAction;
import org.jhotdraw.util.ResourceBundleUtil;
import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;
import org.teamrocket.figures.EndStateFigure;

import edu.umd.cs.findbugs.annotations.Nullable;

public class ErrorTestingAction extends AbstractViewAction {
	public final static String ID = "testforerrors";

	public ErrorTestingAction(Application app, @Nullable View view) {
		super(app, view);
		ResourceBundleUtil labels = ResourceBundleUtil
				.getBundle("org.jhotdraw.app.Labels");
		labels.configureAction(this, ID);
	}

	@Override
	public void actionPerformed(ActionEvent errorCheck) {
		final View view = (View) getActiveView();

		// TODO: Test for exactly one start state and exactly one end state
		List<StateEntity> theBucket = ApplicationModel.getStateEntityBucket();
		int endCounter = 0;

		for (StateEntity se : theBucket) {
			if ((se.getStateFigure() instanceof EndStateFigure))
				endCounter++;
		}

		if (ApplicationModel.getStartStates().size() > 1) {
			JOptionPane.showMessageDialog(null,
					"Diagrams must have exactly one Start State. (You have too many)", "Diagram Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (ApplicationModel.getStartStates().size() < 1) {
			JOptionPane.showMessageDialog(null,
					"Diagrams must have exactly one Start State. (You have too few)", "Diagram Error",
					JOptionPane.ERROR_MESSAGE);
		}
		if (endCounter < 1) {
			JOptionPane.showMessageDialog(null,
					"Diagrams must have exactly one End State. (You have too few)", "Diagram Error",
					JOptionPane.ERROR_MESSAGE);
		}	else if (endCounter > 1) {
			JOptionPane.showMessageDialog(null,
					"Diagrams must have exactly one End State. (You have too many)", "Diagram Error",
					JOptionPane.ERROR_MESSAGE);
		}

		/*
		 * // Cheat for finding one path JOptionPane.showMessageDialog(null,
		 * "Diagram must include at least one complete path.", "Diagram Error",
		 * JOptionPane.ERROR_MESSAGE);
		 * 
		 * // Cheat for no island states JOptionPane.showMessageDialog(null,
		 * "Diagrams must not have island states.", "Diagram Error",
		 * JOptionPane.ERROR_MESSAGE);
		 * 
		 * // Cheat for every state must have unique name
		 * JOptionPane.showMessageDialog(null,
		 * "Each Figure must have a unique name.", "Diagram Error",
		 * JOptionPane.ERROR_MESSAGE);
		 */

		for (StateEntity se : theBucket) {

			for (TransitionEntity te : se.getSuccessors()) {
				StateEntity start = te.getPrev();
				StateEntity end = te.getNext();
				if ((start.getParent() != end.getParent()) && (end.getParent() != null)
						&& (start != end.getParent())) {
					JOptionPane.showMessageDialog(null,
							"Invalid Transition between unrelated states.", "Diagram Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		// TODO : Each stateFigure must have a unique identity / unique name (for
		// XML)

		// TODO : Breadth First Search to determine if there is a complete path from
		// start to end.

		// TODO : No island states (iterate over the bucket to make sure every
		// stateEntity (not start/end) has a predecessor.)

		// TODO : Nested state tests
	}
}
