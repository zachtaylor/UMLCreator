package org.teamrocket.entities;

public class InternalTransition extends AbstractEntity {
  String _event;
  String _action;
  
  public InternalTransition(String event, String action) {
    _event = event;
    _action = action;
  }
  
  @Override
  public boolean equals(Object o) {
	if(!(o instanceof InternalTransition)) return false;
	
	InternalTransition it = (InternalTransition) o;
	return (_event.equals(it._event)&& _action.equals(_action));
  }
  
  @Override
  public int hashCode() {
    return (_event+_action).hashCode();
  }	
}
