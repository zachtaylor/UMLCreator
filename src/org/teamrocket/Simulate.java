package org.teamrocket;

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

import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;

public class Simulate implements Runnable {
  public final static String ENTRY_LABEL = "entry", EXIT_LABEL = "exit";

  @Override
  public void run() {

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

    while (loadAndCheckNextInput()) {
      processInput();
    }

    try {
      _input.close();
    } catch (IOException e1) {
      e1.printStackTrace();
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

  void processInput() {
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

          if (_state.getStateFigure() != null) {
            _state.getStateFigure().toggleHighlight();
            try {
              synchronized (this) {
                wait(3000);
              }
            } catch (InterruptedException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            _state.getStateFigure().toggleHighlight();
          }

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

  public String getOutput() {
    return _output.toString();
  }

  private String _line;
  private StateEntity _state;
  private BufferedReader _input;
  private StringBuilder _output = new StringBuilder();
}