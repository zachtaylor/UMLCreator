package org.teamrocket.entities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.jhotdraw.draw.AttributeKeys;
import org.teamrocket.figures.StartStateFigure;
import org.teamrocket.figures.StateFigure;
import org.zachtaylor.jnodalxml.XMLNode;

public class StateEntity extends AbstractEntity {
  private static Queue<Color> availableColors = new LinkedList<Color>();

  static {
    availableColors.add(Color.orange);
    availableColors.add(Color.green);
    availableColors.add(Color.blue);
    availableColors.add(Color.magenta);
    availableColors.add(Color.cyan);
    availableColors.add(Color.red);
    availableColors.add(Color.yellow);
    availableColors.add(Color.gray);
    availableColors.add(Color.pink);
  }

  public StateEntity(StateFigure parent) {
    _successors = new ArrayList<TransitionEntity>();
    _predecessors = new ArrayList<TransitionEntity>();
    _figure = parent;
  }

  public String getName() {
    if (_label.isEmpty()) {
      if (getStateFigure() instanceof StartStateFigure)
        return "startstate";
      else
        return "endstate";
    }
    return _label;
  }

  public StateFigure getStateFigure() {
    return _figure;
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
      ent = new StateEntity(null);
      ent.setLabel(node.getName());

    } catch (Exception e) {
    }

    return ent;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(getName());

    if (_parent != null)
      node.setAttribute("parent", _parent.getName());

    for (Map.Entry<String, List<String>> me : _internalTransitions.entrySet()) {
      XMLNode childNode = new XMLNode("event");
      childNode.setAttribute("id", me.getKey());

      for (String a : me.getValue()) {
        XMLNode grandChildNode = new XMLNode("action");
        grandChildNode.setAttribute("id", a);
        grandChildNode.setSelfClosing(true);
        childNode.addChild(grandChildNode);
      }

      node.addChild(childNode);
    }

    for (TransitionEntity trans : _successors) {
      XMLNode childNode = new XMLNode("transition");
      childNode.setAttribute("next", trans.getNext().getName());
      childNode.setSelfClosing(true);
      node.addChild(childNode);
    }

    if (node.getAllChildren().isEmpty()) {
      node.setSelfClosing(true);
    }

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

  // Internal transitions start here!
  public boolean addInternalTransition(String event, String action) {
    List<String> value = _internalTransitions.get(event);
    if (value == null) {
      value = new ArrayList<String>();
      value.add(action);
      return _internalTransitions.put(event, value) != null;
    }
    else {
      return value.add(action);
    }
  }

  public boolean containsInternalTransition(String event, String action) {
    // return internalTransitions.
    return false;
  }

  public List<String> getInternalTransitions(String event) {
    if (_internalTransitions.get(event) != null)
      return Collections.unmodifiableList(_internalTransitions.get(event));
    return null;
  }

  public void removeInternalTransition(String event, String action) {
    List<String> value = _internalTransitions.get(event);
    value.remove(action);
    if (value.isEmpty())
      _internalTransitions.remove(event);
  }

  public void removeInternalTransition(String event) {
    _internalTransitions.remove(event);
  }

  // Nested states start here
  public void setParent(StateEntity parent) {
    if (_parent != null)
      _parent.removeChild(this);

    _parent = parent;
    _figure.willChange();
    if(parent == null)
    	_figure.set(AttributeKeys.FILL_COLOR, Color.black);
    else
    	_figure.set(AttributeKeys.FILL_COLOR, parent.getColor());
    _figure.changed();
  }

  public void removeParent() {
    _parent.removeChild(this);
    _parent = null;
    _figure.willChange();
    _figure.set(AttributeKeys.FILL_COLOR, Color.white);
    _figure.changed();
  }

  private Color getColor() {
    if (_color == Color.black && availableColors.size() > 0) {
      _color = availableColors.remove();
      getStateFigure();
    }
    return _color;
  }

  public StateEntity getParent() {
    return _parent;
  }

  public boolean addChild(StateEntity child) {
    _figure.willChange();
    _figure.set(AttributeKeys.STROKE_COLOR, getColor());
    _figure.changed();

    child.setParent(this);
    return _children.add(child);
  }

  public boolean removeChild(StateEntity child) {
    if (!_children.remove(child))
      return false;

    if (_children.isEmpty()) {
      availableColors.add(_color);
      _color = Color.black;
      _figure.willChange();
      _figure.set(AttributeKeys.STROKE_COLOR, _color);
      _figure.changed();
    }

    return true;
  }

  public Set<StateEntity> getChildren() {
    return Collections.unmodifiableSet(_children);
  }

  private Color _color = Color.black;
  private StateEntity _parent = null;
  private StateFigure _figure;
  private List<TransitionEntity> _successors, _predecessors;
  private HashSet<StateEntity> _children = new HashSet<StateEntity>();
  private Map<String, List<String>> _internalTransitions = new HashMap<String, List<String>>();
}