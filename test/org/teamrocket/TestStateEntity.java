package org.teamrocket;

import junit.framework.TestCase;

import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;
import org.teamrocket.figures.StateFigure;

public class TestStateEntity extends TestCase {
  StateEntity ent;

  public void setUp() {
    ent = new StateEntity(null);
  }

  public void testName() {
    String nameValue = "Fu-bar";
    ent.setLabel(nameValue);

    assertEquals(nameValue, ent.getName());
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

  public void testAddRemoveNestedStates() {
    StateEntity child, parent, parent2;
    child = new StateEntity(null);
    parent = new StateEntity(null);
    parent2 = new StateEntity(null);

    // Start off as not children
    assertNull(child.getParent());
    assertNull(parent.getParent());

    // Make sure adding a parent works
    child.setParent(parent);
    parent.addChild(child);
    assertEquals(child.getParent(), parent);
    // it should also add a child to parent
    assertTrue(parent.getChildren().contains(child));

    // Make sure parents can't be made children while still having children
    parent.setParent(child);// fails silently. Maybe throw?
    assertEquals(parent.getParent(), null);

    // Removing parent should be reflected in parent's children
    child.setParent(null);
    assertFalse(parent.getChildren().contains(child));

    child.setParent(parent);
    // removing children should be reflected in child's parent
    parent.removeChild(child);
    assertNull(child.getParent());

    child.setParent(parent);
    // Switching parents should remove child from one parent and add to the other
    assertTrue(parent.getChildren().contains(child));
    child.setParent(parent2);
    assertFalse(parent.getChildren().contains(child));
    assertTrue(parent2.getChildren().contains(child));

    // Add child should set parent on the child
    parent.addChild(child);
    assertFalse(parent2.getChildren().contains(child));
    assertEquals(child.getParent(), parent);

    // Removing child should unset child's parent
    parent.removeChild(child);
    assertNull(child.getParent());
    assertTrue(parent.getChildren().isEmpty());
  }

  public void testAddRemovefNestedTransitions() {
    StateEntity s1, s2, s3, s4;
    s1 = new StateEntity(null);
    s2 = new StateEntity(null);
    s3 = new StateEntity(null);
    s4 = new StateEntity(null);

    // Create transition between parent and child
    TransitionEntity t = new TransitionEntity();
    s2.setParent(s1);
    t.setPrev(s1).setNext(s2);

    // Set transition between child and unrelated state: should fail
    t = new TransitionEntity();
    t.setPrev(s2);
    try {
      t.setNext(s3);
      fail("Can't set transititon between child and unrelated state");
    } finally {
    }
    ;

    // Set transition between two siblings
    t = new TransitionEntity();
    s3.setParent(s2);
    t.setPrev(s2).setNext(s3);

    // Set transition between two regular states
    t = new TransitionEntity();
    s3.setParent(null);
    t.setPrev(s1).setNext(s3);

    // Set transition between two unrelated children
    t = new TransitionEntity();
    s2.setParent(s1);
    s3.setParent(null);
    s4.setParent(s3);
    t.setPrev(s2);
    try {
      t.setNext(s4);
      fail("Can't set transititon between unrelated children");
    } finally {
    }
    ;

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