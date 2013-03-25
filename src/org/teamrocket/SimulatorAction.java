package org.teamrocket;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractViewAction;
import org.jhotdraw.util.ResourceBundleUtil;

import edu.umd.cs.findbugs.annotations.Nullable;

public class SimulatorAction extends AbstractViewAction {
  public final static String ID = "runsimulator";

  /** Creates a new instance. */
  public SimulatorAction(Application app, @Nullable View view, StateEntity start) {
    super(app, view);
    ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels").configureAction(this, ID);
    _state = start;
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    BufferedReader input = new BufferedReader(null);
    // TODO : assign _input
    _input = input;

    while (loadAndCheckNextInput()) {
      List<TransitionEntity> transitions = _state.getSuccessors();
      TransitionEntity transition = null;

      for (int i = 0; transition == null && transitions.size() > i; i++) {
        if (transitions.get(i).getInput().trim().equals(_line))
          transition = transitions.get(i);
      }

      if (transition == null) {
        throw new RuntimeException("AWE SHIT");
      }
      else {
        _state = transition.getNext();
        _output.append(transition.getAction());
        _output.append("\n");
      }
    }
  }

  public String getOutput() {
    return _output.toString();
  }

  private boolean loadAndCheckNextInput() {
    try {
      _line = _input.readLine().trim();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private String _line;
  private StateEntity _state;
  private BufferedReader _input;
  private StringBuilder _output = new StringBuilder();
}