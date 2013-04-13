package org.teamrocket.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zachtaylor.jnodalxml.XMLNode;

public class StateEntity extends AbstractEntity {
  public StateEntity() {
    _successors = new ArrayList<TransitionEntity>();
    _predecessors = new ArrayList<TransitionEntity>();
  }

  public String getDescription() {
    return _description;
  }

  public void setDescription(String d) {
    _description = d;
  }

  public boolean addSuccessor(TransitionEntity ent) {
    if (_successors.contains(ent))
      return false;

    _successors.add(ent);
    return true;
  }

  public boolean removeSuccessor(TransitionEntity ent) {
    if (!_successors.contains(ent))
      return false;

    return _successors.remove(ent);
  }

  public boolean addPredecessor(TransitionEntity ent) {
    if (_predecessors.contains(ent))
      return false;

    _predecessors.add(ent);
    return true;
  }

  public boolean removePredecessor(TransitionEntity ent) {
    if (!_predecessors.contains(ent))
      return false;

    return _predecessors.remove(ent);
  }
  
  public List<TransitionEntity> getSuccessors() {
    return Collections.unmodifiableList(_successors);
  }

  public Set<TransitionEntity> getTransitions() {
    Set<TransitionEntity> ret = new HashSet<TransitionEntity>();

    if (_predecessors != null) {
      for (TransitionEntity ent : _predecessors)
        ret.add(ent);
    }

    if (_successors != null) {
      for (TransitionEntity ent : _successors)
        ret.add(ent);
    }

    return ret;
  }

  public static StateEntity fromXML(XMLNode node) {
    StateEntity ent = null;

    try {
      ent = new StateEntity();
      ent.setLabel(node.getName());
      ent.setDescription(node.getAttribute("description"));

    } catch (Exception e) {
    }

    return ent;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(_label);

    node.setAttribute("description", _description);

    node.setSelfClosing(true);

    return node;
  }

  public boolean equals(Object o) {
    if (!(o instanceof StateEntity))
      return false;

    StateEntity s = (StateEntity) o;

    if ((_label != null && s._label == null) || (_label == null && s._label != null))
      return false;
    if (_label != null && !_label.equals(s._label))
      return false;
    if ((_description != null && s._description == null) || (_description == null && s._description != null))
      return false;
    if (_description != null && !_description.equals(s._description))
      return false;

    if (_successors.size() != s._successors.size())
      return false;
    if (_predecessors.size() != s._predecessors.size())
      return false;

    for (TransitionEntity ent : _successors)
      if (!s._successors.contains(ent))
        return false;
    for (TransitionEntity ent : _predecessors)
      if (!s._predecessors.contains(ent))
        return false;

    return true;
  }

//Internal transitions start here!
  public boolean addInternalTransition(String event, String action) {
	HashSet<String> value = internalTransitions.get(event);	
	if(value == null) {
	  value = new HashSet<String>();
	  value.add(action);
	  return internalTransitions.put(event, value) != null;
	} else {
	  return value.add(action);
	}
  }

  public boolean containsInternalTransition(String event, String action) {
	//return internalTransitions.
	return false;
  } 

  public Set<String> getInternalTransitions(String event) {
    return Collections.unmodifiableSet(internalTransitions.get(event));	  
  }
  public void removeInternalTransition(String event, String action) {
	HashSet<String> value = internalTransitions.get(event);
	value.remove(action);
	if(value.isEmpty()) internalTransitions.remove(event);
  }

  public void removeInternalTransition(String event) {
    internalTransitions.remove(event);
  }
  
//Nested states start here  
  public void setParent(StateEntity parent) {
	// TODO Auto-generated method stub
  }

  public void getParent() {
	// TODO Auto-generated method stub
  }

  public void addChild(StateEntity child) {
	// TODO Auto-generated method stub
  }

  public void removeChild(StateEntity child) {
	// TODO Auto-generated method stub
  }

  public void getChildren() {
	// TODO Auto-generated method stub
  }  
  
  
  private String _description;
  private List<TransitionEntity> _successors, _predecessors;
  private HashMap<String,HashSet<String>> internalTransitions;
  private StateEntity parent = null;
  private HashSet<StateEntity> children = new HashSet<StateEntity>();


}