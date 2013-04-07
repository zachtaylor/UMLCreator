package org.teamrocket;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractViewAction;
import org.jhotdraw.util.ResourceBundleUtil;
import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;

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
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Specify a file to save");

    if (!(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION))
      return;

    File fileToSave = fileChooser.getSelectedFile();
  	
    try {
    	BufferedReader input = new BufferedReader(new FileReader(fileToSave));
    	
      // TODO : assign _input
      _input = input;

      StateEntity state = ApplicationModel.getStartEntity();

      while (loadAndCheckNextInput()) {
        List<TransitionEntity> transitions = state.getSuccessors();
        TransitionEntity transition = null;

        for (int i = 0; transition == null && transitions.size() > i; i++) {
          if (transitions.get(i).getInput().trim().equals(_line))
            transition = transitions.get(i);
        }

        if (transition == null) {
          throw new RuntimeException("AWE SHIT");
        }
        else {
          state = transition.getNext();
          _output.append(transition.getAction());
          _output.append("\n");
        }
      }
    } catch (FileNotFoundException e) {
    	
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
  private BufferedReader _input;
  private StringBuilder _output = new StringBuilder();
}