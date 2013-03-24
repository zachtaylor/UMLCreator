package org.teamrocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class Simulator {
  public Simulator(StateEntity start, BufferedReader input) {
    _state = start;
    _input = input;
  }

  public void run() {
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