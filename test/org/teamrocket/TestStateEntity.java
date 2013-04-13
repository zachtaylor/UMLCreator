package org.teamrocket;

import junit.framework.TestCase;

import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;
import org.teamrocket.figures.StateFigure;

public class TestStateEntity extends TestCase {
  StateEntity ent;

  public void setUp() {
    ent = new StateEntity();
  }

  public void testName() {
    String nameValue = "Fu-bar";
    ent.setLabel(nameValue);

    assertEquals(nameValue, ent.getName());
  }

  public void testDescription() {
    String descriptionValue = "A description goes here";
    ent.setDescription(descriptionValue);

    assertEquals(descriptionValue, ent.getDescription());
  }

  public void testAddInternalTransitions() {
    ent.addInternalTransition(null, null);
    ent.addInternalTransition(null, "action");
    ent.addInternalTransition("Event", null);
	  
    ent.addInternalTransition("Event", "action");
    assertTrue(ent.containsInternalTransition("Event", "action"));
    assertTrue(ent.getInternalTransitions("Event").contains("action"));
    ent.addInternalTransition("Event", "action2");
    assertTrue(ent.getInternalTransitions("Event").contains("action"));
    assertTrue(ent.getInternalTransitions("Event").contains("action2"));
  }
  
  public void testRemoveInternalTransitions() {
	ent.addInternalTransition("Event", "action");	  
    ent.removeInternalTransition("Event", "action");
    
    assertTrue(ent.getInternalTransitions("Event").isEmpty());
    ent.addInternalTransition("Event", "action");
    ent.removeInternalTransition("Event");    
	assertFalse(ent.containsInternalTransition("Event", "action"));	
  }
  
  public void testAcceptState() {
    ent = new StateEntity();

    assertTrue(ent.isAcceptState());

    ent = new StateEntity();

    assertFalse(ent.isAcceptState());
  }

  public void testAddRemoveNestedStates() {
	StateEntity child, parent;
	child = new StateEntity();
	parent = new StateEntity();
	
	assertNull(child.getParent());
	assertNull(parent.getParent());
	
	
	child.setParent(parent);
	assertEqual(child.getParent(), parent);
	
	parent.setParent(child);
	parent.addChild(child);
	parent.removeChild(child);
	parent.getChildren();
  }
  
  public void testAddRemoveNestedTransitions() {
    
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