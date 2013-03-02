package org.teamrocket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import com.zachtaylor.jnodalxml.XMLNode;

public class StateEntity extends Observable {
  public StateEntity(boolean accept) {
    _accept = accept;

    _successors = new ArrayList<TransitionEntity>();
    _predecessors = new ArrayList<TransitionEntity>();
  }

  public boolean isAcceptState() {
    return _accept;
  }

  public String getName() {
    return _name;
  }

  public void setName(String n) {
    _name = n;
  }

  public String getDescription() {
    return _description;
  }

  public void setDescription(String d) {
    _description = d;
  }

  public boolean addSuccessor(TransitionEntity ent) {
    // TODO : verification

    _successors.add(ent);

    return false;
  }

  public boolean removeSuccessor(TransitionEntity ent) {
    // TODO : verification

    return _successors.remove(ent);
  }

  public boolean addPredecessor(TransitionEntity ent) {
    // TODO : verification

    _predecessors.add(ent);

    return false;
  }

  public boolean removePredecessor(TransitionEntity ent) {
    // TODO : verification

    return _predecessors.remove(ent);
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

  public XMLNode toXML() {
    XMLNode node = new XMLNode(_name);
    
    // add stuff to this node for saving purposes...
    
    return node;
  }

  public static StateEntity fromXML(XMLNode node) {
    return null; // TODO : make new state entity from xml node
  }

  public boolean equals(Object o) {
    if (!(o instanceof StateEntity))
      return false;

    StateEntity s = (StateEntity) o;

    if (_accept ^ s._accept) // XOR
      return false;
    if ((_name != null && s._name == null) || (_name == null && s._name != null))
      return false;
    if (_name != null && s._name != null && !_name.equals(s._name))
      return false;
    if ((_description != null && s._description == null) || (_description == null && s._description != null))
      return false;
    if (_description != null && s._description != null && !_description.equals(s._description))
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

  private boolean _accept;
  private String _name, _description;
  private List<TransitionEntity> _successors, _predecessors;
}