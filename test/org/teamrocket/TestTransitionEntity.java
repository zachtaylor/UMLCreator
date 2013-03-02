package org.teamrocket;

import org.junit.Test;

import junit.framework.TestCase;

public class TestTransitionEntity extends TestCase{

  TransitionEntity ent;
  
  public void setUp()
  {
    ent = new TransitionEntity();
  }
	
  @Test
  public void testNext() {
    StateEntity nextValue = new StateEntity(false);
    ent.setNext(nextValue);

    assertEquals(nextValue, ent.getNext());
  }
  
  @Test
  public void testPrev() {
    StateEntity prevValue = new StateEntity(false);
    ent.setPrev(prevValue);
    
    assertEquals(prevValue, ent.getPrev());
  }
  
  @Test
  public void testInput() {
    String InputValue = "Fu-bar";
    ent.setInput(InputValue);

    assertEquals(InputValue, ent.getInput());
  }
  
  @Test
  public void testAction() {
    String actionValue = "This transition does this";
    ent.setAction(actionValue);

    assertEquals(actionValue, ent.getAction());
  }
  
  @Test
  public void testEquals() {
    // TODO: Test the equals method
  }
  
  // TODO: Other Transition Testing methods  (Internal Transitions, also Internal StateEntities in TestStateEntities)
}

