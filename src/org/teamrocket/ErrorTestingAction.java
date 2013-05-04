package org.teamrocket;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractViewAction;
import org.jhotdraw.util.ResourceBundleUtil;
import org.teamrocket.entities.EndStateEntity;
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

		// Test for exactly one start state and exactly one end state
		List<StateEntity> theBucket = ApplicationModel.getStateEntityBucket();
		int endCounter = 0;

		for (StateEntity se : theBucket) {
			if ((se.getStateFigure() instanceof EndStateFigure))
				endCounter++;
		}

		if (ApplicationModel.getStartStates().size() > 1) {
			JOptionPane.showMessageDialog(null,
					"Diagrams must have exactly one Start State. (You have too many)",
					"Diagram Error", JOptionPane.ERROR_MESSAGE);
		} else if (ApplicationModel.getStartStates().size() < 1) {
			JOptionPane.showMessageDialog(null,
					"Diagrams must have exactly one Start State. (You have too few)",
					"Diagram Error", JOptionPane.ERROR_MESSAGE);
		}
		if (endCounter < 1) {
			JOptionPane.showMessageDialog(null,
					"Diagrams must have exactly one End State. (You have too few)",
					"Diagram Error", JOptionPane.ERROR_MESSAGE);
		} else if (endCounter > 1) {
			JOptionPane.showMessageDialog(null,
					"Diagrams must have exactly one End State. (You have too many)",
					"Diagram Error", JOptionPane.ERROR_MESSAGE);
		}

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

		// Each stateFigure must have a unique identity / unique name (for XML)
		Set<String> stateNames = new HashSet<String>();

		for (StateEntity se : theBucket) {
			if (se instanceof EndStateEntity)
				continue;
			else {
				if (!(stateNames.add(se.getName()))) {
					JOptionPane
							.showMessageDialog(
									null,
									("Each Figure must have a unique name:  \"" + se.getName() + "\" appears more than once."),
									"Diagram Error", JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
		}

		// All states much be reachable
		_visited = new HashSet<StateEntity>();
		dfsStates(ApplicationModel.getStartEntity());
		int bucketSize = ApplicationModel.getStateEntityBucket().size()
				+ ApplicationModel.getStartStates().size();
		if (bucketSize != _visited.size()) {
			JOptionPane.showMessageDialog(null,
					"Not all states can be reached from Start State.", "Diagram Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void dfsStates(StateEntity s) {
		if (!(_visited.add(s)))
			return;
		for (TransitionEntity t : s.getSuccessors()) {
			dfsStates(t.getNext());
		}
	}

	Set<StateEntity> _visited;
}
