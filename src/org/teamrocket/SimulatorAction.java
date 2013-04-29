package org.teamrocket;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractViewAction;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.util.ResourceBundleUtil;
import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;

import edu.umd.cs.findbugs.annotations.Nullable;

public class SimulatorAction extends AbstractViewAction {
  public final static String ID = "runsimulator";
  public final static String ENTRY_LABEL = "entry", EXIT_LABEL = "exit";

  /** Creates a new instance. */
  public SimulatorAction(Application app, @Nullable View view) {
    super(app, view);
    ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels").configureAction(this, ID);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Specify an input file");

    if (!(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION))
      return;
    File inputFile = fileChooser.getSelectedFile();

    try {
      _input = new BufferedReader(new FileReader(inputFile));
    } catch (FileNotFoundException e) {
      return;
    }

    _state = ApplicationModel.getStartEntity();

    Color oldColor = _state.getStateFigure().get(AttributeKeys.FILL_COLOR);

    while (loadAndCheckNextInput()) {
      processInput();
    }

    fileChooser.setDialogTitle("Specify a location to save output");
    if (!(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION))
      return;
    File fileToSave = fileChooser.getSelectedFile();

    Writer writer = null;

    try {
      writer = new BufferedWriter(new FileWriter(fileToSave));

      writer.write(getOutput());

      writer.close();
    } catch (IOException e) {
      JOptionPane.showConfirmDialog(null, "Error with save destination");
    }

  }

  public String getOutput() {
    return _output.toString();
  }

  private void processInput() {
    if (_state.getInternalTransitions(_line) != null) {
      for (String s : _state.getInternalTransitions(_line)) {
        appendOutput(s);
      }
    }
    else {
      for (TransitionEntity te : _state.getSuccessors()) {
        if (_line.equals(te.getInput())) {

          if (_state.getInternalTransitions(EXIT_LABEL) != null)
            for (String s : _state.getInternalTransitions(EXIT_LABEL))
              appendOutput(s);

          appendOutput(te.getAction());
          _state = te.getNext();

          if (_state.getInternalTransitions(ENTRY_LABEL) != null)
            for (String s : _state.getInternalTransitions(ENTRY_LABEL))
              appendOutput(s);
          return;
        }
      }
    }

    // TODO : Throw error
  }

  private void appendOutput(String append) {
    _output.append(append);
    _output.append("\n");
  }

  private boolean loadAndCheckNextInput() {
    try {
      _line = _input.readLine().trim();
      if (_line.equals(ENTRY_LABEL) || _line.equals(EXIT_LABEL))
        return loadAndCheckNextInput();
      return true;
    } catch (IOException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
  }

  private String _line;
  private StateEntity _state;
  private BufferedReader _input;
  private StringBuilder _output = new StringBuilder();
}