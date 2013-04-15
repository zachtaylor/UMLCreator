package org.teamrocket;

import junit.framework.TestCase;

import org.teamrocket.entities.StateEntity;

public class TextXMLRepresentations extends TestCase {

  public void testStateEntityEmpty() {
    StateEntity ent = new StateEntity();

    ent.setLabel("foo");

    assertEquals("<foo />\n", ent.toXML().toString());
  }
  
  public void testStateEntityWithInternalTransition() {
    StateEntity ent = new StateEntity();

    ent.setLabel("foo");
    ent.addInternalTransition("event", "action");

    assertEquals("<foo>\n  <event event=\"event\">\n    <action action=\"action\" />\n  </event>\n</foo>", ent.toXML().toString());
  }
  
  public void testStateEntityWithInternalTransitionToMultipleActions() {
    StateEntity ent = new StateEntity();

    ent.setLabel("foo");
    ent.addInternalTransition("event", "action1");
    ent.addInternalTransition("event", "action2");

    assertEquals("<foo>\n  <event event=\"event\">\n    <action action=\"action1\" />\n    <action action=\"action2\" />\n  </event>\n</foo>", ent.toXML().toString());
  }
}
