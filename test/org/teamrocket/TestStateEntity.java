package org.teamrocket;

import junit.framework.TestCase;

public class TestStateEntity extends TestCase {
  StateEntity ent;

  public void setUp() {
    ent = new StateEntity(false);
  }

  public void testName() {
    String nameValue = "Fu-bar";
    ent.setName(nameValue);

    assertEquals(nameValue, ent.getName());
  }

  public void testDescription() {
    String descriptionValue = "A description goes here";
    ent.setDescription(descriptionValue);

    assertEquals(descriptionValue, ent.getDescription());
  }

  public void testAcceptState() {
    ent = new StateEntity(true);

    assertTrue(ent.isAcceptState());

    ent = new StateEntity(false);

    assertFalse(ent.isAcceptState());
  }

  public void testAddTransition() {
    assertTrue(ent.getTransitions().isEmpty());

    // test add successor
    TransitionEntity trans = new TransitionEntity();
    trans.setPrev(ent);
    assertTrue(ent.addSuccessor(trans));
    assertTrue(ent.getTransitions().contains(trans));
    
    // test add predecessor
    TransitionEntity trans2 = new TransitionEntity();
    trans.setNext(ent);
    assertTrue(ent.addPredecessor(trans2));
    assertTrue(ent.getTransitions().contains(trans2));
  }
  
  public void testRemoveTransition() {
	  assertTrue(ent.getTransitions().isEmpty());
	  
	  // test remove successor
	  TransitionEntity trans = new TransitionEntity();
	  
	  trans.setPrev(ent);
	  assertTrue(ent.addSuccessor(trans));
	  assertTrue(ent.getTransitions().contains(trans));
	  
	  assertTrue(ent.removeSuccessor(trans));
	  assertTrue(ent.getTransitions().isEmpty());
	  
	  // test remove predecessor
	  TransitionEntity trans2 = new TransitionEntity();
	  
	  trans.setNext(ent);
	  assertTrue(ent.addPredecessor(trans2));
	  assertTrue(ent.getTransitions().contains(trans2));
	  
	  assertTrue(ent.removePredecessor(trans2));
	  assertTrue(ent.getTransitions().isEmpty());
  }

  public void testAddDuplicateTransition() {
    assertTrue(ent.getTransitions().isEmpty());

    TransitionEntity trans1 = new TransitionEntity();
    TransitionEntity trans2 = new TransitionEntity();

    assertTrue(ent.addSuccessor(trans1));

    assertEquals(trans1, trans2);
    assertFalse(ent.addSuccessor(trans2));
  }
}