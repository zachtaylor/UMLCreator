package org.teamrocket.entities;

import java.util.Observable;

import org.zachtaylor.jnodalxml.XMLNode;

public abstract class AbstractEntity extends Observable {
  public AbstractEntity() {
    super();
  }

  public String getName() {
    return _label;
  }

  public void setLabel(String label) {
    _label = label;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(_label);
    node.setSelfClosing(true);
    return node;
  }

  @Override
  public int hashCode() {
    return _label.hashCode();
  }

  public String toString() {
    return toXML().toString();
  }

  protected String _label = "";
}