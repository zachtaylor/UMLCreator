package org.teamrocket.entities;

import java.util.Observable;


public class TransitionEntity extends AbstractEntity {

  public void setNext(StateEntity n) {
    _next = n;
  }

  public StateEntity getNext() {
    return _next;
  }

  public void setPrev(AbstractEntity p) {
    _prev = p;
  }

  public AbstractEntity getPrev() {
    return _prev;
  }

  public void setInput(String s) {
    _input = s;
  }

  public String getInput() {
    return _input;
  }

  public void setAction(String a) {
    _action = a;
  }

  public String getAction() {
    return _action;
  }

  public boolean equals(Object o) {
    if (!(o instanceof TransitionEntity))
      return false;

    TransitionEntity s = (TransitionEntity) o;

    if ((_input != null && s._input == null) || (_input == null && s._input != null))
      return false;
    if (_input != null && s._input != null && !_input.equals(s._input))
      return false;
    if ((_action != null && s._action == null) || (_action == null && s._action != null))
      return false;
    if (_action != null && !_action.equals(s._action))
      return false;

    if (!(_next == s.getNext()) || !(_prev == s.getPrev()))
      return false;

    return true;
  }

  private String _input;
  private String _action;
  private StateEntity _next;
  private AbstractEntity _prev;
}