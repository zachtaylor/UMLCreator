package org.teamrocket;

import junit.framework.TestCase;

import org.junit.Test;
import org.teamrocket.entities.TransitionEntity;
import org.teamrocket.figures.TransitionFigure;

public class TestTransitionFigure extends TestCase {

  public TransitionFigure tran;
  
  public void setUp() {
    tran = new TransitionFigure();
  }
  
  @Test
  public void testLabel() {
    // TODO:  Test label accessor methods
  	TransitionFigure testTran = new TransitionFigure();
  	String testValue = "this is the test";
  	testTran.setLabel(testValue);
  	assertEquals(testTran.getLabel(), testValue);
  }
  
  @Test
  public void testData() {
    // TODO:  Test data accessor methods
  	TransitionFigure testTran = new TransitionFigure();
    TransitionEntity testEnt = new TransitionEntity();
    testTran.setData(testEnt);
    assertTrue(testEnt.equals(testTran.getData()));
  }
}
