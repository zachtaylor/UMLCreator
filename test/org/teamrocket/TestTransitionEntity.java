package org.teamrocket;

import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;

import junit.framework.TestCase;

public class TestTransitionEntity extends TestCase {
  TransitionEntity ent;

  public void setUp() {
    ent = new TransitionEntity();
  }

  public void testNext() {
    StateEntity nextValue = new StateEntity();
    ent.setNext(nextValue);

    assertEquals(nextValue, ent.getNext());
  }

  public void testPrev() {
    StateEntity prevValue = new StateEntity();
    ent.setPrev(prevValue);

    assertEquals(prevValue, ent.getPrev());
  }

  public void testInput() {
    String InputValue = "Fu-bar";
    ent.setInput(InputValue);

    assertEquals(InputValue, ent.getInput());
  }

  public void testAction() {
    String actionValue = "This transition does this";
    ent.setAction(actionValue);

    assertEquals(actionValue, ent.getAction());
  }

  // TODO: Other Transition Testing methods (Internal Transitions, also Internal
  // StateEntities in TestStateEntities)
}