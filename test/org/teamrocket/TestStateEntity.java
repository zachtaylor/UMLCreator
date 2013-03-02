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
}