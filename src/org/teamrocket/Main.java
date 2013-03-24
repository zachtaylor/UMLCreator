package org.teamrocket;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.OSXApplication;
import org.jhotdraw.app.SDIApplication;

public class Main {
  public static void main(String[] args) {
    Application app;
    String os = System.getProperty("os.name").toLowerCase();
    if (os.startsWith("mac")) {
      app = new OSXApplication();
    }
    else if (os.startsWith("win")) {
      // app = new DefaultMDIApplication();
      app = new SDIApplication();
    }
    else {
      app = new SDIApplication();
    }

    ApplicationModel model = new ApplicationModel();
    model.setName("JHotDraw UMLCreator");
    model.setVersion(Main.class.getPackage().getImplementationVersion());
    model.setViewClassName("org.jhotdraw.samples.pert.PertView");
    app.setModel(model);
    app.launch(args);
  }
}
