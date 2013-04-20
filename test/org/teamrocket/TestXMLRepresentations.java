package org.teamrocket;

import junit.framework.TestCase;

import org.teamrocket.entities.StateEntity;

public class TestXMLRepresentations extends TestCase {

  public void testStateEntityEmpty() {
    StateEntity ent = new StateEntity();

    ent.setLabel("foo");

    assertEquals("<foo />\n", ent.toXML().toString());
  }

  public void testStateEntityWithInternalTransition() {
    StateEntity ent = new StateEntity();

    ent.setLabel("foo");
    ent.addInternalTransition("event", "action");

    assertEquals("<foo>\n\t<event id=\"event\">\n\t\t<action id=\"action\" />\n\t</event>\n</foo>\n", ent.toXML().toString());
  }

  public void testStateEntityWithInternalTransitionToMultipleActions() {
    StateEntity ent = new StateEntity();

    ent.setLabel("foo");
    ent.addInternalTransition("event", "action1");
    ent.addInternalTransition("event", "action2");

    assertEquals("<foo>\n\t<event id=\"event\">\n\t\t<action id=\"action1\" />\n\t\t<action id=\"action2\" />\n\t</event>\n</foo>\n", ent.toXML().toString());
  }

  public void testStateEntityWithMultipleInternalTransitions() {
    StateEntity ent = new StateEntity();

    ent.setLabel("foo");
    ent.addInternalTransition("event1", "action1");
    ent.addInternalTransition("event2", "action2");

    assertEquals("<foo>\n\t<event id=\"event1\">\n\t\t<action id=\"action1\" />\n\t</event>\n\t<event id=\"event2\">\n\t\t<action id=\"action2\" />\n\t</event>\n</foo>\n", ent.toXML().toString());
  }
}
